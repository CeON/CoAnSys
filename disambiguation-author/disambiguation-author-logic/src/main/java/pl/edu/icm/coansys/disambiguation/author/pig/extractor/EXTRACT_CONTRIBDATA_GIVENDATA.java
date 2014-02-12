/*
 * This file is part of CoAnSys project.
 * Copyright (c) 2012-2013 ICM-UW
 *
 * CoAnSys is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * CoAnSys is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with CoAnSys. If not, see <http://www.gnu.org/licenses/>.
 */
package pl.edu.icm.coansys.disambiguation.author.pig.extractor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.mapreduce.Counter;
import org.apache.pig.EvalFunc;
import org.apache.pig.data.DataBag;
import org.apache.pig.data.DataByteArray;
import org.apache.pig.data.DataType;
import org.apache.pig.data.DefaultDataBag;
import org.apache.pig.data.Tuple;
import org.apache.pig.data.TupleFactory;
import org.apache.pig.impl.logicalLayer.FrontendException;
import org.apache.pig.impl.logicalLayer.schema.Schema;
import org.apache.pig.tools.pigstats.PigStatusReporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

import pl.edu.icm.coansys.commons.java.StackTraceExtractor;
import pl.edu.icm.coansys.disambiguation.author.features.extractors.indicators.DisambiguationExtractor;
import pl.edu.icm.coansys.disambiguation.author.features.extractors.indicators.DisambiguationExtractorAuthor;
import pl.edu.icm.coansys.disambiguation.author.features.extractors.indicators.DisambiguationExtractorDocument;
import pl.edu.icm.coansys.disambiguation.author.features.extractors.indicators.DisambiguationExtractorFactory;
import pl.edu.icm.coansys.disambiguation.author.normalizers.ToEnglishLowerCase;
import pl.edu.icm.coansys.disambiguation.features.FeatureInfo;
import pl.edu.icm.coansys.models.DocumentProtos.Author;
import pl.edu.icm.coansys.models.DocumentProtos.DocumentMetadata;
import pl.edu.icm.coansys.models.DocumentProtos.DocumentWrapper;

/**
 * 
 * @author pdendek
 * @author mwos
 */
public class EXTRACT_CONTRIBDATA_GIVENDATA extends EvalFunc<DataBag> {

	private static final Logger logger = LoggerFactory
			.getLogger(EXTRACT_CONTRIBDATA_GIVENDATA.class);
	private List<DisambiguationExtractorDocument> des4Doc = new ArrayList<DisambiguationExtractorDocument>();
	private List<DisambiguationExtractorAuthor> des4Author = new ArrayList<DisambiguationExtractorAuthor>();
	private List<String> des4DocNameOrId = new ArrayList<String>(),
			des4AuthorNameOrId = new ArrayList<String>();

	@Parameter(names = { "-lang", "-language" }, description = "Filter metadata by language", converter = LangConverter.class)
	private String language = null; // null means all
	@Parameter(names = "-skipEmptyFeatures", arity = 1, description = "Skip contributor's features, when feature bag is empty (no data for feature).")
	private boolean skipEmptyFeatures = false;
	@Parameter(names = "-snameToString", arity = 1, description = "Does not normalize surname using to blocking when true. Use only for debuging.")
	private boolean snameToString = false;
	@Parameter(names = "-useIdsForExtractors", arity = 1, description = "Use short ids for extractors (features) names in temporary sequance files.")
	private boolean useIdsForExtractors = false;
	@Parameter(names = "-returnNull", arity = 1, description = "Return null data bag after processing. Use only for debuging.")
	private boolean returnNull = false;
	@Parameter(names = { "-featureinfo", "-featureInfo" }, required = true, description = "Features description - model for calculating affinity and contributors clustering.")
	private String featureinfo = null;

	private DisambiguationExtractorFactory extrFactory = new DisambiguationExtractorFactory();

	@Override
	public Schema outputSchema(Schema p_input) {
		try {
			return Schema.generateNestedSchema(DataType.BAG);
		} catch (FrontendException e) {
			logger.error("Error in creating output schema:", e);
			throw new IllegalStateException(e);
		}
	}

	private void setDisambiguationExtractor(String featureInfo)
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {

		if (featureInfo == null || featureInfo.isEmpty()) {
			throw new IllegalArgumentException("FeatureInfo model is required");
		}

		List<FeatureInfo> features = FeatureInfo
				.parseFeatureInfoString(featureinfo);

		String ExtractorDocClassName = new DisambiguationExtractorDocument()
				.getClass().getSimpleName();
		String ExtractorAuthorClassName = new DisambiguationExtractorAuthor()
				.getClass().getSimpleName();
		DisambiguationExtractor extractor;
		String currentClassNameOrId;

		for (int i = 0; i < features.size(); i++) {
			extractor = extrFactory.create(features.get(i));
			String currentSuperClassName = extractor.getClass().getSuperclass()
					.getSimpleName();
			if (useIdsForExtractors) {
				currentClassNameOrId = extrFactory.toExId(extractor.getClass()
						.getSimpleName());
			} else {
				currentClassNameOrId = extractor.getClass().getSimpleName();
			}

			if (currentSuperClassName.equals(ExtractorDocClassName)) {
				des4Doc.add((DisambiguationExtractorDocument) extractor);
				des4DocNameOrId.add(currentClassNameOrId);
			} else if (currentSuperClassName.equals(ExtractorAuthorClassName)) {
				des4Author.add((DisambiguationExtractorAuthor) extractor);
				des4AuthorNameOrId.add(currentClassNameOrId);
			} else {
				String m = "Cannot create extractor: "
						+ extractor.getClass().getSimpleName()
						+ ". Its superclass: " + currentSuperClassName
						+ " does not match to any superclass.";
				logger.error(m);
				throw new ClassNotFoundException(m);
			}
		}
	}

	public EXTRACT_CONTRIBDATA_GIVENDATA(String params)
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {

		String[] argv = params.split(" ");
		new JCommander(this, argv);
		setDisambiguationExtractor(featureinfo);
	}

	public Map<String, Object> debugComponents() {
		HashMap<String, Object> ret = new HashMap<String, Object>();
		if (language != null) {
			ret.put("-lang", language);
		}
		if (skipEmptyFeatures) {
			ret.put("-skipEmptyFeatures", skipEmptyFeatures);
		}
		if (snameToString) {
			ret.put("-snameToString", snameToString);
		}
		if (useIdsForExtractors) {
			ret.put("-useIdsForExtractors", useIdsForExtractors);
		}
		if (returnNull) {
			ret.put("-returnNull", returnNull);
		}
		if (featureinfo != null) {
			ret.put("-featureinfo", featureinfo);
		}
		return ret;
	}

	@Override
	public DataBag exec(Tuple input) throws IOException {

		initializePigReporterWithZeroes();

		if (input == null || input.size() == 0) {
			return null;
		}

		try {
			DataByteArray dba = (DataByteArray) input.get(0);

			DocumentWrapper dw = DocumentWrapper.parseFrom(dba.get());
			dba = null;

			// metadata
			DocumentMetadata dm = dw.getDocumentMetadata();
			String docKey = dm.getKey();
			dw = null;

			// result bag with tuples, which describe each contributor
			DataBag ret = new DefaultDataBag();

			Collection<Author> authors = dm.getBasicMetadata().getAuthorList();
			reportAuthors(authors);
			if (authors.isEmpty()) {
				// returning empty bag
				return ret;
			}

			Map<String, DataBag> finalAuthorMap;
			// taking from document metadata data universal for all contribs
			Map<String, DataBag> DocumentMap = extractDocBasedFeatures(dm);
			// creating disambiguation extractor only for normalizer
			DisambiguationExtractor extractor = new DisambiguationExtractor();

			// bag making tuples (one tuple for one contributor from document)
			// with replicated metadata for
			int i = -1;
			for (Author a : authors) {
				i++;
				// here we have sure that Object = Integer
				Object normalizedSname = null;
				if (snameToString) {
					normalizedSname = new ToEnglishLowerCase().normalize(a
							.getSurname());
				} else {
					normalizedSname = extractor.normalizeExtracted(a
							.getSurname());
				}

				// pig status reporter
				reportSname(normalizedSname);

				String cId = a.getKey();
				// taking from document metadata data specific for each contrib
				finalAuthorMap = extractAuthBasedFeatures(dm, DocumentMap, i);
				Object[] to = new Object[] { docKey, cId, normalizedSname,
						finalAuthorMap };
				Tuple t = TupleFactory.getInstance()
						.newTuple(Arrays.asList(to));

				ret.add(t);
			}

			if (returnNull) {
				return null;
			}
			return ret;

		} catch (Exception e) {
			logger.error("Error in processing input row:", e);
			throw new IOException("Caught exception processing input row:\n"
					+ StackTraceExtractor.getStackTrace(e));
		}
	}

	private Map<String, DataBag> extractAuthBasedFeatures(DocumentMetadata dm,
			Map<String, DataBag> InitialMap, int authorIndex) {

		Map<String, DataBag> finalAuthorMap = new HashMap<String, DataBag>(
				InitialMap);
		// in arrays we are storing DataBags from extractors
		DataBag extractedAuthorObj;

		for (int j = 0; j < des4Author.size(); j++) {
			extractedAuthorObj = des4Author.get(j).extract(dm, authorIndex,
					language);

			// adding to map extractor name and features' data
			reportAuthorDataExistance(extractedAuthorObj, j);
			if (extractedAuthorObj == null
					|| (extractedAuthorObj.size() == 0 && skipEmptyFeatures)) {
				continue;
			}
			finalAuthorMap.put(des4AuthorNameOrId.get(j), extractedAuthorObj);
		}
		return finalAuthorMap;
	}

	private Map<String, DataBag> extractDocBasedFeatures(DocumentMetadata dm) {
		Map<String, DataBag> map = new HashMap<String, DataBag>();
		// in arrays we are storing DataBags from extractors
		DataBag extractedDocObj;
		for (int i = 0; i < des4Doc.size(); i++) {
			extractedDocObj = des4Doc.get(i).extract(dm, language);
			// monit to pig status reporter
			raportDocumentDataExistance(extractedDocObj, i);
			// adding to map extractor name and features' data
			if (extractedDocObj == null
					|| (extractedDocObj.size() == 0 && skipEmptyFeatures)) {
				continue;
			}
			map.put(des4DocNameOrId.get(i), extractedDocObj);
		}
		return map;
	}

	// Pig Status Reporter staff:
	private PigStatusReporter myreporter = null;
	private Counter counters4Doc[][], counters4Author[][], counterSname[],
			countersExist;

	static class REPORTER_CONST {
		public static final String CONTRIB_EX = "Contrib_Existing";
		public static final String CONTRIB_MS = "Contrib_Missing";
		public static final String DOC_EX = "Doc_Existing";
		public static final String DOC_MS = "Doc_Missing";
		public static final int MISS = 0;
		public static final int EXIST = 1;
	}

	// cannot be run in constructor, have to take instance of reporter in each
	// exec(...) call
	private void initializePigReporterWithZeroes() {
		// instance of reporter may change in each exec(...) run
		myreporter = PigStatusReporter.getInstance();
		counters4Doc = new Counter[des4Doc.size()][2];
		counters4Author = new Counter[des4Author.size()][2];
		counterSname = new Counter[2];
		countersExist = myreporter.getCounter("unused", "unused");

		if (countersExist == null) {
			return;
		}

		for (int i = 0; i < des4Doc.size(); i++) {
			counters4Doc[i][REPORTER_CONST.MISS] = myreporter.getCounter(
					REPORTER_CONST.DOC_MS, des4Doc.get(i).getClass()
							.getSimpleName());
			counters4Doc[i][REPORTER_CONST.EXIST] = myreporter.getCounter(
					REPORTER_CONST.DOC_EX, des4Doc.get(i).getClass()
							.getSimpleName());

			counters4Doc[i][REPORTER_CONST.MISS].increment(0);
			counters4Doc[i][REPORTER_CONST.EXIST].increment(0);
		}
		for (int i = 0; i < des4Author.size(); i++) {
			counters4Author[i][REPORTER_CONST.MISS] = myreporter.getCounter(
					REPORTER_CONST.CONTRIB_MS, des4Author.get(i).getClass()
							.getSimpleName());
			counters4Author[i][REPORTER_CONST.EXIST] = myreporter.getCounter(
					REPORTER_CONST.CONTRIB_EX, des4Author.get(i).getClass()
							.getSimpleName());

			counters4Author[i][REPORTER_CONST.MISS].increment(0);
			counters4Author[i][REPORTER_CONST.EXIST].increment(0);
		}

		counterSname[REPORTER_CONST.MISS] = myreporter.getCounter(
				REPORTER_CONST.CONTRIB_MS,
				"Not null normalized sname to main blocking");
		counterSname[REPORTER_CONST.EXIST] = myreporter.getCounter(
				REPORTER_CONST.CONTRIB_EX,
				"Not null normalized sname to main blocking");
		counterSname[REPORTER_CONST.MISS].increment(0);
		counterSname[REPORTER_CONST.EXIST].increment(0);
	}

	private void reportAuthorDataExistance(DataBag extractedAuthorObj, int j) {
		if (countersExist == null) {
			return;
		}
		if (extractedAuthorObj == null || extractedAuthorObj.size() == 0) {
			counters4Author[j][REPORTER_CONST.MISS].increment(1);
		} else {
			counters4Author[j][REPORTER_CONST.EXIST].increment(1);
		}
	}

	private void raportDocumentDataExistance(DataBag extractedDocObj, int i) {
		if (countersExist == null) {
			return;
		}
		if (extractedDocObj == null || extractedDocObj.size() == 0) {
			counters4Doc[i][REPORTER_CONST.MISS].increment(1);
		} else {
			counters4Doc[i][REPORTER_CONST.EXIST].increment(1);
		}
	}

	private void reportSname(Object sname) {
		if (countersExist == null) {
			return;
		}
		if (sname == null) {
			counterSname[REPORTER_CONST.MISS].increment(1);
		} else {
			counterSname[REPORTER_CONST.EXIST].increment(1);
		}
	}

	private void reportAuthors(Collection<Author> authors) {
		if (countersExist == null) {
			return;
		}
		myreporter.getCounter(REPORTER_CONST.DOC_MS,
				"Any author (processed documents)").increment(
				authors.isEmpty() ? 1 : 0);
		myreporter.getCounter(REPORTER_CONST.DOC_EX,
				"Any author (unprocessed documents)").increment(
				authors.isEmpty() ? 0 : 1);
	}
}

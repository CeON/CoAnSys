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

import pl.edu.icm.coansys.commons.java.StackTraceExtractor;
import pl.edu.icm.coansys.disambiguation.author.pig.normalizers.ToEnglishLowerCase;
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
	private PigStatusReporter myreporter = null;	
	private List<DisambiguationExtractorDocument> des4Doc = new ArrayList<DisambiguationExtractorDocument>();
	private List<DisambiguationExtractorAuthor> des4Author = new ArrayList<DisambiguationExtractorAuthor>();
	private List<String> des4DocNameOrId = new ArrayList<String>(),
			des4AuthorNameOrId = new ArrayList<String>();
	private String language = null; // null means all
	private boolean skipEmptyFeatures = false;
	private boolean snameToString = false;
	private boolean useIdsForExtractors = false;
	private DisambiguationExtractorFactory extrFactory = new DisambiguationExtractorFactory();
	private boolean returnNull = false;

	@Override
	public Schema outputSchema(@SuppressWarnings("unused") Schema p_input) {
		try {
			return Schema.generateNestedSchema(DataType.BAG);
		} catch (FrontendException e) {
			logger.error("Error in creating output schema:", e);
			throw new IllegalStateException(e);
		}
	}

	private void setDisambiguationExtractor(String featureinfo)
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {

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

	public EXTRACT_CONTRIBDATA_GIVENDATA(String in_params)
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		String[] params = in_params.split(" ");
		for (String p : params) {
			if (p.startsWith("featureinfo=")) {
				setDisambiguationExtractor(p.substring("featureinfo=".length()));
			} else if (p.startsWith("lang=")) {
				this.language = parseLng(p.substring("lang=".length()));
			} else if (p.startsWith("skipEmptyFeatures=")) {
				this.skipEmptyFeatures = Boolean.parseBoolean(p
						.substring("skipEmptyFeatures=".length()));
			} else if (p.startsWith("snameToString=")) {
				this.snameToString = Boolean.parseBoolean(p
						.substring("snameToString=".length()));
			} else if (p.startsWith("useIdsForExtractors=")) {
				this.useIdsForExtractors = Boolean.parseBoolean(p
						.substring("useIdsForExtractors=".length()));
			} else if (p.startsWith("returnNull=")) {
				this.returnNull = Boolean.parseBoolean(p
						.substring("returnNull=".length()));
			}
		}
	}

	public EXTRACT_CONTRIBDATA_GIVENDATA(String featureinfo, String lang)
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		this.language = parseLng(lang);
		setDisambiguationExtractor(featureinfo);
	}

	public EXTRACT_CONTRIBDATA_GIVENDATA(String featureinfo, String lang,
			String skipEmptyFeatures, String useIdsForExtractors)
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		this.language = parseLng(lang);
		this.skipEmptyFeatures = Boolean.parseBoolean(skipEmptyFeatures);
		this.useIdsForExtractors = Boolean.parseBoolean(useIdsForExtractors);
		setDisambiguationExtractor(featureinfo);
	}

	private String parseLng(String lng) {
		if (lng == null || lng.equalsIgnoreCase("all")
				|| lng.equalsIgnoreCase("null") || lng.equals("")) {
			return null;
		}
		return lng;
	}

	@Override
	public DataBag exec(Tuple input) throws IOException {

		myreporter = PigStatusReporter.getInstance();
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

			// result bag with tuples, which des4Doccribes each contributor
			DataBag ret = new DefaultDataBag();

			// TODO: Checking for author clones should be in importers
			// START IMPORTER PART
			// getting full author list (probably with duplicates)
			List<Author> dplAuthors = dm.getBasicMetadata().getAuthorList();

			Map<String, Author> filteredAuthors = new HashMap<String, Author>(
					dplAuthors.size());

			// creating disambiguation extractor only for normalizer
			DisambiguationExtractor disam_extractor = new DisambiguationExtractor();

			for (Author a : dplAuthors) {
				Author b = filteredAuthors.put(a.getKey(), a);
				if (b != null) {
					// cId is inside map already. Checking whether cId is cloned
					// or
					// duplicated for different data or incorrectly attributed
					// for different authors
					String aInit = a.getSurname();
					String bInit = b.getSurname();
					Object aNorm = disam_extractor.normalizeExtracted(aInit);
					Object bNorm = disam_extractor.normalizeExtracted(bInit);

					if (a.equals(b)) {
						// all authors data are equal
						// AUTHOR B (AS CLONE A) SCHOULD BE REMOVED FROM
						// DOCUMENT'S AUTHOR LIST IN IMPORTERS
						logger.info("Author metadata clones with key: "
								+ a.getKey() + " in document with key: "
								+ docKey);
					} else if (aNorm.equals(bNorm)) {
						logger.info("Duplicated author key: " + a.getKey()
								+ " for different metadata (except surname!)"
								+ " in document with key: " + docKey);
					} else {
						logger.error("Duplicated aurhor key: " + a.getKey()
								+ " for different authors: " + aInit + ", "
								+ bInit + " in document with key: " + docKey);
					}
				}
			}
			Collection<Author> authors = filteredAuthors.values();
			// END IMPORTER PART

			// TODO: builder for document metadata,
			// replace old author list (with duplicates) with new (filtered)
			// we want replace it, because in the other way EX_AUTH_SNAMES will
			// give us feature description with duplicates OR we would need to
			// write there the same filter as above.
			// Or include author clones checking in IMPORTERS.

			// in arrays we are storing DataBags from extractors
			DataBag[] extractedDocObj = new DataBag[des4Doc.size()];
			DataBag[] extractedAuthorObj;
			Map<String, DataBag> map = new HashMap<String, DataBag>();
			Map<String, DataBag> finalMap;

			for (int i = 0; i < des4Doc.size(); i++) {
				extractedDocObj[i] = des4Doc.get(i).extract(dm, language);
			}

			// adding to map extractor name and features' data
			for (int i = 0; i < des4Doc.size(); i++) {
				raportDocumentDataExistance(extractedDocObj, i);
				if (extractedDocObj[i] == null
						|| (extractedDocObj[i].size() == 0 && skipEmptyFeatures)) {
					continue;
				}
				map.put(des4DocNameOrId.get(i), extractedDocObj[i]);
			}
			extractedDocObj = null;

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
					normalizedSname = extractor.normalizeExtracted(a);
				}
				String cId = a.getKey();

				finalMap = new HashMap<String, DataBag>(map);

				// put author metadata into finalMap
				extractedAuthorObj = new DataBag[des4Author.size()];

				for (int j = 0; j < des4Author.size(); j++) {
					extractedAuthorObj[j] = des4Author.get(j).extract(dm, i,
							language);
				}

				// adding to map extractor name and features' data
				for (int j = 0; j < des4Author.size(); j++) {
					reportAuthorDataExistance(extractedAuthorObj, j);
					if (extractedAuthorObj[j] == null
							|| (extractedAuthorObj[j].size() == 0 && skipEmptyFeatures)) {
						continue;
					}
					finalMap.put(des4AuthorNameOrId.get(j),
							extractedAuthorObj[j]);
				}
				extractedAuthorObj = null;

				Object[] to = new Object[] { docKey, cId, normalizedSname,
						finalMap };
				Tuple t = TupleFactory.getInstance()
						.newTuple(Arrays.asList(to));
				ret.add(t);
			}
			map = null;
			dm = null;

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

	private void initializePigReporterWithZeroes() {
		if ( myreporter == null || myreporter.getCounter( "", "" ) == null  ) {
			return;
		}
		for(int i=0; i<des4Doc.size();i++){
			myreporter.getCounter(REPORTER_CONST.DOC_MS,
					des4Doc.get(i).getClass().getSimpleName()).increment(0);
		}
		for(int i=0; i<des4Doc.size();i++){
			myreporter.getCounter(REPORTER_CONST.DOC_EX,
					des4Doc.get(i).getClass().getSimpleName()).increment(0);
		}
		for(int i=0; i<des4Author.size();i++){
			myreporter.getCounter(REPORTER_CONST.CONTRIB_MS,
					des4Author.get(i).getClass().getSimpleName()).increment(0);
		}
		for(int i=0; i<des4Author.size();i++){
			myreporter.getCounter(REPORTER_CONST.CONTRIB_EX,
					des4Author.get(i).getClass().getSimpleName()).increment(0);
		}
	}

	private void reportAuthorDataExistance(DataBag[] extractedAuthorObj, int j) {
		if ( myreporter == null || myreporter.getCounter( "", "" ) == null  ) {
			return;
		}
		if (extractedAuthorObj[j] == null || extractedAuthorObj[j].size() == 0) {
			myreporter.getCounter(REPORTER_CONST.CONTRIB_MS,
					des4Author.get(j).getClass().getSimpleName()).increment(1);
		} else {
			myreporter.getCounter(REPORTER_CONST.CONTRIB_EX,
					des4Author.get(j).getClass().getSimpleName()).increment(1);
		}
	}

	private void raportDocumentDataExistance(DataBag[] extractedDocObj, int i) {
		if ( myreporter == null || myreporter.getCounter( "", "" ) == null  ) {
			return;
		}
		if (extractedDocObj[i] == null || extractedDocObj[i].size() == 0) {
			myreporter.getCounter(REPORTER_CONST.DOC_MS,
					des4Doc.get(i).getClass().getSimpleName()).increment(1);
		} else {
			myreporter.getCounter(REPORTER_CONST.DOC_EX,
					des4Doc.get(i).getClass().getSimpleName()).increment(1);
		}
	}
	
	static class REPORTER_CONST{
		public final static String CONTRIB_EX = "Contrib_Existing";
		public final static String CONTRIB_MS = "Contrib_Missing";
		public final static String DOC_EX = "Doc_Existing";
		public final static String DOC_MS = "Doc_Missing";
	}
}

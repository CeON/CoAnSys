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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.icm.coansys.commons.java.StackTraceExtractor;
import pl.edu.icm.coansys.disambiguation.author.pig.normalizers.AuthorToInitials;
import pl.edu.icm.coansys.disambiguation.author.pig.normalizers.PigNormalizer;
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
	private String language = null;
	private boolean skipEmptyFeatures = false;
	private boolean useIdsForExtractors = false;
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

	private void setDisambiguationExtractor(String featureinfo)
			throws Exception {

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

			try {
				if (currentSuperClassName.equals(ExtractorDocClassName)) {
					des4Doc.add((DisambiguationExtractorDocument) extractor);
					des4DocNameOrId.add(currentClassNameOrId);
				} else if (currentSuperClassName
						.equals(ExtractorAuthorClassName)) {
					des4Author.add((DisambiguationExtractorAuthor) extractor);
					des4AuthorNameOrId.add(currentClassNameOrId);
				} else {
					String m = "Cannot create extractor: "
							+ extractor.getClass().getSimpleName()
							+ ". Its superclass: " + currentSuperClassName
							+ " does not match to any superclass.";
					throw new Exception(m);
				}
			} catch (Exception e) {
				logger.error(StackTraceExtractor.getStackTrace(e));
				throw e;
			}
		}
	}

	public EXTRACT_CONTRIBDATA_GIVENDATA(String featureinfo) throws Exception {
		setDisambiguationExtractor(featureinfo);
	}

	public EXTRACT_CONTRIBDATA_GIVENDATA(String featureinfo, String lang)
			throws Exception {
		this.language = lang;
		setDisambiguationExtractor(featureinfo);
	}

	public EXTRACT_CONTRIBDATA_GIVENDATA(String featureinfo, String lang,
			String skipEmptyFeatures) throws Exception {
		this.language = lang;
		this.skipEmptyFeatures = Boolean.parseBoolean(skipEmptyFeatures);
		setDisambiguationExtractor(featureinfo);
	}

	public EXTRACT_CONTRIBDATA_GIVENDATA(String featureinfo, String lang,
			String skipEmptyFeatures, String useIdsForExtractors)
			throws Exception {
		this.language = lang;
		this.skipEmptyFeatures = Boolean.parseBoolean(skipEmptyFeatures);
		this.useIdsForExtractors = Boolean.parseBoolean(useIdsForExtractors);
		setDisambiguationExtractor(featureinfo);
	}

	private boolean checkLanguage() {
		return (language != null && !language.equalsIgnoreCase("all")
				&& !language.equalsIgnoreCase("null") && !language.equals(""));
	}

	@Override
	public DataBag exec(Tuple input) throws IOException {

		if (input == null || input.size() == 0) {
			return null;
		}

		try {
			DataByteArray dba = (DataByteArray) input.get(0);

			DocumentWrapper dw = DocumentWrapper.parseFrom(dba.get());
			dba = null;

			// metadata
			DocumentMetadata dm = dw.getDocumentMetadata();
			dw = null;

			// result bag with tuples, which des4Doccribes each contributor
			DataBag ret = new DefaultDataBag();
			
			// TODO: Checking for author clones should be in importers
			// START IMPORTER PART
			// getting full author list (probably with duplicates)
			List<Author> dplAuthors = dm.getBasicMetadata().getAuthorList();
			
			Map <String, Author> filteredAuthors = 
					new HashMap <String, Author> ( dplAuthors.size() );
			
			// removing clones or duplicates (cid - initials hash)
			PigNormalizer toInitials = new AuthorToInitials();
			for ( Author a : dplAuthors ) {
				Author b = filteredAuthors.put( a.getKey(), a );
				if ( b != null ) {
					//cId is inside map already. Checking whether cId is cloned or
					//duplicated for different data or incorrectly attributed for different authors
					String aInit = (String) toInitials.normalize( a );
					String bInit = (String) toInitials.normalize( b );
					Object aNorm = DisambiguationExtractor.normalizeExtracted( aInit );
					Object bNorm = DisambiguationExtractor.normalizeExtracted( bInit );
					
					if ( a.equals( b ) ) {
						// all authors data are equal
						// AUTHOR B (AS CLONE A) SCHOULD BE REMOVED FROM DOCUMENT'S AUTHOR LIST IN IMPORTERS
						logger.info( "Author metadata clones with key: " + a.getKey() + 
								" in document with key: " + dm.getKey() );
					} else if ( aNorm.equals( bNorm ) ) {
						logger.info( "Duplicated author key: " 
								+ a.getKey() +  " for different metadata (except initials)" +
								" in document with key: " + dm.getKey() );
					} else {
						logger.error( "Duplicated aurhor key: " 
								+ a.getKey() + " for different authors: " + aInit 
								+ ", " + bInit + 
								" in document with key: " + dm.getKey() );
					}
				}
			}
			Collection<Author> authors = filteredAuthors.values();
			//END IMPORTER PART
			
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

			if (checkLanguage()) {
				for (int i = 0; i < des4Doc.size(); i++) {
					extractedDocObj[i] = des4Doc.get(i).extract(dm, language);
				}
			} else {
				for (int i = 0; i < des4Doc.size(); i++) {
					extractedDocObj[i] = des4Doc.get(i).extract(dm);
				}
			}

			// adding to map extractor name and features' data
			for (int i = 0; i < des4Doc.size(); i++) {
				if (extractedDocObj[i] == null) {
					continue;
				}
				if (extractedDocObj[i].size() == 0 && skipEmptyFeatures) {
					continue;
				}

				map.put(des4DocNameOrId.get(i), extractedDocObj[i]);
			}
			extractedDocObj = null;

			// bag making tuples (one tuple for one contributor from document)
			// with replicated metadata for

			int i = -1;
			for ( Author a : authors )
			{
				i++;
				// here we have sure that Object = Integer
				Object normalizedSname = EX_AUTH_INITIALS
						.normalizeExtracted( a );
				String cId = a.getKey();

				finalMap = new HashMap<String, DataBag>(map);

				// put author metadata into finalMap
				extractedAuthorObj = new DataBag[des4Author.size()];
				if (checkLanguage()) {
					for (int j = 0; j < des4Author.size(); j++) {
						extractedAuthorObj[j] = des4Author.get(j).extract(dm,
								i, language);
					}
				} else {
					for (int j = 0; j < des4Author.size(); j++) {
						extractedAuthorObj[j] = des4Author.get(j)
								.extract(dm, i);
					}
				}

				// adding to map extractor name and features' data
				for (int j = 0; j < des4Author.size(); j++) {
					if (extractedAuthorObj[j] == null) {
						continue;
					}
					if (extractedAuthorObj[i].size() == 0 && skipEmptyFeatures) {
						continue;
					}

					finalMap.put(des4AuthorNameOrId.get(j),
							extractedAuthorObj[j]);
				}
				extractedAuthorObj = null;

				Object[] to = new Object[] { cId, normalizedSname, finalMap };
				Tuple t = TupleFactory.getInstance()
						.newTuple(Arrays.asList(to));
				ret.add(t);
			}
			map = null;
			dm = null;

			return ret;

		} catch (Exception e) {
			logger.error("Error in processing input row:", e);
			throw new IOException("Caught exception processing input row:\n"
					+ StackTraceExtractor.getStackTrace(e));
		}
	}
}

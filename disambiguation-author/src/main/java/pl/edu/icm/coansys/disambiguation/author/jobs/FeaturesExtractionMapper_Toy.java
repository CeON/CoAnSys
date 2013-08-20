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

package pl.edu.icm.coansys.disambiguation.author.jobs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.icm.coansys.disambiguation.author.features.extractors.ExtractorFactory;
import pl.edu.icm.coansys.disambiguation.author.features.extractors.indicators.AuthorBased;
import pl.edu.icm.coansys.disambiguation.author.features.extractors.indicators.DocumentBased;
import pl.edu.icm.coansys.commons.java.DiacriticsRemover;
import pl.edu.icm.coansys.disambiguation.auxil.TextTextArrayMapWritable;
import pl.edu.icm.coansys.disambiguation.features.Extractor;
import pl.edu.icm.coansys.disambiguation.features.FeatureInfo;
import pl.edu.icm.coansys.models.constants.HBaseConstant;
import pl.edu.icm.coansys.models.DocumentProtos.Author;
import pl.edu.icm.coansys.models.DocumentProtos.DocumentMetadata;

/**
 *
 * @author pdendek
 * @version 1.0
 * @since 2012-08-07
 */
@SuppressWarnings("rawtypes")
public class FeaturesExtractionMapper_Toy extends TableMapper<Text, TextTextArrayMapWritable> {

    private static Logger logger = LoggerFactory.getLogger(FeaturesExtractionMapper_Toy.class);
    private List<FeatureInfo> featureInfos;
    private List<Extractor> featureExtractors;

    @Override
    public void setup(Context context) throws IOException, InterruptedException {
        String fdescription = context.getConfiguration().get("FEATURE_DESCRIPTION");
        if (fdescription != null) {
            featureInfos = FeatureInfo.parseFeatureInfoString(fdescription);
            featureExtractors = new ArrayList<Extractor>();
            ExtractorFactory fe = new ExtractorFactory();
            for (FeatureInfo fi : featureInfos) {
                featureExtractors.add(fe.create(fi));
            }
        }
    }

    @Override
    protected void map(ImmutableBytesWritable rowId, Result documentMetadataColumn, Context context) throws IOException, InterruptedException {
        HashMap<String, List<String>> docBasedFeature = new HashMap<String, List<String>>();

        DocumentMetadata dm = DocumentMetadata.parseFrom(documentMetadataColumn.getValue(Bytes.toBytes(HBaseConstant.FAMILY_METADATA),
                Bytes.toBytes(HBaseConstant.FAMILY_METADATA_QUALIFIER_PROTO)));
        //(1) extract all document-based features, 
        //[which will be passes to the object authorId2FeatureMap] 
        createDocumentBasedFeatureMap(docBasedFeature, dm);
        //(2) for each of authors ...
        for (Author a : dm.getBasicMetadata().getAuthorList()) {
            String authId = a.getKey();
            TextTextArrayMapWritable featureName2FeatureValuesMap =
                    new TextTextArrayMapWritable();
            //(3) extract author-based features to author's featureName2FeatureValuesMap
            //(4) and enrich author's featureName2FeatureValuesMap
            //    with earlier extracted document-based features            
            createFeatureMapForOneAuthor(docBasedFeature, dm, authId,
                    featureName2FeatureValuesMap);
            //(5) in the end add AuthorID          
            addAuthorID(authId, featureName2FeatureValuesMap);
            //(6) log extracted features 
            logAllFeaturesExtractedForOneAuthor(authId,
                    featureName2FeatureValuesMap);
            //(7) after preparing featureName2FeatureValuesMap for one author
            //    emit pair<authId:key,feature2featureValue:value> 


            if ("".equals(DiacriticsRemover.removeDiacritics(a.getSurname().toLowerCase()))) {
                logger.debug("has empty surname: " + a.getSurname());
            }

            logger.debug("==== surname: " + new Text(DiacriticsRemover.removeDiacritics(a.getSurname().toLowerCase())).toString());
            featureName2FeatureValuesMap.getText(new Text(DiacriticsRemover.removeDiacritics(a.getSurname().toLowerCase())).toString());

            Text key = new Text();
            key.set(DiacriticsRemover.removeDiacritics(a.getSurname().toLowerCase()));
//            key.set("TEST");
            context.write(key, featureName2FeatureValuesMap);
        }
    }

    private void addAuthorID(String authId,
            TextTextArrayMapWritable featureName2FeatureValuesMap) {
        featureName2FeatureValuesMap.put("authId", authId);
    }

    protected void createDocumentBasedFeatureMap(Map<String, List<String>> docBasedFeature, DocumentMetadata dm) {
        //(1) extract all document-based features, 
        //[which will be passes to the object authorId2FeatureMap]
        int firstIndex = -1;
        for (Extractor fe : featureExtractors) {
            firstIndex++;
            if (fe instanceof DocumentBased) {
                docBasedFeature.put(featureInfos.get(firstIndex).getDisambiguatorName(),
                        fe.extract(dm));
            }
        }
    }

    protected void createFeatureMapForOneAuthor(Map<String, List<String>> docBasedFeature,
            DocumentMetadata dm, String authId,
            TextTextArrayMapWritable featureName2FeatureValuesMap) {

        int secondIndex = -1;
        for (Extractor fe : featureExtractors) {
            secondIndex++;
            //(3) extract author-based features 
            if (fe instanceof AuthorBased) {
                String featureName = featureInfos.get(secondIndex).getDisambiguatorName();
                List<String> value = fe.extract(dm, authId);
                featureName2FeatureValuesMap.put(featureName, value);
            }
            //(4) and enrich author featureName2FeatureValuesMap
            //    with earlier extracted document-based features
            if (fe instanceof DocumentBased) {
                String featureName = featureInfos.get(secondIndex).getDisambiguatorName();
                List<String> value = docBasedFeature.get(featureName);
                featureName2FeatureValuesMap.put(featureName, value);
            }
        }
    }

    protected void logAllFeaturesExtractedForOneAuthor(String authId,
            TextTextArrayMapWritable featureName2FeatureValuesMap) {
        logger.debug("MAPPER: output key: " + authId);
        logger.debug("MAPPER: output value: " + featureName2FeatureValuesMap);
    }
}

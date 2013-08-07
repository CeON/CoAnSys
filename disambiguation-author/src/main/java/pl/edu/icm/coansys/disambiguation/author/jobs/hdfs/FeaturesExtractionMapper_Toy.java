/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.disambiguation.author.jobs.hdfs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.log4j.Logger;

import pl.edu.icm.coansys.disambiguation.author.features.extractors.ExtractorFactory;
import pl.edu.icm.coansys.disambiguation.author.features.extractors.indicators.AuthorBased;
import pl.edu.icm.coansys.disambiguation.author.features.extractors.indicators.DocumentBased;
import pl.edu.icm.coansys.commons.java.DiacriticsRemover;
import pl.edu.icm.coansys.disambiguation.auxil.TextTextArrayMapWritable;
import pl.edu.icm.coansys.disambiguation.features.Extractor;
import pl.edu.icm.coansys.disambiguation.features.FeatureInfo;
import pl.edu.icm.coansys.models.DocumentProtos.Author;
import pl.edu.icm.coansys.models.DocumentProtos.DocumentMetadata;

/**
 *
 * @author pdendek
 * @version 1.0
 * @since 2012-08-07
 */
@SuppressWarnings("rawtypes")
public class FeaturesExtractionMapper_Toy extends Mapper<BytesWritable, BytesWritable, Text, TextTextArrayMapWritable> {

    private static Logger logger = Logger.getLogger(FeaturesExtractionMapper_Toy.class);
    private List<FeatureInfo> featureInfos;
    private List<Extractor> featureExtractors;

    @Override
    public void setup(Context context) throws IOException, InterruptedException {
        String fdescription = context.getConfiguration().get("FEATURE_DESCRIPTION");
        if (fdescription != null) {
            featureInfos = FeatureInfo.parseFeatureInfoString(fdescription);
            featureExtractors = getFeatureExtractor(featureInfos);
        }
    }

    private List<Extractor> getFeatureExtractor(List<FeatureInfo> inputfeatureInfos) {
        List<Extractor> extractors = new ArrayList<Extractor>();
        ExtractorFactory fe = new ExtractorFactory();

        for (FeatureInfo fi : inputfeatureInfos) {
            extractors.add(fe.create(fi));
        }
        return extractors;
    }

    /*private static List<FeatureInfo> getFeaturesInfos(String feature) {
     List<FeatureInfo> ret = new ArrayList<FeatureInfo>();
     String[] finfos = feature.split(",");
     for (String finfo : finfos) {
     String[] details = finfo.split("#");
     if (details.length != 4) {
     logger.error("Feature info does not contains enought data. "
     + "It should follow the pattern featureName#FeatureExtractorName"
     + "#Weight#MaxValue");
     logger.error("FeatureInfo contains: " + finfo);
     continue;
     } else {
     ret.add(new FeatureInfo(details[0], details[1],
     Double.parseDouble(details[2]),
     Integer.parseInt(details[3])));
     }
     }
     return ret;
     }*/
    @Override
    protected void map(BytesWritable skey, BytesWritable metadataProto, Context context) throws IOException, InterruptedException {
        HashMap<String, List<String>> docBasedFeature = new HashMap<String, List<String>>();
        DocumentMetadata dm = DocumentMetadata.parseFrom(metadataProto.copyBytes());

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
            context.write(key, featureName2FeatureValuesMap);
        }
    }

    private void addAuthorID(String authId,
            TextTextArrayMapWritable featureName2FeatureValuesMap) {
        featureName2FeatureValuesMap.put("authId", authId);
    }

    protected void createDocumentBasedFeatureMap(
            Map<String, List<String>> docBasedFeature, DocumentMetadata dm) {
        //(1) extract all document-based features, 
        //[which will be passes to the object authorId2FeatureMap]
        int firstIndex = -1;
        for (Extractor fe : featureExtractors) {
            firstIndex++;
            if (fe instanceof DocumentBased) {
                docBasedFeature.put(featureInfos.get(firstIndex).getDisambiguatorName(),
                        fe.extract(dm, null));
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

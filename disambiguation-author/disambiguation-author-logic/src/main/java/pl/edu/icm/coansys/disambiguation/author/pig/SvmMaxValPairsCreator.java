/*
 * This file is part of CoAnSys project.
 * Copyright (c) 2012-2015 ICM-UW
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
package pl.edu.icm.coansys.disambiguation.author.pig;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.pig.EvalFunc;
import org.apache.pig.data.DataType;
import org.apache.pig.data.Tuple;
import org.apache.pig.data.TupleFactory;
import org.apache.pig.impl.logicalLayer.FrontendException;
import org.apache.pig.impl.logicalLayer.schema.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.icm.coansys.commons.java.StackTraceExtractor;
import pl.edu.icm.coansys.disambiguation.author.features.disambiguators.Disambiguator;
import pl.edu.icm.coansys.disambiguation.author.features.disambiguators.DisambiguatorFactory;
import pl.edu.icm.coansys.disambiguation.features.FeatureInfo;

/**
 * Flow (constructor and exec method) similar to ExhaustiveAND
 *
 * @author pdendek
 *
 */
public class SvmMaxValPairsCreator extends EvalFunc<Tuple> {

    private static final Logger logger = LoggerFactory.getLogger(SvmMaxValPairsCreator.class);

    @Override
    public Schema outputSchema(Schema p_input) {
        try {
            return Schema.generateNestedSchema(DataType.TUPLE);
        } catch (FrontendException e) {
            logger.error("Error in creating output schema:", e);
            throw new IllegalStateException(e);
        }
    }
    private PigDisambiguator[] features;
    private FeatureInfo[] featureInfos;

    public SvmMaxValPairsCreator(String featureDescription) {
        List<FeatureInfo> FIwithEmpties = FeatureInfo.parseFeatureInfoString(featureDescription);
        List<FeatureInfo> FIFinall = new LinkedList<FeatureInfo>();
        List<PigDisambiguator> FeaturesFinall = new LinkedList<PigDisambiguator>();

        DisambiguatorFactory ff = new DisambiguatorFactory();
        Disambiguator d;

        //separate features which are fully described and able to use
        for (FeatureInfo fi : FIwithEmpties) {
            if (fi.getDisambiguatorName().equals("")) {
                continue;
            }
            if (fi.getFeatureExtractorName().equals("")) {
                continue;
            }
            d = ff.create(fi);
            if (d == null) {
                continue;
            }
            FIFinall.add(fi);
            FeaturesFinall.add(new PigDisambiguator(d));
        }

        this.featureInfos = FIFinall.toArray(new FeatureInfo[FIFinall.size()]);
        this.features = FeaturesFinall.toArray(new PigDisambiguator[FIFinall.size()]);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Tuple exec(Tuple tuple) throws IOException {
        try {
            String c1 = (String) tuple.get(0);//cId1
            //tuple.get(1);//sname
            Map<String, Object> m1 = (Map<String, Object>) tuple.get(2);//map1
            String c2 = (String) tuple.get(3);//cId2
            //tuple.get(4);//sname
            Map<String, Object> m2 = (Map<String, Object>) tuple.get(5);//map2

            int[] a = new int[features.length + 2];

            for (int d = 0; d < features.length; d++) {
                Object oA = m1.get(featureInfos[d].getFeatureExtractorName());
                Object oB = m2.get(featureInfos[d].getFeatureExtractorName());
                if (oA == null || oB == null) {
                    a[d] = 0;
                } else {
                    a[d] = (int) features[d].calculateAffinity(oA, oB);
                }
            }

            if (a[a.length - 1] == 0) {
                a[a.length - 1] = -1;
            }

            Tuple t = TupleFactory.getInstance().newTuple();
            t.append(c1);
            t.append(c2);
            for (int e : a) {
                t.append(e);
            }

            return t;
        } catch (Exception e) {
            logger.error(StackTraceExtractor.getStackTrace(e));
            throw new IOException(e);
        }
    }
}

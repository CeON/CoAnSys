/*
 * This file is part of CoAnSys project.
 * Copyright (c) 20012-2013 ICM-UW
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

package pl.edu.icm.coansys.classification.documents.pig.proceeders;

import java.io.IOException;
import java.util.Arrays;

import org.apache.pig.EvalFunc;
import org.apache.pig.PigServer;
import org.apache.pig.data.DataType;
import org.apache.pig.data.Tuple;
import org.apache.pig.data.TupleFactory;
import org.apache.pig.impl.logicalLayer.FrontendException;
import org.apache.pig.impl.logicalLayer.schema.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.icm.coansys.commons.java.StackTraceExtractor;

/**
 *
 * @author pdendek
 */
public class TFIDF extends EvalFunc<Tuple> {

    private static final Logger logger = LoggerFactory.getLogger(TFIDF.class);

    @Override
    public Schema outputSchema(Schema p_input) {
        try {
            return Schema.generateNestedSchema(DataType.TUPLE,
                    DataType.CHARARRAY, DataType.CHARARRAY, DataType.DOUBLE);
        } catch (FrontendException e) {
            logger.error("Error in creating output schema:", e);
            throw new IllegalStateException(e);
        }
    }

    public Tuple exec(Tuple input) throws IOException {
        if (input == null || input.size() == 0) {
            return null;
        }
        try {
            String word = (String) input.get(0);
            String key = (String) input.get(1);
            long wc = (Long) input.get(2);
            long wid = (Long) input.get(3);
            long dc = (Long) input.get(4);
            long dpw = (Long) input.get(5);

            double tf = wc / (double) wid;
            double idf = dc / (double) dpw;
            Double tfidf = tf * idf;

            Object[] to = new Object[]{key, word, tfidf};
            return TupleFactory.getInstance().newTuple(Arrays.asList(to));
        } catch (Exception e) {
            logger.error("Error in processing input row:", e);
            throw new IOException("Caught exception processing input row:\n"
                    + StackTraceExtractor.getStackTrace(e));
        }
    }

    public static void main(String[] args) {
        try {
            PigServer pigServer = new PigServer("local");
            runQuery(pigServer);
        } catch (Exception e) {
             logger.error("Caught exception:", e);
        }
    }

    public static void runQuery(PigServer pigServer) throws IOException {
        pigServer.registerJar("target/document-classification-1.0-SNAPSHOT-jar-with-depedencies.jar");
        pigServer.registerQuery("raw = LOAD 'hbase://testProto' USING org.apache.pig.backend.hadoop.hbase.HBaseStorage('m:mproto','-loadKey true') AS (id:bytearray, proto:bytearray);");
        pigServer.registerQuery("extracted = FOREACH raw GENERATE pl.edu.icm.coansys.classification.pig.EXTRACT(raw);");
        pigServer.registerQuery("DUMP raw;");
    }
}

package pl.edu.icm.coansys.classification.documents.pig.proceeders;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.pig.EvalFunc;
import org.apache.pig.PigServer;
import org.apache.pig.data.DataBag;
import org.apache.pig.data.DataType;
import org.apache.pig.data.DefaultDataBag;
import org.apache.pig.data.Tuple;
import org.apache.pig.data.TupleFactory;
import org.apache.pig.impl.logicalLayer.FrontendException;
import org.apache.pig.impl.logicalLayer.schema.Schema;

import pl.edu.icm.coansys.classification.documents.auxil.PorterStemmer;
import pl.edu.icm.coansys.classification.documents.auxil.StopWordsRemover;
import pl.edu.icm.coansys.disambiguation.auxil.DiacriticsRemover;
//import pl.edu.icm.coansys.disambiguation.auxil.DiacriticsRemover;

import com.google.common.base.Joiner;

public class TFIDF extends EvalFunc<Tuple>{

	@Override
	public Schema outputSchema(Schema p_input){
		try{
			return Schema.generateNestedSchema(DataType.TUPLE, 
					DataType.CHARARRAY, DataType.CHARARRAY, DataType.DOUBLE);
		}catch(FrontendException e){
			throw new IllegalStateException(e);
		}
	}
	
	public Tuple exec(Tuple input) throws IOException {
		if (input == null || input.size() == 0)
			return null;
		try{
			String word = (String) input.get(0);
			String key = (String) input.get(1);
			long wc = (Long) input.get(2);
			long wid = (Long) input.get(3);
			long dc = (Long) input.get(4);
			long dpw = (Long) input.get(5);

			double tf = wc/(double)wid;
			double idf = dc/(double)dpw;
			Double tfidf = tf*idf;
						
			Object[] to = new Object[]{key,word,tfidf};
		    return TupleFactory.getInstance().newTuple(Arrays.asList(to));
		}catch(Exception e){
			throw new IOException("Caught exception processing input row ", e);
		}
	}

	
	public static void main(String[] args) {
		try {
		    PigServer pigServer = new PigServer("local");
		    runQuery(pigServer);
		    }
		    catch(Exception e) {
		    }
		 }
		public static void runQuery(PigServer pigServer) throws IOException {
			pigServer.registerJar("target/document-classification-1.0-SNAPSHOT-jar-with-depedencies.jar");
		    pigServer.registerQuery("raw = LOAD 'hbase://testProto' USING org.apache.pig.backend.hadoop.hbase.HBaseStorage('m:mproto','-loadKey true') AS (id:bytearray, proto:bytearray);");
		    pigServer.registerQuery("extracted = FOREACH raw GENERATE pl.edu.icm.coansys.classification.pig.EXTRACT(raw);");
		    pigServer.registerQuery("DUMP raw;");
		 }

}

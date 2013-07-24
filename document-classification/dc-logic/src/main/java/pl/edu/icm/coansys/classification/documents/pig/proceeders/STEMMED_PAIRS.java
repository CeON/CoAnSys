/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.classification.documents.pig.proceeders;


import com.google.common.base.Joiner;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.pig.EvalFunc;
import org.apache.pig.PigServer;
import org.apache.pig.data.*;
import org.apache.pig.impl.logicalLayer.FrontendException;
import org.apache.pig.impl.logicalLayer.schema.Schema;
import pl.edu.icm.coansys.classification.documents.auxil.StackTraceExtractor;
import pl.edu.icm.coansys.commons.java.PorterStemmer;
import pl.edu.icm.coansys.commons.java.StopWordsRemover;
import pl.edu.icm.coansys.disambiguation.auxil.DiacriticsRemover;

/**
*
* @author pdendek
*/
public class STEMMED_PAIRS extends EvalFunc<DataBag>{

	@Override
	public Schema outputSchema(Schema p_input){
		try{
			return Schema.generateNestedSchema(DataType.TUPLE, 
					DataType.CHARARRAY, DataType.CHARARRAY);
		}catch(FrontendException e){
			throw new IllegalStateException(e);
		}
	}
	
	public DataBag exec(Tuple input) throws IOException {
		if (input == null || input.size() == 0)
			return null;
		try{
			String key = (String) input.get(0);
			String[] vals = new String[3];

			for(int i = 1; i<4 ;i++){
				Object tmp = input.get(i);
				vals[i-1] = tmp==null ? "" : tmp.toString();  
			}
			String vals_str = Joiner.on(" ").join(vals);
			vals_str = vals_str.toLowerCase();
			vals_str = DiacriticsRemover.removeDiacritics(vals_str);
			vals_str = vals_str.replaceAll("[^a-z ]", "");
			
			PorterStemmer ps = new PorterStemmer();
			List<Tuple> alt = new ArrayList<Tuple>(); 
			for(String s : vals_str.split(" ")){
				if(StopWordsRemover.isAnEnglishStopWords(s)) continue;

				ps.add(s.toCharArray(), s.length());
				ps.stem();
				String[] to = new String[]{key,ps.toString()};
		        alt.add(TupleFactory.getInstance().newTuple(Arrays.asList(to)));
			}
			
			return new DefaultDataBag(alt); 
		}catch(Exception e){
			// Throwing an exception will cause the task to fail.
            throw new IOException("Caught exception processing input row:\n"
            		+ StackTraceExtractor.getStackTrace(e));
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

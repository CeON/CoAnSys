package pl.edu.icm.coansys.classification.documents.pig.extractors;



import java.io.IOException;
import java.util.Arrays;

import org.apache.pig.EvalFunc;
import org.apache.pig.PigServer;
import org.apache.pig.data.DataByteArray;
import org.apache.pig.data.DataType;
import org.apache.pig.data.Tuple;
import org.apache.pig.data.TupleFactory;
import org.apache.pig.impl.logicalLayer.FrontendException;
import org.apache.pig.impl.logicalLayer.schema.Schema;

import pl.edu.icm.coansys.importers.models.DocumentProtos.DocumentMetadata;

import com.google.common.base.Joiner;

public class EXTRACT extends EvalFunc<Tuple>{

	@Override
	public Schema outputSchema(Schema p_input){
		try{
			return Schema.generateNestedSchema(DataType.TUPLE, DataType.CHARARRAY,
					DataType.CHARARRAY, DataType.CHARARRAY, DataType.CHARARRAY);
		}catch(FrontendException e){
			throw new IllegalStateException(e);
		}
	}
	
	public Tuple exec(Tuple input) throws IOException {
		if (input == null || input.size() == 0)
			return null;
		try{
			Object obj = null;
			try{
//				System.out.println("Trying to read field rowId");
				obj = input.get(0);
//				System.out.println("Success!: "+ (String)obj);
//				System.out.println("Trying to read field metadata bytes");
				obj = (DataByteArray) input.get(1);
//				System.out.println("Success!");
			}catch(Exception e){
				System.out.println("Trying to read field rowId");
				System.out.println("Failure!");
				e.printStackTrace();
				throw e;
			}
			
			DataByteArray dba = null;
			try{
//				System.out.println("Trying to cast Object ("+input.getType(1)+") to DataByteArray");
				dba = (DataByteArray) obj;
//				System.out.println("Success!");	
			}catch(Exception e){
				System.out.println("Trying to cast Object ("+input.getType(1)+") to DataByteArray");
				System.out.println("Failure!");
				e.printStackTrace();
				throw e;
			}
			
			DocumentMetadata dm = null;
			try{
//				System.out.println("Trying to read ByteArray to DocumentMetadata");
				dm = DocumentMetadata.parseFrom(dba.get());
//				System.out.println("Success!");
			}catch(Exception e){
				System.out.println("Trying to read ByteArray to DocumentMetadata");
				System.out.println("Failure!");
				e.printStackTrace();
				throw e;
			}
			 
	        String key = dm.getKey();
	        String[] to = new String[]{key,dm.getTitle(),"",""};
//	        String[] to = new String[]{key,dm.getTitle(),dm.getAbstrakt(),Joiner.on(" ").join(dm.getKeywordList())};
	        Tuple t = TupleFactory.getInstance().newTuple(Arrays.asList(to));
			
			return t;
			
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
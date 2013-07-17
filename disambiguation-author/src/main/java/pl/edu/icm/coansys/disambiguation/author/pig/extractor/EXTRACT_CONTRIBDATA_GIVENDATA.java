/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.disambiguation.author.pig.extractor;

import java.io.IOException;
import java.util.Arrays;
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

import pl.edu.icm.coansys.disambiguation.author.auxil.StackTraceExtractor;
import pl.edu.icm.coansys.disambiguation.features.FeatureInfo;
import pl.edu.icm.coansys.models.DocumentProtos.Author;
import pl.edu.icm.coansys.models.DocumentProtos.DocumentMetadata;
import pl.edu.icm.coansys.models.DocumentProtos.DocumentWrapper;

/**
*
* @author pdendek
*/
public class EXTRACT_CONTRIBDATA_GIVENDATA extends EvalFunc<DataBag>{

	DisambiguationExtractor[] des = null;
	
	@Override
	public Schema outputSchema(Schema p_input){
		try{
			return Schema.generateNestedSchema(DataType.BAG);
		}catch(FrontendException e){
			throw new IllegalStateException(e);
		}
	}
	
	public EXTRACT_CONTRIBDATA_GIVENDATA(String info) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		
		List<FeatureInfo>features = FeatureInfo.parseFeatureInfoString( info );
		des = new DisambiguationExtractor[ features.size() ];
		
		int i = 0;
		for ( FeatureInfo f : features ) {
			Class<?> c = Class.forName("pl.edu.icm.coansys.disambiguation.author.pig.extractor." + f.getFeatureExtractorName() );
			des[i] = (DisambiguationExtractor) c.newInstance();
			i++;
		}
	}
	
	public EXTRACT_CONTRIBDATA_GIVENDATA() throws ClassNotFoundException, InstantiationException, IllegalAccessException {

			des = new DisambiguationExtractor[1];
			Class<?> c = Class.forName("pl.edu.icm.coansys.disambiguation.author.pig.extractor.EX_TITLE");
			des[0] = (DisambiguationExtractor) c.newInstance();
	}
	
	public DataBag exec(Tuple input) throws IOException {
		
	
		if (input == null || input.size() == 0)
			return null;
		
		try{		
			DataByteArray dba = null;
			try{
				dba = (DataByteArray) input.get(0);	
			}catch(Exception e){
				System.out.println("Trying to cast Object ("+input.getType(0)
						+") to DataByteArray");
				System.out.println("Failure!");
				e.printStackTrace();
				throw e;
			}
			
			DocumentWrapper dw = null;
			try{
				dw = DocumentWrapper.parseFrom( dba.get() );
			}catch(Exception e){
				System.out.println("Trying to read ByteArray to DocumentMetadata");
				System.out.println("Failure!");
				e.printStackTrace();
				throw e;
			}
			
			//metadane dokumentu
			DocumentMetadata dm = dw.getDocumentMetadata();

			//torba wynikowa z tuplami  opisujacego kazdego kontrybutora
			DataBag ret = new DefaultDataBag();

			//lista autorow
			List <Author> authors =  
					dm.getBasicMetadata().getAuthorList();
			
			//obiekty wynikowe tylko dla danych tyczacych sie dokumentu
			//w dalszej czesci bedzie trzeba zgeneralizowac ten kod,
			//zeby pozwalal tez na wyciaganie metadanych tyczacych sie danego kotrybutora
			//(takich jak jego adres email, afiliacja, etc.).
			//potencjalnie bedzie do tego potrzebna osobna petla.
			Object[] retObj = new Object[des.length];
			int i=-1;
			for(DisambiguationExtractor de : des){
				i++;
				retObj[i] = de.extract(dm);
			}
			
			//dodawanie wyciagnietych danych odnosnie dokumentu i kontrybutora do mapy informacji
			Map<String, Object> map = new HashMap<String, Object>();
			i = -1;
			for(DisambiguationExtractor de : des){
				i++;
				map.put(de.getClass().getSimpleName(), retObj[i]);
			}
            
			//stworzenie torby zawierajacej tuple z informacjami 
			//o wszystkich kontrybutorach z danego dokumentu
			for (i = 0; i < authors.size(); i++ ){
				String sname = authors.get(i).getSurname();
				String cId = authors.get(i).getKey();
				Object[] to = new Object[]{cId, i, sname, map};
				Tuple t = TupleFactory.getInstance().newTuple(Arrays.asList(to));
				ret.add(t);
			}
	        //zwrocenie torby z wynikami
	        return ret;
			
		}catch(Exception e){
			// Throwing an exception will cause the task to fail.
            throw new IOException("Caught exception processing input row:\n"
            		+ StackTraceExtractor.getStackTrace(e));
		}
	}
}

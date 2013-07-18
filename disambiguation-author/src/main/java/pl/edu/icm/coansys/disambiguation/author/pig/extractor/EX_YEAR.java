package pl.edu.icm.coansys.disambiguation.author.pig.extractor;

import org.apache.pig.data.DataBag;
import org.apache.pig.data.DefaultDataBag;
import org.apache.pig.data.Tuple;
import org.apache.pig.data.TupleFactory;

import pl.edu.icm.coansys.models.DocumentProtos.DocumentMetadata;

public class EX_YEAR implements  DisambiguationExtractor {
	
	@Override
	public DataBag extract(Object o, Object... objects){
		DocumentMetadata dm = (DocumentMetadata) o;
		
		DataBag db = new DefaultDataBag();
		Tuple t = TupleFactory.getInstance().newTuple( dm.getBasicMetadata().getYear() );
		db.add( t );
		
		return db;
	}
}

package pl.edu.icm.coansys.disambiguation.author.pig.extractor;

import org.apache.pig.data.DataBag;
import org.apache.pig.data.DefaultDataBag;
import org.apache.pig.data.TupleFactory;

import pl.edu.icm.coansys.models.DocumentProtos.ClassifCode;
import pl.edu.icm.coansys.models.DocumentProtos.DocumentMetadata;

public class EX_CLASSIFICATION_CODES implements  DisambiguationExtractor {
	
	@Override
	public DataBag extract(Object o, Object... objects){
		DocumentMetadata dm = (DocumentMetadata) o;
		DataBag db = new DefaultDataBag();
		
		for(ClassifCode cc : dm.getBasicMetadata().getClassifCodeList()){
			for(String s : cc.getValueList()){
				db.add(TupleFactory.getInstance().newTuple(s));
			}
		}
			
		return db;		
	}
}

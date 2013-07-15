package pl.edu.icm.coansys.disambiguation.author.pig.extractor;

import org.apache.pig.data.DataBag;
import org.apache.pig.data.DefaultDataBag;
import org.apache.pig.data.TupleFactory;

import pl.edu.icm.coansys.models.DocumentProtos.DocumentMetadata;
import pl.edu.icm.coansys.models.DocumentProtos.KeywordsList;

public class EX_KEYWORDS implements  DisambiguationExtractor<DataBag>  {
	public DataBag extract(Object o, Object... objects){
		DocumentMetadata dm = (DocumentMetadata) o;
		DataBag db = new DefaultDataBag();
		String lang = null;
		
		for(KeywordsList k : dm.getKeywordsList() ){
			for(String s : 	k.getKeywordsList() ){
				db.add(TupleFactory.getInstance().newTuple(s));
			}
		}
			
		return db;		
	}
}

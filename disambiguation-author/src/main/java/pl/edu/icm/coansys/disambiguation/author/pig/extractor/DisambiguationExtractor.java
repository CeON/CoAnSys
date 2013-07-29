package pl.edu.icm.coansys.disambiguation.author.pig.extractor;

import org.apache.pig.data.DataBag;

//at least one implementation from these two function required 
public abstract class DisambiguationExtractor {
	
	public abstract DataBag extract( Object o, String lang );

	public DataBag extract( Object o ) {
		return extract( o, null );
	}
}

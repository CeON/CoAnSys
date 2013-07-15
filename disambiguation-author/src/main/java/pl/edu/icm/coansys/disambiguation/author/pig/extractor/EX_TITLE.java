package pl.edu.icm.coansys.disambiguation.author.pig.extractor;

import org.apache.pig.data.Tuple;
import org.apache.pig.data.TupleFactory;

import pl.edu.icm.coansys.models.DocumentProtos.DocumentMetadata;

public class EX_TITLE implements  DisambiguationExtractor<Tuple>  {
	public Tuple extract(Object o, Object... objects){
		DocumentMetadata dm = (DocumentMetadata) o;
		return TupleFactory.getInstance().newTuple(dm.getBasicMetadata().getTitleList().get(0).getText());
	}
}

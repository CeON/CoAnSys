package pl.edu.icm.coansys.disambiguation.author.pig.extractor;

import org.apache.pig.data.DataBag;

public interface DisambiguationExtractor {
	public DataBag extract(Object o, Object...objects);
}

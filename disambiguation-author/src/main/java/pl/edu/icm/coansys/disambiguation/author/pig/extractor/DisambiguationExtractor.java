package pl.edu.icm.coansys.disambiguation.author.pig.extractor;

public interface DisambiguationExtractor<X> {
	public X extract(Object o, Object...objects);
}

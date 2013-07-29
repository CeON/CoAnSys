package pl.edu.icm.coansys.disambiguation.author.pig.extractor;

import org.apache.pig.data.DataBag;

import pl.edu.icm.coansys.disambiguation.author.pig.normalizers.PigNormalizer;
import pl.edu.icm.coansys.disambiguation.author.pig.normalizers.ToEnglishLowerCase;

public abstract class DisambiguationExtractor {
	
	private PigNormalizer normalizers[];
	
	public DisambiguationExtractor() {
		normalizers = new PigNormalizer[1];
		normalizers[0] = new ToEnglishLowerCase();
	}
	
	public String normalizeExtracted( String extracted ) {
		String tmp = extracted;
		for ( PigNormalizer pn: normalizers ) {
			tmp = pn.normalize( tmp );
		}
		return tmp;
	}
	
	public abstract DataBag extract( Object o, String lang );

	public DataBag extract( Object o ) {
		return extract( o, null );
	}
}

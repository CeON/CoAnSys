package pl.edu.icm.coansys.disambiguation.author.pig.normalizers;

public class ToHashCode implements PigNormalizer {

	@Override
	public Object normalize( Object text ){
		Integer tmp;
		
		if ( text instanceof Integer ) {
			tmp = (Integer) text; 
		} else {
			tmp = text.hashCode();
		}
		
		return tmp;
	}

}

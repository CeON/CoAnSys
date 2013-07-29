package pl.edu.icm.coansys.disambiguation.author.pig.normalizers;

import pl.edu.icm.coansys.commons.java.DiacriticsRemover;

public class ToEnglishLowerCase implements PigNormalizer {

	@Override
	public String normalize( String text ) {
		String tmp = text.toLowerCase();
		tmp = DiacriticsRemover.removeDiacritics( tmp );
		tmp = tmp.replaceAll("[^a-z 0-9]", " ").replaceAll(" ++", " ");
		return tmp;
	}

}

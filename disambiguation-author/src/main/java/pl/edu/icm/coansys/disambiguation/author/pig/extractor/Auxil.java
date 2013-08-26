package pl.edu.icm.coansys.disambiguation.author.pig.extractor;

public class Auxil {
	
	static public boolean isClassifCode(String str) {
		if (isMSc(str)) {
			return true;
		} else {
			return false;
		}
	}

	static public boolean isMSc(String str) {
		return str.toUpperCase().matches("[0-9][0-9][A-Z][0-9][0-9]");
	}
}

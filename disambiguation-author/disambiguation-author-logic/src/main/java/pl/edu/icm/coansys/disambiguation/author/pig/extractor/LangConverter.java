package pl.edu.icm.coansys.disambiguation.author.pig.extractor;

import com.beust.jcommander.IStringConverter;

/* converter for JCommander */
public class LangConverter implements IStringConverter<String> {
	@Override
	public String convert(String lng) {
		return parseLng( lng );
	}

	public static String parseLng(String lng) {
		if (lng == null || lng.equalsIgnoreCase("all")
				|| lng.equalsIgnoreCase("null") || lng.equals("")) {
			return null;
		}
		return lng;
	}
}
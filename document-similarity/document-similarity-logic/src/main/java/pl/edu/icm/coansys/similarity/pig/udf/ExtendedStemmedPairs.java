/*
 * This file is part of CoAnSys project.
 * Copyright (c) 2012-2015 ICM-UW
 * 
 * CoAnSys is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * CoAnSys is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with CoAnSys. If not, see <http://www.gnu.org/licenses/>.
 */

package pl.edu.icm.coansys.similarity.pig.udf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.pig.EvalFunc;
import org.apache.pig.data.DataBag;
import org.apache.pig.data.DataType;
import org.apache.pig.data.DefaultDataBag;
import org.apache.pig.data.Tuple;
import org.apache.pig.data.TupleFactory;
import org.apache.pig.impl.logicalLayer.schema.Schema;

import pl.edu.icm.coansys.commons.java.DiacriticsRemover;
import pl.edu.icm.coansys.commons.java.PorterStemmer;
import pl.edu.icm.coansys.commons.java.StackTraceExtractor;

public class ExtendedStemmedPairs extends EvalFunc<DataBag> {

	@Override
	public Schema outputSchema(Schema input) {
		try {
			Schema termSchema = new Schema(new Schema.FieldSchema("term",
					new Schema(new Schema.FieldSchema("value",
							DataType.CHARARRAY)), DataType.TUPLE));

			return new Schema(new Schema.FieldSchema(getSchemaName(this
					.getClass().getName().toLowerCase(), input), termSchema,
					DataType.BAG));
		} catch (Exception e) {
			log.error("Error in the output Schema creation", e);
			log.error(StackTraceExtractor.getStackTrace(e));
			return null;
		}
	}

	private String TYPE_OF_REMOVAL = "latin";
	private static final String SPACE = " ";
	private AllLangStopWordFilter stowordsFilter = null;

	public ExtendedStemmedPairs() throws IOException {
		stowordsFilter = new AllLangStopWordFilter();
	}
	
	public ExtendedStemmedPairs(String params) throws IOException {
		TYPE_OF_REMOVAL = params;
		stowordsFilter = new AllLangStopWordFilter();
	}

	public List<String> getStemmedPairs(final String text) throws IOException {
		String tmp = text.toLowerCase();
		tmp = tmp.replaceAll("[_]+", "_");
		tmp = tmp.replaceAll("[-]+", "-");
		if(!"latin".equals(TYPE_OF_REMOVAL)){
			tmp = tmp.replaceAll("([^\\u0080-\\uFFFF a-zA-Z_\\-\\d\\s'])+", SPACE);
		}
		tmp = tmp.replaceAll("\\s+", SPACE);
		tmp = tmp.trim();
		List<String> strings = new ArrayList<String>();

		if (tmp.length() == 0) {
			return strings;
		}

		PorterStemmer ps = new PorterStemmer();
		for (String s : StringUtils.split(tmp, SPACE)) {
			s = s.replaceAll("^[/\\-]+", "");
			s = s.replaceAll("[\\-/]+$", "");
			if("latin".equals(TYPE_OF_REMOVAL)){
				s = s.replaceAll("[^a-z\\d\\-_/ ]+", SPACE);
			}
			if (s.length() <= 3) {
				continue;
			}
			if (!stowordsFilter.isInAllStopwords(s)) {
				s = DiacriticsRemover.removeDiacritics(s);
				ps.add(s.toCharArray(), s.length());
				ps.stem();
				strings.add(ps.toString());
			}
		}

		return strings;
	}

	@Override
	public DataBag exec(Tuple input) throws IOException {
		if (input == null || input.size() == 0 || input.get(0) == null) {
			return null;
		}

		try {
			List<Tuple> tuples = new ArrayList<Tuple>();

			String terms = (String) input.get(0);
			for (String s : getStemmedPairs(terms)) {
				tuples.add(TupleFactory.getInstance().newTuple(s));
			}

			return new DefaultDataBag(tuples);
		} catch (Exception e) {
			throw new IOException("Caught exception processing input row ", e);
		}
	}
	
	public static void main(String[] args) {
		
		String text = "100688";
		System.out.println("PartA: "+DiacriticsRemover.removeDiacritics(text));
		
//		PorterStemmer ps = new PorterStemmer();
//		for (String s : text.split(SPACE)) {
//			System.out.println("PartB1: "+s);
//			s = s.replaceAll("^[/\\-]+", "");
//			System.out.println("PartB2: "+s);
//			s = s.replaceAll("[\\-/]+$", "");
//			System.out.println("PartB3: "+s);
//			s = s.replaceAll("^[/\\-_0-9]+$", "");
//			System.out.println("PartB4: "+s);
//			if (s.length() <= 3) {
//				continue;
//			}
//			s = DiacriticsRemover.removeDiacritics(s);
//			System.out.println("PartC: "+s);
//			ps.add(s.toCharArray(), s.length());
//			ps.stem();
//			System.out.println("PartD: "+ps.toString());
//		}
//		String text = "Μεταφορά τεχνολογίας : " + "παράγων αναπτύξεως ή μέσον "
//				+ "αποδιαρθρώσεως των οικονομικών " + "του τρίτου κόσμου	"
//				+ "ó	Techn,ology Techn, ology";
//		System.out.println("--------------");
//		System.out.println(DiacriticsRemover.removeDiacritics(text));
//		System.out.println("--------------");
//		System.out.println(text.replaceAll(
//				"([^\\u0080-\\uFFFF a-zA-Z_\\-\\d\\s])+", ""));
//		System.out.println("--------------");
//		text = text.replaceAll("([^\\u0080-\\uFFFF a-zA-Z_\\-\\d\\s])+", "");
//		text = text.replaceAll("\\s+", " ");
//
//		PorterStemmer ps = new PorterStemmer();
//		for (String s : text.split(SPACE)) {
//			s = s.replaceAll("^[/\\-]+", "");
//			s = s.replaceAll("[\\-/]+$", "");
//			s = s.replaceAll("^[/\\-_0-9]+$", "");
//			if (s.length() <= 2) {
//				continue;
//			}
//			s = DiacriticsRemover.removeDiacritics(s);
//			ps.add(s.toCharArray(), s.length());
//			ps.stem();
//			System.out.println(ps.toString());
//		}
	}
}

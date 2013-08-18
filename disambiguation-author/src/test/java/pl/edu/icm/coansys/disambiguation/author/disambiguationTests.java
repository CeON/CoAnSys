/*
 * This file is part of CoAnSys project.
 * Copyright (c) 20012-2013 ICM-UW
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

package pl.edu.icm.coansys.disambiguation.author;

import org.testng.annotations.Test;
import java.io.IOException;

import org.apache.pig.tools.parameters.ParseException;

import pl.edu.icm.coansys.commons.java.DiacriticsRemover;
import pl.edu.icm.coansys.disambiguation.author.pig.extractor.DisambiguationExtractor;
import pl.edu.icm.coansys.disambiguation.author.pig.normalizers.ToEnglishLowerCase;
import pl.edu.icm.coansys.disambiguation.author.pig.normalizers.ToHashCode;

public class disambiguationTests {
	
    private static final String PIG_SCRIPT_DIR = "src/main/pig/";
    private static final String TEST_DIR = "src/test/resources/";
    //to add test case, put input and expected output into proper folder in TEST_DIR
    //Note: output schemes should look like dumped, not stored
    //while input contrariwise

    private PigScriptTester PST = new PigScriptTester( PIG_SCRIPT_DIR, TEST_DIR );

   	@Test(groups = {"fast"})
	public void aproximateAND() throws IOException, ParseException {
		
		String[] params = {
				"dc_m_hdfs_inputDocsData=null",
				"dc_m_hdfs_outputContribs=null",
				"dc_m_str_feature_info=" + "'TitleDisambiguator#EX_TITLE#1#1,YearDisambiguator#EX_YEAR#1#1'",
				"threshold='-1.0'"
			};

   		PST.run( "aproximateAND", "aproximate_AND_test.pig", "B", "E", params );
   	}
  	
   	@Test(groups = {"fast"})
	public void exhaustiveAND() throws IOException, ParseException {
		
		String[] params = {
				"dc_m_hdfs_inputDocsData=null",
				"dc_m_hdfs_outputContribs=null",
				"dc_m_str_feature_info=" + "'TitleDisambiguator#EX_TITLE#1#1,YearDisambiguator#EX_YEAR#1#1'",
				"threshold='-1.0'"
			};

   		PST.run( "exhaustiveAND", "exhaustive_AND_with_sim_test.pig", "A", "B", params );
   	}
   	

   	@Test(groups = {"fast"})
   	public void normalizers() {
		String text = "é{(Zaaaażółć 'gęślą', \"jaź(ń)\"}]# æ 1234567890 !@#$%^&*() _+=?/>.<,-";
		String diacRmExpected = "e{(zaaaazolc 'gesla', \"jaz(n)\"}]# ae 1234567890 !@#$%^&*() _+=?/>.<,-";
		String toELCExpected = "e zaaaazolc gesla jaz n ae 1234567890 _ -";
		Integer toHashExpected = -1486600746;
		Integer DisExtrExpected = -1399651159;
		Object a, b;
		String tmp;
		
		// testing critical changes in DiacriticsRemover
		tmp = DiacriticsRemover.removeDiacritics( text.toLowerCase() );
		assert( tmp.equals(diacRmExpected) );
		
		// testing normalizers
		a = (new ToEnglishLowerCase()).normalize( text );
		assert( a.equals( toELCExpected ) );
		b = (new ToEnglishLowerCase()).normalize( a );
		assert( a.equals( b ) );
		a = (new ToHashCode()).normalize( text );
		assert( a.equals( toHashExpected ) );
		a = (new ToHashCode()).normalize( (Object) text );
		assert( a.equals( toHashExpected ) );
		
		// testing normalize tool, which is using after data extraction
		a = DisambiguationExtractor.normalizeExtracted( text );
		assert( a.equals( DisExtrExpected ) );
   	}
}


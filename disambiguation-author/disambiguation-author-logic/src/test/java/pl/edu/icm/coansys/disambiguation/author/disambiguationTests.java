/*
 * This file is part of CoAnSys project.
 * Copyright (c) 2012-2013 ICM-UW
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
import java.util.Arrays;
import java.util.List;

import org.apache.pig.tools.parameters.ParseException;

import pl.edu.icm.coansys.commons.java.DiacriticsRemover;
import pl.edu.icm.coansys.disambiguation.author.features.disambiguators.ClassifCodeDisambiguator;
import pl.edu.icm.coansys.disambiguation.author.features.disambiguators.CoAuthorsSnameDisambiguatorFullList;
import pl.edu.icm.coansys.disambiguation.author.features.disambiguators.KeyphraseDisambiguator;
import pl.edu.icm.coansys.disambiguation.author.pig.extractor.DisambiguationExtractorDocument;
import pl.edu.icm.coansys.disambiguation.author.pig.extractor.DisambiguationExtractorFactory;
import pl.edu.icm.coansys.disambiguation.author.pig.normalizers.ToEnglishLowerCase;
import pl.edu.icm.coansys.disambiguation.author.pig.normalizers.ToHashCode;
import pl.edu.icm.coansys.disambiguation.features.Disambiguator;

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
				"dc_m_str_feature_info=" + "'TitleDisambiguator#EX_TITLE#1#1,KeywordDisambiguator#EX_KEYWORDS#1#1'",
				"threshold='-1.0'",
				"statistics=false"
			};

   		PST.run( "aproximateAND", "aproximate_AND_test.pig", "B", "E", params );
   	}
  	
    @Test(groups = {"fast"})
	public void aproximateAND_BFS() throws IOException, ParseException {
		
		String[] params = {
				"dc_m_hdfs_inputDocsData=null",
				"dc_m_hdfs_outputContribs=null",
				"dc_m_str_feature_info=" + "'TitleDisambiguator#EX_TITLE#1#1,KeywordDisambiguator#EX_KEYWORDS#1#1'",
				"threshold='-1.0'",
				"statistics=true"
			};

   		PST.run( "aproximateAND_BFS", "aproximate_AND_BFS_test.pig", "B", "E", params );
   	}
    
   	@Test(groups = {"fast"})
	public void exhaustiveAND() throws IOException, ParseException {
		
		String[] params = {
				"dc_m_hdfs_inputDocsData=null",
				"dc_m_hdfs_outputContribs=null",
				"dc_m_str_feature_info=" + "'TitleDisambiguator#EX_TITLE#1#1,KeywordDisambiguator#EX_KEYWORDS#1#1'",
				"threshold='-1.0'",
				"statistics=false"
			};

   		PST.run( "exhaustiveAND", "exhaustive_AND_with_sim_test.pig", "A", "B", params );
   	}
   	
    @Test(groups = {"fast"})
	public void aproximateAND_extrNameToId() throws IOException, ParseException {
		
		String[] params = {
				"dc_m_hdfs_inputDocsData=null",
				"dc_m_hdfs_outputContribs=null",
				"dc_m_str_feature_info=" + "'TitleDisambiguator#EX_TITLE#1#1,KeywordDisambiguator#EX_KEYWORDS#1#1'",
				"use_extractor_id_instead_name='true'",
				"threshold='-1.0'",
				"statistics=false"
			};

   		PST.run( "aproximateAND_extrNameToId", "aproximate_AND_test.pig", "B", "E", params );
   	}
   	
    @Test(groups = {"fast"})
	public void exhaustiveAND_extrNameToId() throws IOException, ParseException {
		
		String[] params = {
				"dc_m_hdfs_inputDocsData=null",
				"dc_m_hdfs_outputContribs=null",
				"dc_m_str_feature_info=" + "'TitleDisambiguator#EX_TITLE#1#1,KeywordDisambiguator#EX_KEYWORDS#1#1'",
				"use_extractor_id_instead_name='true'",
				"threshold='-1.0'",
				"statistics=false"
			};

   		PST.run( "exhaustiveAND_extrNameToId", "exhaustive_AND_with_sim_test.pig", "A", "B", params );
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
		a = DisambiguationExtractorDocument.normalizeExtracted( text );
		assert( a.equals( DisExtrExpected ) );
   	}
   	
   	@Test(groups = {"fast"})
   	public void extractorsFactory() throws Exception {
   		
   		DisambiguationExtractorFactory factory = new DisambiguationExtractorFactory();
   		
   		String[] extractors = {
   			   	"EX_AUTH_SNAMES",
   			   	"EX_KEYWORDS_SPLIT",
   			   	"EX_EMAIL",
   			   	"EX_YEAR",
   			   	"EX_TITLE",
   			   	"EX_TITLE_SPLIT",
   			   	"EX_PERSON_ID",
   			   	"EX_KEYWORDS",
   			   	"EX_COAUTH_SNAME",
   			   	"EX_CLASSIFICATION_CODES",
   			   	"EX_FORENAMES_INITS",
   			   	"EX_EMAIL_PREFIX" 
   		};
   		String[] ids = {
   				"0",
   				"6",
   				"4",
   				"B",
   				"A",
   				"9",
   				"8",
   				"7",
   				"2",
   				"1",
   				"5",
   				"3"
   		};
   		
   		assert ( ids.length == extractors.length );
   		
   		for ( int i = 0; i < extractors.length; i++ ) {
   			assert( factory.convertExNameToId( extractors[i] ).equals( ids[i] ) );
   			assert( factory.convertExIdToName( ids[i] ).equals( extractors[i] ) );
   			assert( factory.toExId( ids[i] ).equals( ids[i] ) );
   			assert( factory.toExId( extractors[i] ).equals( ids[i] ) );
   			assert( factory.toExName( ids[i] ).equals( extractors[i] ) );
   			assert( factory.toExName( extractors[i] ).equals( extractors[i] ) );
   		}
   	}
   	
   	@Test(groups = {"fast"})
   	public void disambiguator() {
   		//'CoAuthorsSnameDisambiguatorFullList#EX_AUTH_SNAMES#-0.0000166#8,ClassifCodeDisambiguator#EX_CLASSIFICATION_CODES#0.99#12,KeyphraseDisambiguator#EX_KEYWORDS_SPLIT#0.99#22,KeywordDisambiguator#EX_KEYWORDS#0.0000369#40'
   		Disambiguator COAUTH = new CoAuthorsSnameDisambiguatorFullList();
   		Disambiguator CC = new ClassifCodeDisambiguator();
   		Disambiguator KP = new KeyphraseDisambiguator();
   		
   		Object atab[] = {-1,"one", "two", "three", "four", 5, 6, 7, 8, 9.0, 10.0};
   		Object btab[] = {-2,-1,"one", "two", 5, 9.0, "eleven", 12, 13.0};		
   		List<Object> a = Arrays.asList(atab);
   		List<Object> b = Arrays.asList(btab);
  		double res = 5.0 / 15.0;
  		
  		assert( new Disambiguator().calculateAffinity(a, b) == res );
  		assert( CC.calculateAffinity(a, b) == res );
  		assert( KP.calculateAffinity(a, b) == res );
  		
  		res = 4.0 / 14.0;
  		assert( COAUTH.calculateAffinity(a, b) == res );
  	  	
   	}
   	
   	
}


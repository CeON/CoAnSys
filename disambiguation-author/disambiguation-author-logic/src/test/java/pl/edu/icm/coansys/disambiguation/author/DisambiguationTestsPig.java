package pl.edu.icm.coansys.disambiguation.author;
///*
// * This file is part of CoAnSys project.
// * Copyright (c) 2012-2015 ICM-UW
// * 
// * CoAnSys is free software: you can redistribute it and/or modify
// * it under the terms of the GNU Affero General Public License as published by
// * the Free Software Foundation, either version 3 of the License, or
// * (at your option) any later version.
//
// * CoAnSys is distributed in the hope that it will be useful,
// * but WITHOUT ANY WARRANTY; without even the implied warranty of
// * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// * GNU Affero General Public License for more details.
// * 
// * You should have received a copy of the GNU Affero General Public License
// * along with CoAnSys. If not, see <http://www.gnu.org/licenses/>.
// */
//
//package pl.edu.icm.coansys.disambiguation.author;
//
//import org.testng.annotations.Test;
//import java.io.IOException;
//import org.apache.pig.tools.parameters.ParseException;
//
//public class DisambiguationTests {
//	
//    private static final String PIG_SCRIPT_DIR = "src/main/pig/";
//    private static final String TEST_DIR = "src/test/resources/";
//    //to add test case, put input and expected output into proper folder in TEST_DIR
//    //Note: output schemes should look like dumped, not stored
//    //while input contrariwise
//
//    private PigScriptTester PST = new PigScriptTester( PIG_SCRIPT_DIR, TEST_DIR );
//
//    @Test(groups = {"fast"})
//	public void aproximateAND() throws IOException, ParseException {
//		
//		String[] params = {
//				"dc_m_hdfs_inputDocsData=null",
//				"dc_m_hdfs_outputContribs=null",
//				"dc_m_str_feature_info=" + "'TitleDisambiguator#EX_TITLE#1#1,KeywordDisambiguator#EX_KEYWORDS#1#1'",
//				"threshold='-1.0'",
//				"statistics=false"
//			};
//
//   		PST.run( "aproximateAND", "aproximate_AND_test.pig", "B", "E", params );
//   	}
//  	
//    @Test(groups = {"fast"})
//	public void aproximateAND_BFS() throws IOException, ParseException {
//		
//		String[] params = {
//				"dc_m_hdfs_inputDocsData=null",
//				"dc_m_hdfs_outputContribs=null",
//				"dc_m_str_feature_info=" + "'TitleDisambiguator#EX_TITLE#1#1,KeywordDisambiguator#EX_KEYWORDS#1#1'",
//				"threshold='-1.0'",
//				"statistics=true"
//			};
//
//   		PST.run( "aproximateAND_BFS", "aproximate_AND_BFS_test.pig", "B", "E", params );
//   	}
//    
//   	@Test(groups = {"fast"})
//	public void exhaustiveAND() throws IOException, ParseException {
//		
//		String[] params = {
//				"dc_m_hdfs_inputDocsData=null",
//				"dc_m_hdfs_outputContribs=null",
//				"dc_m_str_feature_info=" + "'TitleDisambiguator#EX_TITLE#1#1,KeywordDisambiguator#EX_KEYWORDS#1#1'",
//				"threshold='-1.0'",
//				"statistics=false"
//			};
//
//   		PST.run( "exhaustiveAND", "exhaustive_AND_with_sim_test.pig", "A", "B", params );
//   	}
//   	
//    @Test(groups = {"fast"})
//	public void aproximateAND_extrNameToId() throws IOException, ParseException {
//		
//		String[] params = {
//				"dc_m_hdfs_inputDocsData=null",
//				"dc_m_hdfs_outputContribs=null",
//				"dc_m_str_feature_info=" + "'TitleDisambiguator#EX_TITLE#1#1,KeywordDisambiguator#EX_KEYWORDS#1#1'",
//				"use_extractor_id_instead_name='true'",
//				"threshold='-1.0'",
//				"statistics=false"
//			};
//
//   		PST.run( "aproximateAND_extrNameToId", "aproximate_AND_test.pig", "B", "E", params );
//   	}
//   	
//    @Test(groups = {"fast"})
//	public void exhaustiveAND_extrNameToId() throws IOException, ParseException {
//		
//		String[] params = {
//				"dc_m_hdfs_inputDocsData=null",
//				"dc_m_hdfs_outputContribs=null",
//				"dc_m_str_feature_info=" + "'TitleDisambiguator#EX_TITLE#1#1,KeywordDisambiguator#EX_KEYWORDS#1#1'",
//				"use_extractor_id_instead_name='true'",
//				"threshold='-1.0'",
//				"statistics=false"
//			};
//
//   		PST.run( "exhaustiveAND_extrNameToId", "exhaustive_AND_with_sim_test.pig", "A", "B", params );
//   	}
//}
//    

package pl.edu.icm.coansys.disambiguation.author;


import java.io.IOException;
import java.util.LinkedList;

import org.apache.pig.pigunit.PigTest;
import org.apache.pig.tools.parameters.ParseException;
import org.testng.annotations.Test;

import pl.edu.icm.coansys.disambiguation.author.Auxil;

public class disambiguationTests {
	
    private static final String PIG_SCRIPT_DIR = "src/main/pig/";
    private static final String TEST_DIR = "src/test/resources/";
	
	@Test(groups = {"fast"})
	public void aproximateAND() throws IOException, ParseException {

		final String disamScriptPath = PIG_SCRIPT_DIR + "aproximate_AND_test.pig";
		Auxil.fileExist(disamScriptPath);
		final String disamInputPath = TEST_DIR + "aproximationAND/" + "data2.in";
		Auxil.fileExist(disamInputPath);
		final String disamExpectedOutputPath = TEST_DIR + "aproximationAND/" + "data2.out";
		Auxil.fileExist(disamExpectedOutputPath);

		
		String[] params = {
				"dc_m_hdfs_inputDocsData=null",
				"dc_m_hdfs_outputContribs=null",
				"dc_m_str_feature_info=" + "'TitleDisambiguator#EX_TITLE#1#1,YearDisambiguator#EX_YEAR#1#1'",
				"threshold='-1.0'"
			};
		
		//preparing script
		LinkedList<String> script = PigScriptExtractor.extract( disamScriptPath ); 
		PigTest aproximateAND = 
				new PigTest( script.toArray( new String[]{} ), params );
		
		//also loading input in pig script works (while storing output not)
		String[] in = Auxil.readTestToStringArray( disamInputPath );
		String[] out = Auxil.readTestToStringArray( disamExpectedOutputPath );
		aproximateAND.assertOutput("B", in, "E", out);
	}
}

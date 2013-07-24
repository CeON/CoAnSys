

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.Scanner;

import org.apache.pig.pigunit.PigTest;
import org.apache.pig.tools.parameters.ParseException;

public class disambiguationTests {
	
	private final String testPath = "src/test/resources/";
	
	private String readTestToString( final String filePath ) throws FileNotFoundException {
		
		fileExist( filePath );
		StringBuffer ret = new StringBuffer();	
		File file = new File( filePath );
		Scanner in = new Scanner( file );
		
		while ( in.hasNext() ) {
			ret.append( in.nextLine() );
			ret.append("\n");
		}
		
		return ret.toString();
	}
	
	
	public void fileExist( String filePathString ) throws FileNotFoundException {
		File f = new File( filePathString );
		
		if( !f.exists() ) {
			String m = "Cannot find script for test or test case file: ";
			m += "\n\t relative path: " + filePathString;
			m += "\n\t absolut path: " + f.getAbsolutePath();
			
			throw new FileNotFoundException( m );
		}
	}

	
//	@Test
//	public void aproximateANDTest() throws IOException {
//		
//		/// Initialization (for add test case change n and look at next code block):
//		int n = 1;
//		int t = 0;
//		AproximateAND udf = new AproximateAND("-1.0","TitleDisambiguator#EX_TITLE#1#1,YearDisambiguator#EX_YEAR#1#1");
//		Tuple input[] = new DefaultTuple[n]; /// for udf singleAND
//		Tuple output[] = new DefaultTuple[n]; /// from udf singleAND
//		Tuple expected[] = new DefaultTuple[n];
//		String contributorsKeys[] = new String[n]; /// for compare output with expected result
//		
//		for ( int i = 0; i < n; i++ ) 
//			input[i] = new DefaultTuple();
//		
//		/// Adding new test cases (here edit for add test cases):		
//		/// # TEST 0
//			String strMetadata = readTestToString( testPath + "test1_metadata.in" );
//			contributorsKeys[ t ] = "70ba36c9-c204-3436-9f3b-f4485e4a891f#c1";
//			
//			DataByteArray dbaMetadata = new DataByteArray();
//			dbaMetadata.set( strMetadata );
//			
//			//shit happens (przeklejona wywalajaca linijka z SingleAND):
//			DocumentMetadata metadata = DocumentMetadata.parseFrom( dbaMetadata.get() );
//
//		/*	
//		// ciag dalszy, jesli naprawimy powyzsze:
//			input[ t ].append( dbaMetadata );
//			input[ t ].append( 0 ); //position of contributor in metadata's author list
//		
//			
//		/// Compute and check
//		for ( int i = 0; i < n; i++ ) {
//			expected[i] = new DefaultTuple();
//			/// for 'single' contributors UUID should equal authorKey
//			expected[i].append( contributorsKeys[i] );
//			expected[i].append( contributorsKeys[i] );
//			
//			//output[i] = 
//			udf.exec( input[i] );
//		}*/
//		
//		
//		//assertArrayEquals( expected, output );	
//	}
//
//	@Test
	public void newDisambiguation() throws IOException, ParseException {
		
		final String disamScriptPath = "src/main/pig/new_disambiguation.pig";
		fileExist(disamScriptPath);
		final String disamInputPath = testPath + "newDisambiguation/" + "bazekon_secret.sf";
		fileExist(disamInputPath);
		final String disamExpectedOutputPath = testPath + "newDisambiguation/" + "bazekon_secret.out";
		fileExist(disamExpectedOutputPath);
		final String tmpOutput = testPath + "tmp/" + "bazekon_" + (new Date()).getTime() + "_secret.tmp";
		
		
		// x local not needed?
		String[] params = {
				"dc_m_hdfs_inputDocsData=" + disamInputPath,
				"dc_m_hdfs_outputContribs=" + tmpOutput
				};
		
		//running new_disambiguation.pig script with the above parameters
		PigTest new_disambiguation = new PigTest( disamScriptPath, params );
		new_disambiguation.runScript();
		
		//To compare resoult with expected output, first we need to sort it
		String sortingScript[] = {
				"resoult = load '" + tmpOutput + "' as (cId,uuid);",
				"sorted = order resoult by uuid,cId;"
		};
		
		File expected = new File( disamExpectedOutputPath );
		PigTest sorting = new PigTest( sortingScript );
		
		//compare expected with resoult after sorted
		sorting.assertOutput("sorted", expected);
	}
}

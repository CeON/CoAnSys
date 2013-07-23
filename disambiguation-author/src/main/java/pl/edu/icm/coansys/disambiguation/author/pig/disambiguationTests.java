//package pl.edu.icm.coansys.disambiguation.author.pig;
//
//import static org.junit.Assert.*;
//
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.util.Date;
//import java.util.Scanner;
//
//import org.apache.pig.data.DataByteArray;
//import org.apache.pig.data.DefaultTuple;
//import org.apache.pig.data.Tuple;
////import org.apache.pig.pigunit.PigTest;
//import org.apache.pig.tools.parameters.ParseException;
//import org.junit.Test;
//
//import pl.edu.icm.coansys.models.DocumentProtos.Author;
//import pl.edu.icm.coansys.models.DocumentProtos.DocumentMetadata;
//
//public class disambiguationTests {
//	
//	//TODO: przeniesc co trzeba do resources i poprawic ponizsza sciezke
//	private final String testPath = "/home/m/icm/CoAnSys_przez_pdendek/disambiguation-author/src/main/pig/test/";
//	
//	private String readTestToString( final String filePath ) throws FileNotFoundException {
//		StringBuffer ret = new StringBuffer();	
//		File file = new File( filePath );
//		Scanner in = new Scanner( file );
//		
//		while ( in.hasNext() ) {
//			ret.append( in.nextLine() );
//			ret.append("\n");
//		}
//		
//		return ret.toString();
//	}
//	
//	@Test
//	public void singleANDTest() throws IOException {
//		
//		/// Initialization (for add test case change n and look at next code block):
//		int n = 1;
//		int t = 0;
//		SingleAND udf = new SingleAND();
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
//	public void newDisambiguation() throws IOException, ParseException {
//		
//		String tmpOutput = testPath + "test2_" + (new Date()).getTime() + ".tmp";
//		
//		String[] params = {
//				"dc_m_hdfs_inputDocsData=" + testPath + "test2_secret.in",
//				"dc_m_hdfs_outputContribs=" + tmpOutput
//				};
//		
//		//TODO: poprawic sciezke:
//	/*	
//		PigTest test = new PigTest("/home/m/icm/CoAnSys_przez_pdendek/disambiguation-author/src/main/pig/new_disambiguation.pig",
//				params);
//		
//		//1. odsiac input
//		//2. w new_di..pig zparametryzowac input
//		//3. wywolac z parametrami
//		//4. wygenerowac out
//		
//		File expected = new File( testPath + "test2_secret.out" );
//		test.assertOutput( "E1", expected );
//		//TODO
//	*/
//	}
//}

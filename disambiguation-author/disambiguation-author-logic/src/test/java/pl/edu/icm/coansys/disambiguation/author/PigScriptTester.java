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

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.apache.pig.pigunit.PigTest;

import pl.edu.icm.coansys.commons.java.StackTraceExtractor;

/*
 * Note: PigUnit requires output schemes like after dumped, not stored,
 * while input contrarywise.
 * 
 * Differences: 
 * STORE: 
 * - elements (main columns) in record separated by "\t" not ","
 * - no "(" and ")" around elements (all main columns) in record
 * DUMP:
 * - contrary to STORE (each record is recognize as tuple, not as columns)
 * 
 * Example records after:
 * STORE:
 * {(some String, some number),(str, num)}	{(first string in bag), (second)}
 * DUMP:
 * ( {(some String, some number),(str, num)}, {(first string in bag), (second)} )
 */


public class PigScriptTester {

    private String PIG_SCRIPT_DIR = "";
    private String TEST_DIR = "";
	
    public PigScriptTester( String pigScriptDir, String globalTestDir ) {
    	PIG_SCRIPT_DIR = pigScriptDir;
    	TEST_DIR = globalTestDir;
    }
    
    private PigTest pigtest;
    public void run( String testsFolderName, String scriptName, 
    		String inputAlias, String outputAlias, String[] pigParams ) throws IOException {

    	final String scriptPath = PIG_SCRIPT_DIR + scriptName;
		Auxil.fileExist( scriptPath );
		final String testsFolder = TEST_DIR + testsFolderName + "/";
		Auxil.folderExist( testsFolder );
		
		//preparing pig script to run on testNG
    	LinkedList<String> script = PigScriptExtractor.extract( scriptPath ); 
    	pigtest = new PigTest( script.toArray( new String[]{} ), pigParams );
    	
    	//getting test cases' names
    	List<String> tests = Auxil.getTestCaseNames( testsFolder );
    	String in,out;
    	
    	//wrong outputs list (if any appear)
    	List < String > fails = new LinkedList<String>();
    	
    	//running each test case
    	for ( String test : tests ) {
    		//checking test files existing
    		//NOTE: Input file schemes 
    		in = testsFolder + test + ".in";
    		Auxil.fileExist( in );
    		out = testsFolder + test + ".out";
    		Auxil.fileExist( out );
    		//reading tests
    		String[] tableIn = Auxil.readTableToStringArray( in );
    		String[] tableOut = Auxil.readTableToStringArray( out );
    		//run pig script
    		
    		try{
        		pigtest.assertOutput( inputAlias, tableIn, outputAlias, tableOut );    		    			
            	//Note, that loading input in pig script also works (while storing output not)
    		}catch( junit.framework.ComparisonFailure e ) {
    			//noting test fail information
    			fails.add( "- " + test + ": " + e.getMessage() + "\n" + "For more information look at console logs." );
    		}catch( Exception e ){
    			fails.add( "- " + test + ": " + StackTraceExtractor.getStackTrace(e) + "\n" );
    		}
    	}
		
    	//some outputs are incorrect
    	if ( !fails.isEmpty() ) {
    		StringBuffer m = new StringBuffer();
    		m.append( "Wrong output(s) from: '" + scriptPath + "' at tests: \n" );
    		for ( String fail : fails ) {
    			m.append( fail );
    		}
    		
    	   	throw new AssertionError( m.toString() );
    	}
    }
}

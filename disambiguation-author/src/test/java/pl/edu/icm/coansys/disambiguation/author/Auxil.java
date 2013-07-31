package pl.edu.icm.coansys.disambiguation.author;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Auxil {
	
	private static Scanner in;

	public static File fileExist( String filePathString ) throws FileNotFoundException {
		File f = new File( filePathString );
		
		if( !f.exists() ) {
			String m = "Cannot find script for test or test case file: ";
			m += "\n\t relative path: " + filePathString;
			m += "\n\t absolut path: " + f.getAbsolutePath();
			
			throw new FileNotFoundException( m );
		}
		return f;
	}
	
	public static String[] readTestToStringArray( final String filePath ) throws FileNotFoundException {
		List <String> ret = new LinkedList<String>();	
		File file = new File( filePath );
		in = new Scanner( file );
		
		while ( in.hasNext() ) {
			ret.add( in.nextLine() );
		}
		
		return ret.toArray( new String[]{} );
	}
	
}

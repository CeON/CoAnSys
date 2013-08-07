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

	public static File folderExist( String folderPathString ) throws FileNotFoundException {
		File f = new File( folderPathString );
		
		if( !f.isDirectory() ) {
			String m = "Cannot find folder: ";
			m += "\n\t relative path: " + folderPathString;
			m += "\n\t absolut path: " + f.getAbsolutePath();
			
			throw new FileNotFoundException( m );
		}
		return f;
	}	
	
	public static String[] readTableToStringArray( final String filePath ) throws FileNotFoundException {
		List <String> ret = new LinkedList<String>();	
		File file = new File( filePath );
		in = new Scanner( file );
		
		while ( in.hasNext() ) {
			ret.add( in.nextLine() );
		}
		
		return ret.toArray( new String[]{} );
	}
	
    public static List<String> getTestCaseNames( String folderAbsolutePath ) {
        File fileFolder = new File( folderAbsolutePath );
        LinkedList<String> res = new LinkedList<String>();
        
        for ( final File fileEntry : fileFolder.listFiles() ) {
            String name = fileEntry.getName();
            if ( name.endsWith( ".in" ) ) {
                String s = name.substring( 0, name.indexOf( ".in" ) );
                res.add(s);
            }
        }
        return res;
    }
}

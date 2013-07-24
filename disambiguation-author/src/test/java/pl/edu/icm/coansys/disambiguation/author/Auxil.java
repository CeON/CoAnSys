package pl.edu.icm.coansys.disambiguation.author;

import java.io.File;
import java.io.FileNotFoundException;

public class Auxil {
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
}

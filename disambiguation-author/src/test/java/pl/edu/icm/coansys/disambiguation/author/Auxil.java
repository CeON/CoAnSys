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

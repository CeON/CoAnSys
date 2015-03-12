/*
 * This file is part of CoAnSys project.
 * Copyright (c) 2012-2015 ICM-UW
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

package pl.edu.icm.coansys.commons.java;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.xml.transform.stream.StreamSource;

public final class ResourceManager {
    
        private ResourceManager() {}
    
	public static InputStream resourceToInputStream(Object obj,String localization){
		return obj.getClass().getClassLoader().getResourceAsStream(localization);
	}
	
	public static StreamSource resourceToStreamSource(Object obj,String localization){
		return new StreamSource(resourceToInputStream(obj, localization));
	}
	
	public static String resourceToString(Object obj,String localization) throws IOException{
		InputStream is = resourceToInputStream(obj, localization);
		BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
		String line;
		StringBuilder xmlInputSB = new StringBuilder(); 
		while((line=br.readLine())!=null){
			xmlInputSB.append(line).append("\n");
		}
		br.close();
		return xmlInputSB.toString();
	}
}

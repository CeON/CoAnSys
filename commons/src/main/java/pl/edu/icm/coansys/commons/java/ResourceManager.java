package pl.edu.icm.coansys.commons.java;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.xml.transform.stream.StreamSource;

public class ResourceManager {
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
		StringBuffer xmlInputSB = new StringBuffer(); 
		while((line=br.readLine())!=null){
			xmlInputSB.append(line            
					+"\n"
					);
		}
		br.close();
		return xmlInputSB.toString();
	}
}

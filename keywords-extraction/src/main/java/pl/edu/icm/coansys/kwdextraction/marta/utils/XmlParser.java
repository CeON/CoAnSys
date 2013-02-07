/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.kwdextraction.utils;

import java.util.ArrayList;


public class XmlParser {
	
	public ArrayList<String> getText(String text) {
		ArrayList<String> list = new ArrayList<String>();
		int i = 0; 
		String part = "";
		String temp = text;
		int partBegin = 0, partEnd = 0, toCutBegin = 0;
		while (i < text.length()) {
			if(text.charAt(i) == '<') {
				partEnd = i;
				if (partBegin != partEnd) {
					part = text.substring(partBegin, partEnd);
					list.add(part);
				}
				i++;
			}
			
			else if(text.charAt(i) == '>') {
				partBegin = i + 1;
				i++;
			}
			
			else if (text.charAt(i) == '&') {
				toCutBegin = i;
				while (i < text.length() && text.charAt(i) != ';' && text.charAt(i) != ' ') {
					i++;
				}
				temp = text.substring(0, toCutBegin);
				text = temp.concat(text.substring(i));
				i = toCutBegin;
			}
			else 
				i++;
		}
		part = text.substring(partBegin);
		list.add(part);
		return list;
	}
}

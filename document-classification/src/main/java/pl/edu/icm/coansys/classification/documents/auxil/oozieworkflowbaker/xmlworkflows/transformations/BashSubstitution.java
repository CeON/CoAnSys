package pl.edu.icm.coansys.classification.documents.auxil.oozieworkflowbaker.xmlworkflows.transformations;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BashSubstitution {

	final static String substitutedIfPresent = "seq(\n" +
			"";
	
	public static ArrayList<String[]> substitute(StringBuilder propsSb) throws IOException {
		BufferedReader br = new BufferedReader(new StringReader(propsSb.toString()));
		
		ArrayList<String[]> subs = new ArrayList<String[]>();
		
		for(String line = br.readLine();line!=null;line = br.readLine()){
			Matcher m = Pattern.compile("[\\w]+=[\\S]+").matcher(line);
			if(m.find()){
				String[] nameVal = line.substring(m.start(),m.end()).split("=");
				for(String[] sub : subs){
					nameVal[1] = nameVal[1].replace("${"+sub[0]+"}",sub[1]);
				}
				subs.add(nameVal);
			}
		}
		return subs;
	}

	public static StringBuilder applyBashSubstitution(StringBuilder sb,
			StringBuilder propsSb) throws IOException {
		
		ArrayList<String[]> variables = BashSubstitution.substitute(propsSb);
		String[] conditions = substitutedIfPresent.split("\n");
//		for(String[] sub : variables){
//			System.out.println(sub[0]+"\t"+sub[1]);
//		}
		
		BufferedReader br = new BufferedReader(new StringReader(sb.toString()));
		
		StringBuilder newSb = new StringBuilder();
		for(String line = br.readLine();line!=null;line = br.readLine()){
			boolean proceed = false;
			for(String s : conditions){
				if(line.indexOf(s)!=-1) proceed=true;
			}
			
			if(proceed) for(String[] sub : variables){
				line = line.replace("${"+sub[0]+"}", sub[1]);
			}
			newSb.append(line+"\n");
		}
		return newSb;
	}

}

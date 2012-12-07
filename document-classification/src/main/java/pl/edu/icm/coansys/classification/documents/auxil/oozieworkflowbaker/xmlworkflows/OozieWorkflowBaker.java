package pl.edu.icm.coansys.classification.documents.auxil.oozieworkflowbaker.xmlworkflows;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import pl.edu.icm.coansys.classification.documents.auxil.oozieworkflowbaker.xmlworkflows.transformations.Action;
import pl.edu.icm.coansys.classification.documents.auxil.oozieworkflowbaker.xmlworkflows.transformations.BashSubstitution;
import pl.edu.icm.coansys.classification.documents.auxil.oozieworkflowbaker.xmlworkflows.transformations.ForkMerge;
import pl.edu.icm.coansys.classification.documents.auxil.oozieworkflowbaker.xmlworkflows.transformations.InterpretReplace;
import pl.edu.icm.coansys.classification.documents.auxil.oozieworkflowbaker.xmlworkflows.transformations.Replace;


public class OozieWorkflowBaker {
	public static void main(String[] args) throws IOException, IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		if(args.length!=4){
			System.out.println("Usage:\n" +
					"java -cp thisJar thisClass inputScriptPath inputPropertiesParh partToReplaceInOriginalName replacementInFinalFileName");
			System.exit(1);
		}
		File in_file = new File(args[0]);
		String oldN = args[2];
		String newN = args[3];
		File out_file = new File(args[0].replaceAll(oldN, newN));
		
		File in_props = new File(args[1]);
		StringBuilder propsSb = readFileToStringBuilder(in_props);
		StringBuilder sb = readFileToStringBuilder(in_file);
		sb.append("\n");
//		System.out.println(sb);
		sb = new StringBuilder(sb.toString().replaceAll("<!--[\\S\\s.]+?-->", ""));
		//System.out.println(sb);
		sb = BashSubstitution.applyBashSubstitution(sb, propsSb);
//		System.out.println(sb);
		sb = Replace.substitute(sb);
//		System.out.println(sb);
		sb.delete(0, sb.indexOf("# BEG:REMOVE_UP_TO_HERE")+"# BEG:REMOVE_UP_TO_HERE".length()+1);
		sb = InterpretReplace.substitute(sb);
		//System.out.println(sb);
		sb = ForkMerge.substitute(sb);
		sb = Action.substitute(sb);
		int index=-1;
		while((index=sb.indexOf("\n\n\n"))!=-1){
			sb.delete(index, index+1);
		}
			
		FileWriter fw = new FileWriter(out_file);
		fw.write(sb.toString());
		fw.flush();
		fw.close();
		
		return;
	}

	private static StringBuilder readFileToStringBuilder(File in_file) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(in_file));
		StringBuilder sb = new StringBuilder();
		
		for(String line = br.readLine();line!=null;line = br.readLine()){
			sb.append(line+"\n");
		}
		br.close();
		return sb;
	}
	
	public static String escapeDollarSing(String text) {
		if (text.contains("$")) {
			StringBuffer sb = new StringBuffer();
			for (char c : text.toCharArray()) {
				if (c == '$') {
					sb.append("__DOLLAR_SIGN__");
				} else {
					sb.append(c);
				}
			}
			text = sb.toString();	
		}
		return text;
	}

	public static String revertDollarSing(String text) {
		return text.replaceAll("__DOLLAR_SIGN__", "\\$");
	}	
}

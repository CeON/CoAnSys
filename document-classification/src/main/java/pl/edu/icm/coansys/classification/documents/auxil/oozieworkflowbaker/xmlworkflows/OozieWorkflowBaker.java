package pl.edu.icm.coansys.classification.documents.auxil.oozieworkflowbaker.xmlworkflows;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pl.edu.icm.coansys.classification.documents.auxil.oozieworkflowbaker.xmlworkflows.transformations.ForkMerge;


public class OozieWorkflowBaker {
	public static void main(String[] args) throws IOException{
		if(args.length!=2){
			System.out.println("Usage: java Creator input_xml_file_path output_xml_file_path");
			System.exit(1);
		}
		
		File in_file = new File(args[0]);
		File out_file = new File(args[1]+"/"+System.nanoTime()+".xml");
		
		StringBuilder sb = readFileToStringBuilder(in_file);
		
		sb = ForkMerge.substitute(sb);
		
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
}

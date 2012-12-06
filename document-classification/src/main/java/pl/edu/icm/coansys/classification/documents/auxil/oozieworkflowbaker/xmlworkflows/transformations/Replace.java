package pl.edu.icm.coansys.classification.documents.auxil.oozieworkflowbaker.xmlworkflows.transformations;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Replace {

	public static StringBuilder substitute(StringBuilder sb) throws IOException {
		return considerSubstitutions(sb);
	}

	private static StringBuilder considerSubstitutions(StringBuilder sb) throws IOException {
		
		StringBuilder tmpSb = new StringBuilder(sb);
		
		while(true){
			int[] startStop = findStartStop(tmpSb);
			if(startStop[0]==-1) break;
			
			String[] nameVal = findNameVal(tmpSb, startStop);
			
			tmpSb.delete(startStop[0], startStop[1]);

			try{
//				if(tmpSb.toString().length()!=0 && 
	//				tmpSb.toString().length()!=0 &&
		//			tmpSb.toString().length()!=0)
			//	{
						tmpSb = new StringBuilder(tmpSb.toString().replace(
							nameVal[0],nameVal[1]));
				//}
			}catch(NullPointerException e){
				System.out.println(tmpSb.toString());
				System.out.println("!!!!!!!!!!!!!!!!!!!!!!!=============================!!!!!!!!!!!!");
				System.out.println(nameVal[0]);
				System.out.println("!!!!!!!!!!!!!!!!!!!!!!!=============================!!!!!!!!!!!!");
				System.out.println(nameVal[1]);
				System.out.println("=============================");
				System.out.println("=============================");
				System.out.println("=============================");
				System.out.println("=============================");
				System.out.println("=============================");
				System.out.println("=============================");
			}

		}
		
		return tmpSb;
	}
	
	private static String[] findNameVal(StringBuilder sb, int[] startStop) throws IOException {
		int start = startStop[0];
		int stop =  startStop[1];
		//sb.substring(start, stop);
		BufferedReader br = new BufferedReader(new StringReader(sb.substring(start, stop)));
		
		String header = br.readLine();
		String[] params = header.split(" ");
		
		String[] nameVal = new String[2];
		StringBuilder subs = new StringBuilder();
		
		for(String param : params){
			if(param.indexOf("@")!=-1)
				nameVal[0]=param;
		}
		
		for(String line = br.readLine(); line!=null;line=br.readLine()){
			if(line.indexOf("# END:REPLACE")==-1) subs.append(line+"\n");
		}
		
		nameVal[1]=subs.toString();
		return nameVal;
	}

	private static int[] findStartStop(StringBuilder sb) {
		int[] startStop = new int[]{-1,-1};
		
		Matcher matcher;
		matcher = Pattern.compile("[#]*[ \t]*BEG:REPLACE").matcher(sb);
		if(matcher.find()) startStop[0]=matcher.start();
		matcher = Pattern.compile("[#]*[ \t]*END:REPLACE").matcher(sb);
		if(matcher.find()) startStop[1]=matcher.end();
		return startStop;
	}
}

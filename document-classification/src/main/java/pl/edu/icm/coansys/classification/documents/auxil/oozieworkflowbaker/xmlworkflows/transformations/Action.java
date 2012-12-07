package pl.edu.icm.coansys.classification.documents.auxil.oozieworkflowbaker.xmlworkflows.transformations;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import pl.edu.icm.coansys.classification.documents.auxil.oozieworkflowbaker.xmlworkflows.auxiliar.WFList;


public class Action {

	public static StringBuilder substitute(StringBuilder sb) throws IOException {
		
		PresenceChecker pc = new PresenceChecker();
		
		List<String> samples = pc.extractSamples(sb,"ACTION");
		List<String> substituted = substituteSamples(samples);
		
		if(substituted.size()!=samples.size()){
			System.out.println("Something went wrong");
			System.exit(3);
		}
		
		sb = pc.rewriteScript(sb,substituted);
		
		return sb;
	}

	private static StringBuilder rewriteScript(StringBuilder sb,
			List<List<Integer>> startsEnds, List<String> samples, List<String> substituted) {
		int diff = 0;
		
		for(int i = 0; i<samples.size();i++){
			int sta = startsEnds.get(0).get(i);
			int sto = startsEnds.get(1).get(i);
			sb.replace(sta+diff,sto+diff, substituted.get(i));
			diff+=(substituted.get(i).length()-samples.get(i).length());
		}
		
		return sb;
	}

	private static List<String> substituteSamples(List<String> samples) throws IOException {
			
		ArrayList<String> subs_samples = new ArrayList<String>();
		
		for(String sample : samples){
			HashMap<String,String> params = new HashMap<String,String>(); 			 
			ArrayList<WFList> wflists = new ArrayList<WFList>();
			
			getForkMergeDefinition(sample, params,wflists);
			
			StringBuilder script =  getForMergeScript(sample, params,wflists);
			//System.out.println(script);
			subs_samples.add(script.toString());
		}
		
		return subs_samples;
	}

	private static StringBuilder getForMergeScript(String sample,HashMap<String,String> params,
			ArrayList<WFList> wflists) throws IOException {
		
		int numOfForks = 1;
		for(WFList wflist : wflists){
			numOfForks*=wflist.vals.size();
		}
		//build fork node
		StringBuilder script = new StringBuilder("\n\n\n\n");
		
	    ;
		
		script.append("	<action name='"+params.get("name")+"'>\n");
		addScriptLines(script,sample);
		script.append("		<ok to='"+params.get("ok")+"'/>\n");
		script.append("		<error to='"+params.get("error")+"'/>\n");
		script.append("	</action>\n");
		script.append("\n\n");
		
		
		return script;
	}

	private static void addScriptLines(StringBuilder script,String sample) throws IOException {
		BufferedReader br = new BufferedReader(new StringReader(sample));
		for(String line = br.readLine();line!=null;line=br.readLine()){
			//beg
			if(line.startsWith("#")) continue;
			//input parameters
			else if(line.matches("[a-zA-Z0-9_-]+=[a-zA-Z0-9_-]+[\\s]*")) continue;
			//lists
			else if(line.startsWith("@")) continue;
			//script
			else if(line.replaceAll("[ \t]*", "").length()>0){
				script.append(line+"\n");
			}
		}
		br.close();
	}

	private static void getForkMergeDefinition(String sample, HashMap<String,String> params, ArrayList<WFList> wflists) throws IOException {
		BufferedReader br = new BufferedReader(new StringReader(sample));
		for(String line = br.readLine();line!=null;line=br.readLine()){
			//beg
			if(line.startsWith("#")){
				String[] args = line.split(" ");
				for(String arg : args)
					if(arg.matches("[a-zA-Z0-9_-]+=[a-zA-Z0-9_-]+[\\s]*")){
						getMethodParams(params, arg);
					}
			}else if(line.matches("[a-zA-Z0-9_-]+=[a-zA-Z0-9_-]+[\\s]*")){
				getMethodParams(params, line);
			//lists
			}else if(line.startsWith("@")){
				wflists.add(new WFList(line.split(" ")));
			}
			//script
			else if(line.replaceAll("[ \t]*", "").length()>0) continue;
			//end
			else if(line.startsWith("#")) continue;
		}
		br.close();
	}

	private static void getMethodParams(HashMap<String,String> params, String line) {
		line=line.trim();
		params.put(line.split("=")[0], line.split("=")[1]);
	}



	

	

}

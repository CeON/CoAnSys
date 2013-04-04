package pl.edu.icm.coansys.classification.documents.auxil.oozieworkflowbaker.xmlworkflows.transformations;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import pl.edu.icm.coansys.classification.documents.auxil.oozieworkflowbaker.xmlworkflows.OozieWorkflowBaker;
import pl.edu.icm.coansys.classification.documents.auxil.oozieworkflowbaker.xmlworkflows.auxiliar.WFList;


public class ForkMerge {

	public static StringBuilder substitute(StringBuilder sb) throws IOException {
		
		
		PresenceChecker pc = new PresenceChecker();
		
		List<String> samples = pc.extractSamples(sb,"FORK_MERGE");
		List<String> substituted = substituteSamples(samples);
		
		if(substituted.size()!=samples.size()){
			System.out.println("Something went wrong");
			System.exit(3);
		}
		
		sb = pc.rewriteScript(sb,substituted);
		
		return sb;
	}

	/*private static StringBuilder rewriteScript(StringBuilder sb,
			List<List<Integer>> startsEnds, List<String> samples, List<String> substituted) {
		int diff = 0;
		
		for(int i = 0; i<samples.size();i++){
			int sta = startsEnds.get(0).get(i);
			int sto = startsEnds.get(1).get(i);
			sb.replace(sta+diff,sto+diff, substituted.get(i));
			diff+=(substituted.get(i).length()-samples.get(i).length());
		}
		
		return sb;
	}*/

	private static List<String> substituteSamples(List<String> samples) throws IOException {
			
		ArrayList<String> subs_samples = new ArrayList<String>();
		
		for(String sample : samples){
			HashMap<String,String> params = new HashMap<String,String>(); 			 
			ArrayList<WFList> wflists = new ArrayList<WFList>();
			
			getForkMergeDefinition(sample, params,wflists);
			
			StringBuilder script =  getForMergeScript(sample, params,wflists);
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
		
		script.append("    <fork name='").append(params.get("node_name")).append("'>\n");
		for(int i=0;i<numOfForks;i++){
			script.append("        <path start='").append(params.get("node_name")).append("_forked_node_").append(i).append("'/>\n");
		}
		script.append("    </fork>\n");
		script.append("\n\n");
		//build each fork-child node
		
		ArrayList<ArrayList<WFList.WFElement>> alalwfelement = WFList.createCrossProduct(wflists);
		
		int fork_num = 0;
		for(ArrayList<WFList.WFElement> elements : alalwfelement){
			script.append("    <action name='").append(params.get("node_name")).append("_forked_node_").append(fork_num).append("'>\n");
			addScriptLines(script,sample,elements);
			script.append("        <ok to='").append(params.get("node_name")).append("_join'/>\n");
			script.append("        <error to='").append(params.get("node_error")).append("'/>\n");
			script.append("    </action>\n");
			script.append("\n\n");
			fork_num++;
		}
		script.append("\n\n\n");
		script.append("    <join name='").append(params.get("node_name")).append("_join' to='").append(params.get("node_after_join")).append("'/>\n");
		script.append("\n\n\n");
		
		return script;
	}

	private static void addScriptLines(StringBuilder script,String sample, ArrayList<WFList.WFElement> elements) throws IOException {
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
				script.append(addThem(script,line,elements));
			}
		}
		br.close();
	}

	private static String addThem(StringBuilder script,String line, ArrayList<WFList.WFElement> elements) {
		for(WFList.WFElement element : elements){
			line = OozieWorkflowBaker.revertDollarSing(line.replaceAll(element.name, OozieWorkflowBaker.escapeDollarSing(element.val)));
		}
		return line+"\n";
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
			else continue;
		}
		br.close();
	}

	private static void getMethodParams(HashMap<String,String> params, String line) {
		line=line.trim();
		params.put(line.split("=")[0], line.split("=")[1]);
	}

	/*private static List<String> extractSamples(StringBuilder sb,
			List<List<Integer>> startsEnds) {
		ArrayList<String> ret = new ArrayList<String>(); 
		
		int numOfSamples = startsEnds.get(0).size();
		for(int i=0;i<numOfSamples;i++){
			int start = startsEnds.get(0).get(i);
			int end = startsEnds.get(1).get(i);
			ret.add(sb.substring(start,end));
		}
		return ret;
	}*/

}

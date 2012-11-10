package pl.edu.icm.coansys.classification.documents.auxil.oozieworkflowbaker.xmlworkflows.transformations;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pl.edu.icm.coansys.classification.documents.auxil.oozieworkflowbaker.xmlworkflows.auxiliar.WFList;
import pl.edu.icm.coansys.classification.documents.auxil.oozieworkflowbaker.xmlworkflows.auxiliar.WFList.WFElement;


public class ForkMerge {

	public static StringBuilder substitute(StringBuilder sb) throws IOException {
		
		if(!checkBegEnd(sb)){
			System.out.println("Num of '# BEG:FORK_MERGE' and '# END:FORK_MERGE' tags occurences are not equal");
			System.exit(2);
		}
		
		List<List<Integer>> startsEnds = findStartsAndEnds(sb);
		List<String> samples = extractSamples(sb,startsEnds);
		List<String> substituted = substituteSamples(samples);
		
		if(substituted.size()!=samples.size()){
			System.out.println("Something went wrong");
			System.exit(3);
		}
		
		sb = rewriteScript(sb,startsEnds,samples, substituted);
		
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
			
			StringBuilder node_name = new StringBuilder(); //name of node -- also prefix of forked nodes: name_fork_1 and join node: name_after_fork
			StringBuilder node_after_join = new StringBuilder(); //destination node after join node
			StringBuilder node_error =  new StringBuilder(); //destination of error node 
			ArrayList<WFList> wflists = new ArrayList<WFList>();
			
			getForkMergeDefinition(sample, node_name,node_after_join,node_error,wflists);
			
			StringBuilder script =  getForMergeScript(sample, node_name,node_after_join,node_error,wflists);
			subs_samples.add(script.toString());
		}
		
		return subs_samples;
	}

	private static StringBuilder getForMergeScript(String sample,
			StringBuilder node_name, StringBuilder node_after_join,
			StringBuilder node_error, ArrayList<WFList> wflists) throws IOException {
		
		int numOfForks = 1;
		for(WFList wflist : wflists){
			numOfForks*=wflist.vals.size();
		}
		//build fork node
		StringBuilder script = new StringBuilder("\n\n\n\n");
		script.append("    <fork name='"+node_name+"'>\n");
		for(int i=0;i<numOfForks;i++){
			script.append("        <path start='"+node_name+"_forked_node_"+i+"'/>\n");
		}
		script.append("    </fork>\n");
		script.append("\n\n");
		//build each fork-child node
		
		ArrayList<ArrayList<WFList.WFElement>> alalwfelement = WFList.createCrossProduct(wflists);
		
		int fork_num = 0;
		for(ArrayList<WFList.WFElement> elements : alalwfelement){
			script.append("    <action name='"+node_name+"_forked_node_"+fork_num+"'>\n");
			addScriptLines(script,sample,node_name,node_after_join,node_error,elements);
			script.append("        <ok to='"+node_name+"_join'/>\n");
			script.append("        <error to='"+node_error+"'/>\n");
			script.append("    </action>\n");
			script.append("\n\n");
			fork_num++;
		}
		script.append("\n\n\n");
		script.append("    <join name='"+node_name+"_join' to='"+node_after_join+"'/>\n");
		script.append("\n\n\n");
		
		return script;
	}

	private static void addScriptLines(StringBuilder script,String sample,
			StringBuilder node_name, StringBuilder node_after_join,
			StringBuilder node_error, ArrayList<WFList.WFElement> elements) throws IOException {
		BufferedReader br = new BufferedReader(new StringReader(sample));
		for(String line = br.readLine();line!=null;line=br.readLine()){
			//beg
			if(line.startsWith("#")) continue;
			//input parameters
			else if(line.matches("[a-zA-Z0-9_-]+=[a-zA-Z0-9_-]+")) continue;
			//lists
			else if(line.startsWith("@")) continue;
			//script
			else if(line.replaceAll("[ \t]*", "").length()>0){
				script.append(addThem(script,line,node_name,node_after_join,node_error,elements));
			}
			//end
			else if(line.startsWith("#")) continue;
		}
		br.close();
	}

	private static String addThem(StringBuilder script,String line,
			StringBuilder node_name, StringBuilder node_after_join,
			StringBuilder node_error, ArrayList<WFList.WFElement> elements) {
		for(WFList.WFElement element : elements){
			line = line.replaceAll(element.name, element.val);
		}
		return line+"\n";
	}

	private static void getForkMergeDefinition(String sample, StringBuilder node_name, StringBuilder node_after_join, StringBuilder node_error, ArrayList<WFList> wflists) throws IOException {
		BufferedReader br = new BufferedReader(new StringReader(sample));
		for(String line = br.readLine();line!=null;line=br.readLine()){
			//beg
			if(line.startsWith("#")) continue;
			//input parameters
			else if(line.matches("[a-zA-Z0-9_-]+=[a-zA-Z0-9_-]+")){
				String nameTMP = line.split("=")[0];
				String valTMP = line.split("=")[1];
				if("node_name".equals(nameTMP)){
					node_name.append(valTMP);
				}else if("node_after_join".equals(nameTMP)){
					node_after_join.append(valTMP);
				}else if("node_error".equals(nameTMP)){
					node_error.append(valTMP);
				}
			}
			//lists
			else if(line.startsWith("@")){
				wflists.add(new WFList(line.split(" ")));
			}
			//script
			else if(line.replaceAll("[ \t]*", "").length()>0) continue;
			//end
			else if(line.startsWith("#")) continue;
		}
		br.close();
	}

	private static List<String> extractSamples(StringBuilder sb,
			List<List<Integer>> startsEnds) {
		ArrayList<String> ret = new ArrayList<String>(); 
		
		int numOfSamples = startsEnds.get(0).size();
		for(int i=0;i<numOfSamples;i++){
			int start = startsEnds.get(0).get(i);
			int end = startsEnds.get(1).get(i);
			ret.add(sb.substring(start,end));
		}
		return ret;
	}

	private static List findStartsAndEnds(StringBuilder sb) {
		Matcher matcherSTA = Pattern.compile("[#]*[ \t]*BEG:FORK_MERGE").matcher(sb);
		
		ArrayList<Integer> starts = new ArrayList<Integer>(); 
		while(matcherSTA.find()) {
			starts.add(matcherSTA.start());
		}

		Matcher matcherEND = Pattern.compile("[#]*[ \t]*END:FORK_MERGE").matcher(sb);
		
		ArrayList<Integer> ends = new ArrayList<Integer>(); 
		while(matcherEND.find()) {
			ends.add(matcherEND.end());
		}
		
		ArrayList ret = new ArrayList();
		ret.add(starts);
		ret.add(ends);
		return ret;
	}

	private static boolean checkBegEnd(StringBuilder sb) {
		Matcher matcherSTA = Pattern.compile("[#]*[ \t]*BEG:FORK_MERGE").matcher(sb);
		int for_merge_start = 0;
		while(matcherSTA.find()) {
			for_merge_start++;
		}
			
		Matcher matcherEND = Pattern.compile("[#]*[ \t]*END:FORK_MERGE").matcher(sb);
		int for_merge_end = 0;
		while(matcherEND.find()) {
			for_merge_end++;
		}
		
		return for_merge_start==for_merge_end;
		
	}

}

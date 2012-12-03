package pl.edu.icm.coansys.classification.documents.auxil.oozieworkflowbaker.xmlworkflows.transformations;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PresenceChecker {
	
	private List<List<Integer>>  startsEnds;
	private List<String> samples;
	
	public StringBuilder rewriteScript(StringBuilder sb, List<String> substituted) {
		int diff = 0;
		
		for(int i = 0; i<samples.size();i++){
			int sta = startsEnds.get(0).get(i);
			int sto = startsEnds.get(1).get(i);
			sb.replace(sta+diff,sto+diff, substituted.get(i));
			diff+=(substituted.get(i).length()-samples.get(i).length());
		}
		
		return sb;
	}
	
	protected boolean checkBegEnd(StringBuilder sb,String suffix) {
		Matcher matcherSTA = Pattern.compile("[#]*[ \t]*BEG:"+suffix).matcher(sb);
		int for_merge_start = 0;
		while(matcherSTA.find()) {
			for_merge_start++;
		}
			
		Matcher matcherEND = Pattern.compile("[#]*[ \t]*END:"+suffix).matcher(sb);
		int for_merge_end = 0;
		while(matcherEND.find()) {
			for_merge_end++;
		}
		
		return for_merge_start==for_merge_end;
		
	}
	
	protected List findStartsAndEnds(StringBuilder sb,String suffix) {
		
		if(!checkBegEnd(sb,suffix)){
			System.out.println("Num of '# BEG:"+suffix+"' and '# END:"+suffix+"' tags occurences are not equal");
			System.exit(2);
		}
		
		Matcher matcherSTA = Pattern.compile("[#]*[ \t]*BEG:"+suffix).matcher(sb);
		
		ArrayList<Integer> starts = new ArrayList<Integer>(); 
		while(matcherSTA.find()) {
			starts.add(matcherSTA.start());
		}

		Matcher matcherEND = Pattern.compile("[#]*[ \t]*END:"+suffix).matcher(sb);
		
		ArrayList<Integer> ends = new ArrayList<Integer>(); 
		while(matcherEND.find()) {
			ends.add(matcherEND.end());
		}
		
		ArrayList ret = new ArrayList();
		ret.add(starts);
		ret.add(ends);
		startsEnds = ret;
		return ret;
	}
	
	public List<String> extractSamples(StringBuilder sb,String suffix) {
		
		List<List<Integer>> startsEnds = findStartsAndEnds(sb,suffix);
		ArrayList<String> ret = new ArrayList<String>(); 
		
		int numOfSamples = startsEnds.get(0).size();
		for(int i=0;i<numOfSamples;i++){
			int start = startsEnds.get(0).get(i);
			int end = startsEnds.get(1).get(i);
			ret.add(sb.substring(start,end));
		}
		samples=ret;
		return ret;
	}
}

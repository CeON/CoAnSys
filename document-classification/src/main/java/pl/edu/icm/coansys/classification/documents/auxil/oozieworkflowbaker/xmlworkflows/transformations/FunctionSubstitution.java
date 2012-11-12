package pl.edu.icm.coansys.classification.documents.auxil.oozieworkflowbaker.xmlworkflows.transformations;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FunctionSubstitution {

	public static StringBuilder substitute(StringBuilder sb) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Method[] methods = Substitutions.class.getDeclaredMethods();
		for(Method m : methods){
			sb = (StringBuilder) m.invoke(null, sb);
		}
		return sb;
	}

	private static class Substitutions{
		public static StringBuilder seqSubs(StringBuilder sb){
			
			StringBuilder retSb = new StringBuilder(sb);
			int diff = 0;
			
			Matcher matcher = Pattern.compile("seq\\(([0-9]+),([0-9]+),([0-9]+)\\)").matcher(retSb);
			 
			while(matcher.find()) {
				int start = matcher.start()+diff;
				int end = matcher.end()+diff;
				int from = Integer.parseInt(matcher.group(0));
				int to = Integer.parseInt(matcher.group(1));
				int step = Integer.parseInt(matcher.group(2));
				
				StringBuilder sb_seq = new StringBuilder();
				for(int i = from;i<to;i+=step){
					sb_seq.append(i+" ");
				}
				int length_new = sb_seq.length();
				int length_old = end - start;
				diff+=(length_new-length_old);
				
				retSb.replace(start, end, sb_seq.toString());
			}
			return retSb;
		}
	}
}

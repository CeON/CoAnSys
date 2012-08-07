/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.disambiguation.author.model.clustering.strategy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 
 * @author pdendek
 *
 */
public class SingleLinkageHACStrategy_LessThenZero_Or_Max extends SingleLinkageHACStrategy{

	private static final Logger log = LoggerFactory.getLogger(SingleLinkageHACStrategy_LessThenZero_Or_Max.class);
		
	public static void main(String[] args){
		double[][] in = {{},{15},{46,3},{2,-18,-20},{-100,-100,3,-200}};
		int[] out = new SingleLinkageHACStrategy_LessThenZero_Or_Max().clusterize(in);
		StringBuilder sb = new StringBuilder(""); 
		for(int i : out) sb.append(i+"\t");
		sb.append("\n");
		log.info(sb.toString());
	}
	
	protected double SIM(double a, double b) {
		return minMax(a,b);
	}
	
	protected double minMax(double a, double b){
		if(a<0||b<0)
			return Math.min(a, b);
		else return Math.max(a, b);
	}
	
	public Object clone(){
		return new SingleLinkageHACStrategy_LessThenZero_Or_Max();
	}	
}
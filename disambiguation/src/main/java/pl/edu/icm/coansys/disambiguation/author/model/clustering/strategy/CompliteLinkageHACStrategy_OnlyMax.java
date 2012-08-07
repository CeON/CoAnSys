/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.disambiguation.author.model.clustering.strategy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author pdendek
 *
 */
public class CompliteLinkageHACStrategy_OnlyMax extends SingleLinkageHACStrategy{
	
	private static final Logger log = LoggerFactory.getLogger(CompliteLinkageHACStrategy_OnlyMax.class);
	
	public static void main(String[] args){
		double[][] in = {{},{15},{-46,-3},{-2,-18,-20},{-100,-100,-3,-200}};
		int[] out = new CompliteLinkageHACStrategy_OnlyMax().clusterize(in);
		StringBuilder sb = new StringBuilder(""); 
		for(int i : out) sb.append(i+"\t");
		sb.append("\n");
		log.info(sb.toString());
	}
	
	protected double SIM(double a, double b) {
		return Math.min(a, b);
	}
	
	public Object clone(){
		return new CompliteLinkageHACStrategy_OnlyMax();
	}
}
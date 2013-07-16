/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.disambiguation.author.pig;

import java.util.LinkedList;

import org.apache.pig.backend.executionengine.ExecException;
import org.apache.pig.data.DataBag;
import org.apache.pig.data.Tuple;

import pl.edu.icm.coansys.disambiguation.features.Disambiguator;

/**
 * A heuristic for assessing whether two objects, described by two lists of feature values,
 * are similar or not.    
 * 
 * @author pdendek
 * @version 1.0
 * @since 2012-08-07
 */
public class PigDisambiguator extends Disambiguator{
	Disambiguator d = null;
	
	public PigDisambiguator(Disambiguator d){
		this.d = d;
	}
	
	public double calculateAffinity(Object f1, Object f2) throws Exception {
		if(f1 instanceof Tuple && f2 instanceof Tuple ){
			return calculateAffinity((Tuple) f1, (Tuple) f2);
		}else if(f1 instanceof DataBag && f2 instanceof DataBag ){
			return calculateAffinity((DataBag) f1, (DataBag) f2);
		}else{
			throw new Exception("data type "+ f1.getClass()+" unsupported in calculateAffinity");
		}
	}
	
	public double calculateAffinity(Tuple f1, Tuple f2) throws ExecException {
		LinkedList fl1 = new LinkedList();
		fl1.add(f1.get(0));
		LinkedList fl2 = new LinkedList();
		fl2.add(f2.get(0));
		return d.calculateAffinity(fl1, fl2);
	}
	
	public double calculateAffinity(DataBag f1, DataBag f2) {
		return d.calculateAffinity(ToList.execute(f1), ToList.execute(f2));
	}
	
	/**
	 * 
	 * @return {@link PigDisambiguator} id.
	 */
	public String getName(){
		return d.getName();
	}
}

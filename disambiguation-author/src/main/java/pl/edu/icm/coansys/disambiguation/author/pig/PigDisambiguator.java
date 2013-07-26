/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.disambiguation.author.pig;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

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
	
	public PigDisambiguator(Disambiguator d) {
		this.d = d;
	}
	
	//pytanie z cyklu poznajemy swiat: czy przypadkiem nie jest tak, ze polimorfizm tutaj nie zadziala
	//tak jak jest tu oczekiwane?
	public double calculateAffinity(Object f1, Object f2) throws Exception {
		if(f1 instanceof Tuple && f2 instanceof Tuple ){
			return calculateAffinity((Tuple) f1, (Tuple) f2);
		}else if(f1 instanceof DataBag && f2 instanceof DataBag ){
			return calculateAffinity((DataBag) f1, (DataBag) f2);
		}else if(f1 instanceof String && f2 instanceof String ){
			return calculateAffinity((String) f1, (String) f2);
		}else{
			throw new Exception("data type "+ f1.getClass()+" unsupported in calculateAffinity");
		}
	}
	
	public double calculateAffinity(Tuple f1, Tuple f2) throws ExecException {
		LinkedList<String> fl1 = new LinkedList<String>();
		fl1.add( (String) f1.get(0) );
		LinkedList<String> fl2 = new LinkedList<String>();
		fl2.add( (String) f2.get(0) );
		
		return d.calculateAffinity(fl1, fl2);
	}
	

	public double calculateAffinity(DataBag f1, DataBag f2) {
		return d.calculateAffinity(ToList.execute(f1), ToList.execute(f2));
	}
	
	// TODO zajrzec do artykulu, jak nie bedzie info to spytac Piotra czy warto uzyc equalize
	/*@SuppressWarnings("unused")
	private String equalize( String str ) {
		str = str.replace( ",", "" );
		str = str.replace( ".", "" );
		str = str.toLowerCase();
		return str;
	}*/
	
	public double calculateAffinity(String f1, String f2) {
		List <String> fl1 = Arrays.asList( f1.split(" ") );
		List <String> fl2 = Arrays.asList( f2.split(" ") );
		return d.calculateAffinity( fl1, fl2 );
	}
	
	/**
	 * 
	 * @return {@link PigDisambiguator} id.
	 */
        @Override
	public String getName(){
		return d.getName();
	}
}

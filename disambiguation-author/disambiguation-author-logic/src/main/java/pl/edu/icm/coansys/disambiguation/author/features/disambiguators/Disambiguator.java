package pl.edu.icm.coansys.disambiguation.author.features.disambiguators;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.AbstractMap.SimpleEntry;


public abstract class Disambiguator {

	public abstract double calculateAffinity(List<Object> f1, List<Object> f2);
	protected double weight = 1;
	protected double maxVal = 1;
	
	public void setWeight(double weight) {
		this.weight = weight;
	}

	public void setMaxVal(double maxVal) {
		this.maxVal = maxVal;
	}
	
	public Disambiguator () {}
	
	public Disambiguator( double weight, double maxVal ) {
		this.weight = weight;
		this.maxVal = maxVal;
	}

	// O( f1.size() + f2.size() )
	public SimpleEntry<Integer, Integer> intersectionAndSum(
			List<Object> f1, List<Object> f2) {
		
		Set <Object> all = new HashSet<Object>( f1.size() + f2.size() );
		
		all.addAll( f1 );
		int sum = f1.size(), intersection = 0;
		
		for ( Object o : f2 ) {
			if ( all.add( o ) ) {
				sum++;
			} else {
				intersection++;
			}
		}
		return new SimpleEntry<Integer, Integer>(intersection, sum);
	}
}

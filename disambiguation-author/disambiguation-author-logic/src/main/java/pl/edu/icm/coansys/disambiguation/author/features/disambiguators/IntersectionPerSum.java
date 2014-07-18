package pl.edu.icm.coansys.disambiguation.author.features.disambiguators;

import java.util.AbstractMap.SimpleEntry;
import java.util.List;

public class IntersectionPerSum extends Disambiguator {

	public IntersectionPerSum() {
		super();
	}

	public IntersectionPerSum(double weight, double maxVal) {
		//maxVal - unused
		super(weight, maxVal);
	}

	@Override
	public double calculateAffinity(List<Object> f1, List<Object> f2) {
		SimpleEntry<Integer, Integer> p = intersectionAndSum(f1, f2);
		int intersection = p.getKey();
		int sum = p.getValue();

		// Note that inf * 0 is indeterminate form (what gives NaN)
		if ( intersection == 0 ) {
			return 0;
		}
		
		return (double) intersection / sum * weight;
	}

}

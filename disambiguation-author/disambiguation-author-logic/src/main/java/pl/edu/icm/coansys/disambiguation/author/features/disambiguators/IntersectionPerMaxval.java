package pl.edu.icm.coansys.disambiguation.author.features.disambiguators;

import java.util.AbstractMap.SimpleEntry;
import java.util.List;

public class IntersectionPerMaxval extends Disambiguator {

	public IntersectionPerMaxval() {
		super();
	}

	public IntersectionPerMaxval(double weight, double maxVal) {
		super(weight, maxVal);
		if (maxVal == 0) {
			throw new IllegalArgumentException("Max value cannot equal 0.");
		}
	}

	@Override
	public void setMaxVal(double maxVal) {
		if (maxVal == 0) {
			throw new IllegalArgumentException("Max value cannot equal 0.");
		}
		this.maxVal = maxVal;
	}

	@Override
	public double calculateAffinity(List<Object> f1, List<Object> f2) {
		SimpleEntry<Integer, Integer> p = intersectionAndSum(f1, f2);
		int intersection = p.getKey();
		
		// Note that inf * 0 is indeterminate form (what gives NaN)
		if ( intersection == 0 ) {
			return 0;
		}
		return (double) intersection / maxVal * weight;
	}

}

package pl.edu.icm.coansys.disambiguation.author.features.disambiguators;

import java.util.AbstractMap.SimpleEntry;
import java.util.List;

public class IntersectionPerMaxval extends Disambiguator {

	public IntersectionPerMaxval() {
		super();
	}

	public IntersectionPerMaxval(double weight, double maxVal) {
		//maxVal - unused
		super(weight, maxVal);
	}

	@Override
	public double calculateAffinity(List<Object> f1, List<Object> f2) {
		SimpleEntry<Integer, Integer> p = intersectionAndSum(f1, f2);
		int intersection = p.getKey();

		if (maxVal == 0) {
			return 0;
		}

		return (double) intersection / maxVal * weight;
	}

}

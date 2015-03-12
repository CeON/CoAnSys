/*
 * This file is part of CoAnSys project.
 * Copyright (c) 2012-2015 ICM-UW
 * 
 * CoAnSys is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * CoAnSys is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with CoAnSys. If not, see <http://www.gnu.org/licenses/>.
 */

package pl.edu.icm.coansys.disambiguation.clustering;

/**
 * A container gathering all informations about a similarity matrix cell
 * 
 * @author pdendek
 * @version 1.0
 * @since 2012-08-07
 */

public class ClusterElement implements Comparable<Object> {
	private float sim;
	private int index;

	public ClusterElement(float sim, int index) {
		this.sim = sim;
		this.index = index;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public float getSim() {
		return sim;
	}

	public void setSim(float sim) {
		this.sim = sim;
	}
	public static String toStringSim(ClusterElement ce) {
		if(ce==null){
			return "null";
		}
		StringBuilder sb = new StringBuilder(" sim="+ce.sim+" ");
		return sb.toString();
	}
	public static String toString(ClusterElement ce) {
		if(ce==null){
			return "null";
		}
		StringBuilder sb = new StringBuilder("(index=");
		sb.append(ce.index);
		sb.append(", sim=");
		sb.append(ce.sim);
		sb.append(")");
		return sb.toString();
	}
	public String toString() {
		StringBuilder sb = new StringBuilder("(index=");
		sb.append(index);
		sb.append(", sim=");
		sb.append(sim);
		sb.append(")");
		return sb.toString();
	}

	@Override
	public boolean equals(Object o2) {
		if (o2 == null) {
			return false;
		}
		if (!(o2 instanceof ClusterElement)) {
			throw new ClassCastException("" + "Comparison between "
					+ this.getClass() + " and " + o2.getClass()
					+ " is illegal!");
		}
		
		ClusterElement ce2 = (ClusterElement) o2;
		return ce2.sim == this.sim && ce2.index == this.index;
	}

	@Override
	public int compareTo(Object o2) {
		if (o2 == null) {
			return 1;
		}
		if (!(o2 instanceof ClusterElement)) {
			throw new ClassCastException("" + "Comparison between "
					+ this.getClass() + " and " + o2.getClass()
					+ " is illegal!");
		}
		float count = this.sim - ((ClusterElement) o2).sim;
		if (count > 0) {
			return 1;
		} else if (count == 0) {
			return 0;
		} else {
			return -1;
		}
	}
}
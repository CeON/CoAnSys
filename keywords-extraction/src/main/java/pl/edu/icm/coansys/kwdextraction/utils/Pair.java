/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.kwdextraction.utils;

public class Pair<P1, P2> {
	protected P1 first;
	protected P2 second;
	
	Pair() {
		super();
	}
	
	Pair(P1 f, P2 s) {
		first = f;
		second = s;
	}
	
	public P1 getFirst() {
		return first;
	}
	public void setFirst(P1 first) {
		this.first = first;
	}
	public P2 getSecond() {
		return second;
	}
	public void setSecond(P2 second) {
		this.second = second;
	}
	
	@Override
	public String toString() {
		return first.toString().concat(" ").concat(second.toString());
	}
	
}

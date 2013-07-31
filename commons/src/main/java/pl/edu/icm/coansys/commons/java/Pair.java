/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.commons.java;

public class Pair<K,V> {
	public Pair(K x, V y) {
		this.x = x;
		this.y = y;
	}
	
	private K x;
	private V y;
	
	public K getX() {
		return x;
	}
	public void setX(K x) {
		this.x = x;
	}
	public V getY() {
		return y;
	}
	public void setY(V y) {
		this.y = y;
	}
}

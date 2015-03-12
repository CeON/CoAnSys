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

package pl.edu.icm.coansys.disambiguation.auxil;

import java.util.Comparator;
import java.util.PriorityQueue;

public class UniquePriorityQueue<E> extends PriorityQueue<E> {
	private static final long serialVersionUID = 1L;

	public UniquePriorityQueue(){
		super();
	}
	public UniquePriorityQueue(int i){
		super(i);
	}
	public UniquePriorityQueue(int i, Comparator<E> comparator) {
		super(i, comparator);
	}

	public boolean add(E e) {
		boolean isAdded = false;
		if (!super.contains(e)) {
			isAdded = super.add(e);
		}
		return isAdded;
	}

	public static void main(String args[]) {
		PriorityQueue<Integer> p = new UniquePriorityQueue<Integer>();
		p.add(10);
		p.add(20);
//		p.add(10);
		for (int i = 0; i <= 2; i++) {
			System.out.println(p.poll());
		}

	}
}
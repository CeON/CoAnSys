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
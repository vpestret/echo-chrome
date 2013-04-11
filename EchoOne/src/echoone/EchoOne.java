package echoone;

import java.util.Random;

import echoone.adt.DList;
import echoone.adt.Heap;
import echoone.adt.Order;

class SomethingOrdered implements Order {
	private final int m_x;
	
	public SomethingOrdered(int x) { m_x = x; }
		
	public int compare(Order o) {
		SomethingOrdered so;
		
		so = (SomethingOrdered)o;
		if(m_x < so.m_x) { return -1; }
		if(m_x > so.m_x) { return 1; }
		return 0;
	}
	
	public String toString() { return String.format("%d", m_x); }
}


public class EchoOne {
	
	static void test_DList(int n) {
		long dt;
		Random r;
		int i;
		int ii;
		long s;
		DList<Integer> d;
		
		r = new Random();
		d = new DList<Integer>();
		dt = -System.nanoTime();
		
		s = 0;
		for(i = 0; i < n; ++i) {
			int x;
			
			x = r.nextInt();
			d.insert_after_end(new Integer(x));
			s += x;
		}
		dt += System.nanoTime();
		System.err.println("sum = " + s);
		System.err.println(dt  + " used to insert " + n + " elements, " + (dt * 1.0 / n) + " ns / element");
		

		s = 0;
		ii = 0;
		dt = -System.nanoTime();
		for(; !d.is_empty(); ) { s += d.pop_tail(); ++ii; }
		dt += System.nanoTime();
		System.err.println("sum = " + s);
		System.err.println(dt  + " used to add " + ii + " elements, " + (dt * 1.0 / n) + " ns / element");
		
	}
	
	static void test_Heap(int n) {
		int i;
		Random r;
		long dt;
		Heap h;
		
		r = new Random();
		r.setSeed(0);
		h = new Heap();
		dt = -System.nanoTime();
		for(i = 0; i < n; ++i) { h.insert(new SomethingOrdered(Math.abs(r.nextInt() % n))); }
		dt += System.nanoTime();
		System.err.println(dt  + " used to insert " + n + " elements, " + (dt * 1.0 / n) + " ns / element");
		for(i = 0; i < n; ++i) { System.err.println((SomethingOrdered)h.delete_min()); }
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//test_DList(1000000);
		test_Heap(100000);
	}

}

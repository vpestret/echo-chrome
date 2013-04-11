package echoone.adt;

public class Heap {
	public static int m_def_cap = 128;
	private int m_n;
	private int m_cap;
	private Order[] m_arr;
	
	public Heap() {
		m_cap = m_def_cap;
		m_arr = new Order[m_def_cap + 1];
		m_n = 0;
	}

	boolean less(Order a, Order b) { return a.compare(b) == -1; }
	
	void recap() {
		Order[] arr;
		
		m_cap *= 2;
		arr = new Order[m_cap * 2 + 1];
		System.arraycopy(m_arr, 1, arr, 1, m_n);
		m_arr = arr;
	}
	
	public boolean is_empty() { return m_n == 0; }
	
	public int len() { return m_n; }
	
	public void insert(Order c) {
		int j;

		if(m_n == m_cap) { recap(); }
		j = ++m_n;
		while(j > 1 && c.compare(m_arr[j / 2]) < 0) {
			m_arr[j] = m_arr[j / 2];
			j /= 2;
		}
		m_arr[j] = c;
	}

	
	public Order delete_min() {
		Order c;
		Order x;
		int k;
		int j;
		
		if(m_n == 0) { return null; }
		
		c = m_arr[1];
		x = m_arr[1] = m_arr[m_n];
		--m_n;
		if(m_n == 0) { return c; }
		
		k = 1;
		while(2 * k <= m_n) {
			j = 2 * k;
			if(j + 1 <= m_n && m_arr[j + 1].compare(m_arr[j]) < 0) { j += 1; }
			if(x.compare(m_arr[j]) > 0) { m_arr[k] = m_arr[j]; }
			else break;
			k = j;
		}
		m_arr[k] = x;
		return c;
	}
	
	public Order peek_min() {
		return m_arr[0];
	}
}

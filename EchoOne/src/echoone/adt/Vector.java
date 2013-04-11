package echoone.adt;

public class Vector <E> {
	public static int m_def_cap = 128;
	
	private int m_cap;
	private int m_n;
	private Object[] m_sto;
	
	public Vector() {
		m_cap = m_def_cap;
		m_n = 0;
		m_sto = new Object[m_cap]; 
	}
	
	private void recap() {
		Object[] oa;
		
		m_cap = 2 * m_cap;
		oa = new Object[m_cap];
		System.arraycopy(m_sto, 0, oa, 0, m_n);
	}
	
	public void push(E e) {
		if(m_n == m_cap) { recap(); }
		m_sto[m_n++] = e;
	}
	
	@SuppressWarnings("unchecked")
	public E pop() {
		if(m_n == 0) { return null; }
		return (E)m_sto[--m_n];
	}
	
	@SuppressWarnings("unchecked")
	public E index_get(int i) { return (E)m_sto[i]; }
	
	public void index_set(int i, E e) { m_sto[i] = e; }   
}

package echoone.adt;

public class Queue<E> {
	public static int m_def_cap = 128;	
	private Object[] m_sto;
	private int m_cap;
	private int m_n;
	private int m_head;
	private int m_tail;
	
	public Queue() {
		m_cap = m_def_cap;
		m_sto = new Object[m_cap];
		m_head = 0;
		m_tail = 1;
		m_n = 0;
	}
	
	public int len() { return m_n; }
	
	private void recap() {
		Object[] oa;
		
		oa = new Object[2 * m_cap];
		System.arraycopy(m_sto, m_head, oa, 0, m_cap - m_head);
		if(m_tail < m_head) { System.arraycopy(m_sto, 0, oa, m_cap - m_head, m_tail); }
		m_cap = 2 * m_cap;		
		m_sto = oa;
	}
	
	private int dec(int v) { return (v - 1 + m_cap) % m_cap; }
	private int inc(int v) { return (v + 1 + m_cap) % m_cap; }
	
	public void push_first(E e) {
		if(m_n == m_cap) { recap(); }
		m_head = dec(m_head);
		m_sto[m_head] = e;
	}
	
	public void push_last(E e) {
		if(m_n == m_cap) { recap(); }
		m_sto[m_tail] = e;
		m_tail = inc(m_tail);
	}
	
	@SuppressWarnings("unchecked")
	public E peek_first() { return (E)m_sto[m_head]; }
	@SuppressWarnings("unchecked")
	public E peek_last() {  return (E)m_sto[dec(m_tail)]; }
	@SuppressWarnings("unchecked")
	public E peek_index(int i) { return (E)m_sto[(m_head + i) % m_cap]; }
	
	@SuppressWarnings("unchecked")
	public E pop_first() {
		E e;
		
		e = (E)m_sto[m_head];
		m_head = inc(m_head);
		return e;
	}

	@SuppressWarnings("unchecked")
	public E pop_last() {
		m_tail = dec(m_tail);
		return (E)m_sto[dec(m_tail)];
	}
} 

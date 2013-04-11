package echoone.adt;

public class DList<E> {
	class DListEntry {
		DListEntry m_prev;
		DListEntry m_next;
		E m_e;
		
		DListEntry(E e) { m_e = e; m_prev = m_next = null; } 
	}
	
	private DListEntry m_head;
	private DListEntry m_tail;
	private DListEntry m_curr;
	
	public DList() { m_head = m_tail = m_curr = null; }
	public boolean is_empty() { return m_head == null || m_tail == null; }
	
	public E get_head() { return m_head.m_e; }
	public E get_tail() { return m_tail.m_e; } 
	public E get_curr() { return m_curr.m_e; }
	
	public void rw() { m_curr = m_head; }
	public void ff() { m_curr = m_tail; }

	public E curr() {
		if(m_curr == null) { return null; }
		return m_curr.m_e; 
	}
	
	public E prev() {
		E e;
		
		if(m_curr == null) { return null; }
		e = m_curr.m_e;
		m_curr = m_curr.m_prev;
		if(m_curr == null) { return null; }
		return e;
	}
	
	public E next() {
		E e;
		
		if(m_curr == null) { return null; }
		e = m_curr.m_e;
		m_curr = m_curr.m_next;
		if(m_curr == null) { return null; }
		return e;
	}
	
	public void insert_after_end(E e) {
		DListEntry dle;
		
		dle = new DListEntry(e);
		if(m_tail == null) {
			m_tail = dle;
			if(m_head == null) { m_head = dle; }
		} else {
			dle.m_prev = m_tail;
			m_tail.m_next = dle;
			m_tail = dle;
		}
	}

	public void insert_before_start(E e) {
		DListEntry dle;
		
		dle = new DListEntry(e);
		if(m_head == null) {
			m_head = dle;
			if(m_tail == null) { m_tail = dle; }
		} else {
			dle.m_next = m_head;
			m_head.m_prev = dle;
			m_head = dle;
		}
	}

	public void insert_after_curr(E e) {
		DListEntry dle;
		DListEntry next;
		
		dle = new DListEntry(e);
		if(m_curr == null) { insert_after_end(e); return; } 
		
		next = m_curr.m_next;
		m_curr.m_next = dle;
		dle.m_next = next;
		dle.m_prev = m_curr;
		if(next != null) { next.m_prev = dle; }
	}
	
	public void insert_before_curr(E e) {
		DListEntry dle;
		DListEntry prev;
		
		dle = new DListEntry(e);
		if(m_curr == null) { insert_before_start(e); return; }
		
		prev = m_curr.m_prev;
		m_curr.m_prev = dle;
		dle.m_prev = prev;
		dle.m_next = m_curr;
		if(prev != null) { prev.m_next = dle; }
	}
	
	public E pop_tail() {
		E e;
		DListEntry t;
		
		if(m_tail == null) { return null; }
		e = m_tail.m_e;
		t = m_tail;
		m_tail = m_tail.m_prev;
		if(m_tail == null) { m_head = null; }
		t.m_next = t.m_prev = null; 
		t.m_e = null;
		return e;
	}
	
	public E pop_head() {
		E e;
		DListEntry t;
		
		if(m_head == null) { return null; }
		e = m_head.m_e;
		t = m_head;
		m_head = m_head.m_next;
		if(m_head == null) { m_tail = null; }
		t.m_next = t.m_prev = null; 
		t.m_e = null;
		return e;
	}
	
	private void delete() {
		DListEntry prev;
		DListEntry next;
		
		if(m_curr == null) { return; }
		
		if(m_curr == m_head) { pop_head(); return; }
		if(m_curr == m_tail) { pop_tail(); return; }
		
		prev = m_curr.m_prev;
		next = m_curr.m_next;
		if(prev != null) { prev.m_next = next; }
		if(next != null) { next.m_prev = prev; }
		
		m_curr.m_next = m_curr.m_prev = null; 
		m_curr.m_e = null;
	}
	
	public void delete_and_next() {
		DListEntry next;
		
		if(m_curr == null) { return; }
		next = m_curr.m_next;
		delete();
		m_curr = next;
	}
	
	public void delete_and_prev() {
		DListEntry prev;
		
		if(m_curr == null) { return; }
		prev = m_curr.m_prev;
		delete();
		m_curr = prev;
	}
}


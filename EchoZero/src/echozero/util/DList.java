package echozero.util;

public class DList <E> {
	private DListItem<E> m_head;
	private DListItem<E> m_tail;
	private DListItem<E> m_curr;
	
	public DListItem<E> get_head() { return m_head; }
	public DListItem<E> get_tail() { return m_tail; }
	
	public DList() { m_head = m_tail = null; }
	
	public void rw() { m_curr = m_head; }
	public void ff() { m_curr = m_tail; }
	
	public boolean is_empty() { return m_head == null; }
	public boolean is_end() { return m_curr == null; }
	public boolean is_tail() { return m_curr == m_tail; }
	public boolean is_head() { return m_curr == m_head; }
	
	public void next() { m_curr = m_curr.get_next(); }
	public void prev() { m_curr = m_curr.get_prev(); }
	
	public E get() { return m_curr.get_node(); } 

	public void push_front(E e) {
		DListItem<E> di;
		
		di = new DListItem<E>(e);
		di.set_next(m_head);
		if(m_head != null) { m_head.set_prev(di); }
		m_head = di;
		if(m_tail == null) { m_tail = di; }
	}
	
	public void push_back(E e) {
		DListItem<E> di;
		
		di = new DListItem<E>(e);
		di.set_prev(m_tail);
		if(m_tail != null) { m_tail.set_next(di); }
		m_tail = di;
		if(m_head == null) { m_head = di; }
	}
	
	public void insert_after_curr(E e) {
		DListItem<E> di;
		DListItem<E> post;
		
		if(m_curr == null) { return; }
		if(m_curr == m_tail) { push_back(e); }
		
		di = new DListItem<E>(e);
		post = m_curr.get_next();
		
		post.set_prev(di);
		di.set_prev(m_curr);
		di.set_next(post);
		m_curr.set_next(di);
	}
	
	public void insert_before_curr(E e) {
		DListItem<E> di;
		DListItem<E> pre;
		
		if(m_curr == null) { return; }
		if(m_curr == m_head) { push_front(e); return; }
		
		di = new DListItem<E>(e);
		pre = m_curr.get_prev();
		
		pre.set_next(di);
		di.set_next(m_curr);
		di.set_prev(pre);
		m_curr.set_prev(di);
	}
	
	public E pop_front() {
		E e;
		DListItem<E> post;
		
		if(is_empty()) { return null; }
		
		post = m_head.get_next();
		e = m_head.get_node();
		m_head.set_next(null);
		m_head = post;
		if(post != null) { post.set_prev(null); }
		else { m_tail = null; }
		return e;
	}
	
	public E pop_back() {
		E e;
		DListItem<E> pre;
		
		if(is_empty()) { return null; }
		pre = m_tail.get_prev();
		e = m_tail.get_node();
		m_tail.set_prev(null);
		m_tail = pre;
		if(pre != null) { pre.set_prev(null); }
		else { m_head = null; }
		return e;
	}
	
	public void delete_and_move_forward() {
		DListItem<E> pr;
		DListItem<E> fw;
		
		if(m_curr == null || is_empty()) { return; }
		pr = m_curr.get_prev();
		fw = m_curr.get_next();
		
		if(m_curr.get_prev() != null) { m_curr.get_prev().set_next(fw); }
		if(m_curr.get_next() != null) { m_curr.get_next().set_prev(pr); }
		m_curr = fw;
	}

	public void delete_and_move_back() {
		DListItem<E> pr;
		DListItem<E> fw;
		
		if(m_curr == null || is_empty()) { return; }
		pr = m_curr.get_prev();
		fw = m_curr.get_next();
		
		if(m_curr.get_prev() != null) { m_curr.get_prev().set_next(fw); }
		if(m_curr.get_next() != null) { m_curr.get_next().set_prev(pr); }
		m_curr = pr;
	}
}


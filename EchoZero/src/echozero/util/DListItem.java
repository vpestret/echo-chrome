package echozero.util;

public class DListItem <E> {
	private E m_node;
	private DListItem<E> m_prev;
	private DListItem<E> m_next;
	
	public DListItem(E e) {
		m_node = e;
		m_prev = m_next = null;
	}
	
	E get_node() { return m_node; }
	DListItem<E> get_prev() { return m_prev; }
	DListItem<E> get_next() { return m_next; }
	
	void set_prev(DListItem<E> di) { m_prev = di; }
	void set_next(DListItem<E> di) { m_next = di; }
}

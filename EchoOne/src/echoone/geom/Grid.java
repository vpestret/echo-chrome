package echoone.geom;

import echoone.adt.DList;

public class Grid {
	class GridElement {
		DList<BBox> m_list;
		public GridElement() { m_list = new DList<BBox>(); }
	}
	GridElement[] m_grid;
	
	public Grid(int n) {
		m_grid = new GridElement[n * n];
	}
	
	public void add_box(BBox bb) {
		
	}
	
	public DList<BBox> overlap(BBox bb, boolean touch) {
		DList<BBox> result;
		
		result = new DList<BBox>();
		return result;
	}
	
	public DList<BBox> clip(BBox bb) {
		DList<BBox> result;
		
		result = new DList<BBox>();
		return result;
	}
}

package minebot;

public class AStarBinaryHeap extends AStarList {
	
	public AStarBinaryHeap() {
		list.add(null);
	}	
	
	public void add(PathBlock i) {
		list.add(i);
		int m = list.size()-1;
		while (m != 1) {
			int n = m/2;
			if (getf(m) < getf(n)) {
				swap(m, n);
				m = m/2;
			} else {
				break;
			}
		}
	}
	
	public PathBlock pop() {
		if (list.size() == 2) {
			return list.remove(1);
		}
		PathBlock retval = list.get(1);
		list.set(1, list.remove(list.size()-1));
		int v = 1;
		while(true) {
			int u = v;
			if (2*u+1 < list.size()) {
				if (getf(u) >= getf(u*2))
					v = 2*u;
				if (getf(v) >= getf(u*2+1))
					v = u*2+1;
			} else if (2*u < list.size()) {
				if (getf(u) >= getf(2*u)) {
					v = 2*u;
				}
			}
			if (u != v) {
				swap(u, v);
			} else {
				break;
			}
		}
		return retval;
	}
	
	private double getf(int i) {
		return list.get(i).f;
	}
	
	private void swap(int m, int n) {
		PathBlock temp = list.get(m);
		list.set(m, list.get(n));
		list.set(n, temp);
	}
	
	public boolean contains(int x, int y, int z) {
		for (int i = 1; i < list.size(); i++) {
			PathBlock c = list.get(i);
			if (x == c.x && y == c.y && z == c.z) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isEmpty() {
		return list.size() < 2;
	}
}

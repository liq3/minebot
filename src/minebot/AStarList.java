package minebot;

import java.util.Vector;

public class AStarList {
	private Vector<PathBlock> list;
	
	public AStarList() {
		list = new Vector<PathBlock>();
	}
	
	public void add(PathBlock b) {
		list.add(b);
	}
	
	public boolean contains(PathBlock b) {
		return contains(b.x, b.y, b.z); 
	}
	
	public boolean contains(int x, int y, int z) {
		for (int i = 0; i < list.size(); i++) {
			PathBlock c = list.get(i);
			if (x == c.x && y == c.y && z == c.z) {
				return true;
			}
		}
		return false;
	}
	
	public PathBlock pop() {
		return list.remove(0);
	}
	
	public void quicksort() {
		quicksort(0, list.size()-1);
	}
	
	private void quicksort(int left, int right) {
		int index = partition(left, right);
		if (left < index -1) {
			quicksort(left, index-1);
		}
		if (index < right) {
			quicksort(index, right);
		}
	}
	
	private int partition(int left, int right) {
		int i = left, j = right;
		PathBlock temp;
		int pivot = list.get((left+right)/2).f;
		
		while (i <= j) {
			while (list.get(i).f < pivot) 
				i++;
			while (list.get(j).f > pivot)
				j--;
			if	(i <= j) {
				temp = list.get(i);
				list.set(i, list.get(j));
				list.set(j, temp);
				i++;
				j--;
			}
		}
		
		return i;
	}
	
	public boolean isEmpty() {
		return list.isEmpty();
	}
	
	public PathBlock last() {
		return list.lastElement();
	}
}

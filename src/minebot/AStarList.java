package minebot;

import java.util.ArrayList;

public class AStarList {
	protected ArrayList<PathBlock> list;
	
	public AStarList() {
		list = new ArrayList<PathBlock>();
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
	
	public PathBlock get(int i) {
		return list.get(i);
	}
	
	public PathBlock remove(int i) {
		return list.remove(i);
	}
	
	public boolean isEmpty() {
		return list.isEmpty();
	}
	
	public void set(int i, PathBlock b) {
		list.set(i, b);
	}
	
	public int size() {
		return list.size();
	}
}

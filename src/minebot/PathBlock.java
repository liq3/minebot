package minebot;

public class PathBlock {
	public int x,y,z, g, h, f;
	public PathBlock parent;
	public PathBlock (int x, int y, int z, int dx, int dy, int dz, PathBlock parent) {
		this.x = x; this.y = y; this.z = z; 
		this.h = Math.abs(x-dx) + Math.abs(y-dy) + Math.abs(z-dz); 
		this.parent = parent; 
		this.g = parent == null ? 0 : parent.g+1; 
		this.f = g+h;
	}
}

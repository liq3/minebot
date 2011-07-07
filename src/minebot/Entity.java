package minebot;

public class Entity {
	public int EID;
	public double x,y,z;
	public double yaw, pitch;
	
	public Entity() {
		
	}
	
	public Entity(int EID, int x, int y, int z, int yaw, int pitch) {
		this.EID = EID;
		move(x, y, z);
		look(yaw, pitch);
		System.out.println(this.toString());
	}
	
	public void teleport(int x, int y, int z) {
		this.x = (double)x/32;
		this.y = (double)y/32;
		this.z = (double)z/32;
	}
	
	public void move(int dx, int dy, int dz) {
		this.x += (double)dx/32;
		this.y += (double)dy/32;
		this.z += (double)dz/32;
	}
	
	public void look(int yaw, int pitch) {
		this.yaw = yaw;
		this.pitch = pitch;
	}
	
	@Override
	public String toString() {
		return String.format("Entity: EID=%d, x=%f, y=%f, z=%f, yaw=%f, pitch=%f", EID, x, y, z, yaw, pitch);
	}
}
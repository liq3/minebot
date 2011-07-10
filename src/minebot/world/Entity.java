package minebot.world;

public class Entity {
	public int EID;
	public double x, y, z;
	// blocks per second
	public double vx, vy, vz;
	public double yaw, pitch;
	
	public Entity() {
	}
	public Entity(int EID, int x, int y, int z, int yaw, int pitch) {
		this.EID = EID;
		teleport(x, y, z);
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
	public void setVelocity(int vx, int vy, int vz) {
		this.vx = (double)vx/32000*5;
		this.vy = (double)vy/32000*5;
		this.vz = (double)vz/32000*5;
	}
	
	@Override
	public String toString() {
		return String.format("Entity: EID=%d, x=%f, y=%f, z=%f, yaw=%f, pitch=%f", EID, x, y, z, yaw, pitch);
	}
}
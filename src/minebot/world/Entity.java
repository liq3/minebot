package minebot.world;

public class Entity {
	public int EID;
	public int px, py, pz;
	public double vx, vy, vz; // blocks per second
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
		px = x;
		py = y;
		pz = z;
	}
	
	public void teleport(double x, double y, double z) {
		px = (int)Math.floor(x*32);
		py = (int)Math.floor(y*32);
		pz = (int)Math.floor(z*32);
	}
	
	public void move(int dx, int dy, int dz) {
		px += dx;
		py += dy;
		pz += dz;
	}
	
	public void move(double dx, double dy, double dz) {
		px += (int)Math.floor(dx*32);
		py += (int)Math.floor(dy*32);
		pz += (int)Math.floor(dz*32);
	}
	
	public void look(double yaw, double pitch) {
		this.yaw = yaw;
		this.pitch = pitch;
	}
	public void look(int yaw, int pitch) {
		this.yaw = (double)yaw/256*360;
		this.pitch = (double)pitch/256*360;
	}
	
	public void velocity(double vx, double vy, double vz) {
		this.vx = vx;
		this.vy = vy;
		this.vz = vz;
	}
	public void velocity(int vx, int vy, int vz) {
		this.vx = (double)vx/32000*5;
		this.vy = (double)vy/32000*5;
		this.vz = (double)vz/32000*5;
	}
	
	public double x() { return (double)px/32; }
	public double y() { return (double)py/32; }
	public double z() { return (double)pz/32; }
	
	public int bx() { return px >> 5; }
	public int by() { return py >> 5; }
	public int bz() { return pz >> 5; }
	
	public int cx() { return px >> 4; }
	public int cy() { return py >> 4; }
	public int cz() { return pz >> 4; }
	
	@Override
	public String toString() {
		return String.format("Entity: EID=%d, x=%f, y=%f, z=%f, yaw=%f, pitch=%f", EID, x(), y(), z(), yaw, pitch);
	}
}
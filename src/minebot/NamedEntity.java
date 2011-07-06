package minebot;

public class NamedEntity {
	public int EID;
	public String name;
	public double x,y,z;
	public int yaw, pitch;
	public int currentItem;
	
	public NamedEntity(int EID, String name, double x, double y, double z, int yaw, int pitch, int currentItem) {
		this.EID = EID;
		this.name = name;
		this.x = x/32;
		this.y = y/32;
		this.z = z/32;
		this.yaw = yaw;
		this.pitch = pitch;
		this.currentItem = currentItem;
		System.out.println("NamedEntity EID:"+EID+" Name:"+name+" xyz:"+this.x+" "+this.y+" "+this.z+" yawpitch:"+yaw+" "+pitch+" "+currentItem);
	}
	
	public void moveDelta(int dx, int dy, int dz) {
		this.x += dx/32;
		this.y += dy/32;
		this.z += dz/32;
	}
	
	public void teleport(int x, int y, int z) {
		this.x = x/32;
		this.y = y/32;
		this.z = z/32;
	}
}

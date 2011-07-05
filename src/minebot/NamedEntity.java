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
}

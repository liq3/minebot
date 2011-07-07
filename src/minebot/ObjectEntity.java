package minebot;

public final class ObjectEntity extends Entity {
	
	public static final int BOAT			= 1;
	public static final int MINECART		= 10;
	public static final int STORAGE_CART	= 11;
	public static final int POWERED_CAR		= 12;
	public static final int ACTIVE_TNT		= 50;
	public static final int ARROW			= 60;
	public static final int THROWN_SNOWBALL	= 61;
	public static final int THROWN_EGG 		= 62;
	public static final int FALLING_SAND	= 70;
	public static final int FALLING_GRAVEL	= 71;
	public static final int FISHING_BOAT	= 90;
	
	int type;
	
	public ObjectEntity(int EID, int x, int y, int z, int yaw, int pitch, int type) {
		this.EID = EID;
		move(x, y, z);
		look(yaw, pitch);
		this.type = type;
		System.out.println(this.toString());
	}
	
	public String toString() {
		return String.format("Entity: EID=%d, x=%f, y=%f, z=%f, yaw=%f, pitch=%f, type=%d", EID, x, y, z, yaw, pitch, type);
	}
}

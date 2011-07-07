package minebot;

public class MobEntity extends Entity {
	
	public static final int CREEPER			= 50;
	public static final int SKELETON		= 51;
	public static final int GIANT_ZOMBIE	= 53;
	public static final int ZOMBIE			= 54;
	public static final int SLIME			= 55;
	public static final int GHAST			= 56;
	public static final int ZOMBIE_PIGMAN	= 57;
	public static final int PIG				= 90;
	public static final int SHEEP			= 91;
	public static final int COW				= 92;
	public static final int HEN				= 93;
	public static final int SQUID			= 94;
	public static final int WOLF			= 95;
	
	public int type;
	public byte[] metadata;
	
	/* TODO: proper metadata */
	public MobEntity(int EID, int x, int y, int z, int yaw, int pitch, int type, byte[] metadata) {
		this.EID = EID;
		move(x, y, z);
		look(yaw, pitch);
		this.type = type;
		this.metadata = metadata;
		System.out.println(this.toString());
	}
	
	public String toString() {
		return String.format("Entity: EID=%d, x=%f, y=%f, z=%f, yaw=%f, pitch=%f, type=%d", EID, x, y, z, yaw, pitch, type);
	}
}

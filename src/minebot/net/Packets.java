package minebot.net;

import java.lang.reflect.Field;

public final class Packets {
	
	public static String getName(int packet) {
		if (isPacket(packet)) {
			return Names[packet];
		}
		return "Undefined";
	}
	
	public static boolean isPacket(int packet) {
		return packet >= 0 && packet < Names.length && Names[packet] != null;
	}
	
	public static final int    Ping               = 0x00;
	public static final int    LoginRequest       = 0x01;
	public static final int    Handshake          = 0x02;
	public static final int    ChatMessage        = 0x03;
	public static final int    TimeUpdate         = 0x04;
	public static final int    EntityEqupment     = 0x05;
	public static final int    SpawnPosition      = 0x06;
	public static final int    UseEntity          = 0x07;
	public static final int    HealthUpdate       = 0x08;
	public static final int    Respawn            = 0x09;
	public static final int    OnGround           = 0x0A;
	public static final int    Position           = 0x0B;
	public static final int    Look               = 0x0C;
	public static final int    PositionAndLook    = 0x0D;
	public static final int    Digging            = 0x0E; // these use
	public static final int    DropItem           = 0x0E; // the same code
	public static final int    BlockPlacement     = 0x0F;
	public static final int    HoldingChange      = 0x10;
	public static final int    UseBed             = 0x11;
	public static final int    Animation          = 0x12;
	public static final int    EntityAction       = 0x13;
	public static final int    SpawnNamedEntity   = 0x14;
	public static final int    SpawnPickup        = 0x15;
	public static final int    CollectItem        = 0x16;
	public static final int    SpawnObject        = 0x17;
	public static final int    SpawnMob           = 0x18;
	public static final int    Painting           = 0x19;
	public static final int    StanceUpdate       = 0x1B;
	public static final int    EntityVelocity     = 0x1C;
	public static final int    DestroyEntity      = 0x1D;
	public static final int    Entity             = 0x1E;
	public static final int    EntityRelMove      = 0x1F;
	public static final int    EntityLook         = 0x20;
	public static final int    EntityRelMoveLook  = 0x21;
	public static final int    EntityTeleport     = 0x22;
	public static final int    EntityStatus       = 0x26;
	public static final int    AttachEntity       = 0x27;
	public static final int    EntityMetadata     = 0x28;
	public static final int    ChunkAction        = 0x32;
	public static final int    ChunkLoad          = 0x33;
	public static final int    MultiBlockChange   = 0x34;
	public static final int    BlockChange        = 0x35;
	public static final int    BlockAction        = 0x36;
	public static final int    Explosion          = 0x3C;
	public static final int    SoundEffect        = 0x3D;
	public static final int    NewState           = 0x46;
	public static final int    Thunderbolt        = 0x47;
	public static final int    OpenWindow         = 0x64;
	public static final int    CloseWindow        = 0x65;
	public static final int    WindowClick        = 0x66;
	public static final int    SetSlot            = 0x67;
	public static final int    WindowItem         = 0x68;
	public static final int    UpdateProgressBar  = 0x69;
	public static final int    Transaction        = 0x6A;
	public static final int    UpdateSign         = 0x82;
	public static final int    MapData            = 0x83;
	public static final int    IncrementStatistic = 0xC8;
	public static final int    Kick               = 0xFF;
	
	private static String[] Names;
	
	static {
		try {
			Packets o = new Packets();
			Field[] f = o.getClass().getFields();
			
			int maxValue = 0;
			for (int i = 0; i < f.length; i++) {
				int v = f[i].getInt(o);
				if (v > maxValue) {
					maxValue = v;
				}
			}
			
			Names = new String[maxValue+1];
			
			for (int i = 0; i < f.length; i++) {
				Names[f[i].getInt(o)] = f[i].getName();
			}
		} catch (IllegalAccessException e) {
			System.out.println("Problem with loading Packets.java");
			System.exit(0);
		}
	}
}
package minebot.entities;

import java.lang.reflect.Field;

public final class Items {
	
	public static String getName(int item) {
		if (isItem(item)) {
			return Names[item];
		}
		return "Undefined";
	}
	
	public static boolean isItem(int item) {
		return item >= 0 && item < Names.length && Names[item] != null;
	}
	
	public static final int    IronShovel          = 0x100;
	public static final int    IronPickaxe         = 0x101;
	public static final int    IronAxe             = 0x102;
	public static final int    FlintAndSteel       = 0x103;
	public static final int    Apple               = 0x104;
	public static final int    Bow                 = 0x105;
	public static final int    Arrow               = 0x106;
	public static final int    Coal                = 0x107;
	public static final int    Diamond             = 0x108;
	public static final int    IronIngot           = 0x109;
	public static final int    GoldIngot           = 0x10A;
	public static final int    IronSword           = 0x10B;
	public static final int    WoodenSword         = 0x10C;
	public static final int    WoodenShovel        = 0x10D;
	public static final int    WoodenPickaxe       = 0x10E;
	public static final int    WoodenAxe           = 0x10F;
	public static final int    StoneSword          = 0x110;
	public static final int    StoneShovel         = 0x111;
	public static final int    StonePickaxe        = 0x112;
	public static final int    StoneAxe            = 0x113;
	public static final int    DiamondSword        = 0x114;
	public static final int    DiamondShovel       = 0x115;
	public static final int    DiamondPickaxe      = 0x116;
	public static final int    DiamondAxe          = 0x117;
	public static final int    Stick               = 0x118;
	public static final int    Bowl                = 0x119;
	public static final int    MushroomStew        = 0x11A;
	public static final int    GoldSword           = 0x11B;
	public static final int    GoldShovel          = 0x11C;
	public static final int    GoldPickaxe         = 0x11D;
	public static final int    GoldAxe             = 0x11E;
	public static final int    String              = 0x11F;
	public static final int    Feather             = 0x120;
	public static final int    Gunpowder           = 0x121;
	public static final int    WoodenHoe           = 0x122;
	public static final int    StoneHoe            = 0x123;
	public static final int    IronHoe             = 0x124;
	public static final int    DiamondHoe          = 0x125;
	public static final int    GoldHoe             = 0x126;
	public static final int    Seeds               = 0x127;
	public static final int    Wheat               = 0x128;
	public static final int    Bread               = 0x129;
	public static final int    LeatherHelmet       = 0x12A;
	public static final int    LeatherChestplate   = 0x12B;
	public static final int    LeatherLeggings     = 0x12C;
	public static final int    LeatherBoots        = 0x12D;
	public static final int    ChainmailHelmet     = 0x12E;
	public static final int    ChainmailChestplate = 0x12F;
	public static final int    ChainmailLeggings   = 0x130;
	public static final int    ChainmailBoots      = 0x131;
	public static final int    IronHelmet          = 0x132;
	public static final int    IronChestplate      = 0x133;
	public static final int    IronLeggings        = 0x134;
	public static final int    IronBoots           = 0x135;
	public static final int    DiamondHelmet       = 0x136;
	public static final int    DiamondChestplate   = 0x137;
	public static final int    DiamondLeggings     = 0x138;
	public static final int    DiamondBoots        = 0x139;
	public static final int    GoldHelmet          = 0x13A;
	public static final int    GoldChestplate      = 0x13B;
	public static final int    GoldLeggings        = 0x13C;
	public static final int    GoldBoots           = 0x13D;
	public static final int    Flint               = 0x13E;
	public static final int    RawPorkchop         = 0x13F;
	public static final int    CookedPorkchop      = 0x140;
	public static final int    Painting            = 0x141;
	public static final int    GoldenApple         = 0x142;
	public static final int    Sign                = 0x143;
	public static final int    WoodenDoor          = 0x144;
	public static final int    Bucket              = 0x145;
	public static final int    WaterBucket         = 0x146;
	public static final int    LavaBucket          = 0x147;
	public static final int    Minecart            = 0x148;
	public static final int    Saddle              = 0x149;
	public static final int    IronDoor            = 0x14A;
	public static final int    RedstoneDust        = 0x14B;
	public static final int    Snowball            = 0x14C;
	public static final int    Boat                = 0x14D;
	public static final int    Leather             = 0x14E;
	public static final int    MilkBucket          = 0x14F;
	public static final int    ClayBrick           = 0x150;
	public static final int    ClayBall            = 0x151;
	public static final int    SugarCane           = 0x152;
	public static final int    Paper               = 0x153;
	public static final int    Book                = 0x154;
	public static final int    Slimeball           = 0x155;
	public static final int    StorageMinecart     = 0x156;
	public static final int    PoweredMinecart     = 0x157;
	public static final int    Egg                 = 0x158;
	public static final int    Compass             = 0x159;
	public static final int    FishingRod          = 0x15A;
	public static final int    Clock               = 0x15B;
	public static final int    GlowstoneDust       = 0x15C;
	public static final int    RawFish             = 0x15D;
	public static final int    CookedFish          = 0x15E;
	public static final int    InkSac              = 0x15F;
	public static final int    Bone                = 0x160;
	public static final int    Sugar               = 0x161;
	public static final int    Cake                = 0x162;
	public static final int    Bed                 = 0x163;
	public static final int    RedstoneRepeater    = 0x164;
	public static final int    Cookie              = 0x165;
	public static final int    Map                 = 0x166;
	public static final int    Shears              = 0x167;
	public static final int    GoldMusicDisc       = 0x8D0;
	public static final int    GreenMusicDisc      = 0x8D1;
	
	private static String[] Names;
	
	static {
		try {
			Items o = new Items();
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
			System.out.println("Problem with loading Items.java");
			System.exit(0);
		}
	}
}















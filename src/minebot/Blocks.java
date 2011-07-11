package minebot;

import java.lang.reflect.Field;

public final class Blocks {
	
	public static String getName(int block) {
		if (isBlock(block)) {
			return Names[block];
		}
		return "Undefined";
	}
	public static boolean isBlock(int block) {
		return block >= 0 && block < Names.length && Names[block] != null;
	}
	
	public static boolean isSolid(int block) {
		return Solid[block];
	}
	
	public static final int    Air                 = 0x00;
	public static final int    Stone               = 0x01;
	public static final int    Grass               = 0x02;
	public static final int    Dirt                = 0x03;
	public static final int    Cobblestone         = 0x04;
	public static final int    WoodenPlank         = 0x05;
	public static final int    Sapling             = 0x06;
	public static final int    Bedrock             = 0x07;
	public static final int    Water               = 0x08;
	public static final int    StationaryWater     = 0x09;
	public static final int    Lava                = 0x0A;
	public static final int    StationaryLava      = 0x0B;
	public static final int    Sand                = 0x0C;
	public static final int    Gravel              = 0x0D;
	public static final int    GoldOre             = 0x0E;
	public static final int    IronOre             = 0x0F;
	public static final int    CoalOre             = 0x10;
	public static final int    Wood                = 0x11;
	public static final int    Leaves              = 0x12;
	public static final int    Sponge              = 0x13;
	public static final int    Glass               = 0x14;
	public static final int    LapisLazuliOre      = 0x15;
	public static final int    LapisLazuliBlock    = 0x16;
	public static final int    Dispenser           = 0x17;
	public static final int    Sandstone           = 0x18;
	public static final int    NoteBlock           = 0x19;
	public static final int    Bed                 = 0x1A;
	public static final int    PoweredRail         = 0x1B;
	public static final int    DetectorRail        = 0x1C;
	public static final int    StickyPiston        = 0x1D;
	public static final int    Cobweb              = 0x1E;
	public static final int    TallGrass           = 0x1F;
	public static final int    DeadShrub           = 0x20;
	public static final int    Piston              = 0x21;
	public static final int    PistonHead          = 0x22;
	public static final int    Wool                = 0x23;
	public static final int    YellowFlower        = 0x25;
	public static final int    RedRose             = 0x26;
	public static final int    BrownMushroom       = 0x27;
	public static final int    RedMushroom         = 0x28;
	public static final int    GoldBlock           = 0x29;
	public static final int    IronBlock           = 0x2A;
	public static final int    DoubleSlab          = 0x2B;
	public static final int    Slab                = 0x2C;
	public static final int    Brick               = 0x2D;
	public static final int    TNT                 = 0x2E;
	public static final int    Bookshelf           = 0x2F;
	public static final int    MossStone           = 0x30;
	public static final int    Obsidian            = 0x31;
	public static final int    Torch               = 0x32;
	public static final int    Fire                = 0x33;
	public static final int    MonsterSpawner      = 0x34;
	public static final int    WoodenStairs        = 0x35;
	public static final int    Chest               = 0x36;
	public static final int    RedstoneWire        = 0x37;
	public static final int    DiamondOre          = 0x38;
	public static final int    DiamondBlock        = 0x39;
	public static final int    CraftingTable       = 0x3A;
	public static final int    Crop                = 0x3B;
	public static final int    Farmland            = 0x3C;
	public static final int    Furnace             = 0x3D;
	public static final int    BurningFurnace      = 0x3E;
	public static final int    SignPost            = 0x3F;
	public static final int    WoodenDoor          = 0x40;
	public static final int    Ladder              = 0x41;
	public static final int    Rail                = 0x42;
	public static final int    CobblestoneStairs   = 0x43;
	public static final int    WallSign            = 0x44;
	public static final int    Lever               = 0x45;
	public static final int    StonePressurePlate  = 0x46;
	public static final int    IronDoor            = 0x47;
	public static final int    WoodenPressurePlate = 0x48;
	public static final int    RedstoneOre         = 0x49;
	public static final int    GlowingRedstoneOre  = 0x4A;
	public static final int    RedstoneTorchOff    = 0x4B;
	public static final int    RedstoneTorchOn     = 0x4C;
	public static final int    StoneButton         = 0x4D;
	public static final int    Snow                = 0x4E;
	public static final int    Ice                 = 0x4F;
	public static final int    SnowBlock           = 0x50;
	public static final int    Cactus              = 0x51;
	public static final int    Clay                = 0x52;
	public static final int    SugarCane           = 0x53;
	public static final int    Jukebox             = 0x54;
	public static final int    Fence               = 0x55;
	public static final int    Pumpkin             = 0x56;
	public static final int    Netherrack          = 0x57;
	public static final int    SoulSand            = 0x58;
	public static final int    Glowstone           = 0x59;
	public static final int    Portal              = 0x5A;
	public static final int    JackOLantern        = 0x5B;
	public static final int    Cake                = 0x5C;
	public static final int    RedstoneRepeaterOff = 0x5D;
	public static final int    RedstoneRepeaterOn  = 0x5E;
	public static final int    LockedChest         = 0x5F;
	public static final int    Trapdoor            = 0x60;
	
	private static final int[] NotSolid = {
		Air,
		Sapling,
		Water,
		StationaryWater,
		Lava,
		StationaryLava,
		PoweredRail,
		DetectorRail,
		Cobweb,
		TallGrass,
		DeadShrub,
		YellowFlower,
		RedRose,
		BrownMushroom,
		RedMushroom,
		Torch,
		Fire,
		RedstoneWire,
		Crop,
		SignPost,
		Ladder,
		Rail,
		WallSign,
		Lever,
		StonePressurePlate,
		WoodenPressurePlate,
		RedstoneTorchOff,
		RedstoneTorchOn,
		StoneButton,
		Snow,
		SugarCane,
		Portal,
		RedstoneRepeaterOff,
		RedstoneRepeaterOn,
	};
	
	private static boolean[] Solid;
	
	private static String[] Names;
	
	static {
		try {
			Blocks o = new Blocks();
			Field[] f = o.getClass().getFields();
			
			int maxValue = 0;
			for (int i = 0; i < f.length; i++) {
				int v = f[i].getInt(o);
				if (v > maxValue) {
					maxValue = v;
				}
			}
			
			Solid = new boolean[maxValue+1];
			for (int i = 0; i < Solid.length; i++) {
				Solid[i] = true;
			}
			Names = new String[maxValue+1];
			
			for (int i = 0; i < NotSolid.length; i++) {
				Solid[NotSolid[i]] = false;
			}
			
			for (int i = 0; i < f.length; i++) {
				Names[f[i].getInt(o)] = f[i].getName();
			}
		} catch (IllegalAccessException e) {
			System.out.println("Problem with loading Blocks.java");
			System.exit(0);
		}
	}
}
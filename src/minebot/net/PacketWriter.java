package minebot.net;

import java.io.*;

import minebot.bot.Bot;



public final class PacketWriter extends DataOutputStream {
	public static final int PROTOCOL_VERSION = 14;
	
	// Respawn
	public static final int DIMENSION_WORLD	 = 0;
	public static final int DIMENSION_NETHER = -1;
	
	// Digging
	public static final int DIGGING_BEGIN = 0;
	public static final int DIGGING_END   = 2;
	
	// Window
	public static final int WINDOW_CHEST	 = 0;
	public static final int WINDOW_WORKBENCH = 1;
	public static final int WINDOW_FURNACE	 = 2;
	public static final int WINDOW_DISPENSER = 3;
	
	public static final int version = 14;
	
	public PacketWriter(OutputStream outputStream) {
		super(outputStream);
	}
	public void writePing() throws IOException {
		writeByte(Packets.Ping);
	}
	public void writeString8(String str) throws IOException{
		writeUTF(str);
	}
	public void writeString16(String str) throws IOException {
		writeShort(str.length());
		write(str.getBytes("UTF-16BE"), 0, str.length()*2);
	}
	public void writeLoginRequest(String username) throws IOException {
		writeByte(Packets.LoginRequest);
		writeInt(PROTOCOL_VERSION);
		writeString16(username);
		writeLong(0);
		writeByte(0);
	}
	public void writeHandshake(String username) throws IOException {
		writeByte(Packets.Handshake);
		writeString16(username);
	}
	public void writeChatMessage(String str) throws IOException {
		writeByte(Packets.ChatMessage);
		writeString16(str);
	}
	public void writeUseEntity(int target, boolean leftClick) throws IOException {
		writeByte(Packets.UseEntity);
		writeInt(0);
		writeInt(target);
		writeBoolean(leftClick);
	}
	
	public void writeRespawn(int worldType) throws IOException {
		writeByte(Packets.Respawn);
		writeByte((byte)worldType);
	}
	public void writeOnGround(boolean onGround) throws IOException {
		writeByte(Packets.OnGround);
		writeBoolean(onGround);
	}
	public void writePosition(Bot player) throws IOException {
		writeByte(Packets.Position);
		writeDouble(player.x);
		writeDouble(player.y);
		writeDouble(player.stance);
		writeDouble(player.z);
		writeBoolean(player.onGround);
	}
	public void writeLook(Bot player) throws IOException {
		writeByte(Packets.Look);
		writeFloat((float)player.yaw);
		writeFloat((float)player.pitch);
		writeBoolean(player.onGround);
	}
	public void writePositionAndLook(Bot player) throws IOException {
		writeByte(Packets.PositionAndLook);
		writeDouble(player.x);
		writeDouble(player.y);
		writeDouble(player.stance);
		writeDouble(player.z);
		writeFloat((float)player.yaw);
		writeFloat((float)player.pitch);
		writeBoolean(player.onGround);
	}
	
	public void writeDigging(int status, int x, int y, int z, int face) throws IOException {
		writeByte(Packets.Digging);
		writeByte(status);
		writeInt(x);
		writeByte(y);
		writeInt(z);
		writeByte(face);
	}
	public void writeDropItem() throws IOException {
		writeByte(Packets.DropItem);
		writeByte(4);
		writeInt(0);
		writeByte(0);
		writeInt(0);
		writeByte(0);
	}
	public void writeBlockPlacement(int x, int y, int z, int dir) throws IOException {
		writeByte(Packets.BlockPlacement);
		writeInt(x);
		writeByte(y);
		writeInt(z);
		writeByte(dir);
		writeShort(-1); // No item
	}
	public void writeBlockPlacement(int x, int y, int z, int dir, int ID, int amount, int uses) throws IOException {
		writeByte(Packets.BlockPlacement);
		writeInt(x);
		writeByte(y);
		writeInt(z);
		writeByte(dir);
		writeShort(ID);
		if (ID >= 0) {
			writeByte(amount);
			writeShort(uses);
		}
	}
	public void writeHoldingChange(int slot) throws IOException {
		writeByte(Packets.HoldingChange);
		writeShort(slot);
	}
	public void writeAnimation(int type) throws IOException {
		writeByte(Packets.Animation);
		/* TODO */
	}
	public void writeWindowClick() throws IOException {
		writeByte(Packets.WindowClick);
		/* TODO */
	}
}
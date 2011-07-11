package minebot;

import java.io.IOException;

import minebot.entities.*;
import minebot.net.*;

public abstract class Player {
	
	public Inventory inventory;
	
	public int EID;
	public double x, y, z;
	public double yaw, pitch;
	public double stance;
	public int spawnX, spawnY, spawnZ;
	public boolean onGround;
	public boolean digging;
	public boolean spawned;
	
	protected int digX, digY, digZ;
	protected Session session;
	protected PacketWriter writer;
	protected World world;
	
	public int bx() { return (int)Math.floor(x); }
	public int by() { return (int)Math.floor(y); }
	public int bz() { return (int)Math.floor(z); }
	
	public Player(Session session) {
		stance = 100;
		onGround = true;
		spawned = false;

		digging = false;
		digX = digY = digZ = 0;
		
		inventory = new Inventory();
		
		this.session = session;
		this.world = session.world;
		this.writer = session.writer;
		
		session.player = this;
	}
	
	public abstract void logic() throws IOException;
	public abstract void handleChat(String chat);
	
	public void respawn() {
		x = spawnX + 0.5;
		y = spawnY;
		z = spawnZ + 0.5;
		stance = y + 1.62;
	}
	
	private void dig(double x, double y, double z) throws IOException {
		dig(bx(), by(), bz());
	}
	
	private void dig(int x, int y, int z) throws IOException {
		int slot = inventory.equip(274);
		if (slot != -1) { 
			writer.writeHoldingChange(slot-36);
		}
		
		digging = true;
		digX = x;
		digY = y;
		digZ = z;
		writer.writeDigging(PacketWriter.DIGGING_BEGIN, x, y, z, 4);		
		writer.writeDigging(PacketWriter.DIGGING_END, x,y,z, 4);
		writer.flush();
	}
	
	public void doneDigging() {
		digging = false;
	}
	
	private void placeBlock(int type, int x, int y, int z) throws IOException {
		//System.out.println("Placing block.");
		int slot = inventory.equip(type);
		if (slot == -1) {
			return;
		}
		
		writer.writeHoldingChange(slot-36);
		Item item = inventory.getItem(slot);
		writer.writeBlockPlacement(x,y,z,0, item.ID, item.count, item.data);
		writer.flush();
	}
	
	@Override
	public String toString() {
		return String.format("Player: EID=%d, x=%f, y=%f, z=%f, stance=%f, spawnX=%d, spawnY=%d, spawnZ=%d", EID, x, y, z, stance, spawnX, spawnY, spawnZ);
	}
	
	public void printData() {
		System.out.println(toString());
	}
}
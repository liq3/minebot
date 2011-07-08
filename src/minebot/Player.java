package minebot;

import java.io.IOException;

import minebot.net.*;
import minebot.world.*;

public class Player extends NamedEntity {
	
	public Inventory inventory;
	
	public int spawnX;
	public int spawnY;
	public int spawnZ;
	public double stance;
	public boolean onGround;
	public boolean digging;
	public boolean spawned;
	
	protected int digX, digY, digZ;
	protected Session session;
	protected PacketWriter writer;
	protected World world;
	
	public Player(Session session) {
		super(session.EID, 0, 100, 0, 0, 0, session.username, -1);
		stance = 100;
		onGround = true;
		spawned = false;

		digging = false;
		digX = digY = digZ = 0;
		
		inventory = new Inventory();
		
		this.session = session;
		this.world = session.world;
		this.writer = session.writer;
		
		world.entities.add(this);
	}
	
	@Override
	public void teleport(int x, int y, int z) {
		this.x = (double)x/32;
		this.y = (double)y/32;
		this.z = (double)z/32;
		stance = (double)y/32;
	}
	
	@Override
	public void move(int dx, int dy, int dz) {
		this.x += (double)dx/32;
		this.y += (double)dy/32;
		this.z += (double)dz/32;
		stance += (double)dy/32;
	}
	
	private void dig(double x, double y, double z) throws IOException {
		dig((int)Math.floor(x), (int)Math.floor(y), (int)Math.floor(z));
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
	}
	
	@Override
	public String toString() {
		return String.format("Player: EID=%d, x=%f, y=%f, z=%f, stance=%f, spawnX=%d, spawnY=%d, spawnZ=%d", EID, x, y, z, stance, spawnX, spawnY, spawnZ);
	}
	
	public void printData() {
		System.out.println(toString());
	}
}
package minebot;

import java.io.*;
import java.util.LinkedList;
import java.util.Queue;

public class Player {
	
	public int spawnX;
	public int spawnY;
	public int spawnZ;
	public double x,y,z;
	public double stance;
	public float yaw, pitch;
	public boolean onGround;
	private DataOutputStream output;
	private long moveTime;
	private long lastTick;
	public boolean spawned;
	public int EID;
	private Map map;
	private boolean digging;
	private int digX, digY, digZ;
	
	public Inventory inventory;
	
	private Queue<Move> moveList;
	public LinkedList<NamedEntity> entityList;
	
	public Player(DataOutputStream out, Map map) throws SecurityException, IOException {
		spawnX = 0;
		spawnY = 0;
		spawnZ = 0;
		y = stance = 75.65;
		x = z = 0;
		yaw = pitch = 0;
		onGround = true;
		output = out;
		moveTime = 0;
		lastTick = System.currentTimeMillis();
		spawned = false;
		this.map = map;
		digging = false;
		digX = digY = digZ = 0;
		
		inventory = new Inventory();
		moveList = new LinkedList<Move>();
		entityList = new LinkedList<NamedEntity>();
	}
	
	public void logic() throws IOException {
		long tempTime = System.currentTimeMillis();
		moveTime += tempTime - lastTick;
		lastTick = tempTime;
		output.writeByte(0);
		
		int speed = 100;
		while (moveTime > speed && spawned) {
			
			if (digging && map.block(digX, digY, digZ) == 0) { 
				digging = false;
			}
			
			output.writeByte(0x0D);
			output.writeDouble(x);
			output.writeDouble(y);
			output.writeDouble(stance);
			output.writeDouble(z);
			output.writeFloat(yaw);
			output.writeFloat(pitch);
			output.writeBoolean(onGround);
							
			if (map.block(x,y-1,z) == 0 && moveList.isEmpty()) {
				addMove(0,-1,0);
			}
						
			if (!moveList.isEmpty()) {
				Move move = moveList.remove();
				
				x += move.x;
				y += move.y;
				stance += move.y;
				z += move.z;
			}	
			
			moveTime -= speed;
		}
	}

	private boolean canMove(double x, double y, double z) {		
		int b1 = map.block(x,y-1,z);
		int b2 = map.block(x,y,z);
		int b3 = map.block(x,y+1,z);
		if (b1 != 0 && b2 == 0 && b3 == 0)
			return true;
		else 
			return false;
	}
	
	private void addMove(int x, int y, int z) {
		moveList.add(new Move(x,y,z));
	}
	
	private void dig(double x, double y, double z) throws IOException {
		dig((int)Math.floor(x), (int)Math.floor(y), (int)Math.floor(z));
	}
	
	private void dig(int x, int y, int z) throws IOException {
		int slot = inventory.equip(274);
		if (slot != -1) { 
			output.writeByte(0x10);
			output.writeShort(slot-36);
		}
		
		digging = true;
		digX = x;
		digY = y;
		digZ = z;
		output.writeByte(0x0E);
		output.writeByte(0);
		output.writeInt(x);
		output.writeByte(y);
		output.writeInt(z);
		output.writeByte(1);
		
		output.writeByte(0x0E);
		output.writeByte(2);
		output.writeInt(x);
		output.writeByte(y);
		output.writeInt(z);
		output.writeByte(1);
	}
	
	private void placeBlock(int type, int x, int y, int z) throws IOException {
		//System.out.println("Placing block.");
		int slot = inventory.equip(type);
		if (slot == -1) {
			return;
		}
		
		output.writeByte(0x10);
		output.writeShort(slot-36);
	
		Item item = inventory.getItem(slot);
		
		output.writeByte(0x0F);
		output.writeInt(x);
		output.writeByte(y);
		output.writeInt(z);
		output.writeByte(5);
		output.writeShort(item.type);
		output.writeByte(item.count);
		output.writeShort(item.uses);
		

	}
	
	public void printData() {
		System.out.println("Spawn: "+spawnX+" "+spawnY+" "+spawnZ);
		System.out.println("xyzStance: "+ x +" "+y+" "+z+" "+stance);
	}
	
	public void doneDigging() {
		digging = false;
	}
	
	public void handleChat(String msg) {
		System.out.println(msg);
		String name = msg.substring(0,6);
		String cmd = msg.substring(6);
	}
	
	private void AStar(double x, double y, double z) {
		
	}	
}
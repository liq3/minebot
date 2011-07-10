package minebot.bot;

import java.io.*;
import java.util.LinkedList;
import java.util.Queue;

import minebot.Config;
import minebot.Player;
import minebot.entities.*;
import minebot.net.*;


public class Bot extends Player{
	
	private long moveTime;
	private long lastTick;
	private Queue<Move> moveList;
	
	public Bot(Session session) {
		super(session);
		
		moveTime = 0;
		lastTick = System.currentTimeMillis();
		moveList = new LinkedList<Move>();
	}
	
	@Override
	public void logic() throws IOException {
		long tempTime = System.currentTimeMillis();
		moveTime += tempTime - lastTick;
		lastTick = tempTime;
		
		int speed = 50;
		while (moveTime > speed && spawned) {
			
			if (digging && world.getBlock(digX, digY, digZ) == 0) { 
				digging = false;
			}
			
			writer.writePositionAndLook(this);
							
			if (!ItemID.solid[ world.getBlock(bx(),by()-1,bz()) ] && moveList.isEmpty()) {
				addMove(0,-1,0);
			}
						
			if (!moveList.isEmpty()) {
				Move move = moveList.remove();

				x = Math.floor(x+move.x)+0.5;
				y += move.y;
				stance += move.y;
				z = Math.floor(z+move.z)+0.5;
			}	
			
			moveTime -= speed;
		}
	}
	
	private void addMove(int x, int y, int z) {
		moveList.add(new Move(x,y,z));
	}
	
	@Override
	public void handleChat(String msg) {
		System.out.println(msg);
		if (msg.charAt(0) != '<') {
			return;
		}
		String[] name_msg = msg.split("> ", 2);
		String name = name_msg[0].substring(1);
		String cmd = name_msg[1];
		if (name.equals(Config.master) && cmd.equals("move")) {
			NamedEntity ent = world.entities.getByName(Config.master);
			if (ent != null) {
				System.out.println("Starting path. Start:"+x+","+y+","+z+" End:"+ent.x()+","+ent.y()+","+ent.z());
				AStar AStarPath = new AStar(world);
				PathBlock[] path = AStarPath.getPath(bx(), by(), bz(), ent.bx(), ent.by(), ent.bz());
				if (path == null) {
					System.out.println("No path.");
					return;
				}
				System.out.println("Found path!");
				for (int i = path.length-1; i > 0; i--) {
					System.out.println("Node: "+path[i].x+" "+path[i].y+" "+path[i].z);
					addMove(path[i-1].x - path[i].x, path[i-1].y - path[i].y, path[i-1].z - path[i].z);
				}				
			}
		}
	}
}
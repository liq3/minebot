package minebot.bot;

import minebot.*;

public final class AStar {
	
	private World map;
	
	public AStar(World map) {
		this.map = map;
	}
	
	public PathBlock[] getPath(int sx, int sy, int sz, int dx, int dy, int dz) {
		if (!map.canStand(dx, dy, dz)) {
			System.out.println("Path: Invalid destination.");
			return null;
		}
		
		AStarList closed = new AStarList();
		AStarBinaryHeap open = new AStarBinaryHeap();
		
		open.add(new PathBlock(sx, sy, sz, dx, dy, dz, null));
		
		while (true) {
			
			PathBlock block = open.pop();
			closed.add(block);
			
			if (closed.contains(dx,dy,dz)) {
				int count = 1; // Include the origin
				block = closed.get(closed.size()-1);
				while (block.parent != null) {
					block = block.parent;
					count++;
				}
				
				PathBlock[] blockList = new PathBlock[count];
				count = 0;
				block = closed.get(closed.size()-1);
				while (block.parent != null) {
					blockList[count] = block;
					block = block.parent;
					count++;
				}
				blockList[count] = block;
				return blockList;
			}
			
			int[][] dirs = {{-1,0},{1,0},{0,-1},{0,1}};
			for (int i = 0; i < 4; i++) {
				int bx = block.x+dirs[i][0], bz = block.z+dirs[i][1];				
				if (map.canStand(bx,block.y,bz)) {
					PathBlock newBlock = new PathBlock(bx,block.y,bz,dx,dy,dz,block);
					if (!closed.contains(newBlock) && !open.contains(newBlock)) {
						open.add(newBlock);
					}					
				} 
				if (map.canStand(bx,block.y-1,bz) && !Blocks.isSolid( map.getBlock(bx,block.y+1,bz) )) {
					PathBlock newBlock2 = new PathBlock(bx,block.y,bz,dx,dy,dz,block);
					PathBlock newBlock = new PathBlock(bx,block.y-1,bz,dx,dy,dz,newBlock2);
					if (!closed.contains(newBlock) && !open.contains(newBlock) && !closed.contains(newBlock2)) {
						closed.add(newBlock2);
						open.add(newBlock);
					}
				}
				if (map.canStand(bx,block.y+1,bz) && !Blocks.isSolid( map.getBlock(block.x,block.y+2,block.z) )) {
					PathBlock newBlock2 = new PathBlock(block.x,block.y+1,block.z,dx,dy,dz,block);
					PathBlock newBlock = new PathBlock(bx,block.y+1,bz,dx,dy,dz,newBlock2);
					if (!closed.contains(newBlock) && !open.contains(newBlock) && !closed.contains(newBlock2)) {
						closed.add(newBlock2);
						open.add(newBlock);
					}
				}
			}
			
			if (open.isEmpty()) {
				return null;
			}
		}
	}
}
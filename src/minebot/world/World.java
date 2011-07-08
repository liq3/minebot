package minebot.world;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;
import java.util.*;



public final class World {
	private Map<Integer, byte[]> chunks;
	
	public EntityManager entities;
	
	public World() {
		chunks = new HashMap<Integer, byte[]>();
		entities = new EntityManager();
	}
	
	public void readChunkData(int x, int y, int z, int sx, int sy, int sz, byte[] data) throws IOException {
		sx = sx+1;
		sy = sy+1;
		sz = sz+1;
		int size = sx * sy * sz;
		
		Inflater inflater = new Inflater();
		inflater.setInput(data);
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream(data.length);
		
		byte[] buff = new byte[1024];
		while (!inflater.finished()) {
			try {
				int count = inflater.inflate(buff);
				bos.write(buff, 0, count);
			} catch (DataFormatException e) {
			}
		}
		bos.close();
		
		byte[] decompressedData = bos.toByteArray();
		
		byte[] blockData = new byte[size];
		for (int i = 0; i < blockData.length; i++) {
			blockData[i] = decompressedData[i];
		}
		
		int cx = (int)(x) >> 4;
		int cz = (int)(z) >> 4;
		int key = getKey(cx, cz);
		if (!chunks.containsKey(key) && size == 16*16*128) {
			chunks.put(key, blockData);
		} else if (chunks.containsKey(key)) {
			x = x & 15;
			y = y & 127;
			z = z & 15;			
			for (int ix = 0; ix < sx; ix++) {
				for (int iy = 0; iy < sy; iy++) {
					for (int iz = 0; iz < sz; iz++) {
						chunks.get(key)[(y+iy) + (((z+iz) * sy) + ((x+ix) * sy * sx))] = blockData[iy + (iz * sy) + (ix * sy * sz)];
					}
				}
			}
		}
	}
	
	public int block(double x, double y, double z) {
		x = Math.floor(x);
		z = Math.floor(z);
		return block((int)x, (int)y, (int)z);
	}
		
	public int block(int x, int y, int z) {
		if (chunks.containsKey(getKey(x >> 4, z >> 4))) {
			return chunks.get(getKey(x >> 4, z >> 4))[getIndex(x,y,z)];
		} else {
			//System.out.println("Chunk doesn't exist."+x+" "+y+" "+z+" "+cx+" "+cz);
			return 0;
		}
	}
	
	public void setBlockType(int x, int y, int z, int type) {
		int key = getKey(x >> 4, z >> 4);
		if (chunks.containsKey(key)) {
			chunks.get(key)[getIndex(x,y,z)] = (byte)(type);
		}
	}
	
	private int getIndex(int x, int y, int z) {
		return (y&127) + (((z&15) * 128) + ((x&15) * 128 * 16));
	}
	
	public void multiBlockChange(int cx, int cz, int len, byte[] coords, byte[] types, byte[] metadata) {
		int key = getKey(cx,cz);
		int[] x = new int[len];
		int[] y = new int[len];
		int[] z = new int[len];
		for (int i = 0; i < len; i++) {
			x[i] = coords[i*2] >> 4;
			z[i] = coords[i*2] & 15;
			y[i] = coords[i*2+1];
		}
		for (int i = 0; i < len; i++) {
			chunks.get(key)[getIndex(x[i],y[i],z[i])] = types[i];
		}
	}
	
	public void createEmptyChunk(int cx, int cz) {
		int key = getKey(cx, cz);
		if (!chunks.containsKey(key)) {
			byte[] block = new byte[128*16*16];
			for (int i = 0; i < 128*16*16; i++) {
				block[i] = 0;
			}
			chunks.put(key, block);
		}
	}
	
	public void deleteChunk(int cx, int cz) {
		chunks.remove(getKey(cx,cz));
	}
	
	private int getKey(int cx, int cz) {
		return cx + (cz << 16);
	}
	
	public boolean canStand(double x, double y, double z) {
		x = Math.floor(x);
		z = Math.floor(z);
		
		return canStand((int)x, (int)y, (int)z);
	}
	
	public boolean canStand(int x, int y, int z) {		
		int b1 = block(x,y-1,z);
		int b2 = block(x,y,z);
		int b3 = block(x,y+1,z);

		if (ItemID.solid[b1] && !ItemID.solid[b2] && !ItemID.solid[b3]) {
			return true;
		}
		return false;
	}
}

package minebot.net;

import java.io.*;
import java.net.*;

import minebot.*;
import minebot.entities.*;


public class Session {
	
	public Player player;
	public boolean connected;
	
	public World world;
	
	public int EID;
	
	public String username;
	public Socket socket;
	public DataInputStream reader;
	public PacketWriter writer;
	
	private String password;
	private String sessionID;
	
	public Session(String username, String password) {
		this.username = username;
		this.password = password;
		connected = false;
	}
	
	// Login to minecraft.net
	public void login() throws IOException {
		String loginURL = String.format("https://login.minecraft.net/?user=%s&password=%s&version=9999", username, password);
		
		URL url = new URL(loginURL);
		URLConnection conn = url.openConnection();
		
		BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String line = rd.readLine();
		rd.close();
		System.out.println(line);
		if (line.equals("Bad login")) {
			System.exit(0);
		}
		sessionID = line.split(":")[3];
	}
	
	// Connect to a server
	public void connect(String host, int port) throws IOException {
		socket = new Socket(host, port);
		reader = new DataInputStream(socket.getInputStream());
		writer = new PacketWriter(socket.getOutputStream());
		
		writer.writeHandshake(username);
		readPacket();
		
		writer.writeLoginRequest(username);
		readPacket();
	}
	
	// Start listening to packets and running the Player instance
	public void begin() throws IOException, InterruptedException {
		if (!connected) {
			System.out.println("You need to connect to a server first.");
			System.exit(0);
		}
		
		while(socket.isConnected()) {
			while (reader.available() > 0) {
				readPacket();
			}
			player.logic();
			Thread.sleep(100);
		}
	}
	
	public String readString8() throws IOException {
		return reader.readUTF();
	}
	
	public String readString16() throws IOException {
		return new String(readBytes(reader.readShort()*2), "UTF-16BE");
	}
	
	public byte[] readBytes(int length) throws IOException {
		byte[] bytes = new byte[length];
		int recv = 0;
		do {
			recv = reader.read(bytes, recv, length-recv);
		} while (recv < length);
		return bytes;
	}
	
	public void readMetadata() throws IOException {
		byte x = 0;
		while ((x = reader.readByte()) != 127) {
			switch (x >> 5) {
			case 0:
				reader.readByte();
				break;
			case 1:
				reader.readShort();
				break;
			case 2:
				reader.readInt();
				break;
			case 3:
				reader.readFloat();
				break;
			case 4:
				readString16();
				break;
			case 5:
				reader.readShort();
				reader.readByte();
				reader.readShort();
				break;
			case 6:
				reader.readInt();
				reader.readInt();
				reader.readInt();
				break;
			}
		}
	}
	
	private int lastOpcode = -1;
	
	public void readPacket() {
		int opcode = -1;
		try {
			opcode = reader.readUnsignedByte();
			switch (opcode) {
			case Packets.Ping:
				writer.writePing();
				break;
				
			case Packets.LoginRequest: {
				EID = reader.readInt();
				String serverName = readString16();
				reader.skipBytes(9); // not required
				System.out.println("Login Response: EID=" + EID);
				world = new World();
				connected = true;
				break;
			}
			case Packets.Handshake: {
				String hash = readString16();
				String authURL = String.format("http://www.minecraft.net/game/joinserver.jsp?user=%s&sessionId=%s&serverId=%s", username, sessionID, hash);
				URL url = new URL(authURL);
				URLConnection conn = url.openConnection();
				BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				String line = rd.readLine();
				System.out.println("Attempting to join server:" + line + " | " + authURL);
				rd.close();
				if (!line.equals("OK")) {
					throw new IOException("Failed to authenticate with server.");
				}
				break;
			}
			case Packets.ChatMessage: {
				String msg = readString16();
				player.handleChat(msg);
				break;
			}
			case Packets.TimeUpdate: // TODO
				reader.readLong();
				break;
				
			case Packets.EntityEqupment: // TODO
				reader.skipBytes(10);
				break;
				
			case Packets.SpawnPosition:
				player.spawnX = reader.readInt();
				player.spawnY = reader.readInt();
				player.spawnZ = reader.readInt();
				break;
				
			case Packets.UseEntity: // TODO
				reader.skipBytes(9);
				break;
				
			case Packets.HealthUpdate: {
				int hp = reader.readShort();
				if (hp <= 0) {
					writer.writeRespawn(PacketWriter.DIMENSION_WORLD);
				}
				break;
			}
			case Packets.Respawn:
				reader.readByte();
				player.respawn();
				break;
				
			case Packets.PositionAndLook:
				player.x = reader.readDouble();
				player.stance = reader.readDouble();
				player.y = reader.readDouble();
				player.z = reader.readDouble();
				player.yaw = reader.readFloat();
				player.pitch = reader.readFloat();
				player.onGround = reader.readBoolean();
				player.spawned = true;
				break;
				
			case Packets.UseBed: // TODO
				reader.skipBytes(14);
				break;
				
			case Packets.Animation: // TODO
				reader.skipBytes(5);
				break;
				
			case Packets.EntityAction: // TODO
				reader.skipBytes(5);
				break;
				
			case Packets.SpawnNamedEntity: {
				EID = reader.readInt();
				String name = readString16();
				int x = reader.readInt();
				int y = reader.readInt();
				int z = reader.readInt();
				int yaw = reader.readByte();
				int pitch = reader.readByte();
				int item = reader.readShort();
				NamedEntity ent = new NamedEntity(EID, x,y,z, yaw,pitch, name, item);
				world.entities.add(ent);
				break;
			}
			case Packets.SpawnPickup: {
				EID = reader.readInt();
				int itemID = reader.readShort();
				int count = reader.readByte();
				int data = reader.readShort();
				int x = reader.readInt();
				int y = reader.readInt();
				int z = reader.readInt();
				int yaw = reader.readByte();
				int pitch = reader.readByte();
				int roll = reader.readByte();
				Item item = new Item(itemID, count, data);
				ItemEntity ent = new ItemEntity(EID, x,y,z, yaw,pitch,roll, item);
				world.entities.add(ent);
				break;
			}
			case Packets.CollectItem: // TODO
				reader.skipBytes(8);
				break;
				
			case Packets.SpawnObject: {
				int EID = reader.readInt();
				int type = reader.readByte();
				int x = reader.readInt();
				int y = reader.readInt();
				int z = reader.readInt();
				int flag = reader.readInt(); // TODO
				if (flag > 0) {
					reader.skipBytes(6);
				}
				ObjectEntity ent = new ObjectEntity(EID, x,y,z, 0,0, type);
				world.entities.add(ent);
				break;
			}
			case Packets.SpawnMob: {
				int EID = reader.readInt();
				int type = reader.readByte();
				int x = reader.readInt();
				int y = reader.readInt();
				int z = reader.readInt();
				int yaw = reader.readByte();
				int pitch = reader.readByte();
				readMetadata();
				byte[] meta = {};
				MobEntity ent = new MobEntity(EID, x,y,z, yaw,pitch, type, meta);
				world.entities.add(ent);
				break;
			}
			case Packets.Painting: { // TODO
				int EID = reader.readInt();
				readString16();
				reader.skipBytes(16);
				break;
			}
			case Packets.StanceUpdate: // this packet is largely unused/unknown
				reader.skipBytes(18);	// but we'll read it just in case
				break;
				
			case Packets.EntityVelocity: {
				int EID = reader.readInt();
				int vx = reader.readShort();
				int vy = reader.readShort();
				int vz = reader.readShort();
				Entity ent = world.entities.get(EID);
				if (ent != null) {
					ent.velocity(vx, vy, vz);
				}
				break;
			}
			case Packets.DestroyEntity: {
				int EID = reader.readInt();
				world.entities.remove(EID);
				break;
			}
			case Packets.Entity:
				reader.readInt();
				break;
				
			case Packets.EntityRelMove: {
				int EID = reader.readInt();
				int dx = reader.readByte();
				int dy = reader.readByte();
				int dz = reader.readByte();
				Entity ent = world.entities.get(EID);
				if (ent != null) {
					ent.move(dx, dy, dz);
				}
				break;
			}
			case Packets.EntityLook: {
				int EID = reader.readInt();
				int yaw = reader.readByte();
				int pitch = reader.readByte();
				Entity ent = world.entities.get(EID);
				if (ent != null) {
					ent.look(yaw, pitch);
				}
				break;
			}
			case Packets.EntityRelMoveLook: {
				int EID = reader.readInt();
				int dx = reader.readByte();
				int dy = reader.readByte();
				int dz = reader.readByte();
				int yaw = reader.readByte();
				int pitch = reader.readByte();
				Entity ent = world.entities.get(EID);
				if (ent != null) {
					ent.move(dx, dy, dz);
					ent.look(yaw, pitch);
				}
				break;
			}
			case Packets.EntityTeleport: {
				int EID = reader.readInt();
				int x = reader.readInt();
				int y = reader.readInt();
				int z = reader.readInt();
				int yaw = reader.readByte();
				int pitch = reader.readByte();
				Entity ent = world.entities.get(EID);
				if (ent != null) {
					ent.teleport(x, y, z);
					ent.look(yaw, pitch);
				}
				break;
			}
			case Packets.EntityStatus: // TODO
				reader.skipBytes(5);
				break;
				
			case Packets.AttachEntity: // TODO
				reader.skipBytes(8);
				break;
				
			case Packets.EntityMetadata: { // TODO
				int EID = reader.readInt();
				readMetadata();
				break;
			}
			case Packets.ChunkAction: {
				int cx = reader.readInt();
				int cz = reader.readInt();
				boolean create = reader.readBoolean();
				if (create) {
					world.createEmptyChunk(cx, cz);
				} else {
					world.deleteChunk(cx, cz);
				}
				break;
			}
			case Packets.ChunkLoad: {
				int x = reader.readInt();
				int y = reader.readShort();
				int z = reader.readInt();
				int sx = reader.readByte();
				int sy = reader.readByte();
				int sz = reader.readByte();
				int size = reader.readInt();
				byte[] data = readBytes(size);
				world.readChunkData(x, y, z, sx+1, sy+1, sz+1, data);
				break;
			}
			case Packets.MultiBlockChange: {
				int cx = reader.readInt();
				int cz = reader.readInt();
				int len = reader.readShort();
				byte[] coords = readBytes(len*2);
				byte[] types = readBytes(len);
				byte[] metadata = readBytes(len);
				world.multiBlockChange(cx, cz, len, coords, types, metadata);
				break;
			}
			case Packets.BlockChange: {
				int x = reader.readInt();
				int y = reader.readByte();
				int z = reader.readInt();
				int type = reader.readByte();
				int metadata = reader.readByte();
				world.setBlock(x, y, z, type);
				world.setData(x, type, z, metadata);
				break;
			}
			case Packets.BlockAction: // TODO
				reader.skipBytes(12);
				break;
				
			case Packets.Explosion: { // TODO
				reader.skipBytes(28);
				int count = reader.readInt();
				reader.skipBytes(3*count);
				break;
			}
			case Packets.SoundEffect: // TODO
				reader.skipBytes(17);
				break;
				
			case Packets.NewState: // TODO
				reader.readByte();
				break;
				
			case Packets.Thunderbolt: // TODO
				reader.skipBytes(17);
				break;
				
			case Packets.OpenWindow: // TODO
				reader.skipBytes(2);
				readString8();
				reader.readByte();
				break;
				
			case Packets.CloseWindow: // TODO
				reader.readByte();
				break;
				
			case Packets.SetSlot: {
				reader.readByte();
				int slot = reader.readShort();
				int itemID = reader.readShort();
				if (itemID != -1 && slot != -1) {
					int count = reader.readByte();
					int uses = reader.readShort();
					player.inventory.addItem(slot, itemID, count, uses);
				} else if (slot != -1) {
					player.inventory.deleteItem(slot);
				} else if (itemID != -1) {
					reader.readByte();
					reader.readShort();
				}
				break;
			}
			case Packets.WindowItem: {
				reader.readByte();
				int len = reader.readShort();
				int count, uses;
				for (int i = 0; i < len; i++) {
					int type = reader.readShort();
					if (type != -1) {
						count = reader.readByte();
						uses = reader.readShort();
						player.inventory.addItem(i, type, count, uses);
					}						
				}
				player.inventory.print();
				break;
			}
			case Packets.UpdateProgressBar: // TODO
				reader.skipBytes(5);
				break;
				
			case Packets.Transaction: // TODO
				reader.skipBytes(5);
				break;
				
			case Packets.UpdateSign: // TODO
				reader.skipBytes(10);
				System.out.println(readString16());
				readString16();
				readString16();
				readString16();
				break;
				
			case Packets.MapData: { // TODO
				reader.skipBytes(4);
				int len = reader.readUnsignedByte();
				reader.skipBytes(len);
				break;
			}
			case Packets.IncrementStatistic: // TODO
				reader.skipBytes(5);
				break;
				
			case Packets.Kick: {
				String reason = readString16();
				System.out.println("Kicked: " + reason);
				System.exit(0);
				break;
			}	
			default:
				System.out.println("Unknown opcode: "+ Integer.toHexString(opcode) + "|"+opcode);
				System.out.println("Last opcode:"+Integer.toHexString(lastOpcode));
				System.out.println("Quitting.");
				System.exit(0);
				break;
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.out.println("Recorded opcode: " + opcode);
		}
		lastOpcode = opcode;
	}
}
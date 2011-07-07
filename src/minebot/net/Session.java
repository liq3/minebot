package minebot.net;

import java.io.*;
import java.net.*;

import minebot.Player;
import minebot.world.NamedEntity;
import minebot.world.World;


public class Session {
	
	public Player player;
	public boolean connected;
	
	public World map;
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
		String loginData = URLEncoder.encode("user", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");
		loginData += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
		loginData += "&" + URLEncoder.encode("version", "UTF-8") + "=" + URLEncoder.encode("12", "UTF-8");
		
		URL url = new URL("https://login.minecraft.net");
		URLConnection conn = url.openConnection();
		conn.setDoOutput(true);
		
		OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
		wr.write(loginData);
		wr.flush();
		
		BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String line = rd.readLine();
		System.out.println(line);
		if (line.equals("Bad login")) {
			System.exit(0);
		}
		sessionID = line.split(":")[3];
		
		wr.close();
		rd.close();
	}
	
	// Connect to a server
	public void connect(String host, int port) throws IOException {
		socket = new Socket(host, port);
		reader = new DataInputStream(socket.getInputStream());
		writer = new PacketWriter(socket.getOutputStream());
		
		map = new World();
		
		// handshake
		writer.writeHandshake(username);
		
		System.out.println(reader.readByte());
		System.out.println(reader.readShort());
		
		byte buff[] = new byte[1024];
		reader.read(buff);
		String hash = new String(buff, "UTF-16BE");
		System.out.println(hash);
		
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
		
		// login request
		writer.writeLoginRequest(username);
		int packetID = reader.readUnsignedByte();
		if (packetID == PacketID.Kick) {
			throw new IOException("Server did not welcome its new robot overlord.");
		}
		EID = reader.readInt();
		System.out.println("Login Response: " + EID + " "+reader.readShort()+" "+reader.readLong()+" "+reader.readUnsignedByte());
		connected = true;
	}
	
	// Bind player and session instances and then begin work
	public void begin(Player player) throws IOException, InterruptedException {
		if (!connected) {
			System.out.println("You need to connect to a server first.");
			System.exit(0);
		}
		
		this.player = player;
		
		while(socket.isConnected()) {
			while (reader.available() > 0) {
				readPacket();
			}
			player.logic();
			Thread.sleep(100);
		}
	}
	
	private int lastOpcode = 0;
	
	public void readPacket() {
		// Position
		
		byte buff[] = new byte[4092];
		int opcode = 0;		
		try {
			opcode = reader.readUnsignedByte();
			//System.out.println("op:"+Integer.toHexString(opcode));
			int x, y, z, dx, dy, dz, len, type, EID;
			byte[] metadata;
			switch (opcode) {
				case PacketID.ChatMessage:
					len = reader.readShort();
					reader.read(buff, 0, len*2);
					player.handleChat(new String(buff, 0, len*2, "UTF-16BE"));
					break;
					
				case PacketID.TimeUpdate:
					reader.readLong();
					break;
					
				case PacketID.EntityEqupment:
					reader.readInt();
					reader.readShort();
					reader.readShort();
					reader.readShort();
					break;
					
				case PacketID.SpawnPosition:
					player.spawnX = reader.readInt();
					player.spawnY = reader.readInt();
					player.spawnZ = reader.readInt();
					break;
					
				case PacketID.HealthUpdate:
					int hp = reader.readShort();
					if (hp <= 0) {
						writer.writeRespawn(PacketWriter.DIMENSION_WORLD);
					}
					break;
					
				case PacketID.Respawn:
					reader.readByte();
					player.stance = player.y = player.spawnY*32;
					break;
					
				case PacketID.PositionAndLook:
					player.x = reader.readDouble();
					player.stance = reader.readDouble();
					player.y = reader.readDouble();
					player.z = reader.readDouble();
					player.yaw = reader.readFloat();
					player.pitch = reader.readFloat();
					player.onGround = reader.readBoolean();
					player.spawned = true;
					break;
					
				case PacketID.BlockPlacement:
					reader.readInt();
					reader.readByte();
					reader.readInt();
					reader.readByte();
					int id = reader.readShort();
					if (id >= 0) {
						reader.readByte();
						reader.readShort();
					}
					break;
					
				case PacketID.UseBed:
					reader.readInt();
					reader.readByte();
					reader.readInt();
					reader.readByte();
					reader.readInt();
					break;
					
				case PacketID.Animation:
					reader.readInt();
					reader.readByte();
					break;
					
				case PacketID.EntityAction:
					reader.readInt();
					reader.readByte();
					break;
					
				case PacketID.SpawnNamedEntity:
					EID = reader.readInt();
					len = reader.readShort();
					reader.read(buff, 0, len*2);
					x = reader.readInt();
					y = reader.readInt();
					z = reader.readInt();
					int yaw = reader.readByte();
					int pitch = reader.readByte();
					int item = reader.readShort();
					player.entityList.add(new NamedEntity(EID, x,y,z, yaw,pitch, new String(buff, 0, len*2, "UTF-16BE"), item));
					break;
					
				case PacketID.SpawnPickup:
					reader.skipBytes(24);
					break;
					
				case PacketID.CollectItem:
					reader.readInt();
					reader.readInt();
					break;
					
				case PacketID.SpawnObject:
					reader.readInt();
					reader.readByte();
					reader.readInt();
					reader.readInt();
					reader.readInt();
					int flag = reader.readInt();
					if (flag > 0) {
						reader.readShort();
						reader.readShort();
						reader.readShort();
					}
					break;
					
				case PacketID.SpawnMob:
					reader.readInt();
					reader.readByte();
					reader.readInt();
					reader.readInt();
					reader.readInt();
					reader.readByte();
					reader.readByte();
					readMetadata();
					break;
					
				case PacketID.DestroyEntity:
					reader.readInt();
					break;
					
				case PacketID.EntityRelMove:
 					EID = reader.readInt();
					dx = reader.readByte();
					dy = reader.readByte();
					dz = reader.readByte();
					if (EID == player.EID) {
						player.x += (double)dx/32;
						player.y += (double)dy/32;
						player.stance += (double)dy/32;
						player.z += (double)dz/32;
					} else {
						for (int i = 0; i < player.entityList.size(); i++) {
							NamedEntity ent = player.entityList.get(i);
							if (ent.EID == EID) {
								ent.move(dx, dy, dz);
							}
						}
					}
					break;
					
				case PacketID.EntityLook:
					reader.readInt();
					reader.readByte();
					reader.readByte();
					break;
					
				case PacketID.EntityRelMoveLook:
					EID = reader.readInt();
					dx = reader.readByte();
					dy = reader.readByte();
					dz = reader.readByte();
					reader.readByte();
					reader.readByte();
					if (EID == player.EID) {
						player.x += (double)dx/32;
						player.y += (double)dy/32;
						player.stance += (double)dy/32;
						player.z += (double)dz/32;
					} else {
						for (int i = 0; i < player.entityList.size(); i++) {
							NamedEntity ent = player.entityList.get(i);
							if (ent.EID == EID) {
								ent.move(dx, dy, dz);
							}
						}
					}
					break;
					
				case PacketID.EntityTeleport:
					EID = reader.readInt();
					x = reader.readInt();
					y = reader.readInt();
					z = reader.readInt();
					reader.readByte();
					reader.readByte();
					if (EID == player.EID) {
						player.x = (double)x/32;
						player.y = (double)y/32;
						player.stance = (double)y/32 + 1.62;
						player.z = (double)z/32;
					} else {
						for (int i = 0; i < player.entityList.size(); i++) {
							NamedEntity ent = player.entityList.get(i);
							if (ent.EID == EID) {
								ent.teleport(x, y, z);
							}
						}
					}
					break;
					
				case PacketID.EntityStatus:
					reader.readInt();
					reader.readByte();
					break;
					
				case PacketID.AttachEntity:
					reader.readInt();
					reader.readInt();
					break;
					
				case PacketID.EntityMetadata:
					reader.readInt();
					readMetadata();
					break;
					
				case PacketID.ChunkAction:
					int cx = reader.readInt();
					int cz = reader.readInt();
					boolean mode = reader.readBoolean();
					if (mode) {
						map.createEmptyChunk(cx, cz);
					} else {
						map.deleteChunk(cx, cz);
					}
					break;
					
				case PacketID.ChunkLoad:
					x = reader.readInt();
					y = reader.readShort();
					z = reader.readInt();
					int sx = reader.readByte();
					int sy = reader.readByte();
					int sz = reader.readByte();
					int size = reader.readInt();
					byte[] data = new byte[size];
					int recv = reader.read(data, 0, size);
					while (recv < size) {
						recv += reader.read(data, recv, size-recv);
					}
					map.readChunkData(x,y,z,sx,sy,sz,data);
					break;
					
				case PacketID.MultiBlockChange:
					int chunkX = reader.readInt();
					int chunkZ = reader.readInt();
					len = reader.readShort();
					byte[] coords = new byte[len*2];					
					byte[] types = new byte[len];
					metadata = new byte[len];
					reader.read(coords, 0, len*2);
					reader.read(types, 0, len);					
					reader.read(metadata, 0, len);
					map.multiBlockChange(chunkX, chunkZ, len, coords, types, metadata);
					break;
					
				case PacketID.BlockChange:
					x = reader.readInt();
					y = reader.readByte();
					z = reader.readInt();
					type = reader.readByte();
					reader.readByte(); // metadata
					map.setBlockType(x,y,z,type);
					break;
				
				case PacketID.SoundEffect:
					reader.readInt();
					reader.readInt();
					reader.readByte();
					reader.readInt();
					reader.readInt();
					break;
					
				case PacketID.SetSlot:
					reader.readByte();
					int slot = reader.readShort();
					type = reader.readShort();
					if (type != -1 && slot != -1) {
						int count = reader.readByte();
						int uses = reader.readShort();
						player.inventory.addItem(slot, type, count, uses);
					} else if (slot != -1) {
						player.inventory.deleteItem(slot);
					} else {
						if (type != -1) {
							reader.readByte();
							reader.readShort();
						}
					}
					break;
					
				case PacketID.WindowItem:
					reader.readByte();
					len = reader.readShort();
					int count, uses;
					for (int i = 0; i < len; i++) {
						type = reader.readShort();
						if (type != -1) {
							count = reader.readByte();
							uses = reader.readShort();
							player.inventory.addItem(i, type, count, uses);
						}						
					}
					player.inventory.print();
					break;
					
				case PacketID.Transaction:
					reader.skipBytes(5);
					break;
					
				case PacketID.Kick:
					len = reader.readShort();
					reader.read(buff, 0, len*2);
					System.out.println("Kick: "+ new String(buff, "UTF-16BE"));
					System.exit(0);
					break;
	 				
				// We don't care about these yet. All of these are silently ignored. 			
				default: {
					reader.read(buff);
					System.out.println("Unknown opcode: "+ Integer.toHexString(opcode) + "|"+opcode);
					System.out.println("Last opcode:"+Integer.toHexString(lastOpcode));
					System.out.println("Quitting.");
					System.exit(0);
					break;
				}
			}
		} catch (IOException e) {
			System.out.println("Recorded opcode: " + opcode);
		}
		lastOpcode = opcode;
	}
	
	public void readMetadata() throws IOException {
		byte x = reader.readByte();
		while (x != 127) {
			switch (x >> 5) {
			case 0: 
				reader.readByte();
				break;
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:
			case 6:
				System.out.println("Op 0x18 non case 0.");
				System.exit(0);
			}
			x = reader.readByte();
		}
	}
}

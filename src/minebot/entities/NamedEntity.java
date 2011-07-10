package minebot.entities;


public class NamedEntity extends Entity {
	public String name;
	public int currentItem;
	
	public NamedEntity(int EID, int x, int y, int z, int yaw, int pitch, String name, int currentItem) {
		this.EID = EID;
		this.name = name;
		this.currentItem = currentItem;
		teleport(x, y, z);
		look(yaw, pitch);
		System.out.println(this.toString());
	}
	
	public String toString() {
		return String.format("Named Entity: name=%s, EID=%d, x=%f, y=%f, z=%f, yaw=%f, pitch=%f, currentItem=%d", name, EID, x(), y(), z(), yaw, pitch, currentItem);
	}
}

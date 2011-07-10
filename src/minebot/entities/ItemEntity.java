package minebot.entities;


public class ItemEntity extends Entity {
	public Item item;
	public double roll;
	
	public ItemEntity(int EID, int x, int y, int z, int yaw, int pitch, int roll, Item item) {
		this.EID = EID;
		teleport(x, y, z);
		look(yaw, pitch);
		this.item = item;
		this.roll = roll;
	}
	
	@Override
	public String toString() {
		return String.format("Item Entity: EID=%d, x=%f, y=%f, z=%f, yaw=%f, pitch=%f, roll=%f %s", EID, x(), y(), z(), yaw, pitch, roll, item.toString());
	}
}

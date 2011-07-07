package minebot.world;

public class Item {
	public int ID;
	public int count;
	public int data; // damage/metadata, depends on item
	
	public Item(int ID, int count, int data) {
		this.ID = ID;
		this.count = count;
		this.data = data;
	}
	
	@Override
	public String toString() {
		return String.format("Item: ID=%d, count=%d, data=%d", ID, count, data);
	}
}

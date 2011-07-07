package minebot.world;

import java.util.HashMap;


public final class EntityManager {
	
	public HashMap<Integer, Entity> entities;
	
	// for much quicker raw looping, entity add/remove are
	// probably called infrequently enough for cache updating
	// to not be a big deal
	public Entity[] entityCache;
	
	public EntityManager() {
		entities = new HashMap<Integer, Entity>();
		entityCache = new Entity[0];
	}
	
	public void add(Entity ent) {
		entities.put(ent.EID, ent);
		updateCache();
	}
	
	public void remove(int EID) {
		entities.remove(EID);
		updateCache();
	}
	public void remove(Entity ent) {
		remove(ent.EID);
	}
	
	public Entity get(int EID) {
		return entities.get(EID);
	}
	
	public boolean contains(int EID) {
		return entities.containsKey(EID);
	}
	public boolean contains(Entity ent) {
		return entities.containsKey(ent.EID);
	}
	
	private void updateCache() {
		entityCache = (Entity[])entities.values().toArray();
	}
}

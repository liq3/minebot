package minebot.entities;

import java.util.HashMap;

public final class EntityManager {
	
	public HashMap<Integer, Entity> entities;
	
	// quick access of named entities
	public HashMap<String, NamedEntity> namedEntities;
	
	// for much quicker raw looping, entity add/remove are
	// probably called infrequently enough for cache updating
	// to not be a big deal
	public Entity[] entityCache;
	
	public EntityManager() {
		entities = new HashMap<Integer, Entity>();
		namedEntities = new HashMap<String, NamedEntity>();
		entityCache = new Entity[0];
	}
	
	public void add(Entity ent) {
		entities.put(ent.EID, ent);
		updateCache();
		if (ent instanceof NamedEntity) {
			NamedEntity e = (NamedEntity)ent;
			namedEntities.put(e.name, e);
		}
	}
	
	public void remove(int EID) {
		Entity e = entities.remove(EID);
		updateCache();
		if (e instanceof NamedEntity) {
			removeByName(((NamedEntity)e).name);
		}
	}
	public void remove(Entity ent) {
		remove(ent.EID);
	}
	public void removeByName(String name) {
		namedEntities.remove(name);
	}
	
	public Entity get(int EID) {
		return entities.get(EID);
	}
	public NamedEntity getByName(String name) {
		return namedEntities.get(name);
	}
	
	public boolean contains(int EID) {
		return entities.containsKey(EID);
	}
	public boolean contains(Entity ent) {
		return entities.containsKey(ent.EID);
	}
	public boolean containsName(String name) {
		return namedEntities.containsKey(name);
	}
	
	private void updateCache() {
		entityCache = entities.values().toArray(new Entity[entities.size()]);
	}
}
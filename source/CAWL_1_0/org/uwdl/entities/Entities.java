package org.uwdl.entities;

import java.util.HashSet;
import java.util.Set;

public class Entities implements IEntities {
	private final EntitiesTag  entitiesTag;
	private final Set<Entity> entities = new HashSet<Entity>();
	
	public Entities(EntitiesTag entitiesTag) {
		this.entitiesTag = entitiesTag;
	}
	
	public Entity[] getEntities() {
		return entities.toArray(new Entity[0]);
	}
	
	public void addEntity(Entity entity) {
		entities.add(entity);
	}
	
	public EntitiesTag getEntitiesTag() {
		return entitiesTag;
	}
}

package org.game;

import org.game.enums.EntityType;

public class Entity {
    public EntityType type;
    public Position position;

    public Entity(EntityType type, Position position) {
        this.type = type;
        this.position = position;
    }
}

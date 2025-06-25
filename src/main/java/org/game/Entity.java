package org.game;

import org.game.enums.EntityType;

/**
 * Represents an entity on the game board.
 * An entity has a type (e.g., A-player, B-player, obstacle, phone) and a current position.
 */
public class Entity {

    /** The type of entity (A, B, OBSTACLE, PHONE, etc.) */
    public EntityType type;

    /** The current position of the entity on the board */
    public Position position;

    /**
     * Constructs an entity of a given type and position.
     *
     * @param type the type of the entity
     * @param position the position of the entity on the board
     */
    public Entity(EntityType type, Position position) {
        this.type = type;
        this.position = position;
    }
}

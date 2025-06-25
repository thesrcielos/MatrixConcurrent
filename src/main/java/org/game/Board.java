package org.game;

import org.game.enums.EntityType;

import java.util.*;

/**
 * The {@code Board} class represents the 10x10 game matrix for the MatrixConcurrent simulation.
 * It manages the state of all entities in the game: A (agent), B (enemies), T (phones), and obstacles (#).
 *
 * It provides methods for initializing the game state, placing entities randomly,
 * displaying the board, and updating the positions of entities during the simulation.
 */
public class Board {

    /** Fixed size of the board (10x10). */
    private final int size = 10;

    /** The internal matrix grid storing the entity type at each cell. */
    private final EntityType[][] grid = new EntityType[size][size];

    /** List of all entities currently on the board. */
    private final List<Entity> entities = new ArrayList<>();

    /** Reference to the single A-player. */
    public Entity aPlayer;

    /** List of B-player entities (enemies). */
    public List<Entity> bPlayers = new ArrayList<>();

    /** List of phone positions (targets for A). */
    public List<Position> phones = new ArrayList<>();

    /**
     * Constructs an empty board, initializing all cells to {@link EntityType#EMPTY}.
     */
    public Board() {
        for (int i = 0; i < size; i++)
            Arrays.fill(grid[i], EntityType.EMPTY);
    }

    /**
     * Initializes the board by placing:
     * - 7 obstacles
     * - 1 phone
     * - 1 A-player
     * - 2 B-players
     */
    public void initialize() {
        placeRandom(EntityType.OBSTACLE, 7);
        placePhones(1);
        placeA();
        placeB(2);
    }

    /**
     * Places a given number of entities of the specified type at random empty positions.
     *
     * @param type  the entity type to place
     * @param count number of entities to place
     */
    private void placeRandom(EntityType type, int count) {
        Random rand = new Random();
        while (count > 0) {
            int r = rand.nextInt(size);
            int c = rand.nextInt(size);
            if (grid[r][c] == EntityType.EMPTY) {
                grid[r][c] = type;
                entities.add(new Entity(type, new Position(r, c)));
                count--;
            }
        }
    }

    /**
     * Places a given number of phones at random empty positions.
     *
     * @param count number of phones to place
     */
    private void placePhones(int count) {
        Random rand = new Random();
        while (count > 0) {
            int r = rand.nextInt(size);
            int c = rand.nextInt(size);
            if (grid[r][c] == EntityType.EMPTY) {
                Position pos = new Position(r, c);
                grid[r][c] = EntityType.PHONE;
                phones.add(pos);
                entities.add(new Entity(EntityType.PHONE, pos));
                count--;
            }
        }
    }

    /**
     * Places a single A-player at a random empty position.
     */
    private void placeA() {
        Random rand = new Random();
        while (true) {
            int r = rand.nextInt(size);
            int c = rand.nextInt(size);
            if (grid[r][c] == EntityType.EMPTY) {
                Position pos = new Position(r, c);
                aPlayer = new Entity(EntityType.A, pos);
                grid[r][c] = EntityType.A;
                entities.add(aPlayer);
                break;
            }
        }
    }

    /**
     * Places a given number of B-players (enemies) at random empty positions.
     *
     * @param count number of B-players to place
     */
    private void placeB(int count) {
        Random rand = new Random();
        while (count > 0) {
            int r = rand.nextInt(size);
            int c = rand.nextInt(size);
            if (grid[r][c] == EntityType.EMPTY) {
                Position pos = new Position(r, c);
                Entity b = new Entity(EntityType.B, pos);
                bPlayers.add(b);
                grid[r][c] = EntityType.B;
                entities.add(b);
                count--;
            }
        }
    }

    /**
     * Displays the current state of the board in the console.
     * Each cell prints the symbol of the {@link EntityType} it contains.
     */
    public void display() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                System.out.print(grid[i][j].symbol + " ");
            }
            System.out.println();
        }
    }

    /**
     * Checks if the given position is within the bounds of the board.
     *
     * @param p the position to check
     * @return true if the position is valid; false otherwise
     */
    public boolean isInBounds(Position p) {
        return p.row >= 0 && p.row < size && p.col >= 0 && p.col < size;
    }

    /**
     * Determines whether a position can be moved to.
     * A position is walkable if it is empty, contains a phone, or contains A itself.
     *
     * @param p the position to check
     * @return true if the cell is walkable
     */
    public boolean isWalkable(Position p) {
        EntityType t = grid[p.row][p.col];
        return t == EntityType.EMPTY || t == EntityType.PHONE || t == EntityType.A;
    }

    /**
     * Moves the specified entity to a new position on the board.
     * The entity's internal position is updated, and the grid is modified.
     *
     * @param entity the entity to move
     * @param newPos the new position to move to
     */
    public void moveEntity(Entity entity, Position newPos) {
        grid[entity.position.row][entity.position.col] = EntityType.EMPTY;
        entity.position = newPos;
        grid[newPos.row][newPos.col] = entity.type;
    }

    /**
     * Returns the entity type present at a given position on the grid.
     *
     * @param p the position to query
     * @return the entity type at that position
     */
    public EntityType getAt(Position p) {
        return grid[p.row][p.col];
    }

    /**
     * Sets the entity type at a specific position in the grid.
     *
     * @param position the position to set
     * @param entity   the entity type to place
     */
    public void setEntity(Position position, EntityType entity){
        if (isInBounds(position)){
            grid[position.row][position.col] = entity;
        }
    }
}

package org.game;

import org.game.enums.EntityType;

import java.util.*;

public class Board {
    private final int size = 10;
    private final EntityType[][] grid = new EntityType[size][size];
    private final List<Entity> entities = new ArrayList<>();
    public Entity aPlayer;
    public List<Entity> bPlayers = new ArrayList<>();
    public List<Position> phones = new ArrayList<>();

    public Board() {
        for (int i = 0; i < size; i++)
            Arrays.fill(grid[i], EntityType.EMPTY);
    }

    public void initialize() {
        placeRandom(EntityType.OBSTACLE, 7);
        placePhones(1);
        placeA();
        placeB(2);
    }

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

    public void display() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                System.out.print(grid[i][j].symbol + " ");
            }
            System.out.println();
        }
    }

    public boolean isInBounds(Position p) {
        return p.row >= 0 && p.row < size && p.col >= 0 && p.col < size;
    }

    public boolean isWalkable(Position p) {
        EntityType t = grid[p.row][p.col];
        return t == EntityType.EMPTY || t == EntityType.PHONE;
    }

    public void moveEntity(Entity entity, Position newPos) {
        grid[entity.position.row][entity.position.col] = EntityType.EMPTY;
        entity.position = newPos;
        grid[newPos.row][newPos.col] = entity.type;
    }

    public EntityType getAt(Position p) {
        return grid[p.row][p.col];
    }
}

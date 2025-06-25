package org.game;

import org.game.enums.EntityType;

import java.util.*;

/**
 * Utility class for game logic operations, including pathfinding using the A* algorithm.
 */
public class GameUtils {

    /**
     * Finds the next optimal position for an entity to move toward one of the goal positions using
     * the A* pathfinding algorithm.
     *
     * @param board the current game board
     * @param start the starting position of the entity
     * @param goals a list of possible goal positions to reach
     * @return the immediate next {@link Position} toward the closest reachable goal,
     *         or {@code null} if no path is found
     */
    public static synchronized Position findNextMoveTowards(Board board, Position start, List<Position> goals) {
        Set<Position> goalSet = new HashSet<>(goals);
        Map<Position, Position> cameFrom = new HashMap<>();
        Map<Position, Double> gScore = new HashMap<>();
        Map<Position, Double> fScore = new HashMap<>();

        Comparator<Position> comparator = Comparator.comparingDouble(p -> fScore.getOrDefault(p, Double.POSITIVE_INFINITY));
        PriorityQueue<Position> openSet = new PriorityQueue<>(comparator);
        Set<Position> closedSet = new HashSet<>();

        gScore.put(start, 0.0);
        fScore.put(start, estimateMinDistance(start, goals));
        openSet.add(start);

        int[][] dirs = {
                {-1, 0}, {1, 0}, {0, -1}, {0, 1},
                {-1, -1}, {-1, 1}, {1, 1}, {1, -1}
        };

        while (!openSet.isEmpty()) {
            Position current = openSet.poll();

            // If we've reached any goal, backtrack to find the next move
            if (goalSet.contains(current)) {
                Position backtrack = current;
                while (cameFrom.containsKey(backtrack) && !cameFrom.get(backtrack).equals(start)) {
                    backtrack = cameFrom.get(backtrack);
                }
                return cameFrom.containsKey(backtrack) ? backtrack : current;
            }

            closedSet.add(current);

            // Explore neighbors
            for (int[] d : dirs) {
                Position neighbor = new Position(current.row + d[0], current.col + d[1]);

                if (!board.isInBounds(neighbor) || !board.isWalkable(neighbor) || closedSet.contains(neighbor))
                    continue;

                double tentativeG = gScore.get(current) + distance(current, neighbor);

                if (tentativeG < gScore.getOrDefault(neighbor, Double.POSITIVE_INFINITY)) {
                    cameFrom.put(neighbor, current);
                    gScore.put(neighbor, tentativeG);
                    double h = estimateMinDistance(neighbor, goals);
                    fScore.put(neighbor, tentativeG + h);

                    // Update priority in open set
                    openSet.remove(neighbor);
                    openSet.add(neighbor);
                }
            }
        }

        return null;
    }

    /**
     * Calculates the Euclidean distance between two positions.
     *
     * @param a the first position
     * @param b the second position
     * @return the Euclidean distance between a and b
     */
    private static double distance(Position a, Position b) {
        int dx = a.row - b.row;
        int dy = a.col - b.col;
        return Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * Estimates the minimum distance from a given position to any of the goals.
     * Used as the heuristic function in A*.
     *
     * @param from  the starting position
     * @param goals the list of goal positions
     * @return the minimum Euclidean distance to any goal
     */
    private static double estimateMinDistance(Position from, List<Position> goals) {
        return goals.stream()
                .mapToDouble(goal -> distance(from, goal))
                .min()
                .orElse(Double.POSITIVE_INFINITY);
    }
}

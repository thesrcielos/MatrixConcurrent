package org.game;

import org.game.enums.EntityType;

import java.util.*;

public class GameUtils {
    public static synchronized Position findNextMoveTowards(Board board, Position start, List<Position> goals) {
        Queue<Position> q = new LinkedList<>();
        Map<Position, Position> cameFrom = new HashMap<>();
        Set<Position> visited = new HashSet<>();
        q.add(start);
        visited.add(start);

        System.out.println("START-----------------");
        System.out.println("goal " + goals.get(0).row +" "+ goals.get(0).col);
        int[][] dirs = {{-1,0},{1,0},{0,-1},{0,1},{-1,-1},{-1,1},{1,1},{1,-1}};
        Position target = null;
        System.out.println("start " + start.row + " " + start.col);

        while (!q.isEmpty()) {
            Position curr = q.poll();
            if (goals.contains(curr)) {
                target = curr;
                break;
            }
            for (int[] d : dirs) {
                Position next = new Position(curr.row + d[0], curr.col + d[1]);
                if (board.isInBounds(next) && board.isWalkable(next) && !visited.contains(next)) {
                    q.add(next);
                    visited.add(next);
                    cameFrom.put(next, curr);
                }
            }
        }

        if (target == null ) return null;
        System.out.println("target " + target.row + " " + target.col);
        if(!cameFrom.containsKey(target)) return null;

        Position backtrack = target;
        while (!cameFrom.get(backtrack).equals(start) && !cameFrom.get(backtrack).equals(start)) {
            backtrack = cameFrom.get(backtrack);
        }

        if (!cameFrom.containsKey(backtrack)) return null;

        return backtrack;
    }
}

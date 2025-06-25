import org.game.Board;
import org.game.Entity;
import org.game.GameUtils;
import org.game.Position;
import org.game.enums.EntityType;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GameUtilsTest {

    @Test
    public void testDirectPath_NoObstacles() {
        Board board = new Board();

        Position start = new Position(0, 0);
        Position goal = new Position(0, 2);

        board.aPlayer = new Entity(EntityType.A, start);
        board.setEntity(start, EntityType.A);
        board.setEntity(goal, EntityType.PHONE);

        Position next = GameUtils.findNextMoveTowards(board, start, List.of(goal));
        assertNotNull(next);
        assertEquals(new Position(0, 1), next);
    }

    @Test
    public void testPathWithObstacle() {
        Board board = new Board();

        Position start = new Position(0, 2);
        Position goal = new Position(2, 4);

        // Obstáculos que bloquean el camino recto
        board.setEntity(new Position(1, 1), EntityType.OBSTACLE);
        board.setEntity(new Position(1, 2), EntityType.OBSTACLE);
        board.setEntity(new Position(1, 3), EntityType.OBSTACLE);


        board.aPlayer = new Entity(EntityType.A, start);
        board.setEntity(start, EntityType.A);
        board.setEntity(goal, EntityType.PHONE);

        Position next = GameUtils.findNextMoveTowards(board, start, List.of(goal));
        assertNotNull(next);

        assertTrue(
                next.equals(new Position(0, 1)) ||
                        next.equals(new Position(0, 3)),
                "The next step should be avoid the obstacles"
        );
    }

    @Test
    public void testNoPathAvailable() {
        Board board = new Board();

        Position start = new Position(2, 1);
        Position goal = new Position(0, 0);

        board.aPlayer = new Entity(EntityType.A, start);
        board.setEntity(start, EntityType.A);
        board.setEntity(goal, EntityType.PHONE);

        board.setEntity(new Position(0, 1), EntityType.OBSTACLE);
        board.setEntity(new Position(1, 0), EntityType.OBSTACLE);
        board.setEntity(new Position(1, 1), EntityType.OBSTACLE);

        Position next = GameUtils.findNextMoveTowards(board, start, List.of(goal));
        assertNull(next, "it shouldnt exist an available path");
    }


}

import org.game.*;

import org.game.enums.EntityType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BoardTest {

    @Test
    public void testInitializeBoard() {
        Board board = new Board();
        board.initialize();

        int aCount = 0, bCount = 0, phoneCount = 0, obstacleCount = 0;

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                EntityType type = board.getAt(new Position(i, j));
                switch (type) {
                    case A -> aCount++;
                    case B -> bCount++;
                    case PHONE -> phoneCount++;
                    case OBSTACLE -> obstacleCount++;
                }
            }
        }

        assertEquals(1, aCount, "Debe haber exactamente un jugador A");
        assertEquals(2, bCount, "Debe haber exactamente dos jugadores B");
        assertEquals(1, phoneCount, "Debe haber exactamente un teléfono");
        assertEquals(7, obstacleCount, "Debe haber exactamente siete obstáculos");

        assertNotNull(board.aPlayer, "A debe estar inicializado");
        assertEquals(2, board.bPlayers.size(), "Debe haber 2 jugadores B");
        assertEquals(1, board.phones.size(), "Debe haber 1 teléfono en la lista");
    }

    @Test
    public void testMoveEntity() {
        Board board = new Board();
        Position start = new Position(5, 5);
        Position dest = new Position(5, 6);

        Entity entity = new Entity(EntityType.A, start);
        board.setEntity(start, EntityType.A);
        board.aPlayer = entity;

        board.moveEntity(entity, dest);

        assertEquals(EntityType.EMPTY, board.getAt(start), "La celda original debe quedar vacía");
        assertEquals(EntityType.A, board.getAt(dest), "La nueva posición debe contener A");
        assertEquals(dest, entity.position, "La entidad debe actualizar su posición interna");
    }

    @Test
    public void testIsWalkable() {
        Board board = new Board();
        Position walkablePos = new Position(0, 0);
        Position nonWalkablePos = new Position(0, 1);

        board.setEntity(walkablePos, EntityType.EMPTY);
        board.setEntity(nonWalkablePos, EntityType.OBSTACLE);

        assertTrue(board.isWalkable(walkablePos), "Celda vacía debe ser caminable");
        assertFalse(board.isWalkable(nonWalkablePos), "Obstáculo no debe ser caminable");

        board.setEntity(new Position(1, 1), EntityType.PHONE);
        assertTrue(board.isWalkable(new Position(1, 1)), "Celda con teléfono debe ser caminable");

        board.setEntity(new Position(2, 2), EntityType.B);
        assertFalse(board.isWalkable(new Position(2, 2)), "B no debe ser caminable");
    }
}

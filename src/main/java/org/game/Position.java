package org.game;

import java.util.Objects;

/**
 * Represents a coordinate (row, column) on the game board.
 * Positions are immutable and used to determine the location of entities.
 */
public class Position {

    /** The row index on the board */
    public final int row;

    /** The column index on the board */
    public final int col;

    /**
     * Constructs a new position with the specified row and column.
     *
     * @param row the row index
     * @param col the column index
     */
    public Position(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /**
     * Compares this position to another object for equality.
     * Two positions are equal if they have the same row and column.
     *
     * @param o the object to compare
     * @return true if the positions are equal; false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Position)) return false;
        Position that = (Position) o;
        return row == that.row && col == that.col;
    }

    /**
     * Returns a hash code for this position based on its row and column.
     *
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }
}

package org.game.enums;

public enum EntityType {
    OBSTACLE('#'),
    PHONE('T'),
    A('A'),
    B('B'),
    EMPTY('.');

    public final char symbol;

    EntityType(char symbol) {
        this.symbol = symbol;
    }
}

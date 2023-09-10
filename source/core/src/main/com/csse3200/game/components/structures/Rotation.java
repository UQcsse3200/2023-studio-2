package com.csse3200.game.components.structures;

public enum Rotation {
    NORTH,
    SOUTH,
    EAST,
    WEST;

    @Override
    public String toString() {
        return switch (this) {
            case NORTH -> "north";
            case SOUTH -> "south";
            case EAST -> "east";
            case WEST -> "west";
        };
    }

    public Rotation getRotationPos() {
        return switch (this) {
            case NORTH -> NORTH;
            case SOUTH -> SOUTH;
            case EAST -> EAST;
            case WEST -> WEST;
        };
    }
}

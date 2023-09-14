package com.csse3200.game.components.structures;

/**
 * Used by the RotationRenderComponent to indicate which atlasRegion to display.
 */
public enum Rotation {
    NORTH,
    SOUTH,
    EAST,
    WEST;

    /**
     * Converts the enum to a string representation.
     * @return a string representation of the enum.
     */
    @Override
    public String toString() {
        return switch (this) {
            case NORTH -> "north";
            case SOUTH -> "south";
            case EAST -> "east";
            case WEST -> "west";
        };
    }
}

package com.csse3200.game.components.structures;

/**
 * This interface defines methods components can implement which will be called when a structure
 * is placed or removed via the StructurePlacementService.
 */
public interface Placeable {
    /**
     * Called just before the structure is placed into the grid map.
     */
    default void willPlace() {}

    /**
     * Called after the structure is placed into the grid map.
     */
    default void placed() {}

    /**
     * Called after the structure is removed from the grid map.
     */
    default void removed() {}

    /**
     * Called just before the structure is removed from the grid map.
     */
    default void willRemove() {}
}

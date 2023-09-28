package com.csse3200.game.entities;

import com.badlogic.gdx.math.GridPoint2;
import com.csse3200.game.components.structures.Placeable;

public class PlaceableEntity extends Entity {

    boolean irremovable = false;
    private final int width;
    private final int height;
    private GridPoint2 position;

    public PlaceableEntity(int width, int height) {
        super();
        this.width = width;
        this.height = height;
    }

    public void setGridPosition(GridPoint2 position) {
        this.position = position;

    }

    public GridPoint2 getGridPosition() {
        return position;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    /**
     * This function uses the builder pattern to allow the programmer to make a
     * placeable entity irremovable
     * @return The placeable entity
     */
    public PlaceableEntity irremovable() {
        this.irremovable = true;
        return this;
    }

    /**
     * This function is called when a PlaceableEntity is placed via the structure
     * placement service. Override this method to implement specific functionality
     * when placed.
     */
    public void placed() {
        for (var component : components.values()) {
            if (component instanceof Placeable) {
                ((Placeable) component).placed();
            }
        }
    }

    /**
     * This function is called when a PlaceableEntity is removed via the structure
     * placement service. Override this method to implement specific functionality
     * when removed.
     */
    public void removed() {
        if (irremovable) {
            return;
        }
        for (var component : components.values()) {
            if (component instanceof Placeable) {
                ((Placeable) component).removed();
            }
        }
    }

    /**
     * This function is called when a PlaceableEntity is going to be placed via the structure
     * placement service. Override this method to implement specific functionality
     * when the entity is about to be placed.
     */
    public void willPlace() {
        for (var component : components.values()) {
            if (component instanceof Placeable) {
                ((Placeable) component).willPlace();
            }
        }
    }

    /**
     * This function is called when a PlaceableEntity is going to be removed via the structure
     * placement service. Override this method to implement specific functionality
     * when the entity is about to be removed.
     */
    public void willRemove() {
        if (irremovable) {
            return;
        }
        for (var component : components.values()) {
            if (component instanceof Placeable) {
                ((Placeable) component).willRemove();
            }
        }
    }

    public boolean is_irremovable() {
        return this.irremovable;
    }
}

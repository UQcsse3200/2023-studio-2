package com.csse3200.game.entities;

import com.csse3200.game.components.structures.Placeable;

import java.util.Objects;

public class PlaceableEntity extends Entity {

    boolean irremovable = false;
    private final int width;
    private final int height;

    public PlaceableEntity(int width, int height) {
        super();
        this.width = width;
        this.height = height;
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
            if (component instanceof Placeable placeable) {
                placeable.placed();
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
            if (component instanceof Placeable placeable) {
                placeable.removed();
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
            if (component instanceof Placeable placeable) {
                placeable.willPlace();
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
            if (component instanceof Placeable placeable) {
                placeable.willRemove();
            }
        }
    }

    public boolean isIrremovable() {
        return this.irremovable;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        PlaceableEntity that = (PlaceableEntity) o;
        return irremovable == that.irremovable && width == that.width && height == that.height && Objects.equals(position, that.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), irremovable, width, height, position);
    }
}

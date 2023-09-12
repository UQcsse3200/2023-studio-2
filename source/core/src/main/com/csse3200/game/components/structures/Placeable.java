package com.csse3200.game.components.structures;

public interface Placeable {
    default void willPlace() {
    }

    default void placed() {
    }

    default void removed() {
    }

    default void willRemove() {
    }

}

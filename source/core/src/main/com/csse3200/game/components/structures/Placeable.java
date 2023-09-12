package com.csse3200.game.components.structures;

public interface Placeable {
    public default void willPlace() {};
    public default void placed() {};

    public default void removed() {};
    public default void willRemove() {};
}

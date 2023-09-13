package com.csse3200.game.entities;

import com.csse3200.game.components.structures.Placeable;
import com.csse3200.game.services.GameStateInteraction;
import com.csse3200.game.services.GameStateObserver;
import com.csse3200.game.services.ServiceLocator;

import javax.xml.crypto.dsig.keyinfo.KeyValue;
import java.util.Map;

public class PlaceableEntity extends Entity {
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
        for (var component : components.values()) {
            if (component instanceof Placeable) {
                ((Placeable) component).willRemove();
            }
        }
    }
}

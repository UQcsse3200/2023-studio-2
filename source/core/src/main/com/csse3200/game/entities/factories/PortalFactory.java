package com.csse3200.game.entities.factories;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.Portal;

public class PortalFactory {

    /**
     * Teleports the player to a fixed destination
     *
     * @param width - the width of the portal object
     * @param height - the height of the portal object
     * @param player - the player instantiated in the current game area
     */
    public static Entity createPortal(float width, float height, Entity player) {
        Entity portal = new Portal(player);
        portal.setScale(width, height);
        return portal;
    }

    private PortalFactory() {
        throw new IllegalStateException("Instantiating static util class");
    }
}

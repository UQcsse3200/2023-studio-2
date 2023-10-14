package com.csse3200.game.entities;
import com.csse3200.game.components.EnvironmentStatsComponent;
import com.csse3200.game.components.ProximityActivationComponent;

/**
 * Safe zone class which extends Entity. Creates an entity which projects a safe zone in a radius
 * in which a player will stop taking constant environmental damage. Will also reduce the cold / heat
 * counter in the EnnvironmentStatsComponent
 */
public class SafeZone extends Entity {

    /** The x-coordinate. */
    private float x;

    /** The y-coordinate. */
    private float y;

    /**
     * Creates a safe zone entity
     *
     * @param player The player entity
     */
    public SafeZone(Entity player) {
        super();
        addComponent(new ProximityActivationComponent(3f, player, this::setSafe, this::setUnsafe));
    }

    /**
     * Gives the player environmental damage immunity
     *
     * @param player - the player entering the portal
     */
    public void setSafe(Entity player) {
        player.getComponent(EnvironmentStatsComponent.class).setIsImmune();
    }

    /**
     * Removes the player's environmental damage immunity
     *
     * @param player - the player entering the portal
     */
    public void setUnsafe(Entity player) {
        player.getComponent(EnvironmentStatsComponent.class).setNotImmune();
    }
}

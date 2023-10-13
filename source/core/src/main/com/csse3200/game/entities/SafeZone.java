package com.csse3200.game.entities;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.EnvironmentStatsComponent;
import com.csse3200.game.components.ProximityActivationComponent;
import com.csse3200.game.services.ServiceLocator;

/**
 * Portal class which extends Entity. Creates a Portal that if the player is within a certain amount
 * distance it will teleport the player to the set position on the map.
 */
public class SafeZone extends Entity {

    /** The x-coordinate. */
    private float x;

    /** The y-coordinate. */
    private float y;

    /**
     * Creates a portal, that will teleport the player entity.
     *
     * @param player The player entity
     */
    public SafeZone(Entity player) {
        super();
        addComponent(new ProximityActivationComponent(3f, player, this::setSafe, this::setSafe));
    }

    /**
     * Teleports the player to a fixed destination
     *
     * @param player - the player entering the portal
     */
    public void setSafe(Entity player) {
        player.getComponent(EnvironmentStatsComponent.class).setIsImmune();
    }

    /**
     * Sets the teleport location.
     *
     * @param x new x position
     * @param y new y position
     */
    @Override
    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }
}

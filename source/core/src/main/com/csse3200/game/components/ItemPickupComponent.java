package com.csse3200.game.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A component responsible for handling item pickups in the game.
 */
public class ItemPickupComponent extends Component {
    private Entity companion = ServiceLocator.getEntityService().getCompanion();

    private static Logger logger;

    /**
     * Constructs an ItemPickupComponent with the specified target layer for item collisions.
     *
     * @param targetLayer The layer to target for item collisions.
     */
    public ItemPickupComponent(short targetLayer) {
    }

    /**
     * Initializes the ItemPickupComponent and sets up collision event listeners for item pickups.
     */
    @Override
    public void create() {
        logger = LoggerFactory.getLogger(ItemPickupComponent.class);
        entity.getEvents().addListener("collisionStart", this::pickUp);
    }

    /**
     * Handles the pickup of items when a collision occurs.
     *
     * @param me     The fixture of the entity with this component.
     * @param other  The fixture of the other entity involved in the collision.
     */
    private void pickUp(Fixture me, Fixture other) {

        // Log the pick-up
        logger.info("Item picked up");
        // Remove the item from the game area
        Gdx.app.postRunnable(entity::dispose);
    }}

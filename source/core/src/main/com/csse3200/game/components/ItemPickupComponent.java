package com.csse3200.game.components;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.csse3200.game.areas.MapGameArea;
import com.csse3200.game.components.Companion.CompanionInventoryComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.components.HitboxComponent;
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
        Entity entityOfComponent = getEntity();
        /*// Apply the power-up effect
        entity.getComponent(PowerupComponent.class).applyEffect();*/
        // Log the pick-up
        logger.info("Item picked up");

//        // Check if the picked-up item is a potion (adjust this condition based on your implementation)
//        if (entityOfComponent.hasComponent(PotionComponent.class)) {
//            // Trigger the custom event
//            entityOfComponent.getEvents().trigger(CompanionInventoryDisplay.POTION_PICKED_UP_EVENT);
//        }


        // Add the power-up to the companion's inventory
        Entity companionEntity = ServiceLocator.getEntityService().getCompanion();
        CompanionInventoryComponent companionInventory = companionEntity.getComponent(CompanionInventoryComponent.class);

        if (companionInventory != null) {
            companionInventory.addPowerup(entityOfComponent);
        }
        // Remove the item from the game area
        MapGameArea.removeItemOnMap(entityOfComponent);
    }}

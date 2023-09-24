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
        short layer = companion.getComponent(HitboxComponent.class).getLayer();
        boolean isCompanion = PhysicsLayer.contains(layer, (short) (PhysicsLayer.PLAYER | PhysicsLayer.COMPANION));
        if (isCompanion){
        entity.getComponent(PotionComponent.class).applyEffect();
        Entity entityOfComponent = getEntity();
        MapGameArea.removeItemOnMap(entityOfComponent);
        logger.info("Item picked up");
        ServiceLocator.getEntityService().getCompanion().getComponent(CompanionInventoryComponent.class).addItem(entityOfComponent);}
        else {return;}
    }
}

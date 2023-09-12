package com.csse3200.game.components;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.csse3200.game.areas.EarthGameArea;
import com.csse3200.game.components.Companion.CompanionInventoryComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ItemPickupComponent extends Component{

    private static Logger logger;
    private HitboxComponent hitboxComponent;
    private short targetLayer;

    public ItemPickupComponent(short targetLayer) {
        this.targetLayer = targetLayer;
    }

    public void create() {
        logger = LoggerFactory.getLogger(ItemPickupComponent.class);
        entity.getEvents().addListener("collisionStart", this::pickUp);
    }

    private void pickUp(Fixture me, Fixture other){

        hitboxComponent = entity.getComponent(HitboxComponent.class);

        Entity entityOfComponent = getEntity();
        EarthGameArea.removeItemOnMap(entityOfComponent);
        logger.info("Item picked up");
        ServiceLocator.getGameArea().getCompanion().getComponent(CompanionInventoryComponent.class).addItem(entityOfComponent);

    }
}

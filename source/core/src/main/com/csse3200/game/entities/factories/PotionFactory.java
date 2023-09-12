package com.csse3200.game.entities.factories;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.ItemPickupComponent;
import com.csse3200.game.components.PotionComponent;
import com.csse3200.game.components.PotionType;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.TextureRenderComponent;

public class PotionFactory {

    public static Entity createPotion(PotionType type, Entity companionEntity ,Entity laboratoryEntity){
        Entity Potion = new Entity()
                        .addComponent(new PhysicsComponent().setBodyType(BodyDef.BodyType.StaticBody))
                        .addComponent(new HitboxComponent().setLayer(PhysicsLayer.COMPANION))
                        .addComponent(new ItemPickupComponent(PhysicsLayer.COMPANION))
                        .addComponent(new PotionComponent(type))
                        .addComponent(new CombatStatsComponent(1000,10,2,true));
        Potion.setScale(0.4f,0.4f);
        switch (type){
            case DEATH_POTION -> Potion.addComponent(new TextureRenderComponent("images/Potion.png"));
            default -> throw new IllegalArgumentException("You must assign a valid PotionType");
        }
        return Potion;
    }
    public static Entity createDeathPotion(Entity companionEntity, Entity laboratoryEntity) {
        return createPotion(PotionType.DEATH_POTION, companionEntity, laboratoryEntity);
    }
    public static boolean companionTouchesLaboratory( Vector2 companionPosition, Vector2 laboratoryPosition) {

        float distance = companionPosition.dst(laboratoryPosition);
        float touchDistanceThreshold = 8.0f;
        return distance < touchDistanceThreshold;
    }


}

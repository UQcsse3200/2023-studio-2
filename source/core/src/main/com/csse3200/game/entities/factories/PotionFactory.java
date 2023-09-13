package com.csse3200.game.entities.factories;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.csse3200.game.components.*;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.TextureRenderComponent;

public class PotionFactory {

    public static Entity createPotion(PotionType type,Entity player, Entity companion){
        Entity Potion = new Entity()
                        .addComponent(new PhysicsComponent().setBodyType(BodyDef.BodyType.StaticBody))
                        .addComponent(new HitboxComponent().setLayer(PhysicsLayer.COMPANION))
                        .addComponent(new ItemPickupComponent(PhysicsLayer.ITEMS_ABOVE_PLATFORM))
                        .addComponent(new CombatStatsComponent(80,10,2,true));
        Potion.setScale(0.4f,0.4f);
        switch (type){
            case DEATH_POTION -> {
                Potion.addComponent(new TextureRenderComponent("images/Potion3re.png"))
                        /*.addComponent(new PotionComponent(PotionType.DEATH_POTION).applyEffect();)*/;}
            case HEALTH_POTION -> {
                Potion.addComponent(new TextureRenderComponent("images/Potion4re.png"))
                        .addComponent(new PotionComponent(PotionType.DEATH_POTION))
                        .getComponent(PotionComponent.class).applyEffect(player,companion);}
            case SPEED_POTION -> {
                Potion.addComponent(new TextureRenderComponent("images/Potion2re.png"))
                        .addComponent(new PotionComponent(PotionType.SPEED_POTION))
                        .getComponent(PotionComponent.class).applyEffect(player,companion);}
            default -> throw new IllegalArgumentException("You must assign a valid PotionType");
        }
        return Potion;
    }



}

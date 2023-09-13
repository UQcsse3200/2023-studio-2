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
/**
 * The PotionFactory class is responsible for creating potion entities in the game.
 */
public class PotionFactory {

    /**
     * Creates a potion entity with the specified type and applies its effects to the player and companion.
     *
     * @param type       The type of potion to create.
     * @param player     The player entity.
     * @param companion  The companion entity.
     * @return The created potion entity.
     */
    public static Entity createPotion(PotionType type, Entity player, Entity companion) {
        // Create a new potion entity
        Entity potion = new Entity()
                .addComponent(new PhysicsComponent().setBodyType(BodyDef.BodyType.StaticBody))
                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.COMPANION))
                .addComponent(new ItemPickupComponent(PhysicsLayer.COMPANION))
                .addComponent(new CombatStatsComponent(80, 10, 2, true));

        // Set the scale of the potion
        potion.setScale(0.4f, 0.4f);

        // Add components based on the potion type
        switch (type) {
            case DEATH_POTION -> {
                potion.addComponent(new TextureRenderComponent("images/deathpotion.png"));
                // You may add specific effects for the DEATH_POTION here if needed.
            }
            case HEALTH_POTION -> {
                potion.addComponent(new TextureRenderComponent("images/potion2.png"))
                        .addComponent(new PotionComponent(PotionType.HEALTH_POTION))
                        .getComponent(PotionComponent.class).applyEffect(player, companion);
            }
            case SPEED_POTION -> {
                potion.addComponent(new TextureRenderComponent("images/potion3.png"))
                        .addComponent(new PotionComponent(PotionType.SPEED_POTION))
                        .getComponent(PotionComponent.class).applyEffect(player, companion);
            }
            default -> throw new IllegalArgumentException("You must assign a valid PotionType");
        }

        return potion;
    }
}

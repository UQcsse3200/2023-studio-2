package com.csse3200.game.entities.factories;

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

/**
 * Factory class for creating potion entities in the game.
 */
public class PotionFactory {

    /**
     * Creates a potion entity of the specified type.
     *
     * @param type      The type of potion to create.
     * @param player    The player entity in the game.
     * @param companion The companion entity in the game.
     * @return A newly created potion entity.
     */
    public static Entity createPotion(PotionType type, Entity player, Entity companion) {
        Entity potion = new Entity()
                .addComponent(new PhysicsComponent().setBodyType(BodyDef.BodyType.StaticBody))
                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.COMPANION))
                .addComponent(new ItemPickupComponent(PhysicsLayer.ITEMS_ABOVE_PLATFORM))
                .addComponent(new CombatStatsComponent(80, 10, 2, true));
        potion.setScale(0.4f, 0.4f);
        switch (type) {
            case DEATH_POTION -> {
                potion.addComponent(new TextureRenderComponent("images/Potion3re.png"));
                // You may add more components or apply effects here if needed for death potion
            }
            case HEALTH_POTION -> {
                potion.addComponent(new TextureRenderComponent("images/Potion4re.png"))
                        .addComponent(new PotionComponent(PotionType.DEATH_POTION))
                        .getComponent(PotionComponent.class).applyEffect(player, companion);
            }
            case SPEED_POTION -> {
                potion.addComponent(new TextureRenderComponent("images/Potion2re.png"))
                        .addComponent(new PotionComponent(PotionType.SPEED_POTION))
                        .getComponent(PotionComponent.class).applyEffect(player, companion);
            }
            default -> throw new IllegalArgumentException("You must assign a valid PotionType");
        }
        return potion;
    }
}

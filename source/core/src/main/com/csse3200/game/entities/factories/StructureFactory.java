package com.csse3200.game.entities.factories;

import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.resources.ProductionComponent;
import com.csse3200.game.components.resources.Resource;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.PhysicsUtils;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.rendering.DamageTextureComponent;
import com.csse3200.game.rendering.TextureRenderComponent;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;


/**
 * Factory to create structure entities - such as extractors or ships.
 *
 * <p>Each obstacle entity type should have a creation method that returns a corresponding entity.
 * @param health the max and initial health of the extractor
 * @param producedResource the resource type produced by the extractor
 * @param tickRate the frequency at which the extractor ticks (produces resources)
 * @param tickSize the amount of the resource produced at each tick
 */
public class StructureFactory {
    public static Entity createExtractor(int health, Resource producedResource, long tickRate, int tickSize) {
        Entity extractor = new Entity()
                .addComponent(new DamageTextureComponent("images/elixir_collector.png")
                        .addTexture(20, "images/broken_elixir_collector.png"))
                .addComponent(new PhysicsComponent().setBodyType(BodyType.StaticBody))
                .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE))
                .addComponent(new CombatStatsComponent(health, 0))
                .addComponent(new ProductionComponent(producedResource, tickRate, tickSize));
        extractor.setScale(1.8f, 2f);
        PhysicsUtils.setScaledCollider(extractor, 1f, 0.6f);

        return extractor;
    }
}

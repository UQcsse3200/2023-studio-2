package com.csse3200.game.entities;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.csse3200.game.components.*;
import com.csse3200.game.components.resources.ProductionComponent;
import com.csse3200.game.components.structures.ExtractorAnimationController;
import com.csse3200.game.entities.configs.ExtractorConfig;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.PhysicsUtils;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.windows.ExtractorMinigameWindow;

public class Extractor extends PlaceableEntity {
    public Extractor(ExtractorConfig config) {
        super(3, 3);
        AnimationRenderComponent animator =
                new AnimationRenderComponent(
                        ServiceLocator.getResourceService().getAsset(config.spritePath, TextureAtlas.class));
        animator.addAnimation("animateBroken", 0.2f, Animation.PlayMode.LOOP);
        animator.addAnimation("animateExtracting", 0.2f, Animation.PlayMode.LOOP);

        irremovable();
        addComponent(new PhysicsComponent().setBodyType(BodyDef.BodyType.StaticBody));
        addComponent(new ColliderComponent().setLayer(PhysicsLayer.STRUCTURE));
        addComponent(new HitboxComponent().setLayer(PhysicsLayer.STRUCTURE));
        addComponent(animator);
        addComponent(new CombatStatsComponent(config.health, config.maxHealth, 0, 0, false));
        addComponent(new ProductionComponent(config.resource, config.tickRate, config.tickSize));
        addComponent(new ExtractorAnimationController());
        addComponent(new ParticleComponent(config.effects));
        addComponent(new HealthBarComponent(false, 0, 1.9f));

        InteractLabel interactLabel = new InteractLabel();  //code for interaction prompt
        addComponent(new DistanceCheckComponent(5f, interactLabel));
        ServiceLocator.getRenderService().getStage().addActor(interactLabel);

        addComponent(new InteractableComponent(entity -> {
            CombatStatsComponent healthStats = getComponent(CombatStatsComponent.class);

            if (healthStats.isDead()) {
                ExtractorMinigameWindow minigame = ExtractorMinigameWindow.makeNewMinigame(this);
                ServiceLocator.getRenderService().getStage().addActor(minigame);
            }
        }, 5f));
        setScale(1.8f, 2f);
        PhysicsUtils.setScaledCollider(this, 1f, 0.6f);

        addComponent(new SaveableComponent<>(entity -> save(this, config), ExtractorConfig.class));
    }

    @Override
    public void placed() {
        super.placed();

        if (!getComponent(CombatStatsComponent.class).isDead()) {
            getEvents().trigger("startEffect", "rubble");
        }
    }

    /**
     * A function to save the Extractor's properties into its config.
     * @param entity - the extractor to save.
     * @param config - the existing config for the extractor.
     * @return the updated config for the extractor.
     */
    private static ExtractorConfig save(PlaceableEntity entity, ExtractorConfig config) {
        if (!(entity instanceof Extractor)) {
            return new ExtractorConfig();
        }

        config.position = entity.getGridPosition();
        config.health = entity.getComponent(CombatStatsComponent.class).getHealth();

        return config;
    }
}

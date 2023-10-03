package com.csse3200.game.components.explosives;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Timer;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.ParticleComponent;
import com.csse3200.game.components.SoundComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.PlaceableEntity;
import com.csse3200.game.rendering.RenderComponent;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.StructurePlacementService;

import java.util.TimerTask;

public class ExplosiveComponent extends Component {
    private boolean isExploded = false;
    private boolean isChained = false;
    private final ExplosiveConfig explosiveConfig;

    public ExplosiveComponent(ExplosiveConfig explosiveConfig) {
        this.explosiveConfig = explosiveConfig;
    }

    @Override
    public void create() {
        super.create();

        entity.getEvents().addListener("explode", this::explode);
        entity.getEvents().addListener("chainExplode", this::chainExplode);
    }

    public void explode() {
        if (isExploded) {
            return;
        }
        isExploded = true;

        var explosionEntity = new Entity();
        if (explosiveConfig.effectPath != null) {
            explosionEntity.addComponent(new ParticleComponent("explosion", explosiveConfig.effectPath));
        }
        if (explosiveConfig.soundPath != null) {
            explosionEntity.addComponent(new SoundComponent("explosion", explosiveConfig.soundPath));
        }

        ServiceLocator.getEntityPlacementService().PlaceEntityAt(explosionEntity, entity.getPosition());

        // triggers the explosion sound and effect if they exist within the entity
        explosionEntity.getEvents().trigger("startEffect", "explosion");
        explosionEntity.getEvents().trigger("playSound", "explosion");

        // notify explosives within chain radius
        for (var otherEntity : ServiceLocator.getEntityService().getEntitiesByComponent(ExplosiveComponent.class)) {
            if (otherEntity == entity) {
                continue;
            }

            var distance = entity.getCenterPosition().dst(otherEntity.getCenterPosition());

            if (distance <= explosiveConfig.chainRadius) {
                otherEntity.getEvents().trigger("chainExplode", distance/explosiveConfig.chainRadius * 0.4f);
            }
        }

        for (var otherEntity : ServiceLocator.getEntityService().getEntitiesByComponent(CombatStatsComponent.class)) {
            var combatStatsComponent = otherEntity.getComponent(CombatStatsComponent.class);

            if (combatStatsComponent == null) {
                // shouldn't happen, however best to double-check
                return;
            }

            var distance = entity.getCenterPosition().dst(otherEntity.getCenterPosition());

            if (distance <= explosiveConfig.damageRadius) {
                combatStatsComponent.addHealth((int) ((explosiveConfig.damageRadius - distance)
                        / explosiveConfig.damageRadius * -explosiveConfig.damage));
            }
        }

        // deletes the entity
        if (entity instanceof PlaceableEntity) {
            ServiceLocator.getStructurePlacementService().removeStructure((PlaceableEntity)entity);
        } else {
            Gdx.app.postRunnable(entity::dispose);
        }
    }

    /**
     * Triggered through an event when a neighbouring explosive explodes.
     * If chainable is true, this will cause this entity to explode too, otherwise nothing will happen.
     */
    public void chainExplode(float delay) {
        if (isChained) {
            return;
        }

        isChained = true;

        if (explosiveConfig.chainable) {
            Timer.schedule(new TimerAction(this::explode), delay);
        }
    }
}


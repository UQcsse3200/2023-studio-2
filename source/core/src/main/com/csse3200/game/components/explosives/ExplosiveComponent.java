package com.csse3200.game.components.explosives;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Timer;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.ParticleComponent;
import com.csse3200.game.components.SoundComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.PlaceableEntity;
import com.csse3200.game.services.ServiceLocator;

/**
 * The ExplosiveComponent can be added to any entity to allow it to explode through an event driven system.
 */
public class ExplosiveComponent extends Component {
    private boolean isExploded = false;
    private boolean isChained = false;
    private final ExplosiveConfig explosiveConfig;
    private static final String EFFECT_NAME = "explosion";

    /**
     * Creates the ExplosiveComponent with the given ExplosiveConfig.
     * @param explosiveConfig - the config parameters to create the explosive component using.
     */
    public ExplosiveComponent(ExplosiveConfig explosiveConfig) {
        this.explosiveConfig = explosiveConfig;
    }

    /**
     * Creates the component and begins listening for explode and chainExplode events.
     */
    @Override
    public void create() {
        super.create();

        entity.getEvents().addListener("explode", this::explode);
        entity.getEvents().addListener("chainExplode", this::chainExplode);
    }

    /**
     * Detonates the explosion by creating a new entity for the particle and sound effects and deleting the entity
     * the component is added.
     */
    protected void explode() {
        if (isExploded) {
            return;
        }
        isExploded = true;

        var explosionEntity = new Entity();
        if (explosiveConfig.effectPath != null) {
            explosionEntity.addComponent(new ParticleComponent(EFFECT_NAME, explosiveConfig.effectPath));
        }
        if (explosiveConfig.soundPath != null) {
            explosionEntity.addComponent(new SoundComponent(EFFECT_NAME, explosiveConfig.soundPath));
        }

        ServiceLocator.getEntityPlacementService().PlaceEntityAt(explosionEntity, entity.getPosition());

        // triggers the explosion sound and effect if they exist within the entity
        explosionEntity.getEvents().trigger("startEffect", EFFECT_NAME);
        explosionEntity.getEvents().trigger("playSound", EFFECT_NAME);

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

        // damages all entities in the damage radius
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
        if (entity instanceof PlaceableEntity placeableEntity) {
            ServiceLocator.getStructurePlacementService().removeStructure(placeableEntity);
        } else {
            Gdx.app.postRunnable(entity::dispose);
        }
    }

    /**
     * Triggered through an event when a neighbouring explosive explodes.
     * If chainable is true, this will cause this entity to explode too, otherwise nothing will happen.
     */
    protected void chainExplode(float delay) {
        if (isChained) {
            return;
        }

        isChained = true;

        if (explosiveConfig.chainable) {
            Timer.schedule(new PostrunnableTask(this::explode), delay);
        }
    }
}


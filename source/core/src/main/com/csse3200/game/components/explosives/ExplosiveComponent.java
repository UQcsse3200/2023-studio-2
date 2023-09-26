package com.csse3200.game.components.explosives;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Timer;
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
    private final String explosionEffectPath;
    private final String explosionSoundPath;
    private final float chainRadius;
    private final boolean chainable;

    public ExplosiveComponent(String explosionEffectPath, String explosionSoundPath, float chainRadius, boolean chainable) {
        this.explosionEffectPath = explosionEffectPath;
        this.explosionSoundPath = explosionSoundPath;
        this.chainRadius = chainRadius;
        this.chainable = chainable;
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
        if (explosionEffectPath != null) {
            explosionEntity.addComponent(new ParticleComponent("explosion", explosionEffectPath));
        }
        if (explosionSoundPath != null) {
            explosionEntity.addComponent(new SoundComponent("explosion", explosionSoundPath));
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

            if (distance <= chainRadius) {
                otherEntity.getEvents().trigger("chainExplode", distance/chainRadius * 0.4f);
            }
        }

        // deletes the entity
        if (entity instanceof PlaceableEntity) {
            ServiceLocator.getStructurePlacementService().removeStructure(entity);
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

        if (chainable) {
            Timer.schedule(new TimerAction(this::explode), delay);
        }
    }
}


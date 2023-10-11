package com.csse3200.game.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.csse3200.game.entities.configs.ParticleEffectsConfig;
import com.csse3200.game.rendering.RenderComponent;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class ParticleComponent extends RenderComponent {
    private static final float SCALE_REDUCTION = 100;
    private static final Logger logger = LoggerFactory.getLogger(ParticleComponent.class);
    private final ParticleEffectsConfig effectsConfig;
    private final Map<String, ParticleEffect> effects;

    /**
     * Creates a new EffectComponent using the effects specified in the effectsConfig.
     * @param effectsConfig - the config file containing the effects which can be played.
     */
    public ParticleComponent(ParticleEffectsConfig effectsConfig) {
        this.effectsConfig = effectsConfig;
        effects = new HashMap<>();
    }

    /**
     * Creates a new EffectComponent with the particle effect specified.
     *
     * @param particleEffectId - the id of the given particle effect
     * @param particleEffectPath - the path to locate the particle effect
     */
    public ParticleComponent(String particleEffectId, String particleEffectPath) {
        this.effectsConfig = new ParticleEffectsConfig();
        this.effectsConfig.effectsMap.put(particleEffectId, particleEffectPath);
        effects = new HashMap<>();
    }

    /**
     * Loads the effects specified in the config file into a hashmap for later use.
     */
    @Override
    public void create() {
        super.create();

        // loads the sounds and stores them in a map for later access
        for (var entry : effectsConfig.effectsMap.entries()) {
            logger.debug("Entity {} loading in sound {} with id {}", entity, entry.value, entry.key);

            var effect = ServiceLocator.getResourceService().getAsset(entry.value, ParticleEffect.class);

            if (effect == null) {
                // debug log will occur in the ResourceService class
                continue;
            }

            try {
                // attempt to duplicate, although this will fail if mocked
                effect = new ParticleEffect(effect);
            } catch (NullPointerException e) {
                // pass
            }

            effects.put(entry.key, effect);
        }

        entity.getEvents().addListener("startEffect", this::startEffect);
        entity.getEvents().addListener("stopEffect", this::stopEffect);
    }

    @Override
    protected void draw(SpriteBatch batch) {
        Matrix4 originalMatrix = batch.getProjectionMatrix().cpy();

        var newScale = new Vector3();
        newScale = originalMatrix.getScale(newScale);
        newScale.scl(1/SCALE_REDUCTION);

        var originalPosition = originalMatrix.getTranslation(new Vector3());
        var originalRotation = originalMatrix.getRotation(new Quaternion());

        var newMatrix = new Matrix4(originalPosition, originalRotation, newScale);
        batch.setProjectionMatrix(newMatrix);


        var position = new Vector2(entity.getCenterPosition().x, entity.getPosition().y);
        position.scl(SCALE_REDUCTION);
        for (var effect : effects.values()) {
            if (!effect.isComplete()) {
                effect.setPosition(position.x, position.y);
                effect.draw(batch, Gdx.graphics.getDeltaTime());
            }
        }
        batch.setProjectionMatrix(originalMatrix); //revert projection
    }

    /**
     * Plays the effect which effectName maps to if it exists. If effectName does not map to
     * a loaded effect, nothing happens.
     * @param effectName - the name of the effect to play as specified in the config file.
     */
    public void startEffect(String effectName) {
        logger.debug("Entity {} playing effect {}", entity, effectName);

        var effect = effects.get(effectName);

        if (effect == null) {
            logger.warn("Sound {} does not exist for entity {}", effectName, entity);
            return;
        }

        effect.start();
    }

    /**
     * Stops the playing of the effect which effectName maps to if it exists. If effectName does not map to
     * a loaded effect, nothing happens.
     * @param effectName - the name of the effect to stop as specified in the config file.
     */
    public void stopEffect(String effectName) {
        logger.debug("Entity {} stopping effect {}", entity, effectName);

        var effect = effects.get(effectName);

        if (effect == null) {
            logger.warn("Sound {} does not exist for entity {}", effectName, entity);
            return;
        }

        effect.reset();
    }
}

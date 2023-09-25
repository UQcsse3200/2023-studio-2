package com.csse3200.game.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.csse3200.game.entities.configs.ParticleEffectsConfig;
import com.csse3200.game.entities.configs.SoundsConfig;
import com.csse3200.game.files.UserSettings;
import com.csse3200.game.rendering.RenderComponent;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ParticleComponent extends RenderComponent {
    private static final Logger logger = LoggerFactory.getLogger(SoundComponent.class);
    private final ParticleEffectsConfig effectsConfig;
    private final Map<String, ParticleEffect> effects;

    /**
     * Creates a new EffectComponent using the effects specified in the effectsConfig.
     * @param effectsConfig - the config file containing the effects which can be played.
     */
    public ParticleComponent(ParticleEffectsConfig effectsConfig) {
        this.effectsConfig = effectsConfig;
        effects = new HashMap<>();
        looping = new HashSet<>();
    }

    /**
     * Loads the effects specified in the config file into a hashmap for later use.
     */
    @Override
    public void create() {
        super.create();

        // loads the sounds and stores them in a map for later access
        for (var entry : effectsConfig.effectsMap.entries()) {
            logger.debug("Entity {} loading in sound {} with id {}", entity.toString(), entry.value, entry.key);

            ParticleEffect effect = ServiceLocator.getResourceService().getAsset(entry.value, ParticleEffect.class);

            if (effect == null) {
                // debug log will occur in the ResourceService class
                continue;
            }

            effects.put(entry.key, effect);
        }

        entity.getEvents().addListener("startEffect", this::startEffect);
    }

    @Override
    protected void draw(SpriteBatch batch) {
        for (var effect : effects.values()) {
            if (!effect.isComplete()) {
                effect.draw(batch, Gdx.graphics.getDeltaTime());
            }
        }
    }

    /**
     * Plays the effect which effectName maps to if it exists. If effectName does not map to
     * a loaded effect, nothing happens.
     * @param effectName - the name of the effect to play as specified in the config file.
     */
    public void startEffect(String effectName) {
        logger.debug("Entity {} playing effect {}", entity.toString(), effectName);

        var effect = effects.get(effectName);

        if (effect == null) {
            logger.warn("Sound {} does not exist for entity {}", effect, entity.toString());
            return;
        }

        effect.start();
    }
}

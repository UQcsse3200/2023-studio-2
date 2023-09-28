package com.csse3200.game.entities;

import com.badlogic.gdx.utils.ObjectMap;
import com.csse3200.game.components.ParticleComponent;
import com.csse3200.game.entities.configs.FissureConfig;
import com.csse3200.game.entities.configs.ParticleEffectsConfig;
import com.csse3200.game.rendering.TextureRenderComponent;

public class Fissure extends PlaceableEntity {
    public Fissure(FissureConfig config) {
        super(2, 2);
        TextureRenderComponent textureRenderComponent = new TextureRenderComponent("images/resources/Cracked_Ground.png");
        textureRenderComponent.overrideZIndex(-100);
        addComponent(textureRenderComponent);
        setScale(0.4f, 0.4f);

        // Assigns particle texture based on the specific resource
        var particleEffectsConfig = new ParticleEffectsConfig();
        particleEffectsConfig.effectsMap = new ObjectMap<>();
        switch (config.resource) {
            case Durasteel -> particleEffectsConfig.effectsMap.put("open", "particle-effects/extractor/durasteel.effect");
            case Nebulite -> particleEffectsConfig.effectsMap.put("open", "particle-effects/extractor/nebulite.effect");
            case Solstite -> particleEffectsConfig.effectsMap.put("open", "particle-effects/extractor/solstite.effect");
            default -> throw new IllegalArgumentException("You must assign a valid resource!");
        }
        ParticleComponent particleComponent = new ParticleComponent(particleEffectsConfig);
        addComponent(particleComponent);

        this.irremovable = true;
    }

    @Override
    public void update() {
        super.update();
        this.getComponent(ParticleComponent.class).startEffect("open");
    }
}

package com.csse3200.game.entities.factories;

import com.badlogic.gdx.utils.ObjectMap;
import com.csse3200.game.components.ParticleComponent;
import com.csse3200.game.components.resources.Resource;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.FissureConfig;
import com.csse3200.game.entities.configs.ParticleEffectsConfig;
import com.csse3200.game.rendering.TextureRenderComponent;

public class FissureFactory {
    public static Entity createFissure(FissureConfig config) {

        Entity fissure = new Entity();

        fissure.addComponent(new TextureRenderComponent("images/resources/Cracked_Ground.png"));
        fissure.setScale(0.4f, 0.4f);

        // Assigns particle texture based on the specific resource
        var particleEffectsConfig = new ParticleEffectsConfig();
        particleEffectsConfig.effectsMap = new ObjectMap<>();
        particleEffectsConfig.effectsMap.put("explosion", "particle-effects/explosion/explosion.effect");
        ParticleComponent particleComponent = new ParticleComponent(particleEffectsConfig);
        fissure.addComponent(particleComponent);
//        Resource resource = config.resource;
//        switch (resource) {
//            case Durasteel -> fissure.addComponent(new TextureRenderComponent("images/resources/durasteel.png"));
//            case Nebulite -> fissure.addComponent(new TextureRenderComponent("images/resources/nebulite.png"));
//            case Solstite -> fissure.addComponent(new TextureRenderComponent("images/resources/solstite.png"));
//            default -> throw new IllegalArgumentException("You must assign a valid resource!");
//        }

        return fissure;
    }
}

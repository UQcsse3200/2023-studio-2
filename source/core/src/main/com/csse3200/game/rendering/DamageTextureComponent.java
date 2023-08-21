package com.csse3200.game.rendering;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.Component;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.ServiceLocator;

import java.util.HashMap;
import java.util.Map;

public class DamageTextureComponent extends RenderComponent {
    Map<Integer, Texture> thresholds = new HashMap<>();
    Sprite sprite = new Sprite();

    public DamageTextureComponent(String texturePath) {
        this(GetTexture(texturePath));
    }

    public DamageTextureComponent(Texture texture) {
        sprite.setTexture(texture);
        thresholds.put(Integer.MAX_VALUE, texture);
    }

    public DamageTextureComponent addTexture(int threshold, String texturePath) {
        return addTexture(threshold, GetTexture(texturePath));
    }

    public DamageTextureComponent addTexture(int threshold, Texture texture) {
        thresholds.put(threshold, texture);
        return this;
    }

    private static Texture GetTexture(String texturePath) {
        return ServiceLocator.getResourceService().getAsset(texturePath, Texture.class);
    }

    @Override
    protected void draw(SpriteBatch batch) {
        Vector2 position = entity.getPosition();
        Vector2 scale = entity.getScale();
        sprite.setTexture(getHealthTexture());
        batch.draw(sprite.getTexture(), position.x, position.y, scale.x, scale.y);
    }

    private Texture getHealthTexture() {
        CombatStatsComponent healthStats = entity.getComponent(CombatStatsComponent.class);
        Texture texture = null;
        int prevThreshold = Integer.MAX_VALUE;
        for (Map.Entry<Integer, Texture> entry : thresholds.entrySet()) {
            if (healthStats.getHealth() <= entry.getKey() && entry.getKey() <= prevThreshold) {
                texture = entry.getValue();
                prevThreshold = entry.getKey();
            }
        }

        if (texture == null) {
            texture = thresholds.values().stream().findFirst().get();
        }

        return texture;
    }
}

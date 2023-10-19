package com.csse3200.game.rendering;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.services.ServiceLocator;

import java.util.HashMap;
import java.util.Map;

/**
 * A Rendering component used to switch between different textures based on the current entity's health.
 */
public class DamageTextureComponent extends RenderComponent {
    private final Map<Integer, Texture> thresholds;

    /**
     * Creates a DamageTextureComponent with the given texture
     * @param texturePath path to texture to be displayed
     */
    public DamageTextureComponent(String texturePath) {
        this(getTexture(texturePath));
    }

    /**
     * Creates a DamageTextureComponent with the given texture
     * @param texture Texture object to be displayed
     */
    public DamageTextureComponent(Texture texture) {
        thresholds = new HashMap<>();
        thresholds.put(Integer.MAX_VALUE, texture);
    }

    /**
     * Add a texture with a given threshold such that if
     * health &lt;= threshold then the texture will show.
     * @param threshold Threshold to show texture at
     * @param texturePath Path to texture to be displayed
     * @return current DamageTextureComponent to allow for chaining
     */
    public DamageTextureComponent addTexture(int threshold, String texturePath) {
        return addTexture(threshold, getTexture(texturePath));
    }

    /**
     * Add a texture with a given threshold such that if
     * health &lt;= threshold then the texture will show.
     * @param threshold Threshold to show texture at
     * @param texture Texture to be displayed
     * @return current DamageTextureComponent to allow for chaining
     */
    public DamageTextureComponent addTexture(int threshold, Texture texture) {
        thresholds.put(threshold, texture);
        return this;
    }

    //Helper method to get Texture resource from file path
    private static Texture getTexture(String texturePath) {
        return ServiceLocator.getResourceService().getAsset(texturePath, Texture.class);
    }

    @Override
    protected void draw(SpriteBatch batch) {
        Vector2 position = entity.getPosition();
        Vector2 scale = entity.getScale();
        Texture texture = getCurrentHealthTexture();
        batch.draw(texture, position.x, position.y, scale.x, scale.y);
    }

    /**
     * Calculates the correct texture to display based on the thresholds and current health
     * @return Texture to be displayed for entity - undefined behaviour if multiple textures
     * exist with the same threshold.
     */
    private Texture getCurrentHealthTexture() {
        CombatStatsComponent healthStats = entity.getComponent(CombatStatsComponent.class);
        Texture texture = null;
        int prevThreshold = Integer.MAX_VALUE;

        //Will never be empty as constructor initialises with 1 texture with max threshold
        for (Map.Entry<Integer, Texture> entry : thresholds.entrySet()) {
            if (healthStats.getHealth() <= entry.getKey() && entry.getKey() <= prevThreshold) {
                texture = entry.getValue();
                prevThreshold = entry.getKey();
            }
        }

        return texture;
    }
}

package com.csse3200.game.rendering;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.player.IAlpha;
import com.csse3200.game.services.ServiceLocator;

/** Render a static texture. */
public class TextureRenderComponent extends RenderComponent implements IAlpha {
    private final Texture texture;
    private float rotation = 0.0f;
    private boolean yFlip = false;
    private float alpha = 1.0f;

    /**
     * @param texturePath Internal path of static texture to render.
     *                    Will be scaled to the entity's scale.
     */
    public TextureRenderComponent(String texturePath) {
        this(ServiceLocator.getResourceService().getAsset(texturePath, Texture.class));
    }
//...

    /**
     * @param texture Static texture to render. Will be scaled to the entity's scale.
     */
    public TextureRenderComponent(Texture texture) {
        this.texture = texture;
    }

    /**
     * Scale the entity to a width of 1 and a height matching the texture's ratio
     */
    public void scaleEntity() {
        entity.setScale(1f, (float) texture.getHeight() / texture.getWidth());
    }

    public void setRotation(float degrees) {
        rotation = degrees;
    }

    public void setYFlip(boolean flipped) {
        yFlip = flipped;
    }

    /**
     * Sets the alpha of the spritebatch
     *
     * @param alpha the alpha to set
     */
    @Override
    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    @Override
    protected void draw(SpriteBatch batch) {
        Vector2 position = entity.getPosition();
        Vector2 scale = entity.getScale();

        Color colour = batch.getColor() == null ? Color.valueOf("FFFFFF") : batch.getColor();
        batch.setColor(colour.r, colour.g, colour.b, this.alpha);

        batch.draw(texture, position.x, position.y, scale.x / 2, scale.y / 2, scale.x, scale.y, 1, 1, rotation, 0, 0, texture.getWidth(), texture.getHeight(), false, yFlip);
    }
}
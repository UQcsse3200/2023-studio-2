package com.csse3200.game.rendering;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable;
import com.csse3200.game.components.player.IAlpha;
import com.csse3200.game.services.ServiceLocator;

/**
 * Extends BaseDrawable to implement the ability to create a drawable for a custom color.
 * Taken from a <a href="https://stackoverflow.com/a/54642587">StackOverflow answer</a> by Steve Moseley.
 */
public class ColorDrawable extends BaseDrawable implements IAlpha {
    private final float red, green, blue;
    private float alpha;
    private final Color savedBatchColor = new Color();

    /**
     * Creates a new ColorDrawable with the given color (in RGB).
     * @param red - the fraction of red in the color (0 to 1)
     * @param green - the fraction of green in the color (0 to 1)
     * @param blue - the fraction of blue in the color (0 to 1)
     * @param alpha - the fraction of alpha in the color (0 to 1)
     */
    public ColorDrawable(float red, float green, float blue, float alpha) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
    }

    /**
     * Draws the ColorDrawable to the screen by recoloring a white texture to the defined color.
     * @param batch - the batch to draw the color to.
     * @param x - the x position of the drawable
     * @param y - the y position of the drawable
     * @param width - the width of the drawable
     * @param height - the height of the drawable
     */
    @Override
    public void draw(Batch batch, float x, float y, float width, float height) {
        // Save the batch colour as we are about to change it
        savedBatchColor.set(batch.getColor());
        batch.setColor(red, green, blue, alpha);
        // Draw a white texture with the current batch colour
        batch.draw(ServiceLocator.getResourceService().getAsset("images/white_color_texture.png", Texture.class),
                x, y, width, height);
        batch.setColor(savedBatchColor);
    }

    /**
     * Changes the alpha of the ColorDrawable.
     * @param alpha - the new alpha to use.
     */
    @Override
    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }
}

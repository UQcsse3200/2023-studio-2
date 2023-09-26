package com.csse3200.game.rendering;

import com.badlogic.gdx.assets.loaders.AssetLoader;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable;
import com.csse3200.game.components.player.IAlpha;
import com.csse3200.game.services.ServiceLocator;

/**
 * This class is used to programmatically create a color which can be used as a background in UI components.
 * This is a slightly modified version of a class specified in <a href="https://stackoverflow.com/a/54642587">this stack overflow answer</a>.
 */
public class ColorDrawable extends BaseDrawable implements IAlpha {
    private float r, g, b, a;
    private Color savedBatchColor = new Color();

    public ColorDrawable(float r, float g, float b, float a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    @Override
    public void draw(Batch batch, float x, float y, float width, float height) {
        // Save the batch colour as we are about to change it
        savedBatchColor.set(batch.getColor());
        batch.setColor(r, g, b, a);
        // Draw a white texture with the current batch colour
        batch.draw(ServiceLocator.getResourceService()
                .getAsset("images/action-feedback/white_color_texture.png", Texture.class),
                x, y, width, height);
        batch.setColor(savedBatchColor);
    }

    @Override
    public void setAlpha(float alpha) {
        this.a = alpha;
    }
}

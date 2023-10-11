package com.csse3200.game.components.spacenavigation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.backgrounds.StarBackground;

public class NavigationBackground extends StarBackground {

    /**
     * The texture for the space background of the navigation screen.
     */
    private final Texture spaceBackground;

    public NavigationBackground() {
        super(200);
        spaceBackground = new Texture(Gdx.files.internal("images/navigationmap/background.png"));
    }

    @Override
    protected Vector2 starShift(int x, int y){
        // Shift star around regions of no go space (bounding boxes)
        while ((x > Gdx.graphics.getWidth() / 4 && // Planets box
                x < 3 * (Gdx.graphics.getWidth() / 4) &&
                y > 2 * Gdx.graphics.getHeight() / 6 &&
                y < 3 * (Gdx.graphics.getHeight() / 6)) ||
               (x > 2 * (Gdx.graphics.getWidth() / 6) && // Title box
                x < 4 * (Gdx.graphics.getWidth() / 6) &&
                y > 4 * (Gdx.graphics.getHeight() / 6) &&
                y < 5 * (Gdx.graphics.getHeight() / 6)
                )) {
            x = MathUtils.random(0, Gdx.graphics.getWidth());
            y = MathUtils.random(0, Gdx.graphics.getHeight());
        }
        return new Vector2(x, y);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(spaceBackground, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        super.draw(batch, parentAlpha);
    }
}

package com.csse3200.game.components.spacenavigation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

public class NavigationBackground extends StarBackground {

    /**
     * The texture for the space background of the navigation screen.
     */
    private final Texture spaceBackground;

    public NavigationBackground() {
        super();
        spaceBackground = new Texture(Gdx.files.internal("images/navigationmap/background.png"));
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(spaceBackground, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        super.draw(batch, parentAlpha);
    }
}

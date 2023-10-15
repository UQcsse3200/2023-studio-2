package com.csse3200.game.components.settingsmenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.spacenavigation.StarBackground;

// This is heavily inspired by the Space Team's Space Minigame screen
public class SettingsStarBackground extends StarBackground {

    /**
     * Array of animations for individual star sprites.
     */
    private final Animation<TextureRegion>[] animations;

    /**
     * Array of positions for individual star sprites.
     */
    private final Vector2[] spritePositions;

    /**
     * The number of star sprites in the background.
     */
    private final int numOfSprites = 30;

    /**
     * Array to store the time passed for each sprite's animation.
     */
    private final float[] stateTimes;  // Time passed for each sprite's animation

    /**
     * Constructs a new NavigationBackground instance.
     * Loads necessary textures and initializes the star animations and positions.
     */
    public SettingsStarBackground() {
        super(30);

        int numOfFrames = 8;
        TextureRegion[] frames = new TextureRegion[numOfFrames];

        for (int i = 0; i < numOfFrames; i++) {
            frames[i] = new TextureRegion(new Texture
                    (Gdx.files.internal("images/space/space_navigation_background_star_frame_"
                            + i + ".png")));
        }

        animations = new Animation[numOfSprites];
        spritePositions = new Vector2[numOfSprites];
        stateTimes = new float[numOfSprites];

        for (int i = 0; i < numOfSprites; i++) {
            animations[i] = new Animation<>(0.25f, frames);
            animations[i].setPlayMode(Animation.PlayMode.LOOP_PINGPONG); // Ping pong effect

            // No stars overlapping with planets and text
            int x = MathUtils.random(0, Gdx.graphics.getWidth());
            int y = MathUtils.random(0, Gdx.graphics.getHeight());

            while ((x >= (65 / 1280f) * Gdx.graphics.getWidth() && x <= (260 / 1280f) * Gdx.graphics.getWidth() && y >= (520 / 720f) * Gdx.graphics.getHeight() && y <= (670 / 720f) * Gdx.graphics.getHeight()) ||
                    (x >= (415 / 1280f) * Gdx.graphics.getWidth() && x <= (830 / 1280f) * Gdx.graphics.getWidth() && y >= (580 / 720f) * Gdx.graphics.getHeight() && y <= (650 / 720f) * Gdx.graphics.getHeight()) ||
                    (x >= (1000 / 1280f) * Gdx.graphics.getWidth() && x <= (1175 / 1280f) * Gdx.graphics.getWidth() && y >= (320 / 720f) * Gdx.graphics.getHeight() && y <= (485 / 720f) * Gdx.graphics.getHeight()) ||
                    (x >= (1050 / 1280f) * Gdx.graphics.getWidth() && x <= (1220 / 1280f) * Gdx.graphics.getWidth() && y >= (535 / 720f) * Gdx.graphics.getHeight() && y <= (690 / 720f) * Gdx.graphics.getHeight())) {
                x = MathUtils.random(0, Gdx.graphics.getWidth());
                y = MathUtils.random(0, Gdx.graphics.getHeight());
            }




            spritePositions[i] = new Vector2(x, y);
            stateTimes[i] = MathUtils.random(0f, 1f);  // Offset animation start times
        }
    }

    /**
     * Called when the actor should perform its action.
     * Updates the state times for star animations.
     *
     * @param delta The time in seconds since the last frame.
     */
    @Override
    public void act(float delta) {
        super.act(delta);

        for (int i = 0; i < numOfSprites; i++) {
            stateTimes[i] += delta;
        }
    }

    /**
     * Called when the actor should be drawn.
     * Draws the black background and the animated star sprites.
     *
     * @param batch        The batch to draw with.
     * @param parentAlpha  The parent alpha value.
     */
    @Override
    public void draw(Batch batch, float parentAlpha) {
        for (int i = 0; i < numOfSprites; i++) {
            TextureRegion currentFrame = animations[i].getKeyFrame(stateTimes[i]);
            batch.draw(currentFrame, spritePositions[i].x, spritePositions[i].y);
        }
    }
}

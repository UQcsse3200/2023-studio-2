package com.csse3200.game.components.spacenavigation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class NavigationBackground extends Actor {
    /**
     * The texture for the space background of the navigation screen.
     */
    private final Texture spaceBackground;

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
    private final int numOfSprites = 150;

    /**
     * Array to store the time passed for each sprite's animation.
     */
    private final float[] stateTimes;  // Time passed for each sprite's animation

    /**
     * Constructs a new NavigationBackground instance.
     * Loads necessary textures and initializes the star animations and positions.
     */
    public NavigationBackground() {
        spaceBackground = new Texture(Gdx.files.internal("images/space_navigation_background.png"));

        int numOfFrames = 8;
        TextureRegion[] frames = new TextureRegion[numOfFrames];

        for (int i = 0; i < numOfFrames; i++) {
            frames[i] = new TextureRegion(new Texture(Gdx.files.internal("images/space_navigation_background_star_frame_" + i + ".png")));
        }

        animations = new Animation[numOfSprites];
        spritePositions = new Vector2[numOfSprites];
        stateTimes = new float[numOfSprites];

        for (int i = 0; i < numOfSprites; i++) {
            animations[i] = new Animation<>(0.1f, frames);
            animations[i].setPlayMode(Animation.PlayMode.LOOP_PINGPONG); // Ping pong effect

            // No stars in the centre third
            int x = MathUtils.random(0, Gdx.graphics.getWidth());
            while (x > Gdx.graphics.getWidth() / 3 && x < 2 * (Gdx.graphics.getWidth() / 3)) {
                x = MathUtils.random(0, Gdx.graphics.getWidth());
            }
            spritePositions[i] = new Vector2(x, MathUtils.random(0, Gdx.graphics.getHeight()));
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
     * @param batch       The batch to draw with.
     * @param parentAlpha The parent alpha value.
     */
    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(spaceBackground, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        for (int i = 0; i < numOfSprites; i++) {
            TextureRegion currentFrame = animations[i].getKeyFrame(stateTimes[i]);
            batch.draw(currentFrame, spritePositions[i].x, spritePositions[i].y);
        }
    }
}

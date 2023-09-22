package com.csse3200.game.components.spacenavigation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class StarBackground extends Actor {
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
    protected int numOfSprites = 150;

    /**
     * Array to store the time passed for each sprite's animation.
     */
    private final float[] stateTimes;  // Time passed for each sprite's animation

    /**
     * Constructs a new NavigationBackground instance.
     * Loads necessary textures and initializes the star animations and positions.
     */
    public StarBackground() {

        int numOfFrames = 8;
        TextureRegion[] frames = new TextureRegion[numOfFrames];

        String STAR_IMAGE_PATH = "images/navigationmap/stars/";
        for (int i = 0; i < numOfFrames; i++) {
            frames[i] = new TextureRegion(new Texture(Gdx.files.internal(
                    STAR_IMAGE_PATH + "background_star_frame_" + i + ".png")));
        }

        animations = new Animation[numOfSprites];
        spritePositions = new Vector2[numOfSprites];
        stateTimes = new float[numOfSprites];

        for (int i = 0; i < numOfSprites; i++) {
            animations[i] = new Animation<>(0.1f, frames);
            animations[i].setPlayMode(Animation.PlayMode.LOOP_PINGPONG); // Ping pong effect

            // Apply star shift across x and y to create bounding boxes
            spritePositions[i] = starShift(MathUtils.random(0, Gdx.graphics.getWidth()),
                    MathUtils.random(0, Gdx.graphics.getHeight()));
            stateTimes[i] = MathUtils.random(0f, 1f);  // Offset animation start times
        }
    }

    protected Vector2 starShift(int x, int y) {
        while (x > Gdx.graphics.getWidth() / 3 && x < 2 * (Gdx.graphics.getWidth() / 3)) {
            x = MathUtils.random(0, Gdx.graphics.getWidth());
        }
        return new Vector2(x, y);
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

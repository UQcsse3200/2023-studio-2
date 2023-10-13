package com.csse3200.game.components.backgrounds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Disposable;

public class StarBackground extends Actor implements Disposable{

    /**
     * Constant containing the directory of the frames of the star animation
     */
    protected static final String STAR_IMAGE_PATH = "images/navigationmap/stars/";
    /**
     * Constant containing the frame timings for the animation
     */
    protected static final float ANIMATION_FRAME_DURATION = 0.1f;

    /**
     * Array of animations for individual star sprites.
     */
    protected final Animation<TextureRegion>[] animations;

    /**
     * Array of animations for individual star sprites.
     */
    protected final Vector2[] spritePositions;

    /**
     * The number of star sprites in the background.
     */
    protected final int numStars;

    /**
     * Array to store the time passed for each sprite's animation.
     */
    protected final float[] stateTimes;


    /**
     * Stores individual frames of animation
     */
    protected TextureRegion[] frames;

    /**
     * Constructs a new StarBackground instance.
     * Animates the stars across the screen and load in necessary textures.
     *
     * @param numStars  The number of stars to create.
     */
    public StarBackground(int numStars) {
        this.numStars = numStars;
        int numOfFrames = 8;
        frames = new TextureRegion[numOfFrames];

        for (int i = 0; i < numOfFrames; i++) {
            frames[i] = new TextureRegion(new Texture(Gdx.files.internal(
                    STAR_IMAGE_PATH + "background_star_frame_" + i + ".png")));
        }

        animations = new Animation[numStars];
        spritePositions = new Vector2[numStars];
        stateTimes = new float[numStars];

        for (int i = 0; i < numStars; i++) {
            animations[i] = new Animation<>(ANIMATION_FRAME_DURATION, frames);
            animations[i].setPlayMode(Animation.PlayMode.LOOP_PINGPONG);

            spritePositions[i] = starShift(MathUtils.random(0, Gdx.graphics.getWidth()),
                    MathUtils.random(0, Gdx.graphics.getHeight()));
            stateTimes[i] = MathUtils.random(0f, 1f);
        }
    }

    /**
     * Shifts a star from the given x-y coordinates to new position.
     * Can be used to draw bounding boxes around content or cluster stars.
     *
     * @param x     The x coordinate of star to move.
     * @param y     The y coordinate of the star to move.
     * @return      A vector of the new position for the star.
     */
    protected Vector2 starShift(int x, int y) {
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

        for (int i = 0; i < numStars; i++) {
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
        for (int i = 0; i < numStars; i++) {
            TextureRegion currentFrame = animations[i].getKeyFrame(stateTimes[i]);
            batch.draw(currentFrame, spritePositions[i].x, spritePositions[i].y);
        }
    }

    /**
     * Called at the end of the actor lifecycle to clean up
     */
    @Override
    public void dispose() {
        for (TextureRegion frame : frames) {
            frame.getTexture().dispose();
        }
    }
}

package com.csse3200.game.components.mainmenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.csse3200.game.components.backgrounds.StarBackground;
//import com.csse3200.game.components.spacenavigation.StarBackground;

public class MainMenuStarBackground extends StarBackground {

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
    public MainMenuStarBackground(int stars) {
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
            animations[i] = new Animation<>(0.1f, frames);
            animations[i].setPlayMode(Animation.PlayMode.LOOP_PINGPONG); // Ping pong effect

            // No stars in the rightmost quarter of the screen, overlapping with menu buttons
            int x = MathUtils.random(0, Gdx.graphics.getWidth());
            int y = MathUtils.random((Gdx.graphics.getHeight() / 3), (int) (0.85 * Gdx.graphics.getHeight()));

            while ((x >= (140 / 1280f) * Gdx.graphics.getWidth() && x <= (690 / 1280f) * Gdx.graphics.getWidth() && y >= (500 / 720f) * Gdx.graphics.getHeight() && y <= (600 / 720f) * Gdx.graphics.getHeight()) ||
                    (x >= (400 / 1280f) * Gdx.graphics.getWidth() && x <= (500 / 1280f) * Gdx.graphics.getWidth() && y >= (250 / 720f) * Gdx.graphics.getHeight() && y <= (350 / 720f) * Gdx.graphics.getHeight()) ||
                    (x >= (950 / 1280f) * Gdx.graphics.getWidth() && x <= (1130 / 1280f) * Gdx.graphics.getWidth() && y >= (150 / 720f) * Gdx.graphics.getHeight() && y <= (550 / 720f) * Gdx.graphics.getHeight())) {
                x = MathUtils.random(0, Gdx.graphics.getWidth());
                y = MathUtils.random((Gdx.graphics.getHeight() / 3), (int) (0.85 * Gdx.graphics.getHeight()));
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

    public int getNumSprites() {
        return this.numOfSprites;
    }
}

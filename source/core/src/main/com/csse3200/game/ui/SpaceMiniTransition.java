/**
 * The SpaceMiniTransition class represents a screen transition effect used in a LibGDX game.
 * It allows for transitioning between different game screens with a visual effect.
 *
 * This class extends the LibGDX Actor class and is designed to be used as an actor within a Stage.
 * The transition effect is achieved by moving a transition image diagonally across the screen.
 * When the transition is complete, it can trigger a callback or change to a new game screen.
 */
package com.csse3200.game.ui;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.csse3200.game.GdxGame;

public class SpaceMiniTransition extends Actor {

    // Fields and properties
    private GdxGame game;              // Reference to the game instance
    private Runnable callback;         // Callback to execute after the transition
    public boolean isTransitioning;    // Indicates if the transition is in progress
    public Texture transitionImage;    // The image used for the transition effect
    public SpriteBatch spriteBatch;    // Batch for rendering the transition image
    public Sprite transitionSprite;    // Sprite for rendering the transition
    public float transitionX;          // X-axis position for the transition
    public float transitionY;          // Y-axis position for the transition
    private float transitionSpeedX;    // Speed of the X-axis transition
    private float transitionSpeedY;    // Speed of the Y-axis transition
    public boolean isExit;             // Indicates if the transition is an exit transition

    /**
     * Creates a new SpaceMiniTransition instance.
     *
     * @param game      The main game instance.
     * @param alert     A string representing the type of alert (e.g., "Exit game").
     * @param skin      The skin used for UI elements (not used in this class).
     * @param alertText The text for the alert (not used in this class).
     */
    public SpaceMiniTransition(GdxGame game, String alert, Skin skin, String alertText) {

        this.game = game;
        if("Exit game".equals(alert))
            isExit =true;
        startTransition();
        // Load your transition image (replace with the actual image path)
        transitionImage = new Texture(Gdx.files.internal("images/game-over-final.png"));

        // Create a SpriteBatch for rendering the transition image
        spriteBatch = new SpriteBatch();

        // Create a transition sprite for rendering
        transitionSprite = new Sprite(transitionImage);
        transitionSprite.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        transitionSprite.setSize(1600f, 1080f);
    }

    /**
     * Updates the actor's state on each frame.
     *
     * @param delta The time elapsed since the last frame in seconds.
     */

    @Override
    public void act(float delta) {
        if (isTransitioning) {
            // Move the transition image diagonally
            transitionX += transitionSpeedX * delta;
            transitionY += transitionSpeedY * delta;
            if (transitionX >= Gdx.graphics.getWidth() && transitionY >= Gdx.graphics.getHeight()) {
                // Transition is complete
                transitionX = Gdx.graphics.getWidth();
                transitionY = Gdx.graphics.getHeight();
                isTransitioning = false;
                // Change the screen after the transition
                // Replace with your actual screen class
                if (isExit)
                    this.callback.run();
                else
                    game.setScreen(GdxGame.ScreenType.MAIN_MENU);
            }
        }
    }

    /**
     * Draws the actor on the screen.
     *
     * @param batch       The batch used for rendering.
     * @param parentAlpha The alpha value from the parent actor (not used in this class).
     */
    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (isTransitioning) {
            // Draw the transition image at the current X and Y positions
            batch.draw(transitionSprite, transitionX, transitionY);
        }
    }

    public void startTransition() {
        isTransitioning = true;
        transitionX = 0;
        transitionY = 0;
    }

    /**
     * Displays the SpaceMiniTransition actor on the specified stage and sets a callback to execute after the transition.
     *
     * @param stage    The Stage where the actor will be displayed.
     * @param callback The callback to execute after the transition completes.
     */

    public void showDialog(Stage stage, Runnable callback) {
        this.callback = callback;
        stage.addActor(this);
    }

    /**
     * Disposes of resources used by the SpaceMiniTransition instance.
     * Call this method when the transition effect is no longer needed.
     */
    public void dispose() {
        // Dispose of resources when done
        transitionImage.dispose();
        spriteBatch.dispose();
    }
}
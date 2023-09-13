package com.csse3200.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.csse3200.game.GdxGame;

public class MainAlertBox extends Actor {

    private GdxGame game;
    private Runnable callback;
    private boolean isTransitioning = false;
    private Texture transitionImage;
    private SpriteBatch spriteBatch;
    private Sprite transitionSprite;
    private float transitionX = 50f; // X-axis position for the transition
    private float transitionSpeed = 450f; // Adjust the speed of the transition

    private boolean isExit=false;

    public MainAlertBox(GdxGame game, String alert, Skin skin, String alertText) {

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


    @Override
    public void act(float delta) {
        if (isTransitioning) {
            // Move the transition image along the X-axis
            transitionX += transitionSpeed * delta;
            if (transitionX >= Gdx.graphics.getWidth()) {
                // Transition is complete
                transitionX = Gdx.graphics.getWidth();
                isTransitioning = false;
                // Change the screen after the transition
                // Replace with your actual screen class
                if(isExit)
                    game.setScreen(GdxGame.ScreenType.MAIN_MENU);
                else
                    this.callback.run();
            }

        }
    }
    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (isTransitioning) {
            // Draw the transition image at the current X-axis position
            batch.draw(transitionSprite, transitionX, 10);
        }

    }

    private void startTransition() {
        isTransitioning = true;
        transitionX = 0;
    }

    public void showDialog(Stage stage, Runnable callback) {
        this.callback = callback;
        stage.addActor(this);
    }

    public void dispose() {
        // Dispose of resources when done
        transitionImage.dispose();
        spriteBatch.dispose();
    }
}
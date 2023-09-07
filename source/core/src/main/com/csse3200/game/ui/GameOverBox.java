package com.csse3200.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GameOverBox extends Dialog {

    private Image gameOverImage;

    public GameOverBox(String title, Skin skin, Viewport viewport) {
        super(title, skin);

        // Create an Image widget for the "Game Over" image
        Texture gameOverTexture = new Texture(Gdx.files.internal("images/1.png"));
        gameOverImage = new Image(gameOverTexture);

        // Position the "Game Over" image at the center
        gameOverImage.setPosition((viewport.getWorldWidth() - gameOverImage.getWidth()) / 2,
                (viewport.getWorldHeight() - gameOverImage.getHeight()) / 2);

        // Add the "Game Over" image to the dialog's content table
        getContentTable().addActor(gameOverImage);

        // Set the size of the dialog to match the image size
        setSize(gameOverImage.getWidth(), gameOverImage.getHeight());

        // Center the dialog on the screen
        setPosition((viewport.getWorldWidth() - getWidth()) / 2, (viewport.getWorldHeight() - getHeight()) / 2);
    }

    @Override
    public void draw(com.badlogic.gdx.graphics.g2d.Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
    }

    public void showDialog(Stage stage) {
        stage.addActor(this);
    }
}

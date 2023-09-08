package com.csse3200.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.csse3200.game.GdxGame;
import static com.csse3200.game.screens.MainMenuScreen.logger;

public class MainAlert extends Dialog {

    private GdxGame game;
    private Runnable callback;
    public MainAlert(GdxGame game, String alert, Skin skin, String alertText) {
        super(alert, skin);
        this.game = game;
        setMovable(false);
        setResizable(true);
        text(alertText);
        // Create a "Start" button to close the dialog
        TextButton startButton = new TextButton("OK", skin);
        button(startButton, true);
        startButton.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("OK button clicked");
                        hide();
                        if (callback != null) {
                            callback.run(); // Execute the callback function
                        }
                    }
                });


        // Lay out the dialog's components
        getContentTable().row();
        getContentTable().add(startButton).pad(20f).center();

        // Size and positioning of the dialog
        setSize(1080f, 1200); // Adjust the size as needed
        setPosition((Gdx.graphics.getWidth() - getWidth()) / 2, (Gdx.graphics.getHeight() - getHeight()) / 2);
    }

    public void showDialog(Stage stage, Runnable callback) {
        this.callback = callback;
        stage.addActor(this);
    }


}
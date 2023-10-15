package com.csse3200.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.csse3200.game.GdxGame;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.services.PlanetTravel;

import static com.csse3200.game.screens.MainMenuScreen.logger;


public class AlertBox extends Dialog {

    private final GdxGame game;
    public AlertBox( GdxGame game, String alert, Skin skin) {
        super(alert, skin);
        this.game = game;
        setMovable(false);
        setResizable(true);

        // Create a label with your alert text
        String alertText = "Hey, it's your Companion\n";
        text(alertText);

        // Create a "Start" button to close the dialog
        TextButton startButton = new TextButton("OK", skin);
        button(startButton, true);
        Entity entity = new Entity();
        entity.getEvents().addListener("ok", this::onOK);
        startButton.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("OK button clicked");
                        entity.getEvents().trigger("ok");
                    }
                });


        // Lay out the dialog's components
        getContentTable().row();
        getContentTable().add(startButton).pad(20f).center();

        // Size and positioning of the dialog
        setSize(1920f, 1200f); // Adjust the size as needed
        setPosition((Gdx.graphics.getWidth() - getWidth()) / 2, (Gdx.graphics.getHeight() - getHeight()) / 2);
    }

    public void showDialog(Stage stage) {
        stage.addActor(this);
    }
    private void onOK() {
        logger.info("Start game");
        new PlanetTravel(game).returnToCurrent();
    }
}


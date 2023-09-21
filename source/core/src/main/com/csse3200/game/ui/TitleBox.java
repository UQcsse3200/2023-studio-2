/**
 * TitleBox class represents a dialog box that displays a title, story text, and an "OK" button.
 * It provides information about the game's backstory and allows the player to start the game.
 */
package com.csse3200.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.csse3200.game.GdxGame;
import com.csse3200.game.entities.Entity;

import static com.csse3200.game.screens.MainMenuScreen.logger;

public class TitleBox extends Dialog {

    private GdxGame game;

    /**
     * Constructs a TitleBox dialog.
     *
     * @param game  The main GdxGame instance.
     * @param title The title of the dialog.
     * @param skin  The skin to style the dialog's components.
     */
    public TitleBox(GdxGame game, String title, Skin skin) {
        super(title, skin);
        this.game = game;
        setMovable(false);
        setResizable(true);

        // Story text describing the game's premise
        String storyText = "ASTRONAUT : HELP ME!";

        Label titleLabel = getTitleLabel();
        titleLabel.setText(title);
        titleLabel.setAlignment(Align.center);
        titleLabel.setFontScale(0.3f); // Adjust font scale as needed
        titleLabel.setColor(Color.BLACK); // TitleBox Title Colour can be changed here

        Label.LabelStyle labelStyle = new Label.LabelStyle(skin.get("small", Label.LabelStyle.class));
        labelStyle.font.getData().setScale(0.3f); // Set the font scale to make it larger

        Label storyLabel = new Label(storyText, labelStyle);
        storyLabel.setAlignment(Align.center);
        storyLabel.setWrap(true); // Enable text wrapping
//        ScrollPane scrollPane = new ScrollPane(storyLabel, skin);
//        getContentTable().add(scrollPane).width(1300f).height(800f).pad(20f).center(); // Adjust width and height as needed

        // Create an "OK" button to close the dialog
        TextButton startButton = new TextButton("HELP HIM", skin);
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

        Table buttonTable = new Table();
        buttonTable.add(startButton).pad(20f).expandX().center().row();

        buttonTable.padBottom(100f);

        getContentTable().add(storyLabel).width(Gdx.graphics.getWidth() * 0.8f).pad(20f).center().row();
        getContentTable().add(buttonTable).expandX().center().right();

        // Size and positioning of the dialog
        setSize(Gdx.graphics.getWidth(), 200f); // Adjust the height as needed
        setPosition((Gdx.graphics.getWidth() - getWidth()) / 2, 0);
    }

    /**
     * Displays the TitleBox dialog on the specified stage.
     *
     * @param stage The stage to add the dialog to.
     */
    public void showDialog(Stage stage) {
        stage.addActor(this);
    }

    /**
     * Event handler for the "OK" button click event. Initiates the game.
     */
    private void onOK() {
        logger.info("Start game");
        remove();
    }
}

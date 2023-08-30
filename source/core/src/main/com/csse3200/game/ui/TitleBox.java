/**
 * TitleBox class represents a dialog box that displays a title, story text, and an "OK" button.
 * It provides information about the game's backstory and allows the player to start the game.
 */
package com.csse3200.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.graphics.Color;
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
        String storyText = "Earth has become a desolate wasteland ravaged by a deadly virus. Civilisation as we know  has crumbled, and humanity's last hope lies among the stars. You are one of the few survivors who have managed to secure a spot on a spaceship built with the hopes of finding a cure or a new home on distant planets. The spaceship belongs to Dr Emily Carter, a brilliant scientist determined to find a cure for the virus and make the earth habitable again. But the cosmos is a vast and dangerous place, filled with unknown challenges and mysteries, from alien encounters to unexpected phenomena. Your journey begins now as you board the spaceship \"Aurora\" and venture into the unknown.";

        Label titleLabel = getTitleLabel();
        titleLabel.setText(title);
        titleLabel.setAlignment(Align.center);
        titleLabel.setFontScale(1.8f); // Adjust font scale as needed
        titleLabel.setColor(Color.WHITE); // TitleBox Title Colour can be changed here

        Label.LabelStyle labelStyle = new Label.LabelStyle(skin.get("large", Label.LabelStyle.class));
        labelStyle.font.getData().setScale(1.6f); // Set the font scale to make it larger

        Label storyLabel = new Label(storyText, labelStyle);
        storyLabel.setAlignment(Align.center);
        storyLabel.setWrap(true); // Enable text wrapping
        ScrollPane scrollPane = new ScrollPane(storyLabel, skin);
        getContentTable().add(scrollPane).width(1300f).height(800f).pad(20f).center(); // Adjust width and height as needed

        // Create an "OK" button to close the dialog
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
        setSize(1600f, 1080f); // Adjust the size as needed
        setPosition((Gdx.graphics.getWidth() - getWidth()) / 2, (Gdx.graphics.getHeight() - getHeight()) / 2);
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
        game.setScreen(GdxGame.ScreenType.GAME_STORY);
    }
}

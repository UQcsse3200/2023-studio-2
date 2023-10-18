package com.csse3200.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.csse3200.game.GdxGame;
import com.csse3200.game.entities.Entity;
import com.rafaskoberg.gdx.typinglabel.TypingLabel;


/**
 * A custom dialog box with a title, description, and an OK button.
 * This dialog is typically used to display information to the player.
 */
public class TitleBox extends Dialog {

    private GdxGame game;


    /**
     * Creates a new TitleBox with the specified title, description, skin, and window style name.
     *
     * @param game             The main game instance.
     * @param title            The title text displayed at the top of the dialog.
     * @param description      The description text displayed in the dialog.
     * @param skin             The skin to use for styling the UI elements.
     * @param windowStyleName  The name of the window style to use.
     */
    public TitleBox(GdxGame game, String title, String description, Skin skin, String windowStyleName) {
        super(title, skin, windowStyleName);
        this.game = game;
        setMovable(false);
        setResizable(true);
        setSize(Gdx.graphics.getWidth(), 350);
        setPosition(0, 0);


        Label titleLabel = getTitleLabel();
        titleLabel.setText(title);
        titleLabel.setAlignment(Align.center);
        titleLabel.setFontScale(0.2f);
        titleLabel.setColor(Color.BLACK);

        TypingLabel descriptionLabel = new TypingLabel("", skin);


        // Initialize TypingLabel with your description
        descriptionLabel = new TypingLabel(description, skin);
        descriptionLabel.setAlignment(Align.center);
        descriptionLabel.setWrap(true);
        descriptionLabel.setColor(Color.BLACK);

        TextButton okButton = new TextButton("OK", skin);
        button(okButton, true);

        Entity entity = new Entity();
        entity.getEvents().addListener("ok", this::onOK);
        okButton.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        entity.getEvents().trigger("ok");
                    }
                }
        );

        Table buttonTable = new Table();
        buttonTable.add(okButton).pad(20f).expandX().center().row();

        ScrollPane scrollPane = new ScrollPane(descriptionLabel, skin);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollingDisabled(true, false);
        getContentTable().add(scrollPane).width(Gdx.graphics.getWidth() * 0.8f).height(90f).right().padTop(50f).row();
        getContentTable().add(buttonTable).expandX().center();


    }

    /**
     * Shows the dialog on the specified stage.
     *
     * @param stage The stage to which the dialog will be added.
     */
    public void showDialog(Stage stage) {
        stage.addActor(this);
    }

    private void onOK() {
        hide();
    }
}

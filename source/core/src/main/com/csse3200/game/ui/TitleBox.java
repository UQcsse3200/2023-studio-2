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

public class TitleBox extends Dialog {

    private GdxGame game;
    private Label descriptionLabel;

    public TitleBox(GdxGame game, String title, String description, Skin skin) {
        super(title, skin);
        this.game = game;
        setMovable(false);
        setResizable(true);

        Label titleLabel = getTitleLabel();
        titleLabel.setText(title);
        titleLabel.setAlignment(Align.center);
        titleLabel.setFontScale(0.2f); // Adjust font scale as needed
        titleLabel.setColor(Color.BLACK); // TitleBox Title Color can be changed here

        Label.LabelStyle labelStyle = new Label.LabelStyle(skin.get("thick", Label.LabelStyle.class));
        labelStyle.font.getData().setScale(0.3f); // Set the font scale to make it larger

        descriptionLabel = new Label(description, labelStyle);
        descriptionLabel.setAlignment(Align.center);
        descriptionLabel.setWrap(true); // Enable text wrapping


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



//         Use a ScrollPane for description text if it exceeds the dialog's height
        ScrollPane scrollPane = new ScrollPane(descriptionLabel, skin);
        scrollPane.setFadeScrollBars(false); // Disable fading
        scrollPane.setScrollingDisabled(true, false); // Disable horizontal scrolling
        getContentTable().add(scrollPane).width(Gdx.graphics.getWidth() * 0.8f).height(100f).pad(20f).center().row();

        getContentTable().add(buttonTable).expandX().center().center();
        descriptionLabel.setPosition(0, 300f);

        setSize(Gdx.graphics.getWidth(), 300f); // Adjust the height as needed
    }

    public void showDialog(Stage stage) {
        stage.addActor(this);
    }

    private void onOK() {
       hide();
    }
}
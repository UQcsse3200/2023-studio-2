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
import com.rafaskoberg.gdx.typinglabel.TypingLabel;

import static java.awt.SystemColor.text;

public class TitleBox extends Dialog {

    private GdxGame game;

    public TitleBox(GdxGame game, String title, String description, Skin skin) {
        super(title, skin);
        this.game = game;
        setMovable(false);
        setResizable(true);


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
        getContentTable().add(scrollPane).width(Gdx.graphics.getWidth() * 0.8f).height(100f).pad(20f).center().row();

        getContentTable().add(buttonTable).expandX().center().center();

        setSize(Gdx.graphics.getWidth(), 550f);
    }

    public void showDialog(Stage stage) {
        stage.addActor(this);
    }

    private void onOK() {
        hide();
    }
}

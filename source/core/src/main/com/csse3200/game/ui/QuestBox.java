package com.csse3200.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;

public class QuestBox extends Dialog {

    private Label questLabel;
    private Skin skin;

    private Stage stage;

    public QuestBox(String quest, Skin skin, Stage stage) {
        super("", skin);
        this.stage=stage;
        this.skin = skin;
        questLabel = new Label(quest, skin);
        setQuestText(quest);
        create();
    }

    public void create() {
        questLabel.setAlignment(Align.center);
        questLabel.setFontScale(1.0f);
        questLabel.setColor(Color.BLACK);

        this.getContentTable().add(questLabel).width(500f).pad(20f).center();

        float xOffset = 0f; // Adjust the horizontal offset if needed
        float yOffset = -180f; // Adjust the vertical offset to move it downwards

        setSize(600f, 200f);
        setPosition(Gdx.graphics.getWidth() - getWidth() - 20 + xOffset, Gdx.graphics.getHeight() - getHeight() - 20 + yOffset);
    }


    public void setQuestText(String quest) {
        questLabel.setText(quest);
    }

    public void showQuestBox() {
        stage.addActor(questLabel);
    }
}

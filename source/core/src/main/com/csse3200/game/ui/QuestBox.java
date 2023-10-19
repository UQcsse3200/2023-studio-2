package com.csse3200.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;

/**
 * A custom dialog box for displaying quest information.
 * It includes a label with quest details.
 */
public class QuestBox extends Dialog {

    private Label questLabel;
    private Skin skin;

    private Stage stage;

    /**
     * Creates a new QuestBox with the specified quest description, skin, and stage.
     *
     * @param quest The quest description to display.
     * @param skin  The skin to use for styling the UI elements.
     * @param stage The stage to which the QuestBox will be added.
     */
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

    /**
     * Sets the text for the quest label.
     *
     * @param quest The quest description to display.
     */
    public void setQuestText(String quest) {
        questLabel.setText(quest);
    }

    public void showQuestBox() {
        stage.addActor(questLabel);
    }
}

package com.csse3200.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.csse3200.game.GdxGame;
import com.rafaskoberg.gdx.typinglabel.TypingLabel;

public class TutorialDialogue extends Dialog {
    private int nextIndex = 0;
    private Skin skin;
    String[] nextMessages;
    String[] nextTitles;
    String[] window;


    private TypingLabel contentLabel; // Changed the variable name to "contentLabel"

    public TutorialDialogue(GdxGame game, String[] title, String[] contentText, Skin skin, String[] windowStyleName) {
        super("", skin, windowStyleName[0]);
        nextMessages = contentText;
        nextTitles = title;
        window=windowStyleName;
        this.skin=skin;

        // Create the LabelStyle and set the font scale
        Label.LabelStyle labelStyle = new Label.LabelStyle(skin.get("small", Label.LabelStyle.class));
        labelStyle.font.getData().setScale(0.3f);

        contentLabel = new TypingLabel(contentText[0], labelStyle); // Changed the variable name to "contentLabel"
        create();
    }

    private void create() {
        getTitleLabel().setAlignment(Align.center);
        getTitleLabel().setColor(Color.BLACK);
        getTitleLabel().setFontScale(0.35f);

        contentLabel.setWrap(true);
        contentLabel.setAlignment(Align.center);
        contentLabel.setColor(Color.BLACK);

        contentLabel = new TypingLabel(nextMessages[0], skin);

        TextButton okButton = new TextButton("Next", skin);
        okButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

                nextIndex++;

                if (nextIndex < nextTitles.length) {
                    getTitleLabel().setText(nextTitles[nextIndex]);
                    contentLabel.setText(nextMessages[nextIndex]);
                    setStyle(skin.get(window[nextIndex], Window.WindowStyle.class));
                } else {
                    remove();
                }
            }
        });
        okButton.setSize(50, 25);

        Table contentTable = getContentTable();
        contentTable.add(contentLabel).expandX().fillX().pad(20f).center().row();
        contentTable.add(okButton).pad(10f).center();

        setMovable(false);
        setResizable(true);
        setSize(Gdx.graphics.getWidth(), 550f);
    }

    public void showDialog(Stage stage) {
        stage.addActor(this);
    }
}

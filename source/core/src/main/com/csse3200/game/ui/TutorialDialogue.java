package com.csse3200.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.csse3200.game.GdxGame;

public class TutorialDialogue extends Dialog {
    private int nextIndex = -1;
    private Skin skin;
    private Label contentLabel; // Changed the variable name to "contentLabel"

    public TutorialDialogue(GdxGame game, String title, String contentText, Skin skin) {
        super(title, skin);
        this.skin = skin;

        // Create the LabelStyle and set the font scale
        Label.LabelStyle labelStyle = new Label.LabelStyle(skin.get("small", Label.LabelStyle.class));
        labelStyle.font.getData().setScale(0.3f);

        contentLabel = new Label(contentText, labelStyle); // Changed the variable name to "contentLabel"
        create();
    }

    private void create() {
        getTitleLabel().setAlignment(Align.center);
        getTitleLabel().setColor(Color.BLACK);
        getTitleLabel().setFontScale(0.35f);

        contentLabel.setWrap(true);
        contentLabel.setAlignment(Align.center);
        contentLabel.setColor(Color.BLACK);

        TextButton okButton = new TextButton("Next", skin);
        okButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String[] nextMessages = {
                        "W- Move Up\n A- Move Right \n D- Move Left \n S-Move Down ",
                        "SpaceBar is used to Sprint",
                        "You can swap around powers by 1,2 and 3 \n 1 is Sword \n 2 is Fire Boomerang\n 3 is hammer which builds/repair things",
                        "After using 3 click on T to open the action picker and click on the action you want to do \n you can also directly switch by using the mouse and clicking on the action you want to do on the right "
                };
                String[] nextTitles = {"", "", "", ""};

                nextIndex++;

                if (nextIndex < nextTitles.length) {
                    getTitleLabel().setText(nextTitles[nextIndex]);
                    contentLabel.setText(nextMessages[nextIndex]);
                } else {
                    hide();
                }
            }
        });
        okButton.setSize(50, 25);

        Table contentTable = getContentTable();
        contentTable.add(contentLabel).expandX().fillX().pad(20f).center().row();
        contentTable.add(okButton).pad(10f).center();

        setMovable(false);
        setResizable(true);
        setSize(Gdx.graphics.getWidth(), 300f);
    }

    public void showDialog(Stage stage) {
        stage.addActor(this);
    }
}

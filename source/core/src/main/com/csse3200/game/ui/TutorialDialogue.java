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
    private Label content;

    public TutorialDialogue(GdxGame game, String title, String content, Skin skin) {
        super(title, skin); // Set the title during initialization
        this.skin=skin;
        Label.LabelStyle labelStyle = new Label.LabelStyle(skin.get("thick", Label.LabelStyle.class));
        labelStyle.font.getData().setScale(0.3f); // Set the font scale to make it larger
        this.content=new Label(content,labelStyle);
        create();
    }

        private void create() {
            // Access the title label and center-align it
            getTitleLabel().setAlignment(Align.center);
            getTitleLabel().setColor(Color.BLACK);
            getTitleLabel().setFontScale(0.35f); // Adjust font scale as needed


            // Create and configure the content label
            Label contentLabel = content;
            contentLabel.setWrap(true); // Enable text wrapping
            contentLabel.setAlignment(Align.center); // Center-align the content label
            contentLabel.setColor(Color.BLACK);


            // Create and configure the OK button
            TextButton okButton = new TextButton("Next", skin);
            okButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    String[] nextMessages = {"NPC: (Relieved) Thank you so much!\nThere's a spaceship not far from here\nthat can get us off this planet. But\nbe warned, it's guarded by infected.", "Emily: We can handle it. \nLead the way!"};
                    String[] nextTitles = {"", ""};

                    // Increment the index for the next click
                    nextIndex++;

                    // Check if there are more dialogues to show
                    if (nextIndex < nextTitles.length) {
                        getTitleLabel().setText(nextTitles[nextIndex]);
                        contentLabel.setText(nextMessages[nextIndex]);
                        getContentTable().clear();
                        create();


                    } else {
                        // No more dialogues, close the last one
                        hide();
                    }

                }
            });
            okButton.setSize(50, 25);

            // Create a table to hold the content label and OK button
            Table contentTable = getContentTable();
            contentTable.add(contentLabel).expandX().fillX().pad(20f).center().row(); // Center-align the content label
            contentTable.add(okButton).pad(10f).center(); // Center-align the OK button

            // Set dialog properties
            setMovable(false);
            setResizable(true);
            setSize(Gdx.graphics.getWidth(), 300f); // Adjust the size as needed

    }

    public void showDialog(Stage stage) {
        stage.addActor(this);
    }
}

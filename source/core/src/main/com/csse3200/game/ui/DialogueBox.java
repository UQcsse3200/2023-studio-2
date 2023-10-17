package com.csse3200.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.csse3200.game.entities.Entity;
import static com.csse3200.game.screens.MainMenuScreen.logger;

/**
 * A custom dialog box for displaying in-game dialogues with an OK button.
 */
public class DialogueBox extends Dialog {

    private Label dialogueLabel; // Change TypingLabel to Label
    private Label titleLabel; // Remove TypingLabel for title
    private String[] titles;
    private String[] messages;
    TextButton info;
    private int nextIndex = 0;
    private Skin skin;

    /**
     * Creates a new DialogueBox instance.
     *
     * @param title The title of the dialogue box.
     * @param skin  The skin to use for styling the dialogue box.
     */
    public DialogueBox(String[] title, String[] message, Skin skin) {
        super("", skin);
        this.skin = skin;
        titleLabel = new Label(title[0], skin); // Create a Label for the title
        this.dialogueLabel = new Label(message[0], skin); // Create a Label for the dialogue
        this.titles = title;
        this.messages = message;
        create();
    }

    public void create() {
        // Create a Pixmap for the dialog background
        Pixmap bgPixmap = new Pixmap(900, 600, Pixmap.Format.RGBA8888);
        bgPixmap.setColor(0.5f, 0.5f, 0.5f, 0.8f); // Gray color with alpha
        bgPixmap.fill();

        Texture bgTexture = new Texture(bgPixmap);


        titleLabel.setAlignment(Align.top);
        titleLabel.setFontScale(0.3f);
        titleLabel.setColor(Color.BLACK);

        getContentTable().add(titleLabel).padTop(20f).row();

        // Create a Label for the dialog text
        Label dialogueLabel = new Label("", skin);
        dialogueLabel.setColor(1, 1, 1, 0); // Set the background color of the Label to transparent
        getContentTable().add(dialogueLabel).width(500f).height(-15f).pad(20f).center();

        TextButton startButton = new TextButton("OK", skin);
        button(startButton, true);
        Entity entity = new Entity();
        entity.getEvents().addListener("ok", this::onOK);
        startButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                logger.debug("OK button clicked");
                entity.getEvents().trigger("ok");
            }
        });

        Table customButtonTable = new Table();
        customButtonTable.add(startButton).pad(20f);

        info = new TextButton("Next", skin);
        button(info, true);
        entity.getEvents().addListener("info", this::oninfo);
        info.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                logger.debug("Next button clicked");
                entity.getEvents().trigger("info");
            }
        });

        // Add the custom button table to the dialog
        getContentTable().row();
        getContentTable().add(customButtonTable).center();

        // Set the background using the Texture
        getContentTable().background(new Image(bgTexture).getDrawable());

        setSize(900f, 600f);
        setPosition((Gdx.graphics.getWidth() - getWidth()) / 2, Gdx.graphics.getHeight() - getHeight() - 500);
    }



    /**
     * Sets the text to be displayed in the dialogue box.
     *
     * @param text The text to display.
     */
    public void setDialogueText(String text) {
        dialogueLabel.setText(text);
    }

    /**
     * Shows the dialogue box on the specified stage.
     *
     * @param stage The stage to display the dialogue box on.
     */
    public void showDialog(Stage stage) {
        stage.addActor(this);
    }

    public void onOK() {
        logger.info("Back to the game");
        hide();
    }

    public void oninfo() {
        String[] nextTitles = titles;
        String[] nextMessages = messages;

        // Increment the index for the next click
        nextIndex++;

        // Check if there are more dialogues to show
        if (nextIndex < nextTitles.length) {
            titleLabel.setText(nextTitles[nextIndex]);
            dialogueLabel.setText(nextMessages[nextIndex]);

            getContentTable().clear();
            create();
        } else {
            // No more dialogues, close the last one
            onOK();
        }
    }
}

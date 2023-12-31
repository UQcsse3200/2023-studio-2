package com.csse3200.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.csse3200.game.components.maingame.MainGameActions;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.services.ServiceLocator;
import static com.csse3200.game.screens.MainMenuScreen.logger;
import com.rafaskoberg.gdx.typinglabel.TypingLabel;

/**
 * A custom dialog box for displaying in-game dialogues with an OK button.
 */
public class DialogueBox extends Dialog {

    private TypingLabel dialogueLabel;
    private Label titleLabel;
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
        titleLabel = new TypingLabel(title[0], skin);
        this.dialogueLabel = new TypingLabel(message[0], skin);
        this.titles=title;
        this.messages=message;
        create();
    }


    public void create(){
        for (Entity mainGame : ServiceLocator.getEntityService().getEntitiesByComponent(MainGameActions.class)) {
            mainGame.getEvents().trigger("pauseGame");
        }
        titleLabel.setAlignment(Align.top);
        titleLabel.setFontScale(0.3f); // Adjust font scale as needed
        titleLabel.setColor(Color.BLACK); // TitleBox Title Colour can be changed here


        this.getContentTable().add(titleLabel).padTop(20f).row();
        this.getContentTable().add(dialogueLabel).width(500f).height(-15f).pad(20f).center(); // Adjust width and height as needed


        TypingLabel.LabelStyle labelStyle = new Label.LabelStyle(skin.get("small", Label.LabelStyle.class));
        labelStyle.font.getData().setScale(0.8f); // Set the font scale to make it larger


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

            Table customButtonTable = new Table();
        customButtonTable.add(startButton).pad(20f); // Add padding as needed
            info = new TextButton("Next", skin);
            button(info, true);
        entity.getEvents().addListener("info", this::oninfo);
        info.addListener(
                new ChangeListener() {
                @Override
                public void changed(ChangeEvent changeEvent, Actor actor) {
                    logger.debug("Next button clicked");
                    entity.getEvents().trigger("info");


                }
            });

        customButtonTable.add(info).pad(25f); // Add padding as needed


            // Add the custom button table to the dialog
        this.getContentTable().row();
        this.getContentTable().add(customButtonTable).center();


            // Size and positioning of the dialog
            setSize(900f, 600f); // Adjust the size as needed
            setPosition((Gdx.graphics.getWidth() - getWidth()) / 2, Gdx.graphics.getHeight() - getHeight() - 500); // Adjust the Y position as needed

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
        logger.info("Back to game");
        for (Entity mainGame : ServiceLocator.getEntityService().getEntitiesByComponent(MainGameActions.class)) {
            mainGame.getEvents().trigger("resumeGame");
        }
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

            // Set the text of the TypingLabel
            dialogueLabel.setText(nextMessages[nextIndex]);

            // Start the TypingLabel animation
            dialogueLabel.restart();

            getContentTable().clear();
            create();
        } else {
            // No more dialogues, close the last one
            onOK();
        }
    }

}
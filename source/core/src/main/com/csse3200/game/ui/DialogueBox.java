package com.csse3200.game.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.csse3200.game.GdxGame;
import com.csse3200.game.entities.Entity;

import static com.csse3200.game.screens.MainMenuScreen.logger;

/**
 * A custom dialog box for displaying in-game dialogues with an OK button.
 */
public class DialogueBox extends Dialog {

    private GdxGame game;
    private TitleBox titleBox;
    private Label dialogueLabel;
    private DialogueBox dialogueBox;

    /**
     * Creates a new DialogueBox instance.
     *
     * @param game       The game instance.
     * @param dialogueBox The dialogue box instance.
     * @param title      The title of the dialogue box.
     * @param skin       The skin to use for styling the dialogue box.
     */
    public DialogueBox(GdxGame game, DialogueBox dialogueBox, String title, Skin skin) {
        super(title, skin);
        this.game = game;
        this.dialogueBox = dialogueBox;
        this.dialogueLabel = new Label("", skin);
        this.getContentTable().add(dialogueLabel).align(Align.left);
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

    private void onOK() {
        logger.info("Start game");
        game.setScreen(GdxGame.ScreenType.MAIN_GAME);
    }
}

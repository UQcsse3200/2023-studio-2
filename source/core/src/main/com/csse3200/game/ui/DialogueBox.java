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
import com.csse3200.game.services.ServiceLocator;

import static com.csse3200.game.screens.MainMenuScreen.logger;

/**
 * A custom dialog box for displaying in-game dialogues with an OK button.
 */
public class DialogueBox extends Dialog {

    //private final InputOverrideComponent inputOverrideComponent;

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


        Label titleLabel = getTitleLabel();
        titleLabel.setAlignment(Align.center);
        titleLabel.setFontScale(1.8f); // Adjust font scale as needed
        titleLabel.setColor(Color.WHITE); // TitleBox Title Colour can be changed here


        this.getContentTable().add(titleLabel).padTop(20f).row();
        this.getContentTable().add(dialogueLabel).width(800f).height(350f).pad(20f).center(); // Adjust width and height as needed


        Label.LabelStyle labelStyle = new Label.LabelStyle(skin.get("large", Label.LabelStyle.class));
        labelStyle.font.getData().setScale(1.6f); // Set the font scale to make it larger




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


        // Add the custom button table to the dialog
        this.getContentTable().row();
        this.getContentTable().add(customButtonTable).center();


        // Size and positioning of the dialog
        setSize(1200f, 600f); // Adjust the size as needed
        setPosition((Gdx.graphics.getWidth() - getWidth()) / 2, Gdx.graphics.getHeight() - getHeight() - 60); // Adjust the Y position as needed


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
    @Override
    public boolean remove() {
        //Stop overriding input when exiting minigame
        return super.remove();
    }
    private void onOK() {
        logger.info("Start game");
        game.setScreen(GdxGame.ScreenType.MAIN_GAME);
    }
}

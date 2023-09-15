/**
 * The `DialogComponent` class represents a component responsible for displaying dialog boxes
 * and handling dialog-related actions in the game's user interface.
 */
package com.csse3200.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.csse3200.game.GdxGame;
import com.csse3200.game.components.Component;
import com.badlogic.gdx.scenes.scene2d.ui.*;

import static com.csse3200.game.screens.MainMenuScreen.logger;
import static com.csse3200.game.ui.UIComponent.skin;
import com.csse3200.game.components.mainmenu.MainMenuActions;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.ServiceLocator;

public class DialogComponent extends Component {
    public Stage stage = ServiceLocator.getRenderService().getStage();
    private DialogueBox dialogueBox;
    private GdxGame game = MainMenuActions.game;

    /**
     * Creates a new DialogComponent with the given DialogueBox instance.
     *
     * @param dialogueBox The DialogueBox instance to use for displaying dialog boxes.
     */
    public DialogComponent(DialogueBox dialogueBox) {
        this.dialogueBox = dialogueBox;
    }

    /**
     * Displays a dialog box with the specified title and text.
     *
     * @param title The title of the dialog box.
     * @param text  The text content of the dialog box.
     */
    public void showdialogue(String title, String text) {
        dialogueBox = new DialogueBox(title, text, skin);
        dialogueBox.setDialogueText(text);
        dialogueBox.showDialog(stage);
    }
}

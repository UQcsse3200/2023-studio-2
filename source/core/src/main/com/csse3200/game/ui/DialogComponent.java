package com.csse3200.game.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
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
    public Stage stage=ServiceLocator.getRenderService().getStage();
    private DialogueBox dialogueBox;
    //private Dialog activeDialog;
    private GdxGame game= MainMenuActions.game;
    public DialogComponent(DialogueBox dialogueBox){}
    public void showdialogue(String text) {
        dialogueBox = new DialogueBox(game, dialogueBox, text, skin);
        dialogueBox.setDialogueText(text);
        dialogueBox.showDialog(stage);

    }
}
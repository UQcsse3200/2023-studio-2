package com.csse3200.game.ui;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.csse3200.game.GdxGame;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.mainmenu.MainMenuActions;
import com.csse3200.game.services.ServiceLocator;

import static com.csse3200.game.ui.UIComponent.skin;

public class DialogComponent extends Component {
    public Stage stage=ServiceLocator.getRenderService().getStage();
    private DialogueBox dialogueBox;
    //private Dialog activeDialog;
    private GdxGame game= MainMenuActions.game;
    public DialogComponent(DialogueBox dialogueBox){}
    public void showdialogue(String Title ,String text) {
        dialogueBox = new DialogueBox(Title,text, skin);
        dialogueBox.setDialogueText(text);
        dialogueBox.showDialog(stage);

    }


}
package com.csse3200.game.ui;

import com.csse3200.game.components.Component;

import static com.csse3200.game.entities.factories.NPCFactory.dialogueBox;

public class DialogComponent extends Component {

    public void showDialogue(String text) {
        dialogueBox.setDialogueText(text);
        dialogueBox.showDialogue();
        
    }



}
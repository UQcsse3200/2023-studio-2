package com.csse3200.game.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.Align;
import com.csse3200.game.areas.ForestGameArea;

public class DialogueBox extends Dialog {
    private Label dialogueLabel;

    public DialogueBox(String title, Skin skin) {
        super(title, skin);
        this.dialogueLabel = new Label("", skin);
        this.getContentTable().add(dialogueLabel).align(Align.left);
    }

    public void setDialogueText(String text) {
        dialogueLabel.setText(text);
    }

    public void showDialogue() {
        this.show(getStage());
    }

}
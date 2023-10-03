package com.csse3200.game.areas;

import com.csse3200.game.components.player.KeyboardPlayerInputComponent;

public class KeyboardInputStep1 extends KeyboardInputTutorialStep {
    public KeyboardInputStep1(TutorialDialogue tutorialDialogue, KeyboardPlayerInputComponent inputComponent) {
        super(tutorialDialogue, inputComponent);
    }

    @Override
    public void showStep() {
        TutorialDialogue tutorialDialogue = new TutorialDialogue();
        tutorialDialogue.showMessage("Step 1: Welcome to the game!");
        tutorialDialogue.showMessage("Press W, A, S, D keys to move.");
    }
}

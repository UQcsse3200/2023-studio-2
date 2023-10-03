package com.csse3200.game.areas;

import com.csse3200.game.components.player.KeyboardPlayerInputComponent;

public abstract class KeyboardInputTutorialStep {
    protected TutorialDialogue tutorialDialogue;
    protected KeyboardPlayerInputComponent inputComponent;

    public KeyboardInputTutorialStep(TutorialDialogue tutorialDialogue, KeyboardPlayerInputComponent inputComponent) {
        this.tutorialDialogue = tutorialDialogue;
        this.inputComponent = inputComponent;
    }

    public abstract void showStep();
}

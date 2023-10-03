// TutorialGameArea.java
package com.csse3200.game.areas;

import com.csse3200.game.GdxGame;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.components.player.KeyboardPlayerInputComponent;
import com.csse3200.game.entities.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TutorialGameArea extends MapGameArea {
    private static final Logger logger = LoggerFactory.getLogger(TutorialGameArea.class);
    private int stepIndex = 0;
    private KeyboardInputTutorialStep[] tutorialSteps;
    private TutorialDialogue tutorialDialogue;


    public TutorialGameArea(String configPath, TerrainFactory terrainFactory, GdxGame game, int playerLives) {
        super(configPath, terrainFactory, game, playerLives);
    }


    /**
     * Create the game area
     */
    @Override
    public void create() {
        super.create();
        createDialogueUI();
        createKeyboardInputTutorialSteps();
        showNextTutorialStep();
    }
    public TutorialDialogue getTutorialDialogue() {
        return tutorialDialogue;
    }


    private void createDialogueUI() {
        Entity ui = new Entity();
        TutorialDialogue tutorialDialogue = new TutorialDialogue();
        ui.addComponent(tutorialDialogue);
        spawnEntity(ui);
    }

    private void createKeyboardInputTutorialSteps() {
        KeyboardPlayerInputComponent keyboardInputComponent = new KeyboardPlayerInputComponent();
        tutorialSteps = new KeyboardInputTutorialStep[]{
                new KeyboardInputStep1(getTutorialDialogue(), keyboardInputComponent),
                new KeyboardInputStep2(getTutorialDialogue(), keyboardInputComponent),
                // Add more steps as needed...
        };
    }

    private void showNextTutorialStep() {
        if (stepIndex < tutorialSteps.length) {
            KeyboardInputTutorialStep currentStep = tutorialSteps[stepIndex];
            currentStep.showStep();
            stepIndex++;
        } else {
            // Tutorial completed, you can proceed with the game.
        }
    }



}

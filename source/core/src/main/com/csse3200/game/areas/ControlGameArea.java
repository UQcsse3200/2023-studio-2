// ControlGameArea.java
package com.csse3200.game.areas;

import com.badlogic.gdx.math.GridPoint2;
import com.csse3200.game.GdxGame;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.PlayerConfig;
import com.csse3200.game.entities.factories.PlayerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ControlGameArea extends MapGameArea {

    private static final Logger logger = LoggerFactory.getLogger(ControlGameArea.class);
    private int stepIndex = 0;
    //private KeyboardInputTutorialStep[] tutorialSteps;
    // private TutorialDialogue tutorialDialogue;


    public ControlGameArea(TerrainFactory terrainFactory, GdxGame game) {
        super("controls", "main_area", terrainFactory, game);
    }


    /**
     * Create the game area
     */
    @Override
    public void create() {
        super.create();
//        createDialogueUI();
//        createKeyboardInputTutorialSteps();
//        showNextTutorialStep();
    }
    public Entity spawnPlayer() {
        Entity newPlayer;
        PlayerConfig playerConfig = null;
        if (mapConfig.areaEntityConfig != null) {
            playerConfig = mapConfig.areaEntityConfig.getEntity(PlayerConfig.class);
        }

        if (playerConfig != null) {
            newPlayer = PlayerFactory.createPlayer(playerConfig);
        } else {
            logger.info("Player not found in config file - creating generic player");
            newPlayer = PlayerFactory.createPlayer();
        }

        if (playerConfig != null && playerConfig.position != null) {
            spawnEntityAt(newPlayer, playerConfig.position, true, true);
        } else {
            logger.info("Failed to load player position - created player at middle of map");
            //If no position specified spawn in middle of map.
            GridPoint2 pos = new GridPoint2(terrain.getMapBounds(0).x/2,terrain.getMapBounds(0).y/2);
            spawnEntityAt(newPlayer, pos, true, true);
        }
        return newPlayer;
    }
    //public TutorialDialogue getTutorialDialogue() {
//        return tutorialDialogue;
//    }


//    private void createDialogueUI() {
//        Entity ui = new Entity();
//        TutorialDialogue tutorialDialogue = new TutorialDialogue();
//        ui.addComponent(tutorialDialogue);
//        spawnEntity(ui);
//    }

//    private void createKeyboardInputTutorialSteps() {
//        KeyboardPlayerInputComponent keyboardInputComponent = new KeyboardPlayerInputComponent();
//        tutorialSteps = new KeyboardInputTutorialStep[]{
//                new KeyboardInputStep1(getTutorialDialogue(), keyboardInputComponent),
//                new KeyboardInputStep2(getTutorialDialogue(), keyboardInputComponent),
//                // Add more steps as needed...
//        };
//    }
//
//    private void showNextTutorialStep() {
//        if (stepIndex < tutorialSteps.length) {
//            KeyboardInputTutorialStep currentStep = tutorialSteps[stepIndex];
//            currentStep.showStep();
//            stepIndex++;
//        } else {
//            // Tutorial completed, you can proceed with the game.
//        }
//    }



}
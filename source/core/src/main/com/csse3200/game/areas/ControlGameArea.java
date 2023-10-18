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

/**
 * Represents a specific game area dedicated to controls.
 * This class extends the general game area and adds functionality specific to the control game area.
 */
public class ControlGameArea extends MapGameArea {

    /** Logger instance to log important game events and information */
    private static final Logger logger = LoggerFactory.getLogger(ControlGameArea.class);
    private int stepIndex = 0;
    //private KeyboardInputTutorialStep[] tutorialSteps;
    // private TutorialDialogue tutorialDialogue;

    /**
     * Constructs a ControlGameArea with the specified terrain factory and game instance.
     *
     * @param terrainFactory The factory to create terrain elements.
     * @param game The main game instance.
     */
    public ControlGameArea(TerrainFactory terrainFactory, GdxGame game) {
        super("controls", "main_area", terrainFactory, game);
    }


    /**
     * Initializes the game area and sets up specific components.
     * This method is meant to be called once to prepare the game area.
     */
    @Override
    public void create() {
        super.create();
//        createDialogueUI();
//        createKeyboardInputTutorialSteps();
//        showNextTutorialStep();
    }

    /**
     * Spawns a player in the game area. The player's configuration is fetched from the area's configuration.
     * If no configuration is found, a generic player is created. The player is then positioned based on the
     * configuration, or at the center of the map if no position is specified.
     *
     * @return The spawned player entity.
     */

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
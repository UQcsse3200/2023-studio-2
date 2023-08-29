package com.csse3200.game.services;

import java.util.*;
import java.util.List;
import java.util.Arrays;
import com.csse3200.game.GdxGame;
import com.csse3200.game.services.GameState;

    /**
     * Responsible for travel between planets and stored the number of planets remaining
     */
    public class PlanetTravel {
        /**
         * The variable to set screen for the minigame
         */
        private final GdxGame game;

        /**
         * The current gameState
         */
        private final GameState gameState;

        /**
         * Constructor to set the current game state for using
         * @param gameState
         */
        public PlanetTravel(GameState gameState, GdxGame game) {
            this.gameState = gameState;
            this.game = game; // Set the game object
        }

        /**
         * Move to the next planet and spawn the minigame screen
         * @param planet - the destination planet
         */
        public void moveToNextPlanet(Object planet) {
            game.setScreen(GdxGame.ScreenType.SPACE_MAP);
            gameState.put("planet", planet);
        }

        /**
         * Return the current planet where player are in
         * @return current planet
         */
        public Object returnCurrent() {
            return gameState.get("planet");
        }
    }

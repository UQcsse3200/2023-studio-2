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
        private final GdxGame game = new GdxGame();
        private final GameState gameState;

        public PlanetTravel(GameState gameState) {
            this.gameState = gameState;
        }

        /**
         * Move to the next planet and spawn the minigame screen
         * @param planet - the destination planet
         */
        public void moveToNextPlanet(Object planet) {
            gameState.put("planet", planet);
        }

        /**
         * Return the planet where player are currently in
         * @return current planet
         */
        public Object returnCurrent() {
            return gameState.get("planet");
        }
    }

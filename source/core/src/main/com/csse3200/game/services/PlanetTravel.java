package com.csse3200.game.services;
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
         * Constructor to set the current game state for using
         */
        public PlanetTravel(GdxGame game) {
            this.game = game; // Set the game object
        }

        /**
         * Move to the next planet and spawn the minigame screen
         * @param planet - the destination planet
         */
        public void moveToNextPlanet(Object planet) {
            game.setScreen(GdxGame.ScreenType.SPACE_MAP);
            ServiceLocator.getGameStateObserverService().trigger("setCurrentPlanet", planet);
        }

        /**
         * Return the current planet where player are in
         * @return current planet
         */
        
    }

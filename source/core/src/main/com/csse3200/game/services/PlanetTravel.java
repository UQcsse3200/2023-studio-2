package com.csse3200.game.services;
import com.csse3200.game.GdxGame;
import com.csse3200.game.screens.PlanetScreen;
import com.csse3200.game.services.GameState;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.GameStateObserver;

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
        this.game = game;
        ServiceLocator.registerGameStateObserverService(new GameStateObserver()); // TODO: Can be removed once map button is removed from main menu
    }

    /**
     * Begin transitioning to the new planet from the current one. Displaying all
     * intermediate gameplay in between
     *
     * @param targetPlanetName  The name of the planet to transition to.
     */
    public void beginPlanetTransition(String targetPlanetName) {
        // Begin the transition.
        game.setScreen(GdxGame.ScreenType.INITIAL_SCREEN);

        // Play mini-game.

        // End transition period.

        // Finally, go to planet.
//        PlanetScreen currentPlanet = (PlanetScreen) ServiceLocator.getGameStateObserverService().getStateData("currentPlanet");
//        PlanetScreen nextPlanet = currentPlanet.getNextPlanet();
//        ServiceLocator.getGameStateObserverService().trigger("updatePlanet", "currentPlanet", nextPlanet);
//        game.setScreen(nextPlanet);
    }

    /**
     * Move to the next planet and spawn the minigame screen
     * @param planet - the destination planet
     */
    public void moveToNextPlanet(Object planet) {
        game.setScreen(GdxGame.ScreenType.SPACE_MAP);
        ServiceLocator.getGameStateObserverService().trigger("updatePlanet", "currentPlanet", planet);
    }

    /**
     * Return the current planet where player are in
     * @return current planet
     */
    public Object returnCurrent() {
        return ServiceLocator.getGameStateObserverService().getStateData("currentPlanet");
    }
}

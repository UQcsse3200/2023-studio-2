package com.csse3200.game.services;
import com.csse3200.game.GdxGame;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.screens.PlanetScreen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Responsible for travel between planets and stored the number of planets remaining
 */
public class PlanetTravel {
    private static final Logger logger = LoggerFactory.getLogger(PlanetTravel.class);
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
     * Begin transitioning to the next planet from the current one. Displaying all
     * intermediate gameplay in between
     */
    public void beginFullTravel() {
        game.setScreen(GdxGame.ScreenType.SPACEMINI_SCREEN);
    }

    /**
     * Travel from the current planet to the next planet instantly.
     */
    public void beginInstantTravel() {
        String nextPlanet = (String) ServiceLocator.getGameStateObserverService().getStateData("nextPlanet");
        ServiceLocator.getGameStateObserverService().trigger("updatePlanet", "currentPlanet", nextPlanet);
        if (!nextPlanet.equals("none")) {
            //TODO: MAKE SHOW WIN SCREEN?
            PlanetScreen planet = new PlanetScreen(game, nextPlanet);
            ServiceLocator.getGameStateObserverService().trigger("updatePlanet", "nextPlanet", planet.getNextPlanetName());
            game.setScreen(planet);
        } else {
            game.setScreen(GdxGame.ScreenType.MAIN_MENU);
            logger.info("FINISHED GAME! - Returning to main menu");
        }
    }

    /**
     * Travel back to the currently loaded planet.
     */
    public void returnToCurrent() {
        String currentPlanet = (String) ServiceLocator.getGameStateObserverService().getStateData("currentPlanet");
        game.setScreen(new PlanetScreen(game, currentPlanet));
        ServiceLocator.getEntityService().getPlayer().getComponent(CombatStatsComponent.class).setHealth(100);
        ServiceLocator.getEntityService().getCompanion().getComponent(CombatStatsComponent.class).setHealth(50);
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

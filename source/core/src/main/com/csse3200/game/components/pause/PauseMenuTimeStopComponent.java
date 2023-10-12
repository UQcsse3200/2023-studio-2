package com.csse3200.game.components.pause;

import com.csse3200.game.components.Component;
import com.csse3200.game.entities.Entity;
import com.badlogic.gdx.utils.Array;
import com.csse3200.game.services.ServiceLocator;

/**
 * Handles the pausing/resuming of time when the pause menu is brought up/put away.
 */
public class PauseMenuTimeStopComponent extends Component {

    private Array<Entity> freezeList;
    public PauseMenuTimeStopComponent() {
    }

    /**
     * Handles the pausing of the game entities when the pause menu is made.
     */
    @Override
    public void create() {
        freezeList = ServiceLocator.getEntityService().getEntities();
        for (Entity pauseTarget : freezeList) {
            if (pauseTarget.getId() != getEntity().getId()) {
                // ZA WARUDO! TOKI WO TOMARE!
                pauseTarget.setEnabled(false);
            }
        }
    }

    /**
     * Handles the un-pausing of the game entities when the pause menu is closed.
     * Also notifies the pause menu factory that the pause menu is being disposed.
     */
    @Override
    public void dispose() {
        for (Entity pauseTarget : freezeList) {
            pauseTarget.setEnabled(true);
        }
    }
}
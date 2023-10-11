
/**
 * Component used to store information related to combat such as health, attack, etc. Any entities
 * which engage in combat should have an instance of this class registered. This class can be
 * extended for more specific combat needs.
 */
package com.csse3200.game.components;

import com.badlogic.gdx.utils.Timer.Task;
import com.badlogic.gdx.utils.Timer;

import com.csse3200.game.areas.mapConfig.GameAreaConfig;
import com.csse3200.game.components.maingame.MainGameActions;
import com.csse3200.game.components.player.KeyboardPlayerInputComponent;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//import java.util.Timer;
//import java.util.TimerTask;

/**
 * Component used to store information related to combat such as health, attack, etc. Any entities
 * which engage in combat should have an instance of this class registered. This class can be
 * extended for more specific combat needs.
 */
public class EnvironmentStatsComponent extends Component {

    private static final Logger logger = LoggerFactory.getLogger(EnvironmentStatsComponent.class);
    private Boolean isImmune = false;

    private int frozenLevel;

    public EnvironmentStatsComponent() {
        this.frozenLevel = 0;
    }

    public int getFrozenLevel() {
        return this.frozenLevel;
    }


    public void setFrozenLevel(int newFrozenLevel) {
        this.frozenLevel = newFrozenLevel;
    }

    public Boolean getImmunity() {
        return isImmune;
    }

    /**
     * Sets the entity's immunity status.
     *
     * 
     */
    public void setImmunity(GameAreaConfig mapConfig) {
        //System.out.println(mapConfig.mapName);
        this.isImmune = !mapConfig.mapName.equals("Earth");
        
    }

    // Damage over time effect, has a five second delay. Repeats every second.
    // Effect checks whether or not you are in an applicable area
    public void damage(CombatStatsComponent player) {

        if (!getImmunity()) {
            Timer timer = new Timer();
            timer.scheduleTask(new Timer.Task() {
                @Override
                public void run() {
                    player.addHealth(-1);
                    if (player.getHealth() <= 0) {
                        timer.stop();
                    }
                }
            },1,1);
        }
    }
}

/**
 * Component used to store information related to combat such as health, attack, etc. Any entities
 * which engage in combat should have an instance of this class registered. This class can be
 * extended for more specific combat needs.
 */
package com.csse3200.game.components;

import com.csse3200.game.components.maingame.MainGameActions;
import com.csse3200.game.components.player.KeyboardPlayerInputComponent;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Component used to store information related to combat such as health, attack, etc. Any entities
 * which engage in combat should have an instance of this class registered. This class can be
 * extended for more specific combat needs.
 */
public class EnvironmentStatsComponent extends Component {

    private static final Logger logger = LoggerFactory.getLogger(EnvironmentStatsComponent.class);
    private Boolean isImmune;

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
     * @param isImmune true to make the entity immune to attacks, false otherwise.
     */
    public void setImmunity(boolean isImmune) {
        this.isImmune = isImmune;
    }

    public void damage(CombatStatsComponent player) {
        if (getImmunity()) {
            return;
        }
        int newHealth;
        newHealth = player.getHealth() - 10;
        player.setHealth(newHealth);
    }
}
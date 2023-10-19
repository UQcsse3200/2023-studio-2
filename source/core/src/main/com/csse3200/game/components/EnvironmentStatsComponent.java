package com.csse3200.game.components;

import com.badlogic.gdx.utils.Timer;
import com.csse3200.game.areas.MapGameArea;
import com.csse3200.game.areas.map_config.GameAreaConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Component used to store information related to combat such as health, attack, etc. Any entities
 * which engage in combat should have an instance of this class registered. This class can be
 * extended for more specific combat needs.
 */
public class EnvironmentStatsComponent extends Component {

    private static final Logger logger = LoggerFactory.getLogger(EnvironmentStatsComponent.class);
    private Boolean isImmune = false;

    private boolean isSafeMap = false;
    private boolean freezeMap = false;

    private int frozenLevel;
    private boolean burning;

    public EnvironmentStatsComponent() {
        this.frozenLevel = 0;
        this.burning = false;
    }

    public int getFrozenLevel() {
        return this.frozenLevel;
    }


    public void setFrozenLevel(int newFrozenLevel) {
        this.frozenLevel = newFrozenLevel;
    }

    public boolean getBurning() {
        return this.burning;
    }

    public void setBurning(boolean newBurnState) {
        this.burning = newBurnState;
    }

    public Boolean getIsSafe() {
        return this.isSafeMap;
    }

    public Boolean getImmunity() {
        return isImmune;
    }

    public Boolean getIsFreeze() {
        return this.freezeMap;
    }


    public void setSafeMap(GameAreaConfig mapConfig) {
        logger.info(mapConfig.mapName);
        this.isSafeMap = (mapConfig.mapName.equals("Earth") || mapConfig.mapName.equals("Flora Haven"));
        this.freezeMap = mapConfig.mapName.equals("Cryoheim");
    }

    /**
     * Sets the entity's immunity status.
     */
    public void setIsImmune() {
        this.isImmune = true;
    }

    /**
     * Resets the entity's immunity status.
     */
    public void setNotImmune() {
        this.isImmune = false;
    }

    /** Damages over time effect, has a five-second delay. Repeats every second.
     *  Effect checks whether you are in an applicable area
     */
    public void damage(CombatStatsComponent player) {
        if (this.isSafeMap) {
            return;
        }
        Timer timer = new Timer();
        timer.scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                MapGameArea.isBurning();
                if (burning) {
                    player.addHealth(-2);
                    if (player.getHealth() <= 0) {
                        timer.stop();
                    }
                }
                if (getImmunity()) {
                    if (getIsFreeze()) {
                        if (getFrozenLevel() > 4) {
                            setFrozenLevel(getFrozenLevel() - 4);
                        } else {
                            setFrozenLevel(0);
                        }
                    }
                    return;
                }
                if (getIsFreeze()) {
                    if (getFrozenLevel() < 50) {
                        setFrozenLevel(getFrozenLevel() + 2);
                    } else {
                        setFrozenLevel(50);
                    }
                }
                player.addHealth(-1);
                if (player.getHealth() <= 0) {
                    timer.stop();
                }
            }
        },1,1);
    }
}
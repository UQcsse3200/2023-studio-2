package com.csse3200.game.components;

import com.csse3200.game.entities.Entity;


/**
 * Represents a power-up component within the game. This will be extended
 * further to specify the type of power-up or its effects.
 */
public class PowerupComponent extends Component {

    private PowerupType type;
    private double modifier;

    /**
     * Constructor for the PowerupComponent.
     */
    public PowerupComponent(PowerupType type, double modifier) {
        this.type = type;
        this.modifier = modifier;
    }

    // applies type effect
    public void applyEffect(Entity target) {
        CombatStatsComponent playerStats = target.getComponent(CombatStatsComponent.class);
        switch (type) {
            case HEALTH_BOOST:
                playerStats.addHealth((int) modifier);
                break;
        }
    }
    
    public PowerupType getType() {
        return type;
    }
    
    public double getModifier() {
        return modifier;
    }
    
    public void setType(PowerupType type) {
        this.type = type;
    }
    
    public void setModifier(double modifier) {
        this.modifier = modifier;
    }
}


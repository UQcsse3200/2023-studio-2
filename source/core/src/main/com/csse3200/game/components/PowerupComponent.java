package com.csse3200.game.components;

import com.csse3200.game.entities.factories.PlayerFactory;

/**
 * Represents a power-up component within the game. This will be extended
 * further to specify the type of power-up or its effects.
 */
public class PowerupComponent extends Component {

    private PowerupType type;
    private float modifier;

    /**
     * Constructor for the PowerupComponent.
     */
    public PowerupComponent(PowerupType type, float modifier) {
        this.type = type;
        this.modifier = modifier;
    }

    // TBD - playerstats required
    // public void applyEffect(Player player) {
    //      System.out.println("Applying effect");
    // }
    
    public PowerupType getType() {
        return type;
    }
    
    public float getModifier() {
        return modifier;
    }
    
    public void setType(PowerupType type) {
        this.type = type;
    }
    
    public void setModifier(float modifier) {
        this.modifier = modifier;
    }
}


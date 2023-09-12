package com.csse3200.game.components;

import com.csse3200.game.entities.Entity;

import java.util.Objects;


/**
 * Represents a power-up component within the game.
 */
public class PotionComponent extends Component {

    private PotionType type;
    private long duration;

    /**
     * Assigns a type and targetLayer value to a given Potion
     */
    public PotionComponent(PotionType type) {
        this.type = type;
    }

    /**
     * Overrides the Component create() function
     */
    @Override
    public void create() {
    }

    /**
     * Applies the effects of the Potion to the specified target entity.
     *
     * @param target The entity receiving the Potion effect.
     */
    public void applyEffect(Entity target) {

        if (Objects.requireNonNull(type) == PotionType.DEATH_POTION) {
        } else {
            throw new IllegalArgumentException("You must specify a valid PotionType");
        }
    }

    /**
     * Retrieves the duration for which the Potion effect should last.
     *
     * @return Duration in milliseconds.
     */
    public long getDuration() {
        return duration;
    }

    /**
     * Sets the duration for which the Potion effect should last.
     *
     * @param duration Duration in milliseconds.
     */
    public void setDuration(long duration) {
        this.duration = duration;
    }

    /**
     * Retrieves the type of the Potion.
     *
     * @return The current Potion type.
     */
    public PotionType getType() {
        return type;
    }

    /**
     * Sets the type of the Potion.
     *
     * @param type The type to set.
     */
    public void setType(PotionType type) {
        this.type = type;
    }
}
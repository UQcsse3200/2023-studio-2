package com.csse3200.game.entities.configs;

/**
 * Defines the properties stored in Companion config files to be loaded by the Companion Factory.
 * Extends {@link BaseEntityConfig} to inherit common entity configuration properties.
 */
public class CompanionConfig extends HealthEntityConfig {

    public int speed;
    public int health;
    public int baseAttack;
    public int attackMultiplier;
    public boolean isImmune;

    public CompanionConfig(){this.spritePath="images/companion/Companion_spritesheet.atlas";}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
       CompanionConfig that = (CompanionConfig) o;
        if (speed != that.speed) return false;
        if (health != that.health) return false;
        if (baseAttack != that.baseAttack) return false;
        if (attackMultiplier != that.attackMultiplier) return false;
        return isImmune == that.isImmune;
    }
    @Override
    public int hashCode() {
        int result = speed;
        result = 31 * result + health;
        result = 31 * result + baseAttack;
        result = 31 * result + attackMultiplier;
        result = 31 * result + (isImmune ? 1 : 0);
        return result;
    }
}

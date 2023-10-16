package com.csse3200.game.entities.configs;

public class HealthEntityConfig extends BaseEntityConfig {
    public int health;
    public int maxHealth;
    public int baseAttack;
    public int attackMultiplier = 1;
    public boolean isImmune = false;



    public HealthEntityConfig() {

    }

    public HealthEntityConfig(HealthEntityConfig config) {
        super(config);

        health = config.health;
        maxHealth = config.maxHealth;
        baseAttack = config.baseAttack;
        attackMultiplier = config.attackMultiplier;
        isImmune = config.isImmune;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HealthEntityConfig that = (HealthEntityConfig) o;

        if (health != that.health) return false;
        if (maxHealth != that.maxHealth) return false;
        if (baseAttack != that.baseAttack) return false;
        if (attackMultiplier != that.attackMultiplier) return false;
        return isImmune == that.isImmune;
    }

    @Override
    public int hashCode() {
        int result = health;
        result = 31 * result + maxHealth;
        result = 31 * result + baseAttack;
        result = 31 * result + attackMultiplier;
        result = 31 * result + (isImmune ? 1 : 0);
        return result;
    }
}

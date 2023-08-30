package com.csse3200.game.components;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Component used to store information related to combat such as health, attack, etc. Any entities
 * which engage it combat should have an instance of this class registered. This class can be
 * extended for more specific combat needs.
 */
public class CombatStatsComponent extends Component {

  private static final Logger logger = LoggerFactory.getLogger(CombatStatsComponent.class);
  private int health;
  private int baseAttack;
  private int maxHealth;
  private int attackMultiplier;
  private Boolean isImmune;

  public CombatStatsComponent(int health, int baseAttack, int attackMultiplier, boolean isImmune) {
    this.maxHealth = health;
    this.setHealth(health);
    this.setBaseAttack(baseAttack);
    this.setAttackMultiplier(attackMultiplier);
    this.setImmunity(isImmune);
  }

  /**
   * Returns true if the entity's has 0 health, otherwise false.
   *
   * @return is player dead
   */
  public Boolean isDead() {
    return this.health == 0;
  }

  /**
   * Returns the entity's health.
   *
   * @return entity's health
   */
  public int getHealth() {
    return this.health;
  }

  /**
   * Returns the entity's maximum health.
   *
   * @return entity's maximum health
   */
  public int getMaxHealth() {
    return this.maxHealth;
  }

  /**
   * Sets the entity's health. Health has a minimum bound of 0.
   *
   * @param health health
   */
  public void setHealth(int health) {
    if (health >= 0) {
      this.health = health;
    } else {
      this.health = 0;
    }
    if (entity != null) {
      entity.getEvents().trigger("updateHealth", this.health);
    }
  }

  /**
   * Adds to the player's health. The amount added can be negative.
   *
   * @param health health to add
   */
  public void addHealth(int health) {
    setHealth(this.health + health);
  }

  /**
   * Returns the entity's base attack damage.
   *
   * @return base attack damage
   */
  public int getBaseAttack() {
    return baseAttack;
  }

  /**
   * Sets the entity's attack damage. Attack damage has a minimum bound of 0.
   *
   * @param attack Attack damage
   */
  public void setBaseAttack(int attack) {
    if (attack >= 0) {
      this.baseAttack = attack;
    } else {
      logger.error("Can not set base attack to a negative attack value");
    }
  }

  /**
   * Returns the entity's attack multiplier.
   *
   * @return attack multiplier
   */
  public int getAttackMultiplier() {
    return attackMultiplier;
  }

  /**
   * Sets the entity's attack multiplier. Attack multiplier has a minimum bound of 0.
   *
   * @param attackMultiplier attack multiplier
   */
  public void setAttackMultiplier(int attackMultiplier) {
    if (attackMultiplier >= 0) {
      this.attackMultiplier = attackMultiplier;
    } else {
      logger.error("Can not set base attack multiplier to a negative attack value");
    }
  }

  /**
   * Returns the entity's immunity status.
   *
   * @return immunity status
   */
  public Boolean getImmunity() {
    return isImmune;
  }

  /**
   * Sets the entity's immunity status.
   *
   * @param isImmune immunity status
   */
  public void setImmunity(boolean isImmune) {
    this.isImmune = isImmune;
  }

  /**
   * Inverts the entity's current immunity status.
   */
  public void changeImmunityStatus() {
    this.isImmune = !this.getImmunity();
  }

  /**
   * Returns the entity's attack damage. This is the base damage scaled by
   *  the entity's attack multiplier.
   *
   * @return attack damage
   */
  public int getAttack() {
    return getBaseAttack() * getAttackMultiplier();
  }

  public void hit(CombatStatsComponent attacker) {
    if (getImmunity()) {
      return;
    }
    int newHealth = getHealth() - attacker.getAttack();
    setHealth(newHealth);
  }
}

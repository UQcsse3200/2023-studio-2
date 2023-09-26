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
public class CombatStatsComponent extends Component {

  private static final Logger logger = LoggerFactory.getLogger(CombatStatsComponent.class);
  private int health;
  private int baseAttack;
  private final int maxHealth;
  private int attackMultiplier;
  private Boolean isImmune;
  private int lives;


  /**
   * Initializes a CombatStatsComponent with specified attributes.
   *
   * @param health           The initial health of the entity.
   * @param baseAttack       The base attack damage of the entity.
   * @param attackMultiplier The attack multiplier of the entity.
   * @param isImmune         A flag indicating whether the entity is immune to attacks.
   */
  public CombatStatsComponent(int health, int baseAttack, int attackMultiplier, boolean isImmune) {
    this.maxHealth = health;
    this.setHealth(health);
    this.setBaseAttack(baseAttack);
    this.setAttackMultiplier(attackMultiplier);
    this.setImmunity(isImmune);
  }

  /**
   * Returns true if the entity's health is 0 or less, indicating that it's dead; otherwise, returns false.
   *
   * @return true if the entity is dead, false otherwise.
   */
  public Boolean isDead() {
    return this.health <= 0;
  }

  /**
   * Returns the entity's current health.
   *
   * @return The entity's health.
   */
  public int getHealth() {
    return this.health;
  }

  /**
   * Returns the entity's maximum health.
   *
   * @return The entity's maximum health.
   */
  public int getMaxHealth() {
    return this.maxHealth;
  }

  /**
   * Sets the entity's health. Health cannot be less than 0. If health reaches 0, a death event is triggered after a delay.
   *
   * @param health The new health value.
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
    if (entity != null) {
      if (isDead() && entity.getEntityType().equals("player")) {
        entity.getComponent(KeyboardPlayerInputComponent.class).playerDead(); // Stop player from walking
        final Timer timer = new Timer();
        entity.getEvents().trigger("playerDeath"); // Trigger death animation
        TimerTask killPlayer = new TimerTask() {
          @Override
          public void run() {
            entity.getComponent(CombatStatsComponent.class).setImmunity(true); // Prevent dying before respawn
            entity.getEvents().trigger("death");
            timer.cancel();
            timer.purge();
          }
        };
        timer.schedule(killPlayer, 500);
      } else if (isDead() && entity.getEntityType().equals("companion")) {
          final Timer timer1 = new Timer();
          TimerTask killCompanion = new TimerTask() {
            @Override
            public void run() {
              entity.getEvents().trigger("death");
              timer1.cancel();
              timer1.purge();
            }
          };
          timer1.schedule(killCompanion,500);

      } else if (isDead() && entity.getEntityType().equals("playerWeapon")) {
        entity.getEvents().trigger("death", 0);
      }
    }
  }



  /**
   * Adds to the player's health. The amount added can be negative.
   *
   * @param health The health to add.
   */
  public void addHealth(int health) {
    setHealth(this.health + health);
  }

  /**
   * Returns the entity's base attack damage.
   *
   * @return The entity's base attack damage.
   */
  public int getBaseAttack() {
    return baseAttack;
  }

  /**
   * Sets the entity's base attack damage. Attack damage cannot be less than 0.
   *
   * @param attack The new base attack damage value.
   */
  public void setBaseAttack(int attack) {
    if (attack >= 0) {
      this.baseAttack = attack;
    } else {
      logger.error("Cannot set base attack to a negative attack value");
    }
  }

  /**
   * Returns the entity's attack multiplier.
   *
   * @return The entity's attack multiplier.
   */
  public int getAttackMultiplier() {
    return attackMultiplier;
  }

  /**
   * Sets the entity's attack multiplier. Attack multiplier cannot be less than 0.
   *
   * @param attackMultiplier The new attack multiplier value.
   */
  public void setAttackMultiplier(int attackMultiplier) {
    if (attackMultiplier >= 0) {
      this.attackMultiplier = attackMultiplier;
    } else {
      logger.error("Cannot set base attack multiplier to a negative attack value");
    }
  }

  /**
   * Returns the entity's immunity status.
   *
   * @return true if the entity is immune to attacks, false otherwise.
   */
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

  /**
   * Inverts the entity's current immunity status.
   */
  public void changeImmunityStatus() {
    this.isImmune = !this.getImmunity();
  }

  /**
   * Returns the entity's total attack damage, which is the base attack damage multiplied by the attack multiplier.
   *
   * @return The entity's total attack damage.
   */
  public int getAttack() {
    return getBaseAttack() * getAttackMultiplier();
  }

  /**
   * Sets the number of lives player has left.
   * @param lives
   */
  public void setLives(int lives) {
    this.lives = lives;
  }

  /**
   * Subtracts one life from player lives.
   */
  public void minusLife() {
    this.lives -= 1;
  }
  /**
   * Adds one life to player lives.
   */
  public void addLife() {
    this.lives += 1;
  }
  /**
   * returns number of lives player has left.
   * @return number of lives
   */
  public int getLives() {
    return this.lives;
  }

  /**
   * Inflicts damage on the entity by reducing its health. If the entity is immune, no damage is applied.
   *
   * @param attacker The entity causing the damage.
   */
  public void hit(CombatStatsComponent attacker) {
    if (getImmunity()) {
      return;
    }
    int newHealth;
    newHealth = getHealth() - attacker.getAttack();
    setHealth(newHealth);
  }
}

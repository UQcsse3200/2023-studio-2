package com.csse3200.game.components;

import com.csse3200.game.extensions.GameExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ExtendWith(GameExtension.class)
class CombatStatsComponentTest {
  @Test
  void shouldSetGetHealth() {
    CombatStatsComponent combat = new CombatStatsComponent(100, 20, 1, false);
    assertEquals(100, combat.getHealth());

    combat.setHealth(150);
    assertEquals(150, combat.getHealth());

    combat.setHealth(-50);
    assertEquals(0, combat.getHealth());
  }

  @Test
  void shouldCheckIsDead() {
    CombatStatsComponent combat = new CombatStatsComponent(100, 20, 1, false);
    assertFalse(combat.isDead());

    combat.setHealth(0);
    assertTrue(combat.isDead());
  }

  @Test
  void shouldAddHealth() {
    CombatStatsComponent combat = new CombatStatsComponent(100, 20, 1, false);
    combat.addHealth(-500);
    assertEquals(0, combat.getHealth());

    combat.addHealth(100);
    combat.addHealth(-20);
    assertEquals(80, combat.getHealth());
  }

  @Test
  void shouldSetGetBaseAttack() {
    CombatStatsComponent combat = new CombatStatsComponent(100, 20, 1, false);
    assertEquals(20, combat.getBaseAttack());

    combat.setBaseAttack(150);
    assertEquals(150, combat.getBaseAttack());

    combat.setBaseAttack(-50);
    assertEquals(150, combat.getBaseAttack());
  }

  @Test
  void shouldSetGetAttackMultiplier() {
    CombatStatsComponent combat = new CombatStatsComponent(100, 20, 1, false);
    assertEquals(1, combat.getAttackMultiplier());

    combat.setAttackMultiplier(2);
    assertEquals(2, combat.getAttackMultiplier());

    combat.setAttackMultiplier(-1);
    assertEquals(2, combat.getAttackMultiplier());
  }

  @Test
  void shouldSetGetImmunity() {
    CombatStatsComponent combat = new CombatStatsComponent(100, 20, 1, false);
    assertFalse(combat.getImmunity());

    combat.setImmunity(true);
    assertTrue(combat.getImmunity());

    combat.setImmunity(false);
    assertFalse(combat.getImmunity());
  }

  @Test
  void shouldChangeImmunityStatus() {
    CombatStatsComponent combat = new CombatStatsComponent(100, 20, 1, false);
    assertFalse(combat.getImmunity());

    combat.changeImmunityStatus();
    assertTrue(combat.getImmunity());

    combat.changeImmunityStatus();
    assertFalse(combat.getImmunity());
  }
}

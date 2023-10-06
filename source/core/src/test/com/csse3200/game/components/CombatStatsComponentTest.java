package com.csse3200.game.components;

import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.events.EventHandler;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the {@link CombatStatsComponent} class.
 */
@ExtendWith(GameExtension.class)
class CombatStatsComponentTest {

  @Mock
  EventHandler eventHandler;

  RenderService renderService;
  @Mock
  ResourceService resourceService;

  @Mock
  Entity entity;

  @Mock
  EntityService entityService;

  CombatStatsComponent entityStats;

  @BeforeEach
  void setUp() {
    ServiceLocator.registerEntityService(entityService);

    // Set up a non-immune test entity with 100 health, 100 attack damage and no attack multiplier.
    entity = new Entity();
    entity.addComponent(new CombatStatsComponent(100, 100, 1, false));
    entityStats = entity.getComponent(CombatStatsComponent.class);

    MockitoAnnotations.openMocks(this);
    when(entity.getEvents()).thenReturn(eventHandler);
  }

  /**
   * Test setting and getting health values.
   */
  @Test
  void shouldSetGetHealth() {
    // Test set and get, basic tests
    assertEquals(100, entityStats.getHealth()); // default health
    entityStats.setHealth(50);
    assertEquals(50, entityStats.getHealth()); // basic set and get

    // set and get basic edge cases
    entityStats.setHealth(-100);
    assertEquals(0, entityStats.getHealth()); // edge case, set health < 0
    entityStats.setHealth(0);
    assertEquals(0, entityStats.getHealth()); // ensure setting health to zero is valid

    // Test set multiple times, then get health
    entityStats.setHealth(50);
    entityStats.setHealth(33);
    entityStats.setHealth(100);
    assertEquals(100, entityStats.getHealth());
  }

  /**
   * Test checking if the entity is dead.
   */
  @Test
  void shouldCheckIsDead() {
    // Basic test to ensure entities with health are not dead
    assertFalse(entityStats.isDead()); // default; entity is not dead

    // Basic death test
    entityStats.setHealth(0);
    assertTrue(entityStats.isDead()); // basic test; entities health is now zero (dead)

    // Ensure isDead if hit by another entity (with one hit dealing full health damage)
    Entity enemy = new Entity();
    enemy.addComponent(new CombatStatsComponent(100, 100, 1, false));
    CombatStatsComponent enemyStats = enemy.getComponent(CombatStatsComponent.class);

    enemyStats.hit(entityStats);
    assertTrue(entityStats.isDead());
  }

  /**
   * Test adding or subtracting health.
   */
  @Test
  void shouldAddHealth() {
    // Test add health, basic tests
    entityStats.addHealth(100);
    assertEquals(200, entityStats.getHealth());
    entityStats.addHealth(50);
    assertEquals(250, entityStats.getHealth());
    entityStats.addHealth(-250); // remove all health
    assertEquals(0, entityStats.getHealth());

    // Test add health multiple times, then get health
    entityStats.addHealth(50);
    entityStats.addHealth(50);
    entityStats.addHealth(50);
    assertEquals(150, entityStats.getHealth());
  }

  /**
   * Test setting and getting base attack values.
   */
  @Test
  void shouldSetGetBaseAttack() {
    // Basic set and get base attack
    assertEquals(100, entityStats.getBaseAttack()); // defaults
    entityStats.setBaseAttack(150);
    assertEquals(150, entityStats.getBaseAttack()); // basic
    entityStats.setBaseAttack(-150);
    assertEquals(150, entityStats.getBaseAttack()); // edge case: don't change if base attach < 0
  }

  /**
   * Test setting and getting attack multiplier values.
   */
  @Test
  void shouldSetGetAttackMultiplier() {
    // Basic set and get base attack multiplier
    assertEquals(1, entityStats.getAttackMultiplier()); // defaults
    entityStats.setAttackMultiplier(2);
    assertEquals(2, entityStats.getAttackMultiplier()); // basic

    // Edge cases
    entityStats.setAttackMultiplier(-2);
    assertEquals(2, entityStats.getAttackMultiplier()); // edge case: multiplier is < 0
    entityStats.setAttackMultiplier(0);
    assertEquals(0, entityStats.getAttackMultiplier()); // edge case: multiplier is == 0
  }

  /**
   * Test setting and getting immunity status.
   */
  @Test
  void shouldSetGetImmunity() {
    // Basic set and get entity immunity status
    assertFalse(entityStats.getImmunity()); // defaults
    entityStats.setImmunity(true);
    assertTrue(entityStats.getImmunity()); // basic
    entityStats.setImmunity(false); // set immune entity to non-immune
    assertFalse(entityStats.getImmunity());
  }

  /**
   * Test changing immunity status.
   */
  @Test
  void shouldChangeImmunityStatus() {
    // Basic immunity set and get tests
    assertFalse(entityStats.getImmunity()); // default: non immune
    entityStats.changeImmunityStatus();
    assertTrue(entityStats.getImmunity()); // basic immunity swap
    entityStats.changeImmunityStatus();
    assertFalse(entityStats.getImmunity()); // basic immunity swap
  }

  @Test
  void shouldSetGetLives() {
    // basic set get lives
    assertEquals(0, entityStats.getLives()); // default: no lives
    entityStats.setLives(3);
    assertEquals(3, entityStats.getLives()); // basic set get lives

    // edge cases
    entityStats.setLives(0);
    assertEquals(0, entityStats.getLives()); // edge case: lives set to zero
    entityStats.setLives(-1);
    assertEquals(0, entityStats.getLives()); // edge case: lives set below 0
  }

  @Test
  void shouldMinusLife() {
    // basic minus life test - should subtract one life
    entityStats.setLives(3); // default
    entityStats.minusLife(); // 2 lives
    assertEquals(2, entityStats.getLives());
    entityStats.minusLife(); // 1 lives
    assertEquals(1, entityStats.getLives());

    // edge case: subtracting lives when current lives are zero
    entityStats.minusLife(); // 0 lives
    entityStats.minusLife(); // 0 lives minus one
    assertEquals(0, entityStats.getLives()); // edge case: minus a life from zero is still zero
  }

  @Test
  void shouldAddLife() {
    // basic add life
    entityStats.setLives(0); // 0 lives
    entityStats.addLife(); // 1 lives
    assertEquals(1, entityStats.getLives());
    entityStats.addLife();
    assertEquals(2, entityStats.getLives());

    // edge case: cannot add more than 4 lives
    entityStats.addLife(); // 3 lives
    entityStats.addLife(); // 4 lives
    entityStats.addLife(); // 5 lives are not allowed, stay on 4 lives
    assertEquals(4, entityStats.getLives());
  }

  @Test
  void shouldTriggerHealthEvent() {
    entityStats.setEntity(entity);
    entityStats.setHealth(50);
    verify(entity.getEvents()).trigger("updateHealth", 50);
  }

  @Test
  void shouldTriggerUpdateLivesEvent() {
    entityStats.setEntity(entity);
    entityStats.setLives(3);
    verify(entity.getEvents()).trigger(eq("updateLives"), anyInt());
  }

  @Test
  void shouldTriggerMaxLivesEvent() {
    entityStats.setEntity(entity);
    entityStats.setLives(4);
    entityStats.addLife();
    verify(entity.getEvents()).trigger("maxLivesAlert");
  }
}

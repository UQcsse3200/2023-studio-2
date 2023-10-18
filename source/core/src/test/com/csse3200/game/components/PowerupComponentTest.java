package com.csse3200.game.components;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.companion.CompanionActions;
import com.csse3200.game.components.player.PlayerActions;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PowerupComponentTest {

    @Mock
    EntityService entityService;

    @BeforeEach
    void setup() {
        entityService = mock(EntityService.class);
        ServiceLocator.registerEntityService(entityService);

    }

    @Test
    void shouldApplyExtraLife() {
        Entity playerEntity = new Entity().addComponent(new CombatStatsComponent(100, 100, 10, 1, false, 1))
                .addComponent(new PlayerActions());

        when(entityService.getPlayer()).thenReturn(playerEntity);

        PowerupComponent powerupComponent = new PowerupComponent(PowerupType.EXTRA_LIFE);
        powerupComponent.applyEffect();

        // Assert
        assertEquals(2, playerEntity.getComponent(CombatStatsComponent.class).getLives());
    }

    /**
     * Tests the effect of applying an extra life power-up to an entity's combat
     * stats
     * when its number of lives is already at a maximum (4).
     * Ensures that after applying the extra life power-up, the number of entity's
     * lives
     * does not change.
     */
    @Test
    public void testExtraLifeMaxReached() {
        Entity playerEntity = new Entity().addComponent(new CombatStatsComponent(100, 100, 10, 1, false, 4))
                .addComponent(new PlayerActions());

        when(entityService.getPlayer()).thenReturn(playerEntity);

        PowerupComponent powerupComponent = new PowerupComponent(PowerupType.EXTRA_LIFE);
        powerupComponent.applyEffect();

        // Assert
        assertEquals(4, playerEntity.getComponent(CombatStatsComponent.class).getLives());
    }

    @Test
    void shouldApplyHealthBoost() {
        Entity playerEntity = new Entity().addComponent(new CombatStatsComponent(100, 100, 10, 2, false, 3))
                .addComponent(new PlayerActions());

        Entity companionEntity = new Entity().addComponent(new CombatStatsComponent(50, 50, 5, 1, false))
                .addComponent(new CompanionActions());

        when(entityService.getPlayer()).thenReturn(playerEntity);
        when(entityService.getCompanion()).thenReturn(companionEntity);

        PowerupComponent powerupComponent = new PowerupComponent(PowerupType.HEALTH_BOOST);
        powerupComponent.applyEffect();

        // Assert
        assertEquals(100, playerEntity.getComponent(CombatStatsComponent.class).getHealth());
        assertEquals(50, companionEntity.getComponent(CombatStatsComponent.class).getHealth());
    }

    @Test
    void shouldApplySpeedBoost() {
        Entity playerEntity = new Entity().addComponent(new PlayerActions());
        Entity companionEntity = new Entity()
                .addComponent(new CompanionActions())
                .addComponent(new FollowComponent(playerEntity, 5.0f));

        when(entityService.getPlayer()).thenReturn(playerEntity);
        when(entityService.getCompanion()).thenReturn(companionEntity);

        // Mock the isDead method of companion's CombatStatsComponent to return false
        CombatStatsComponent companionCombatStats = mock(CombatStatsComponent.class);
        when(companionCombatStats.isDead()).thenReturn(false);

        // Set the mocked component on the companionEntity directly
        companionEntity.addComponent(companionCombatStats);

        PowerupComponent powerupComponent = new PowerupComponent(PowerupType.SPEED_BOOST);
        powerupComponent.applyEffect();

        Vector2 playerSpeed = new Vector2(6f, 6f);
        Vector2 companionSpeed = new Vector2(7f, 7f);

        // Assert
        assertEquals(playerSpeed, playerEntity.getComponent(PlayerActions.class).getSpeed());
        assertEquals(companionSpeed, companionEntity.getComponent(CompanionActions.class).getSpeed());
        assertEquals(5, companionEntity.getComponent(FollowComponent.class).getFollowSpeed());
    }

    @Test
    void shouldApplyTempImmunity() throws InterruptedException {
        Entity playerEntity = new Entity().addComponent(new PlayerActions())
                .addComponent(new CombatStatsComponent(100, 100, 10, 1, false, 1));
        Entity companionEntity = new Entity().addComponent(new CompanionActions())
                .addComponent(new CombatStatsComponent(100, 100, 10, 1, false, 1));
        when(entityService.getPlayer()).thenReturn(playerEntity);
        when(entityService.getCompanion()).thenReturn(companionEntity);

        PowerupComponent powerupComponent = new PowerupComponent(PowerupType.TEMP_IMMUNITY);
        powerupComponent.applyEffect();

        // Assert
        assertTrue(playerEntity.getComponent(CombatStatsComponent.class).isImmune);
        assertTrue(companionEntity.getComponent(CombatStatsComponent.class).isImmune);

        // Wait for the duration of immunity
        TimeUnit.MILLISECONDS.sleep(7000);

        // Assert after the duration
        assertFalse(playerEntity.getComponent(CombatStatsComponent.class).isImmune);
        assertFalse(companionEntity.getComponent(CombatStatsComponent.class).isImmune);
    }

    @Test
    void shouldApplyDoubleDamage() throws InterruptedException {
        Entity playerEntity = new Entity().addComponent(new PlayerActions())
                .addComponent(new CombatStatsComponent(100, 100, 10, 1, false, 1));
        when(entityService.getPlayer()).thenReturn(playerEntity);

        PowerupComponent powerupComponent = new PowerupComponent(PowerupType.DOUBLE_DAMAGE);
        powerupComponent.applyEffect();

        // Assert
        assertEquals(2, playerEntity.getComponent(CombatStatsComponent.class).getAttackMultiplier());

        // Wait for the duration of double damage
        TimeUnit.MILLISECONDS.sleep(powerupComponent.getDuration() + 10);

        // Assert after the duration
        assertEquals(1, playerEntity.getComponent(CombatStatsComponent.class).getAttackMultiplier());
    }

    @Test
    void shouldApplySnap() throws InterruptedException {

        Entity playerEntity = new Entity().addComponent(new PlayerActions())
                .addComponent(new CombatStatsComponent(100, 100, 10, 1, false, 1));
        when(entityService.getPlayer()).thenReturn(playerEntity);

        List<Entity> enemies = Mockito.mock(List.class);

        Entity enemy1 = Mockito.mock(Entity.class);
        Entity enemy2 = Mockito.mock(Entity.class);

        Mockito.when(enemies.size()).thenReturn(2);
        Mockito.when(enemies.get(0)).thenReturn(enemy1);
        Mockito.when(enemies.get(1)).thenReturn(enemy2);

        PowerupComponent powerupComponent = new PowerupComponent(PowerupType.SNAP);
        powerupComponent.applyEffect();

        Mockito.verify(enemies, Mockito.times(0)).remove(Mockito.any(Entity.class));
    }

    /**
     * Tests the retrieval of the type of power-up component.
     * Ensures that the returned power-up type matches the type with which the
     * component was initialized.
     */
    @Test
    public void testGetType() {
        // Initialise Health and Speed Powerups
        PowerupComponent healthpowerup = new PowerupComponent(PowerupType.HEALTH_BOOST);
        PowerupComponent speedpowerup = new PowerupComponent(PowerupType.SPEED_BOOST);
        // Test Health PowerupType
        assertNotNull(healthpowerup);
        assertEquals(healthpowerup.getType(), PowerupType.HEALTH_BOOST);
        assertNotSame(healthpowerup.getType(), PowerupType.SPEED_BOOST);
        // Test Speed PowerupType
        assertNotNull(speedpowerup);
        assertEquals(speedpowerup.getType(), PowerupType.SPEED_BOOST);
        assertNotSame(speedpowerup.getType(), PowerupType.HEALTH_BOOST);
        // Test Speed and Health Powerup types
        assertNotSame(speedpowerup.getType(), healthpowerup.getType());
    }

    /**
     * Tests the ability to change the type of a power-up component.
     * Ensures that after setting a new type, the returned type matches the newly
     * set type.
     */
    @Test
    public void testSetType() {
        PowerupComponent healthpowerup = new PowerupComponent(PowerupType.HEALTH_BOOST);
        PowerupComponent speedpowerup = new PowerupComponent(PowerupType.SPEED_BOOST);
        // Test setting health powerup
        healthpowerup.setType(PowerupType.SPEED_BOOST);
        assertEquals(healthpowerup.getType(), PowerupType.SPEED_BOOST);
        // Test setting speed powerup
        speedpowerup.setType(PowerupType.HEALTH_BOOST);
        assertEquals(speedpowerup.getType(), PowerupType.HEALTH_BOOST);
        // Test setting both back
        speedpowerup.setType(PowerupType.SPEED_BOOST);
        healthpowerup.setType(PowerupType.HEALTH_BOOST);
        assertEquals(speedpowerup.getType(), PowerupType.SPEED_BOOST);
        assertEquals(healthpowerup.getType(), PowerupType.HEALTH_BOOST);
    }

    /**
     * Tests the ability to set the duration for which the power-up effect lasts.
     * Ensures that after setting a duration, the returned duration matches the set
     * value.
     */
    @Test
    public void testSetDuration() {
        PowerupComponent powerup = new PowerupComponent(PowerupType.HEALTH_BOOST);
        long testDuration1 = 100;
        long testDuration2 = 1000;
        // Test setting the first duration
        powerup.setDuration(testDuration1);
        assertEquals(testDuration1, powerup.getDuration());
        // Test setting the second duration
        powerup.setDuration(testDuration2);
        assertEquals(testDuration2, powerup.getDuration());
    }

    /**
     * Tests the retrieval of the duration for which the power-up effect lasts.
     * Ensures that the returned duration matches the set value.
     */
    @Test
    public void testGetDuration() {
        PowerupComponent healthpowerup = new PowerupComponent(PowerupType.HEALTH_BOOST);
        healthpowerup.setDuration(100);
        long initialDuration = healthpowerup.getDuration();
        assertTrue(initialDuration > 0);
        long newDuration = 200;
        healthpowerup.setDuration(newDuration);
        assertEquals(newDuration, healthpowerup.getDuration());
    }

}

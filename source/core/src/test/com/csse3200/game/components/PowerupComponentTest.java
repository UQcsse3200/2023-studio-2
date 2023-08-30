package com.csse3200.game.components;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.csse3200.game.components.player.PlayerActions;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.physics.PhysicsLayer;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static junit.framework.TestCase.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the {@code PowerupComponent} class, validating the behavior of various power-up effects,
 * as well as the get and set operations for the power-up type and duration.
 * <p>
 * This class mocks essential services and components to ensure that the tested methods of {@code PowerupComponent}
 * work as expected in different scenarios. The test suite covers power-ups like health boost and speed boost, and
 * examines the interactions of these power-ups with entities, such as players, within the game environment.
 * </p>
 * <p>
 * The tests are designed to run in isolation with mock dependencies to ensure accurate results without side effects.
 * </p>
 */
@ExtendWith(MockitoExtension.class)
@ExtendWith(GameExtension.class)
public class PowerupComponentTest {

    /**
     * Tests the effect of applying a health power-up to an entity's combat stats.
     * Ensures that after applying the health power-up, the entity's health is set to full (100).
     */
    @Test
    public void testApplyHealthEffect() {
        // Initialise player
        Gdx.app = mock(Application.class);
        PowerupComponent healthpowerup = new PowerupComponent(PowerupType.HEALTH_BOOST, PhysicsLayer.PLAYER);
        Entity player = mock(Entity.class);
        CombatStatsComponent playerStats = mock(CombatStatsComponent.class);

        // Test Health Powerup
        when(player.getComponent(CombatStatsComponent.class)).thenReturn(playerStats);
        healthpowerup.applyEffect(player);
        verify(playerStats).setHealth(100);
    }

    /**
     * Tests the effect of applying a speed power-up to an entity's player actions.
     * Ensures that after applying the speed power-up, the entity's speed is set to (5, 5).
     */
    @Test
    public void testApplySpeedEffect() {
        // Initialise player
        Gdx.app = mock(Application.class);
        PowerupComponent speedpowerup = new PowerupComponent(PowerupType.SPEED_BOOST, PhysicsLayer.PLAYER);
        Entity player = mock(Entity.class);
        PlayerActions playerActions = mock(PlayerActions.class);

        // Test Speed Powerup
        when(player.getComponent(PlayerActions.class)).thenReturn(playerActions);
        speedpowerup.applyEffect(player);
        verify(playerActions).setSpeed(5, 5);
    }

    /**
     * Tests the retrieval of the type of power-up component.
     * Ensures that the returned power-up type matches the type with which the component was initialized.
     */
    @Test
    public void testGetType() {
        // Initialise Health and Speed Powerups
        PowerupComponent healthpowerup = new PowerupComponent(PowerupType.HEALTH_BOOST, PhysicsLayer.PLAYER);
        PowerupComponent speedpowerup = new PowerupComponent(PowerupType.SPEED_BOOST, PhysicsLayer.NONE);

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
     * Ensures that after setting a new type, the returned type matches the newly set type.
     */
    @Test
    public void testSetType() {
        PowerupComponent healthpowerup = new PowerupComponent(PowerupType.HEALTH_BOOST, PhysicsLayer.ALL);
        PowerupComponent speedpowerup = new PowerupComponent(PowerupType.SPEED_BOOST, PhysicsLayer.ALL);

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
     * Ensures that after setting a duration, the returned duration matches the set value.
     */
    @Test
    public void testSetDuration() {
        PowerupComponent powerup = new PowerupComponent(PowerupType.HEALTH_BOOST, PhysicsLayer.ALL);
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
        PowerupComponent healthpowerup = new PowerupComponent(PowerupType.HEALTH_BOOST, PhysicsLayer.ALL);
        healthpowerup.setDuration(100);
        long initialDuration = healthpowerup.getDuration();

        assertTrue(initialDuration > 0);

        long newDuration = 200;
        healthpowerup.setDuration(newDuration);
        assertEquals(newDuration, healthpowerup.getDuration());
    }
}
package com.csse3200.game.components.Companion;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.events.listeners.EventListener0;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the {@link CompanionActions} class.
 */
class CompanionActionsTest {

    private CompanionActions companionActions;
    private PhysicsComponent physicsComponent;
    private Entity playerEntity;
    private ResourceService resourceService;

    @Before
    public void setUp() {
        // Initialize the physics component and player entity
        physicsComponent = Mockito.mock(PhysicsComponent.class);
        playerEntity = Mockito.mock(Entity.class);

        // Create a mock ResourceService to simulate the game's resource service
        resourceService = Mockito.mock(ResourceService.class);

        // Create the CompanionActions component
        companionActions = new CompanionActions();
        companionActions.entity = new Entity();
        companionActions.entity.addComponent(physicsComponent);
        companionActions.setPlayerEntity(playerEntity);
        companionActions.create();
        companionActions.setBulletTexturePath("path_to_bullet_texture");

        // Register the mock ResourceService with ServiceLocator
        ServiceLocator.registerResourceService(resourceService);
    }

    /**
     * Test the create method of CompanionActions.
     */
    @Test
    public void testCreate()
    {
        verify(companionActions.entity.getEvents()).trigger(eq("displayMessage"), anyString());

        // Verify the initial rotation calculation
        verify(physicsComponent.getBody()).getAngle();
    }



    /**
     * Test the walk method of CompanionActions.
     */
    @Test
    public void testMoving() {
        Vector2 direction = new Vector2(1f, 0f);
        companionActions.move(direction);
        assertEquals(direction, companionActions.movingDirection);
        assertEquals(true, companionActions.moving);
    }

    /**
     * Test the stopWalking method of CompanionActions.
     */
    @Test
    public void testStopMoving() {
        companionActions.stopMoving();
        assertEquals(Vector2.Zero, companionActions.movingDirection);
        assertEquals(false, companionActions.moving);
    }

    /**
     * Test the attack method of CompanionActions.
     */
    @Test
    public void testAttack() {
        // Mock the resource service to simulate loading a sound
        Sound sound = Mockito.mock(Sound.class);
        when(resourceService.getAsset(eq("path_to_bullet_texture"), eq(Sound.class))).thenReturn(sound);

        // Call the attack method
        companionActions.attack();

        // Verify that the sound is played
        verify(sound).play();
    }

    // Helper method to simulate movement keys being pressed
    private void simulateMovementKeysPressed() {
        when(Gdx.input.isKeyPressed(Input.Keys.I)).thenReturn(true);
        when(Gdx.input.isKeyPressed(Input.Keys.J)).thenReturn(true);
        when(Gdx.input.isKeyPressed(Input.Keys.K)).thenReturn(true);
        when(Gdx.input.isKeyPressed(Input.Keys.L)).thenReturn(true);
    }
}

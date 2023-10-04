package com.csse3200.game.entities;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(GameExtension.class)
public class PortalTest {

    private Portal portal;
    private Entity player;

    @BeforeEach
    public void setUp() {
        // Create a mock companion entity
        Entity companion = new Entity();
        companion.setPosition(0, 0);

        // Create a player entity
        player = new Entity();
        player.setPosition(0, 0);
        EntityService entityService = mock(EntityService.class);
        // Mock the ServiceLocator to return the mock companion
        ServiceLocator.registerEntityService(entityService);
        when(ServiceLocator.getEntityService().getCompanion()).thenReturn(companion);

        // Create a portal with the player as a parameter
        portal = new Portal(player);
    }

    @Test
    public void testCreation() {
        assertNotNull(portal);
    }

    @Test
    public void testSetPosition() {
        assertNotNull(portal);
        portal.setPosition(10, 10);
        portal.teleport(player);

        Vector2 actual = new Vector2(10, 10);

        assertEquals(player.getPosition(), actual);
    }

    @Test
    public void testTeleportWithNullEntity() {
        Portal portal = new Portal(null);
        // Ensure that teleporting with a null entity doesn't throw an exception
        assertDoesNotThrow(() -> portal.teleport(player));
    }

    @Test
    public void testTeleportToSamePosition() {
        Vector2 initialPosition = new Vector2(player.getPosition());
        portal.teleport(player);
        // Ensure that teleporting to the same position doesn't change the player's position
        assertEquals(initialPosition, player.getPosition());
    }

    @Test
    public void testTeleportWithDifferentCompanionPosition() {
        Entity companion = ServiceLocator.getEntityService().getCompanion();
        companion.setPosition(10, 10);

        portal.teleport(player);

        Vector2 expected = new Vector2(0, 0);

        assertEquals(player.getPosition(), expected);
    }

    @Test
    public void testTeleportWithDifferentPortalPosition() {
        portal.setPosition(20, 20);

        portal.teleport(player);

        Vector2 expected = new Vector2(20, 20);

        assertEquals(player.getPosition(), expected);
    }

    @Test
    public void testTeleportWithCompanionOffset() {
        Entity companion = ServiceLocator.getEntityService().getCompanion();
        companion.setPosition(5, 5);

        portal.teleport(player);

        Vector2 expected = new Vector2(0, 0);

        assertEquals(player.getPosition(), expected);
    }
}

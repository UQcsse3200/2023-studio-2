package com.csse3200.game.entities;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.extensions.GameExtension;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(GameExtension.class)
public class PortalTest {

    private Portal portal;
    private Entity player;

    @Test
    public void testCreation() {
        player = new Entity();
        player.setPosition(0,0);
        portal = new Portal(player);
        assertNotNull(portal);
    }

    @Test
    public void testSetPosition() {
        player = new Entity();
        player.setPosition(0,0);
        portal = new Portal(player);
        portal.setPosition(10,10);

        portal.teleport(player);

        Vector2 actual = new Vector2(10, 10);

        assertEquals(player.getPosition(), actual);
    }
}

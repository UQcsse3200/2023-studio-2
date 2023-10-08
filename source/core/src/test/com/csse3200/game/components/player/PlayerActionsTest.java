package com.csse3200.game.components.player;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.components.PhysicsComponent;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

public class PlayerActionsTest {

    @Mock
    private Entity entity;

    @Mock
    private PhysicsComponent physicsComponent;

    @Mock
    private Body body;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);


        when(entity.getComponent(PhysicsComponent.class)).thenReturn(physicsComponent);
        when(physicsComponent.getBody()).thenReturn(body);
    }

    @Test
    public void testWalk() {
        PlayerActions playerActions = new PlayerActions();
        playerActions.setEntity(entity);

        Vector2 walkDirection = new Vector2(1.0f, 0.0f);

        playerActions.walk(walkDirection);

        assert (playerActions.walkDirection.equals(walkDirection));

    }

    @Test
    public void testStopWalking() {
        PlayerActions playerActions = new PlayerActions();
        playerActions.setEntity(entity);

        Vector2 walkDirection = new Vector2(1.0f, 0.0f);
        playerActions.walk(walkDirection);


        assert (!playerActions.isMoving());

        //   verify(playerActions).updateSpeed();
    }
}

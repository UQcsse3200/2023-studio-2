package com.csse3200.game.components;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.FollowComponent;
import com.csse3200.game.entities.Entity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class FollowComponentTest {

    private FollowComponent followComponent;
    private Entity followEntity;
    private Entity entity;

    @BeforeEach
    void setUp() {
        followEntity = mock(Entity.class);
        entity = mock(Entity.class);
        followComponent = new FollowComponent(followEntity, 1.0f);
        followComponent.setEntity(entity);
    }

    @Test
    void setFollowSpeed() {
        followComponent.setFollowSpeed(2.0f);
        assertEquals(2.0f, followComponent.getFollowSpeed());
    }

    @Test
    void updateMoveEntityTowardFollowingEntityWithMinimumDistance() {
        Vector2 followEntityPosition = new Vector2(1.0f, 1.0f);
        Vector2 currentPosition = new Vector2(1.0f, 1.0f);

        when(followEntity.getPosition()).thenReturn(followEntityPosition);
        when(entity.getPosition()).thenReturn(currentPosition);

        followComponent.update();

        // Verify that the position of the entity is not updated when the distance is less than the minimum distance
        verify(entity, never()).setPosition(any());
    }
}


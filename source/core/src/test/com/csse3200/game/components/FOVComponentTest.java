package com.csse3200.game.components;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.entities.Entity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static junit.framework.TestCase.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FOVComponentTest {
    @Mock
    private FOVComponent fovComponent;
    @Mock
    private Entity mockEnemy;

    @Test
    public void testEnemyIsInFOV() {
        mockEnemy = mock(Entity.class);
        fovComponent = new FOVComponent(10.0f, null, null);
        // Set the position of the mock enemy
        when(mockEnemy.getCenterPosition()).thenReturn(new Vector2(5.0f, 5.0f));

        // Set the position of the FOVComponent entity
        fovComponent.entity = new Entity();
        fovComponent.entity.setPosition(new Vector2(0.0f, 0.0f));

        // Test if the enemy is within FOV
        Assertions.assertTrue(fovComponent.enemyIsInFOV(mockEnemy));
    }

    @Test
    public void testEnemyIsNotInFOV() {

        mockEnemy = mock(Entity.class);
        fovComponent = new FOVComponent(5.0f, null, null);
        // Set the position of the mock enemy
        when(mockEnemy.getCenterPosition()).thenReturn(new Vector2(15.0f, 15.0f));

        // Set the position of the FOVComponent entity
        fovComponent.entity = new Entity();
        fovComponent.entity.setPosition(new Vector2(0.0f, 0.0f));

        // Test if the enemy is not within FOV
        assertFalse(fovComponent.enemyIsInFOV(mockEnemy));
    }

}

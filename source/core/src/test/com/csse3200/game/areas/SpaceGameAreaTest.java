package com.csse3200.game.areas;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.extensions.GameExtension;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import com.badlogic.gdx.math.GridPoint2;
import static org.junit.Assert.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(GameExtension.class)
public class SpaceGameAreaTest {
    @Test
    public void testCalculateDistanceWithZeroDistance() {

        Entity entity1 = mock(Entity.class);
        Entity entity2 = mock(Entity.class);


        when(entity1.getCenterPosition()).thenReturn(new Vector2(0, 0));
        when(entity2.getCenterPosition()).thenReturn(new Vector2(0, 0));


        float distance = SpaceGameArea.calculateDistance(entity1, entity2);


        assertEquals(0.0f, distance, 0.001f);
    }

    @Test
    public void testCalculateDistanceWithNonZeroDistance() {

        Entity entity1 = mock(Entity.class);
        Entity entity2 = mock(Entity.class);

        when(entity1.getCenterPosition()).thenReturn(new Vector2(0, 0));
        when(entity2.getCenterPosition()).thenReturn(new Vector2(3, 4));

        float distance = SpaceGameArea.calculateDistance(entity1, entity2);

        assertEquals(5.0f, distance, 0.001f);
    }
    @Test
    public void testIsPositionOccupied() {

        SpaceGameArea spaceGameArea = mock(SpaceGameArea.class);




        // Define an unoccupied position
        GridPoint2 unoccupiedPosition = new GridPoint2(1, 1);


        when(spaceGameArea.isPositionOccupied(unoccupiedPosition)).thenReturn(false);
        assertFalse(spaceGameArea.isPositionOccupied(unoccupiedPosition));

        // Define an occupied position
        GridPoint2 occupiedPosition = new GridPoint2(2, 2);

        when(spaceGameArea.isPositionOccupied(occupiedPosition)).thenReturn(true);
        assertTrue(spaceGameArea.isPositionOccupied(occupiedPosition));
    }



}

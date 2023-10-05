package com.csse3200.game.services;

import com.badlogic.gdx.math.GridPoint2;
import com.csse3200.game.entities.PlaceableEntity;
import com.csse3200.game.services.StructurePlacementService;
import com.csse3200.game.events.EventHandler;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class StructurePlacementServiceTest {

    private StructurePlacementService structurePlacementService;

    @Before
    public void setUp() {
        // Mock the event handler for testing
        EventHandler mockEventHandler = new EventHandler();

        // Initialize the StructurePlacementService
        structurePlacementService = new StructurePlacementService(mockEventHandler);
    }

    @Test
    public void testGetStructurePosition() {
        // Create a sample entity with a width and height of 2
        PlaceableEntity entity = new PlaceableEntity(2, 2);

        // Define the expected position
        GridPoint2 expectedPosition = new GridPoint2(1, 2);

        // Simulate placing the entity at the expected position
        structurePlacementService.placeStructureAt(entity, expectedPosition, true, true);

        // Check if getStructurePosition returns the expected position
        GridPoint2 actualPosition = structurePlacementService.getStructurePosition(entity);
        assertEquals(expectedPosition, actualPosition);
    }

    @Test
    public void testCanPlaceStructureAt() {
        // Create a sample entity with a width and height of 2
        PlaceableEntity entity = new PlaceableEntity(2, 2);

        // Define a position
        GridPoint2 position = new GridPoint2(1, 2);

        // Ensure that the position is initially empty and can be placed
        assertTrue(structurePlacementService.canPlaceStructureAt(entity, position));

        // Simulate placing another entity at the same position
        PlaceableEntity anotherEntity = new PlaceableEntity(2, 2);
        structurePlacementService.placeStructureAt(anotherEntity, position, true, true);

        // Verify that you cannot place the first entity at the same position now
        assertFalse(structurePlacementService.canPlaceStructureAt(entity, position));
    }
}

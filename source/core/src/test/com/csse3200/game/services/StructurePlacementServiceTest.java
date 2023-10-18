package com.csse3200.game.services;

import com.badlogic.gdx.math.GridPoint2;
import com.csse3200.game.entities.PlaceableEntity;
import com.csse3200.game.services.StructurePlacementService;
import com.csse3200.game.events.EventHandler;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for the {@link StructurePlacementService}.
 */
public class StructurePlacementServiceTest {

    private StructurePlacementService structurePlacementService;

    /**
     * Set up the test environment.
     */
    @Before
    public void setUp() {
        // Mock the event handler for testing
        EventHandler mockEventHandler = new EventHandler();

        // Initialize the StructurePlacementService
        structurePlacementService = new StructurePlacementService(mockEventHandler);
    }

    /**
     * Test case for the {@link StructurePlacementService#getStructurePosition(PlaceableEntity)} method.
     */
    @Test
    public void testGetStructurePosition() {
        // Create a sample entity with a width and height of 2
        PlaceableEntity entity = new PlaceableEntity(2, 2);

        // Define the expected position
        GridPoint2 expectedPosition = new GridPoint2(1, 2);

        // Simulate placing the entity at the expected position
        structurePlacementService.placeStructureAt(entity, expectedPosition);

        // Check if getStructurePosition returns the expected position
        GridPoint2 actualPosition = structurePlacementService.getStructurePosition(entity);
        assertEquals(expectedPosition, actualPosition);
    }

    /**
     * Test case for the {@link StructurePlacementService#canPlaceStructureAt(PlaceableEntity, GridPoint2)} method.
     */
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
        structurePlacementService.placeStructureAt(anotherEntity, position);

        // Verify that you cannot place the first entity at the same position now
        assertFalse(structurePlacementService.canPlaceStructureAt(entity, position));
    }

}

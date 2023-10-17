package com.csse3200.game.components.player;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import com.csse3200.game.GdxGame;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.PlaceableEntity;
import com.csse3200.game.events.EventHandler;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.StructurePlacementService;
import com.csse3200.game.services.TerrainService;
import com.csse3200.game.services.TerrainServiceTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.security.Provider;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ExtendWith(GameExtension.class)
public class PlayerActionsTest {

    @Mock
    private Entity entity;

    @Mock
    private PhysicsComponent physicsComponent;

    @Mock
    private Body body;
    private StructurePlacementService structurePlacementService;

    @Mock
    private TerrainService terrainService;
    @Mock
    private Application application;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mock the event handler for testing
        EventHandler mockEventHandler = new EventHandler();
        ServiceLocator.registerTerrainService(terrainService);

        // Initialize the StructurePlacementService
        structurePlacementService = new StructurePlacementService(mockEventHandler);
        ServiceLocator.registerStructurePlacementService(structurePlacementService);

        when(entity.getComponent(PhysicsComponent.class)).thenReturn(physicsComponent);
        when(entity.getCenterPosition()).thenReturn(Vector2.Zero);
        when(entity.getEvents()).thenReturn(new EventHandler());
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

    /**
     * Test case for the {@link StructurePlacementService#removeStructureAt(GridPoint2)} method.
     */
    @Test
    public void testCannotRemoveStructureInRange() {
        // Create a sample entity with a width and height of 2
        PlaceableEntity placeableEntity = new PlaceableEntity(1, 1);

        // Define a position
        GridPoint2 position = new GridPoint2(0, 6);

        // Place the structure
        structurePlacementService.placeStructureAt(placeableEntity, position);

        // Assert that we cannot remove it from position (0,0)
        PlayerActions playerActions = new PlayerActions();
        playerActions.setEntity(entity);

        // Ensure that the structure cannot be removed
        var clickLocation = new Vector2(0, 6);
        when(terrainService.screenCoordsToGameCoords((int) clickLocation.x, (int) clickLocation.y))
                .thenReturn(clickLocation);
        playerActions.remove((int) clickLocation.x, (int) clickLocation.y);

        assertNotNull(structurePlacementService.getStructureAt(position));
    }

    /**
     * Test case for the {@link StructurePlacementService#removeStructureAt(GridPoint2)} method.
     */
    @Test
    public void testCanRemoveStructureInRange() {
        // Create a sample entity with a width and height of 2
        PlaceableEntity placeableEntity = new PlaceableEntity(1, 1);

        // Define a position
        GridPoint2 position = new GridPoint2(0, 5);

        // Place the structure
        structurePlacementService.placeStructureAt(placeableEntity, position);

        // Assert that we cannot remove it from position (0,0)
        PlayerActions playerActions = new PlayerActions();
        playerActions.setEntity(entity);

        // Ensure that the structure cannot be removed
        var clickLocation = new Vector2(0, 5);
        when(terrainService.screenCoordsToGameCoords((int) clickLocation.x, (int) clickLocation.y))
                .thenReturn(clickLocation);
        Gdx.app = application;

        playerActions.remove((int) clickLocation.x, (int) clickLocation.y);

        assertNull(structurePlacementService.getStructureAt(position));
    }
}

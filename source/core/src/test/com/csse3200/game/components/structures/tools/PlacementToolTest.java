package com.csse3200.game.components.structures.tools;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.utils.ObjectMap;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.entities.PlaceableEntity;
import com.csse3200.game.events.EventHandler;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.services.GameStateObserver;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.StructurePlacementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ExtendWith(GameExtension.class)
class PlacementToolTest {
    @Mock
    Entity player;
    @Mock
    StructurePlacementService structurePlacementService;
    @Mock
    EntityService entityService;
    @Mock
    GameStateObserver stateObserver;
    @Mock
    ResourceService resourceService;
    @Mock
    PhysicsService physicsService;

    @BeforeEach
    void beforeEach() {
        ServiceLocator.registerStructurePlacementService(structurePlacementService);
        ServiceLocator.registerEntityService(entityService);
        ServiceLocator.registerGameStateObserverService(stateObserver);
        ServiceLocator.registerResourceService(resourceService);
        ServiceLocator.registerPhysicsService(physicsService);
    }
    @Test
    void testInteractNoExistingStructureNoCost() {
        ObjectMap<String, Integer> cost = new ObjectMap<>();

        var tool = new MockPlacementTool(cost, 0, "texture.png");

        var position = new GridPoint2(0, 0);

        when(structurePlacementService.canPlaceStructureAt(any(), eq(position))).thenReturn(true);

        tool.interact(player, position);

        verify(stateObserver, never()).getStateData(any());

        verify(structurePlacementService).placeStructureAt(any(), eq(position),
                eq(false), eq(false));
    }

    @Test
    void testInteractExistingStructure() {
        ObjectMap<String, Integer> cost = new ObjectMap<>();

        var tool = new MockPlacementTool(cost, 0, "texture.png");

        var position = new GridPoint2(0, 0);

        when(structurePlacementService.canPlaceStructureAt(any(), eq(position))).thenReturn(false);

        when(player.getEvents()).thenReturn(mock(EventHandler.class));

        tool.interact(player, position);

        verify(structurePlacementService, never()).placeStructureAt(any(), eq(position),
                eq(false), eq(false));
    }

    @Test
    void testInteractWithCostAndSufficientFunds() {
        ObjectMap<String, Integer> cost = new ObjectMap<>();
        cost.put("resource1", 10);
        cost.put("resource2", 25);

        when(stateObserver.getStateData(any())).thenReturn(100);

        var tool = new MockPlacementTool(cost, 0, "texture.png");

        var position = new GridPoint2(0, 0);

        when(structurePlacementService.canPlaceStructureAt(any(), eq(position))).thenReturn(true);

        tool.interact(player, position);

        verify(stateObserver).getStateData("resource/resource1");
        verify(stateObserver).getStateData("resource/resource2");
        verify(structurePlacementService).placeStructureAt(any(), eq(position),
                eq(false), eq(false));
    }

    @Test
    void testInteractWithCostAndInsufficientFunds() {
        ObjectMap<String, Integer> cost = new ObjectMap<>();
        cost.put("resource1", 10);
        cost.put("resource2", 25);

        when(stateObserver.getStateData(any())).thenReturn(15);

        when(player.getEvents()).thenReturn(mock(EventHandler.class));

        var tool = new MockPlacementTool(cost, 0, "texture.png");

        var position = new GridPoint2(0, 0);

        when(structurePlacementService.canPlaceStructureAt(any(), eq(position))).thenReturn(true);

        tool.interact(player, position);

        // don't care which order they get checked in. Don't care if loops breaks early.
        verify(stateObserver, atMost(1)).getStateData("resource/resource1");
        verify(stateObserver, atMost(1)).getStateData("resource/resource2");
        verify(structurePlacementService, never()).placeStructureAt(any(), eq(position),
                eq(false), eq(false));
    }
}

class MockPlacementTool extends PlacementTool {

    public MockPlacementTool(ObjectMap<String, Integer> cost, int ordering, String texture) {
        super(cost, ordering, texture);
    }

    @Override
    public PlaceableEntity createStructure(Entity player) {
        return mock(PlaceableEntity.class);
    }
}
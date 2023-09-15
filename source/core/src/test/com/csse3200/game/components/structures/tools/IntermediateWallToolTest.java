package com.csse3200.game.components.structures.tools;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.ObjectMap;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.entities.PlaceableEntity;
import com.csse3200.game.entities.buildables.Wall;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.physics.PhysicsEngine;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ExtendWith(GameExtension.class)
class IntermediateWallToolTest {
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

    void setupResourceAndPhysics() {
        var physicsEngine = mock(PhysicsEngine.class);
        when(physicsService.getPhysics()).thenReturn(physicsEngine);
        when(physicsEngine.createBody(any())).thenReturn(mock(Body.class));

        var textureAtlas = mock(TextureAtlas.class);
        when(resourceService.getAsset(any(), eq(TextureAtlas.class))).thenReturn(textureAtlas);
        when(textureAtlas.findRegion(any())).thenReturn(mock(TextureAtlas.AtlasRegion.class));
    }

    @Test
    void testInteractNoExistingStructureNoCost() {
        setupResourceAndPhysics();

        ObjectMap<String, Integer> cost = new ObjectMap<>();

        var tool = new IntermediateWallTool(cost);

        var position = new GridPoint2(0, 0);

        when(structurePlacementService.getStructureAt(position)).thenReturn(null);

        tool.interact(player, position);

        verify(stateObserver, never()).getStateData(any());

        verify(structurePlacementService).placeStructureAt(any(), eq(position),
                eq(false), eq(false));
    }

    @Test
    void testInteractExistingStructureNotWall() {
        ObjectMap<String, Integer> cost = new ObjectMap<>();

        var tool = new IntermediateWallTool(cost);

        var position = new GridPoint2(0, 0);

        when(structurePlacementService.getStructureAt(position)).thenReturn(mock(PlaceableEntity.class));

        tool.interact(player, position);

        verify(structurePlacementService, never()).placeStructureAt(any(), eq(position),
                eq(false), eq(false));
    }

    @Test
    void testInteractExistingWall() {
        setupResourceAndPhysics();

        ObjectMap<String, Integer> cost = new ObjectMap<>();

        var tool = new IntermediateWallTool(cost);

        var position = new GridPoint2(0, 0);

        when(structurePlacementService.getStructureAt(position)).thenReturn(mock(Wall.class));

        tool.interact(player, position);

        verify(structurePlacementService, atLeastOnce()).getStructureAt(position);
        verify(structurePlacementService).replaceStructureAt(any(), eq(position),
                eq(false), eq(false));
    }

    @Test
    void testInteractExistingWallSufficientFunds() {
        setupResourceAndPhysics();

        ObjectMap<String, Integer> cost = new ObjectMap<>();
        cost.put("resource1", 10);
        cost.put("resource2", 25);

        when(stateObserver.getStateData(any())).thenReturn(100);

        var tool = new IntermediateWallTool(cost);

        var position = new GridPoint2(0, 0);

        when(structurePlacementService.getStructureAt(position)).thenReturn(mock(Wall.class));

        tool.interact(player, position);

        verify(stateObserver).getStateData("resource/resource1");
        verify(stateObserver).getStateData("resource/resource2");
        verify(structurePlacementService, atLeastOnce()).getStructureAt(position);
        verify(structurePlacementService).replaceStructureAt(any(), eq(position),
                eq(false), eq(false));
    }

    @Test
    void testInteractExistingWallInsufficientFunds() {
        ObjectMap<String, Integer> cost = new ObjectMap<>();
        cost.put("resource1", 10);
        cost.put("resource2", 25);

        when(stateObserver.getStateData(any())).thenReturn(15);

        var tool = new IntermediateWallTool(cost);

        var position = new GridPoint2(0, 0);

        when(structurePlacementService.getStructureAt(position)).thenReturn(mock(Wall.class));

        tool.interact(player, position);

        verify(stateObserver).getStateData("resource/resource1");
        verify(stateObserver).getStateData("resource/resource2");
        verify(structurePlacementService, atLeastOnce()).getStructureAt(position);
        verify(structurePlacementService, never()).replaceStructureAt(any(), eq(position),
                eq(false), eq(false));
    }

    @Test
    void testInteractWithCostAndSufficientFunds() {
        setupResourceAndPhysics();

        ObjectMap<String, Integer> cost = new ObjectMap<>();
        cost.put("resource1", 10);
        cost.put("resource2", 25);

        when(stateObserver.getStateData(any())).thenReturn(100);

        var tool = new IntermediateWallTool(cost);

        var position = new GridPoint2(0, 0);

        when(structurePlacementService.getStructureAt(position)).thenReturn(null);

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

        var tool = new IntermediateWallTool(cost);

        var position = new GridPoint2(0, 0);

        when(structurePlacementService.getStructureAt(position)).thenReturn(null);

        tool.interact(player, position);

        // don't care which order they get checked in. Don't care if loops breaks early.
        verify(stateObserver, atMost(1)).getStateData("resource/resource1");
        verify(stateObserver, atMost(1)).getStateData("resource/resource2");
        verify(structurePlacementService, never()).placeStructureAt(any(), eq(position),
                eq(false), eq(false));
    }
}
package com.csse3200.game.components.structures.tools;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ObjectMap;
import com.csse3200.game.components.resources.FissureComponent;
import com.csse3200.game.components.resources.Resource;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.entities.Fissure;
import com.csse3200.game.entities.PlaceableEntity;
import com.csse3200.game.events.EventHandler;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.GameStateObserver;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.StructurePlacementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ExtractorToolTest {
    @Mock
    Entity player;
    @Mock
    EventHandler eventHandler;
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
    @Mock
    RenderService renderService;
    ExtractorTool extractorTool;

    @BeforeEach
    void before() {
        ServiceLocator.registerStructurePlacementService(structurePlacementService);
        ServiceLocator.registerEntityService(entityService);
        ServiceLocator.registerGameStateObserverService(stateObserver);
        ServiceLocator.registerResourceService(resourceService);
        ServiceLocator.registerPhysicsService(physicsService);
        ServiceLocator.registerRenderService(renderService);

        extractorTool = new ExtractorTool(new ObjectMap<>(), 5f, "texture.png", 0);

        when(player.getCenterPosition()).thenReturn(new Vector2(0, 0));
        when(player.getEvents()).thenReturn(eventHandler);
    }

    @Test
    void validTest() {
        when(stateObserver.getStateData("extractorsMax/" + Resource.Durasteel)).thenReturn(4);
        when(stateObserver.getStateData("extractorsTotal/" + Resource.Durasteel)).thenReturn(0);

        var fissure = mock(Fissure.class);
        var fissureComponent = mock(FissureComponent.class);

        when(fissure.getComponent(FissureComponent.class)).thenReturn(fissureComponent);
        when(fissureComponent.getProduces()).thenReturn(Resource.Durasteel);

        when(structurePlacementService.getStructureAt(new GridPoint2(0, 0))).thenReturn(fissure);

        assertTrue(extractorTool.isPositionValid(new GridPoint2(0, 0)).isValid());

        when(structurePlacementService.getStructureAt(new GridPoint2(1, 1))).thenReturn(mock(PlaceableEntity.class));
            assertFalse(extractorTool.isPositionValid(new GridPoint2(1, 1)).isValid());
    }

    @Test
    void testInteractOutOfRange() {
        ObjectMap<String, Integer> cost = new ObjectMap<>();

        var position = new GridPoint2(6, 0);

        when(structurePlacementService.canPlaceStructureAt(any(), eq(position))).thenReturn(true);

        extractorTool.interact(player, position);

        verify(stateObserver, never()).getStateData(any());

        verify(structurePlacementService, never()).placeStructureAt(any(), eq(position));
    }

    @Test
    void testInteractAtRange() {
        ObjectMap<String, Integer> cost = new ObjectMap<>();

        var tool = new MockPlacementTool(cost, 5f, "texture.png", 0);

        var position = new GridPoint2(5, 0);

        when(structurePlacementService.canPlaceStructureAt(any(), eq(position))).thenReturn(true);

        tool.interact(player, position);

        verify(stateObserver, never()).getStateData(any());

        verify(structurePlacementService).placeStructureAt(any(), eq(position));
    }

    @Test
    void testInteractInRange() {
        ObjectMap<String, Integer> cost = new ObjectMap<>();

        var tool = new MockPlacementTool(cost, 5f, "texture.png", 0);

        var position = new GridPoint2(4, 0);

        when(structurePlacementService.canPlaceStructureAt(any(), eq(position))).thenReturn(true);

        tool.interact(player, position);

        verify(stateObserver, never()).getStateData(any());

        verify(structurePlacementService).placeStructureAt(any(), eq(position));
    }

    @Test
    void testInteract0Range() {
        ObjectMap<String, Integer> cost = new ObjectMap<>();

        var tool = new MockPlacementTool(cost, 5f, "texture.png", 0);

        var position = new GridPoint2(0, 0);

        when(structurePlacementService.canPlaceStructureAt(any(), eq(position))).thenReturn(true);

        tool.interact(player, position);

        verify(stateObserver, never()).getStateData(any());

        verify(structurePlacementService).placeStructureAt(any(), eq(position));
    }
}
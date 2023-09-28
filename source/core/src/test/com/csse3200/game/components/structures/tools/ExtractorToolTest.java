package com.csse3200.game.components.structures.tools;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.utils.ObjectMap;
import com.csse3200.game.components.resources.Resource;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.entities.PlaceableEntity;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.GameStateObserver;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.StructurePlacementService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)
class ExtractorToolTest {
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
    @Mock
    RenderService renderService;

    @Test
    void validTest() {
            ServiceLocator.registerStructurePlacementService(structurePlacementService);
            ServiceLocator.registerEntityService(entityService);
            ServiceLocator.registerGameStateObserverService(stateObserver);
            ServiceLocator.registerResourceService(resourceService);
            ServiceLocator.registerPhysicsService(physicsService);
            ServiceLocator.registerRenderService(renderService);

            when(stateObserver.getStateData("extractorsMax/" + Resource.Durasteel)).thenReturn(4);
            when(stateObserver.getStateData("extractorsTotal/" + Resource.Durasteel)).thenReturn(0);
            ExtractorTool extractorTool = new ExtractorTool(new ObjectMap<>());

            assertTrue(extractorTool.isPositionValid(new GridPoint2(0, 0), mock(PlaceableEntity.class)));

            when(stateObserver.getStateData("extractorsTotal/" + Resource.Durasteel)).thenReturn(5);

            assertFalse(extractorTool.isPositionValid(new GridPoint2(0, 0), mock(PlaceableEntity.class)));
    }

}
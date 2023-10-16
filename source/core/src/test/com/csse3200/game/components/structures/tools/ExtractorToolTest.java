package com.csse3200.game.components.structures.tools;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.utils.ObjectMap;
import com.csse3200.game.components.resources.FissureComponent;
import com.csse3200.game.components.resources.Resource;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.entities.Fissure;
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
            ExtractorTool extractorTool = new ExtractorTool(new ObjectMap<>(), 0, "texture.png");

            var fissure = mock(Fissure.class);
            var fissureComponent = mock(FissureComponent.class);

            when(fissure.getComponent(FissureComponent.class)).thenReturn(fissureComponent);
            when(fissureComponent.getProduces()).thenReturn(Resource.Durasteel);

            when(structurePlacementService.getStructureAt(new GridPoint2(0, 0))).thenReturn(fissure);

            assertTrue(extractorTool.isPositionValid(new GridPoint2(0, 0)).isValid());

        when(structurePlacementService.getStructureAt(new GridPoint2(1, 1))).thenReturn(mock(PlaceableEntity.class));
            assertFalse(extractorTool.isPositionValid(new GridPoint2(1, 1)).isValid());
    }

}
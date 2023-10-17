package com.csse3200.game.areas;

import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.areas.terrain.TerrainFactory.TerrainType;
import com.csse3200.game.areas.terrain.TerrainComponent;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.areas.ExtractorMiniGameArea;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.entities.EntityService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
class ExtractorGameAreaTest {

    ExtractorMiniGameArea extractorMiniGameArea;

    @Test
    void testCreate() {

        ServiceLocator.registerEntityService(new EntityService());
        TerrainFactory terrainFactory = mock(TerrainFactory.class);
        ResourceService resourceService = mock(ResourceService.class);
        ServiceLocator.registerResourceService(resourceService);

        // Create a new instance of ExtractorMiniGameArea
        extractorMiniGameArea = new ExtractorMiniGameArea(terrainFactory);

        // Mock the behavior of the terrainFactory
        TerrainComponent mockTerrainComponent = mock(TerrainComponent.class);
        when(terrainFactory.createSpaceTerrain(any(TerrainFactory.TerrainType.class))).thenReturn(mockTerrainComponent);

        // Use a spy to capture the number of invocations
        ResourceService resourceServiceSpy = spy(ServiceLocator.getResourceService());
        ServiceLocator.registerResourceService(resourceServiceSpy);

        // Call the method under test
        extractorMiniGameArea.create();

        // Verify that the terrainFactory is called
        verify(terrainFactory).createSpaceTerrain(any(TerrainFactory.TerrainType.class));

        // Verify that the resourceService's loadTextures is called at least once
        verify(resourceServiceSpy, atLeastOnce()).loadTextures(any(String[].class));
    }
}

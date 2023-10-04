package com.csse3200.game.components.structures.tools;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ObjectMap;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ExtendWith(GameExtension.class)
class LandmineToolTest {
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
    PhysicsService physicsService = new PhysicsService();

    @BeforeEach
    void beforeEach() {
        ServiceLocator.registerStructurePlacementService(structurePlacementService);
        ServiceLocator.registerEntityService(entityService);
        ServiceLocator.registerGameStateObserverService(stateObserver);
        ServiceLocator.registerResourceService(resourceService);
        ServiceLocator.registerPhysicsService(physicsService);

        var texture = mock(Texture.class);
        when(resourceService.getAsset(any(), eq(Texture.class))).thenReturn(texture);
    }

    // only method overridden in class
    @Test
    void testCreateStructure() {
        var tool = new LandmineTool(new ObjectMap<>());

        assertNotNull(tool.createStructure(player));
    }

    @Test
    void testCreateStructureWithCost() {
        var cost = new ObjectMap<String, Integer>();
        cost.put("Durasteel", 10);
        cost.put("Solstite", 5);

        var tool = new LandmineTool(cost);

        assertNotNull(tool.createStructure(player));
    }
}
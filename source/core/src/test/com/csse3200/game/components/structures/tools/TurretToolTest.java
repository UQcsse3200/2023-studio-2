package com.csse3200.game.components.structures.tools;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g3d.model.Animation;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.ObjectMap;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.physics.PhysicsEngine;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.GameStateObserver;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.StructurePlacementService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ExtendWith(GameExtension.class)
class TurretToolTest {
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
    @Test
    void createTest() {
        ServiceLocator.registerStructurePlacementService(structurePlacementService);
        ServiceLocator.registerEntityService(entityService);
        ServiceLocator.registerGameStateObserverService(stateObserver);
        ServiceLocator.registerResourceService(resourceService);
        ServiceLocator.registerPhysicsService(physicsService);

        var physicsEngine = mock(PhysicsEngine.class);
        when(physicsService.getPhysics()).thenReturn(physicsEngine);
        when(physicsEngine.createBody(any())).thenReturn(mock(Body.class));

        var animation = mock(TextureAtlas.class);
        when(resourceService.getAsset(any(), eq(TextureAtlas.class))).thenReturn(animation);

        TurretTool turretTool = new TurretTool(new ObjectMap<>());

        assertNotNull(turretTool.createStructure(player));
    }
}
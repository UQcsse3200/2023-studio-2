package com.csse3200.game.entities.factories;

import com.csse3200.game.entities.Entity;

import com.csse3200.game.entities.EntityService;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.input.InputService;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@ExtendWith(GameExtension.class)
public class EnemyFactoryTest {

    @BeforeEach
    void setUp() {
        ServiceLocator.registerEntityService(new EntityService());
        ServiceLocator.registerPhysicsService(new PhysicsService());
        ResourceService resourceService = new ResourceService();
        ServiceLocator.registerResourceService(resourceService);
        ServiceLocator.registerInputService(new InputService());
        ServiceLocator.registerRenderService(new RenderService());
        String[] textures = {"images/box_boy_leaf.png"};

        // These will be the player images once feature/player pushes to main :)
        // String[] textures = {"images/player_blank.png"};
        // String[] atlases = {"images/playerSS.atlas"};
        // resourceService.loadTextureAtlases(atlases);
        resourceService.loadTextures(textures);
        resourceService.loadAll();
    }

    @Test
    void createEnemyTypeTest(){
        Entity enemy = new Entity();
        Entity player = PlayerFactory.createPlayer();
    }
}

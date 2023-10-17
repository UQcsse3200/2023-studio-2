package com.csse3200.game.components.enemy;



import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class godComponentTest {
    private GodComponent godComponent;
    private Entity mockTarget;
    private Entity mockEnemy;
//    @BeforeEach
//    void beforeEach() {
////        mockTarget = new Entity();
////        mockEnemy =  makePhysicsEntity();
////        godComponent = new GodComponent(mockEnemy);
//
////        RenderService renderService = new RenderService();
////
////        ServiceLocator.registerRenderService(renderService);
////        GameTime gameTime = mock(GameTime.class);
////        when(gameTime.getDeltaTime()).thenReturn(20f / 1000);
////        ServiceLocator.registerTimeSource(gameTime);
////        ServiceLocator.registerPhysicsService(new PhysicsService());
//
//    }

    @Test
    public void modeSwitchingTest(){
        mockTarget = new Entity();
        mockEnemy =  new Entity();
        godComponent = new GodComponent(mockEnemy);
        assertFalse(godComponent.getMode());
        godComponent.toggleMode();
        assertTrue(godComponent.getMode());

        // After toggling again, it should be false
        godComponent.toggleMode();
        assertFalse(godComponent.getMode());
    }







}

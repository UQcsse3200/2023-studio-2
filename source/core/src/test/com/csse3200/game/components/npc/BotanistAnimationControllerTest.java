package com.csse3200.game.components.npc;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.csse3200.game.components.Component;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.rendering.AnimationRenderComponent;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class BotanistAnimationControllerTest {

    private BotanistAnimationController animationController;
    private AnimationRenderComponent animator;

    @Before
    public void setUp() {
        animator = Mockito.mock(AnimationRenderComponent.class);
        animationController = new BotanistAnimationController();
        animationController.entity = new EntityMock(animator);
        animationController.create();
    }

    @Test
    public void testAnimateWander() {
        // Test animation for wandering
        animationController.animateWander();
        Mockito.verify(animator).startAnimation("wanderStart_right");
    }

    @Test
    public void testAnimateIdle() {
        // Test animation for being idle
        animationController.animateIdle();
        Mockito.verify(animator).startAnimation("idle_right");
    }

    @Test
    public void testSetDirection() {
        // Test setting direction
        animationController.setDirection("left");
        animationController.animateWander();
        Mockito.verify(animator).startAnimation("wanderStart_left");
    }

    // Mock class to simulate the entity for testing
    private class EntityMock extends Entity {
        private AnimationRenderComponent animator;

        public EntityMock(AnimationRenderComponent animator) {
            this.animator = animator;
        }

        @Override
        public <T extends Component> T getComponent(Class<T> componentClass) {
            return (T) animator;
        }
    }
}

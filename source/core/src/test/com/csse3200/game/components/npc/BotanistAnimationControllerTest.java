package com.csse3200.game.components.npc;

import com.csse3200.game.entities.Entity;
import com.csse3200.game.rendering.AnimationRenderComponent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

/**
 * Unit tests for the {@link BotanistAnimationController} class.
 */
class BotanistAnimationControllerTest {

    @Mock
    AnimationRenderComponent animator;

    BotanistAnimationController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        controller = new BotanistAnimationController();

        controller.entity = mock(Entity.class);

        when(controller.entity.getComponent(AnimationRenderComponent.class)).thenReturn(animator);

    }

    /**
     * Test the {@link BotanistAnimationController#create()} method.
     */
    @Test
    void testCreate() {
        controller.create();

        // Modify this part to test the behavior without using the animationDuration field.
        when(animator.getCurrentAnimation()).thenReturn("row-1-column-1");

        controller.update();
    }
}

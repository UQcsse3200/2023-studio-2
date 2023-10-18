package com.csse3200.game.components.npc;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.rendering.AnimationRenderComponent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

/**
 * Unit tests for the {@link AstroAnimationController} class.
 */
class AstroAnimationControllerTest {

    @Mock
    AnimationRenderComponent animator;

    @Mock
    TextureAtlas atlas;

    AstroAnimationController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        controller = new AstroAnimationController();

        controller.entity = mock(Entity.class);

        when(controller.entity.getComponent(AnimationRenderComponent.class)).thenReturn(animator);

    }

    /**
     * Test the {@link AstroAnimationController#create()} method.
     */
    @Test
    void testCreate() {
        controller.create();

        verify(animator).startAnimation("Astro_Down");
    }

    /**
     * Test the {@link AstroAnimationController#update()} method.
     */
    @Test
    void testUpdate() {
        controller.animationDuration= 10f ;

        when(animator.getCurrentAnimation()).thenReturn("Astro_Up");

        controller.update();
    }
}

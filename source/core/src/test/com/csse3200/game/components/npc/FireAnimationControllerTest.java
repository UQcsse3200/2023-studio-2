package com.csse3200.game.components.npc;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.csse3200.game.components.Component;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.rendering.AnimationRenderComponent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the {@link FireAnimationController} class.
 */
class FireAnimationControllerTest {

    @Mock
    AnimationRenderComponent animator;

    FireAnimationController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        controller = new FireAnimationController();

        controller.entity = mock(Entity.class);

        when(controller.entity.getComponent(AnimationRenderComponent.class)).thenReturn(animator);
    }

    /**
     * Test the {@link FireAnimationController#create()} method.
     */
    @Test
    void testCreate() {
        controller.create();

        verify(animator).startAnimation("image_part1");
    }

    /**
     * Test the {@link FireAnimationController#update()} method.
     */
    @Test
    void testUpdate() {
        controller.animationDuration = 10f;

        when(animator.getCurrentAnimation()).thenReturn("image_part1");

        controller.update();
    }
}

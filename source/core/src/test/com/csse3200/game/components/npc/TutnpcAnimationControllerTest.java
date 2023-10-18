package com.csse3200.game.components.npc;

import com.csse3200.game.entities.Entity;
import com.csse3200.game.rendering.AnimationRenderComponent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

class TutnpcAnimationControllerTest {

    @Mock
    AnimationRenderComponent animator;

    TutnpcAnimationController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        controller = new TutnpcAnimationController();
        controller.entity = mock(Entity.class);
        when(controller.entity.getComponent(AnimationRenderComponent.class)).thenReturn(animator);
    }

    /**
     * Test the {@link TutnpcAnimationController#create()} method.
     */
    @Test
    void testCreate() {
        controller.create();
        verify(animator).startAnimation("Tut_Down");
    }

    /**
     * Test the {@link TutnpcAnimationController#update()} method.
     */
    @Test
    void testUpdate() {
        controller.animationDuration = 10f;

        when(animator.getCurrentAnimation()).thenReturn("Tut_Down");

        controller.update();


    }
}

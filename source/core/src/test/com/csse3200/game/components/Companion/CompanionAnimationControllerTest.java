package com.csse3200.game.components.Companion;

import com.csse3200.game.rendering.AnimationRenderComponent;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class CompanionAnimationControllerTest {
    private CompanionAnimationController controller;
    private AnimationRenderComponent animator;

    @Before
    public void setUp() {
        // Mock the AnimationRenderComponent
        animator = mock(AnimationRenderComponent.class);

        // Set up the controller with the mocked components
        controller = new CompanionAnimationController();
        controller.animator = animator;

        // Additional setup for event listeners, if needed
    }

    @Test
    public void testAnimateLeft() {
        controller.animateLeft();
        verify(animator).startAnimation("Companion_Left");
    }

    @Test
    public void testAnimateRight() {
        controller.animateRight();
        verify(animator).startAnimation("Companion_Right");
    }

    @Test
    public void testAnimateUp() {
        controller.animateUp();
        verify(animator).startAnimation("Companion_Up");
    }

    @Test
    public void testAnimateDown() {
        controller.animateDown();
        verify(animator).startAnimation("Companion_Down");
    }

    @Test
    public void testAnimateUpLeft() {
        controller.animateUpLeft();
        verify(animator).startAnimation("Companion_UpLeft");
    }

    @Test
    public void testAnimateUpRight() {
        controller.animateUpRight();
        verify(animator).startAnimation("Companion_UpRight");
    }

    @Test
    public void testAnimateDownLeft() {
        controller.animateDownLeft();
        verify(animator).startAnimation("Companion_DownLeft");
    }

    @Test
    public void testAnimateDownRight() {
        controller.animateDownRight();
        verify(animator).startAnimation("Companion_DownRight");
    }

}

package com.csse3200.game.components.npc;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.csse3200.game.components.npc.AstronautAnimationController;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.entities.Entity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the {@link AstronautAnimationController} class.
 */
class AstronautAnimationControllerTest {

    @Mock
    AssetManager assetManager;

    @Mock
    AnimationRenderComponent animator;

    @Mock
    TextureAtlas atlas;

    AstronautAnimationController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        when(assetManager.get("images/npc/astronaut_npc.atlas")).thenReturn(atlas);

        controller = new AstronautAnimationController();

        controller.entity = mock(Entity.class);

        when(controller.entity.getComponent(AnimationRenderComponent.class)).thenReturn(animator);

    }

    /**
     * Test the {@link AstronautAnimationController#create()} method.
     */
    @Test
    void testCreate() {
        controller.create();

        verify(animator).startAnimation("row-1-column-1");

    }

    /**
     * Test the {@link AstronautAnimationController#update()} method.
     */
    @Test
    void testUpdate() {
        controller.animationDuration = 10f;

        when(animator.getCurrentAnimation()).thenReturn("row-2-column-2");

        controller.update();
    }
}


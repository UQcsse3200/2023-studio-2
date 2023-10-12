package com.csse3200.game.components.npc;

import com.badlogic.gdx.assets.AssetManager;
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
 * Unit tests for the {@link HellmanAnimationController} class.
 */
class HellmanAnimationControllerTest {

    @Mock
    AssetManager assetManager;

    @Mock
    AnimationRenderComponent animator;

    @Mock
    TextureAtlas atlas;

    HellmanAnimationController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        when(assetManager.get("images/npc/Hellman.atlas")).thenReturn(atlas);

        controller = new HellmanAnimationController();

        controller.entity = mock(Entity.class);

        when(controller.entity.getComponent(AnimationRenderComponent.class)).thenReturn(animator);
    }

    /**
     * Test the {@link HellmanAnimationController#create()} method.
     */
    @Test
    void testCreate() {
        controller.create();

        verify(animator).startAnimation("row-1-column-1");

    }

    /**
     * Test the {@link HellmanAnimationController#update()} method.
     */
    @Test
    void testUpdate() {
        controller.animationDuration = 10f;

        when(animator.getCurrentAnimation()).thenReturn("row-1-column-12");

        controller.update();

    }
}

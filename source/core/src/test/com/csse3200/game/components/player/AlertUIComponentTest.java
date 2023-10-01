package com.csse3200.game.components.player;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.resources.Resource;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.rendering.RenderComponent;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
//import org.testng.Assert;
import org.junit.Assert;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)
class AlertUIComponentTest {
    @Mock
    ResourceService resourceService;
    @Mock
    RenderService renderService;

    @BeforeEach
    void beforeEach() {
        ServiceLocator.registerResourceService(resourceService);
        ServiceLocator.registerRenderService(renderService);
        when(resourceService.getAsset(any(), eq(Texture.class))).thenReturn(mock(Texture.class));
    }

    @Test
    void create() {
        // pretty much just tests that it doesn't crash since you cannot assume anything about create
        var component = new AlertUIComponent("test");

        component.create();

        verify(renderService, times(1)).register(component);
    }

    @Test
    void draw() {
        var component = new AlertUIComponent("test");

        var entity = mock(Entity.class);
        when(entity.getPosition()).thenReturn(new Vector2(8, 8));
        when(entity.getCenterPosition()).thenReturn(new Vector2(10, 10));
        var batch = mock(SpriteBatch.class);

        when(batch.getProjectionMatrix()).thenReturn(new Matrix4());
        when(batch.getColor()).thenReturn(new Color());

        component.setEntity(entity);

        component.create();
        component.draw(batch);

        verify(entity).getPosition();
    }

    @Test
    void setAlpha() {
        var component = new AlertUIComponent("test");
        component.create();

        // expected default 1.
        Assert.assertEquals(component.getAlpha(), 1);

        component.setAlpha(0.5f);

        Assert.assertEquals(component.getAlpha(), 0.5f);

        component.setAlpha(0);

        Assert.assertEquals(component.getAlpha(), 0);

    }
}
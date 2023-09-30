package com.csse3200.game.rendering;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)
class ColorDrawableTest {
    @Mock
    ResourceService resourceService;
    @Mock
    Texture texture;

    @BeforeEach
    void beforeEach() {
        ServiceLocator.registerResourceService(resourceService);
        when(resourceService.getAsset(any(), eq(Texture.class))).thenReturn(texture);
    }

    @Test
    void draw() {
        testDraw(1, 0, 0, 0);
        testDraw(0, 1, 0, 0);
        testDraw(0, 0, 1, 0);
        testDraw(0, 0, 0, 1);
        testDraw(0.5f, 0.5f, 0.5f, 0.5f);
    }

    void testDraw(float r, float g, float b, float a) {
        var batch = mock(Batch.class);
        var color = new Color(0.5f, 0.4f, 0, 1);
        when(batch.getColor()).thenReturn(color);

        var drawable = new ColorDrawable(r, g, b, a);

        drawable.draw(batch, 0, 0, 1, 1);

        verify(batch, atLeast(1)).getColor();
        verify(batch, times(1)).setColor(r, g, b, a);
        verify(batch, times(1)).setColor(eq(color));
        verify(batch, times(1)).draw(texture, 0, 0, 1, 1);
    }

    @Test
    void setAlpha() {var batch = mock(Batch.class);
        var color = new Color(0.5f, 0.4f, 0, 1);
        when(batch.getColor()).thenReturn(color);

        var drawable = new ColorDrawable(0, 1, 0, 0.5f);

        // change the alpha
        drawable.setAlpha(0.75f);

        drawable.draw(batch, 0, 0, 1, 1);

        verify(batch, atLeast(1)).getColor();
        // check using changed alpha
        verify(batch, times(1)).setColor(0, 1, 0, 0.75f);
        verify(batch, times(1)).setColor(eq(color));
        verify(batch, times(1)).draw(texture, 0, 0, 1, 1);
    }
}
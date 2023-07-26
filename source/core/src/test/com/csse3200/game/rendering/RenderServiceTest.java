package com.csse3200.game.rendering;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.csse3200.game.extensions.GameExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mockito;

@ExtendWith(GameExtension.class)
class RenderServiceTest {
  @Test
  void shouldRender() {
    RenderService renderService = new RenderService();
    Renderable renderable = mock(Renderable.class);
    SpriteBatch spriteBatch = mock(SpriteBatch.class);
    renderService.register(renderable);
    renderService.render(spriteBatch);
    verify(renderable).render(spriteBatch);

    renderService.unregister(renderable);
  }

  @Test
  void shouldNotRenderAfterUnregister() {
    RenderService renderService = new RenderService();
    Renderable renderable = mock(Renderable.class);
    SpriteBatch spriteBatch = mock(SpriteBatch.class);
    renderService.register(renderable);
    renderService.unregister(renderable);
    renderService.render(spriteBatch);
    verify(renderable, times(0)).render(any());
  }

  @Test
  void shouldRenderInZIndexOrder() {
    RenderService renderService = new RenderService();
    SpriteBatch spriteBatch = mock(SpriteBatch.class);
    RenderComponent renderable1 = mock(RenderComponent.class);
    RenderComponent renderable2 = mock(RenderComponent.class);

    // Same layer, renderable2 is in front
    when(renderable1.getLayer()).thenReturn(1);
    when(renderable2.getLayer()).thenReturn(1);
    when(renderable1.compareTo(any())).thenReturn(1);
    when(renderable2.compareTo(any())).thenReturn(-1);

    renderService.register(renderable1);
    renderService.register(renderable2);

    InOrder inOrder = Mockito.inOrder(renderable1, renderable2);
    renderService.render(spriteBatch);
    inOrder.verify(renderable2).render(any());
    inOrder.verify(renderable1).render(any());
  }

  @Test
  void shouldRenderInLayerOrder() {
    RenderService renderService = new RenderService();
    SpriteBatch spriteBatch = mock(SpriteBatch.class);
    Renderable renderable1 = mock(Renderable.class);
    Renderable renderable2 = mock(Renderable.class);

    when(renderable1.getLayer()).thenReturn(1);
    when(renderable2.getLayer()).thenReturn(2);
    when(renderable1.compareTo(any())).thenReturn(1);
    when(renderable2.compareTo(any())).thenReturn(-1);

    renderService.register(renderable1);
    renderService.register(renderable2);

    InOrder inOrder = Mockito.inOrder(renderable1, renderable2);
    renderService.render(spriteBatch);
    inOrder.verify(renderable1).render(any());
    inOrder.verify(renderable2).render(any());
  }
}

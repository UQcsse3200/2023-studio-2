package com.csse3200.game.rendering;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.extensions.GameExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)
class TextureRenderComponentTest {
  @Mock Texture texture;
  @Mock SpriteBatch spriteBatch;
  @Mock Entity entity;

  @Test
  void shouldDrawTexture() {
    when(entity.getPosition()).thenReturn(new Vector2(2f, 2f));
    when(entity.getScale()).thenReturn(new Vector2(1f, 1f));
    TextureRenderComponent component = new TextureRenderComponent(texture);
    component.setEntity(entity);
    component.render(spriteBatch);

    verify(spriteBatch).draw(texture, 2f, 2f, 1f, 1f);
  }
}
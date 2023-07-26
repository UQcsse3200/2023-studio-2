package com.csse3200.game.rendering;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Array;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
class AnimationRenderComponentTest {
  @Test
  void shouldAddRemoveAnimation() {
    TextureAtlas atlas = createMockAtlas("test_name", 1);
    AnimationRenderComponent animator = new AnimationRenderComponent(atlas);

    assertTrue(animator.addAnimation("test_name", 0.1f));
    assertTrue(animator.removeAnimation("test_name"));
    assertFalse(animator.removeAnimation("test_name"));
  }

  @Test
  void shouldFailRemoveInvalidAnimation() {
    TextureAtlas atlas = mock(TextureAtlas.class);
    when(atlas.findRegions("test_name")).thenReturn(null);
    AnimationRenderComponent animator = new AnimationRenderComponent(atlas);

    assertFalse(animator.addAnimation("test_name", 0.1f));
    assertFalse(animator.removeAnimation("test_name"));
  }

  @Test
  void shouldFailDuplicateAddAnimation() {
    TextureAtlas atlas = createMockAtlas("test_name", 1);
    AnimationRenderComponent animator = new AnimationRenderComponent(atlas);

    assertTrue(animator.addAnimation("test_name", 0.1f));
    assertFalse(animator.addAnimation("test_name", 0.2f));
  }

  @Test
  void shouldHaveAnimation() {
    TextureAtlas atlas = createMockAtlas("test_name", 1);
    AnimationRenderComponent animator = new AnimationRenderComponent(atlas);

    animator.addAnimation("test_name", 0.1f);
    assertTrue(animator.hasAnimation("test_name"));
    animator.removeAnimation("test_name");
    assertFalse(animator.hasAnimation("test_name"));
  }

  @Test
  void shouldPlayAnimation() {
    int numFrames = 5;
    String animName = "test_name";
    float frameTime = 1f;

    // Mock texture atlas
    TextureAtlas atlas = createMockAtlas(animName, numFrames);
    Array<AtlasRegion> regions = atlas.findRegions(animName);
    SpriteBatch batch = mock(SpriteBatch.class);

    // Mock game time
    GameTime gameTime = mock(GameTime.class);
    ServiceLocator.registerTimeSource(gameTime);
    when(gameTime.getDeltaTime()).thenReturn(frameTime);

    // Start animation
    AnimationRenderComponent animator = new AnimationRenderComponent(atlas);
    Entity entity = new Entity();
    animator.setEntity(entity);
    animator.addAnimation(animName, frameTime);
    animator.startAnimation(animName);

    for (int i = 0; i < 5; i++) {
      // Each draw advances 1 frame, check that it matches for each
      animator.draw(batch);
      verify(batch).draw(
          regions.get(i),
          entity.getPosition().x,
          entity.getPosition().y,
          entity.getScale().x,
          entity.getScale().y
      );
    }
  }

  @Test
  void shouldFinish() {
    TextureAtlas atlas = createMockAtlas("test_name", 1);
    SpriteBatch batch = mock(SpriteBatch.class);

    GameTime gameTime = mock(GameTime.class);
    ServiceLocator.registerTimeSource(gameTime);
    when(gameTime.getDeltaTime()).thenReturn(1f);

    AnimationRenderComponent animator = new AnimationRenderComponent(atlas);
    Entity entity = new Entity();
    animator.setEntity(entity);
    animator.addAnimation("test_name", 1f);
    assertFalse(animator.isFinished());

    animator.startAnimation("test_name");
    assertFalse(animator.isFinished());

    animator.draw(batch);
    assertTrue(animator.isFinished());
  }

  @Test
  void shouldStopAnimation() {
    TextureAtlas atlas = createMockAtlas("test_name", 1);
    AnimationRenderComponent animator = new AnimationRenderComponent(atlas);
    animator.addAnimation("test_name", 1f);
    assertFalse(animator.stopAnimation());

    animator.startAnimation("test_name");
    assertTrue(animator.stopAnimation());
    assertNull(animator.getCurrentAnimation());
  }

  static TextureAtlas createMockAtlas(String animationName, int numRegions) {
    TextureAtlas atlas = mock(TextureAtlas.class);
    Array<AtlasRegion> regions = new Array<>(numRegions);
    for (int i = 0; i < numRegions; i++) {
      regions.add(mock(AtlasRegion.class));
    }
    when(atlas.findRegions(animationName)).thenReturn(regions);
    return atlas;
  }
}
package com.csse3200.game.rendering;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Can be rendered onto the screen given a Sprite batch.
 */
public interface Renderable extends Comparable<Renderable> {
  /**
   * Render the renderable. Should be called only by the renderer, not manually.
   * @param batch Batch to render to.
   */
  void render(SpriteBatch batch);

  /**
   * Z index controls rendering order within a layer. Higher Z index is drawn on top.
   * @return Z index
   */
  float getZIndex();

  /**
   * Layer to be rendered in. Higher layers will be rendered on top of lower layers.
   * @return layer
   */
  int getLayer();
}

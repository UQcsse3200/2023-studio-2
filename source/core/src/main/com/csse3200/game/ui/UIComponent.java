package com.csse3200.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.csse3200.game.rendering.RenderComponent;
import com.csse3200.game.rendering.Renderable;
import com.csse3200.game.services.ServiceLocator;

/**
 * A generic component for rendering onto the ui.
 */
public abstract class UIComponent extends RenderComponent implements Renderable {
  private static final int UI_LAYER = 2;
  public static final Skin skin =
      new Skin(Gdx.files.internal("kenney-rpg-expansion/kenneyrpg.json"));
  protected Stage stage;

  @Override
  public void create() {
    super.create();
    stage = ServiceLocator.getRenderService().getStage();
  }

  @Override
  public int getLayer() {
    return UI_LAYER;
  }

  @Override
  public float getZIndex() {
    return 1f;
  }
}

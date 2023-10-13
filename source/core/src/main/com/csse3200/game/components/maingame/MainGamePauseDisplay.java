package com.csse3200.game.components.maingame;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.csse3200.game.GdxGame;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Displays a button to exit the Main Game screen to the Main Menu screen.
 */
public class MainGamePauseDisplay extends UIComponent {
  private static final Logger logger = LoggerFactory.getLogger(MainGamePauseDisplay.class);
  private static final float Z_INDEX = 2f;
  private Table table;
  private GdxGame.ScreenType screen;

  public MainGamePauseDisplay(GdxGame.ScreenType screen){
    this.screen = screen;
  }

  @Override
  public void create() {
    super.create();
    addActors();
  }

  private void addActors() {
    table = new Table();
    table.top().right();
    table.setFillParent(true);

    TextButton mainMenuBtn = new TextButton("Menu", skin);
    // Triggers an event when the button is pressed.
    mainMenuBtn.addListener(
      new ChangeListener() {
        @Override
        public void changed(ChangeEvent changeEvent, Actor actor) {
          if (screen == GdxGame.ScreenType.BRICK_BREAKER_SCREEN || screen == GdxGame.ScreenType.EXTRACTOR_GAME || screen == GdxGame.ScreenType.SPACEMINI_SCREEN || screen == GdxGame.ScreenType.MINI_SCREEN || screen == GdxGame.ScreenType.TUTORIAL_SCREEN) {
            logger.debug("Pause button clicked");
            entity.getEvents().trigger("exitPressed");
          }
          else {
            logger.debug("Exit button clicked");
            entity.getEvents().trigger("pause");
          }
        }
      });

    table.add(mainMenuBtn).padTop(10f).padRight(10f);

    stage.addActor(table);
  }

  @Override
  public void draw(SpriteBatch batch) {
    // draw is handled by the stage
  }

  @Override
  public float getZIndex() {
    return Z_INDEX;
  }

  @Override
  public void dispose() {
    table.clear();
    super.dispose();
  }
}

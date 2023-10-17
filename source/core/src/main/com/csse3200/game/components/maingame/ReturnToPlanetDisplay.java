package com.csse3200.game.components.maingame;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import com.csse3200.game.GdxGame;
import org.slf4j.LoggerFactory;

import static com.csse3200.game.components.mainmenu.MainMenuActions.game;

/**
 * Displays a button to exit the current game screen to the current Planet screen.
 */
public class ReturnToPlanetDisplay extends UIComponent {
  private static final Logger logger = LoggerFactory.getLogger(ReturnToPlanetDisplay.class);
  private static final float Z_INDEX = 2f;
  private Table table;

  @Override
  public void create() {
    super.create();
    addActors();
  }

  /**
   * Add Table containing the Return and Restart buttons
   * that will be displayed on top left of the screen
   */
  private void addActors() {
    table = new Table();
    table.top().right();
    table.setFillParent(true);
    TextButton refill = new TextButton("Refill", skin);
    TextButton mainMenuBtn = new TextButton("Return", skin);
    TextButton restartBtn = new TextButton("Restart", skin);
    // Triggers an event when the button is pressed.
    mainMenuBtn.addListener(
            new ChangeListener() {
              @Override
              public void changed(ChangeEvent changeEvent, Actor actor) {
                logger.debug("Return button clicked");
                //Add once it is clear which planet it intends to return to
                entity.getEvents().trigger("exit");
              }
            });
    restartBtn.addListener(
            new ChangeListener() {
              @Override
              public void changed(ChangeEvent changeEvent, Actor actor) {
                logger.debug("Restart button clicked");
                entity.getEvents().trigger("restart");
              }
            }
    );
    refill.addListener(
            new ChangeListener() {
              @Override
              public void changed(ChangeEvent changeEvent, Actor actor) {
                logger.debug("Refill button clicked");
                game.setScreen(GdxGame.ScreenType.UPGRADE_SHOP);
              }
            }
    );
    table.add(mainMenuBtn).padTop(10f).padRight(10f);
    table.add(restartBtn).padTop(10f).padRight(10f);
    table.add(refill).padTop(10f).padRight(10f);
    //Return button on top left, Restart button right of return button
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

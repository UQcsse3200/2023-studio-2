package com.csse3200.game.components.maingame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.csse3200.game.GdxGame;
import com.csse3200.game.components.controls.ControlsScreenDisplay;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.csse3200.game.components.mainmenu.MainMenuActions.game;

/**
 * Displays a button to exit the Main Game screen to the Main Menu screen.
 */
public class MainGameExitDisplay extends UIComponent {
  private static final Logger logger = LoggerFactory.getLogger(MainGameExitDisplay.class);
  private static final float Z_INDEX = 2f;
  private Table table;

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
    Table exitFrame = new Table();
    exitFrame.setVisible(false);
    TextButton exitBtn = new TextButton("Exit", skin);
    TextButton controlMenuBtn = new TextButton("Controls", skin);
    TextButton resume = new TextButton("Resume", skin);
    exitFrame.add(exitBtn).pad(10f).row();
    exitFrame.add(controlMenuBtn).pad(10f).row();
    exitFrame.add(resume);
    // Triggers an event when the button is pressed.
    Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
    Color color = new Color(1f, 1f, 1f, 0.4f); // white with 30% opacity
    pixmap.setColor(color);
    pixmap.fill();
    Texture texture = new Texture(pixmap);
    TextureRegionDrawable drawable = new TextureRegionDrawable(new TextureRegion(texture));
    exitFrame.setBackground(drawable);
    pixmap.dispose();
    exitFrame.setPosition(Gdx.graphics.getWidth()/2f - 200, Gdx.graphics.getHeight()/2f - 50);
    exitFrame.setSize(500, 300);
    // Triggers an event when the button is pressed.
    mainMenuBtn.addListener(
      new ChangeListener() {
        @Override
        public void changed(ChangeEvent changeEvent, Actor actor) {
          logger.debug("Menu button clicked");
          exitFrame.setVisible(true);
        }
      });
    exitBtn.addListener(
            new ChangeListener() {
              @Override
              public void changed(ChangeEvent changeEvent, Actor actor) {
                logger.debug("Exit button clicked");
                entity.getEvents().trigger("exit");
              }
            });
    controlMenuBtn.addListener(
            new ChangeListener() {
              @Override
              public void changed(ChangeEvent changeEvent, Actor actor) {
                logger.debug("Control Screen button clicked");
                game.setScreen(GdxGame.ScreenType.CONTROL_SCREEN_GAME);
              }
            });
    resume.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        exitFrame.setVisible(false); // Hide the mini-frame when Current Game button is clicked
      }
    });

    table.add(mainMenuBtn).padTop(10f).padRight(10f);
    stage.addActor(exitFrame);
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

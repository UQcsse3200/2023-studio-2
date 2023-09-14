package com.csse3200.game.components.gamearea;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.csse3200.game.ui.UIComponent;

/**
 * Displays the name of the current game area.
 */
public class GameAreaDisplay extends UIComponent {
  private String gameAreaName = "";
  private Label title;
  //private Label alert;

  public GameAreaDisplay(String gameAreaName) {
    this.gameAreaName = gameAreaName;
  }

  @Override
  public void create() {
    super.create();
    addActors();
  }

  private void addActors() {
    title = new Label(this.gameAreaName, skin, "large");
    stage.addActor(title);

    /*alert = new Label("Hey, it's your Companion" , skin);
    alert.setFontScale(.5f);
    stage.addActor(alert);
    */

  }

  @Override
  public void draw(SpriteBatch batch)  {
    int screenHeight = Gdx.graphics.getHeight();
    float offsetX = 10f;
    float offsetY = 30f;

   // float offsetA = 500f;
   // float offsetB = 570f;

    title.setPosition(offsetX, screenHeight - offsetY);
   // alert.setPosition(offsetA, screenHeight - offsetB);
  }





  @Override
  public void dispose() {
    super.dispose();
    title.remove();
  }
}

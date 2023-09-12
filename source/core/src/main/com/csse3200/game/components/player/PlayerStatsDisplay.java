package com.csse3200.game.components.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;


/**
 * An ui component for displaying player stats, e.g. health, healthBar, Dodge Cool-down Bar.
 */
public class PlayerStatsDisplay extends UIComponent {
  Table table, table1;
  private Image heartImage;
  private Label healthLabel;
  private ProgressBar healthBar;
  private ProgressBar DodgeBar;
  private Label DodgeLabel;
  private final float healthWidth = 1000f;
  /**
   * Creates reusable ui styles and adds actors to the stage.
   */
  @Override
  public void create() {
    super.create();
    addActors();

    entity.getEvents().addListener("updateHealth", this::updatePlayerHealthUI);
    entity.getEvents().addListener("updateDodgeCool down", this::updateDodgeBarUI);
  }

  /**
   * Creates actors and positions them on the stage using a table.
   * @see Table for positioning options
   */
  private void addActors() {
    table = new Table();
    table.top().left();
    table.setFillParent(true);
    table.padTop(45f).padLeft(5f);
    table1 = new Table();
    table1.top().left();
    table1.setFillParent(true);
    table1.padTop(165f).padLeft(5f);

    //health Bar
    int health = entity.getComponent(CombatStatsComponent.class).getHealth();
    healthBar = new ProgressBar(0, 100, 1, false, skin);

    //setting initial value of health Bar
    healthBar.setValue(100);

    //setting the position of health Bar
    healthBar.setPosition(10, Gdx.graphics.getHeight()  - healthBar.getHeight());
    healthBar.setWidth(healthWidth);
    healthBar.setDebug(true);

    // Heart image
    float heartSideLength = 30f;
    heartImage = new Image(ServiceLocator.getResourceService().getAsset("images/heart.png", Texture.class));

    // Health text
    CharSequence healthText = String.format("Health: %d", health);
    healthLabel = new Label(healthText, skin, "large");

    // Dodge Text for cool down
    int dodge = entity.getComponent(KeyboardPlayerInputComponent.class).triggerDodgeEvent();
    CharSequence dodgeText = String.format("Dodge Cool down : %d" , dodge);
    DodgeLabel = new Label(dodgeText, skin, "large");


    // Dodge Cool down Bar
    DodgeBar = new ProgressBar(0, 100, 1, false, skin);

    //Setting initial value of Dodge Cool down  bar
    DodgeBar.setValue(100);

    // setting the position of Dodge Cool down Bar
    DodgeBar.setPosition(0, Gdx.graphics.getHeight() - healthBar.getHeight());
    DodgeBar.setWidth(200f);
    DodgeBar.setDebug(true);

    table.add(heartImage).size(heartSideLength).pad(5);
    table.add(healthLabel);
    table.add(healthBar);
    table1.add(DodgeLabel);
    table1.add(DodgeBar);
    stage.addActor(table);
    stage.addActor(table1);
  }

  @Override
  public void draw(SpriteBatch batch)  {
    // draw is handled by the stage
  }

  /**
   * Updates the player's health on the ui.
   * @param health player health
   */
  public void updatePlayerHealthUI(int health) {
    CharSequence text = String.format("Health: %d", health);
    healthLabel.setText(text);
    healthBar.setValue(health);
  }

  /**
   * Updates the Player's Dodge on the UI
   * @param dodge player Dodge
   */
  public void updateDodgeBarUI (int dodge) {
    CharSequence text = String.format("Dodge Cool down : %d", dodge);
    DodgeLabel.setText(text);
    DodgeBar.setValue(dodge);
  }

  @Override
  public void dispose() {
    super.dispose();
    heartImage.remove();
    healthLabel.remove();
    healthBar.remove();
    DodgeLabel.remove();
    DodgeBar.remove();
  }
}

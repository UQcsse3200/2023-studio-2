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

import java.util.Timer;
import java.util.TimerTask;


/**
 * An ui component for displaying player stats, e.g. health, healthBar, Dodge Cool-down Bar.
 */
public class PlayerStatsDisplay extends UIComponent {
  private Table healthTable;
  private Table dodgeTable;
  private Table livesTable;
  private Image heartImage;
  private Label healthLabel;
  public ProgressBar healthBar;
  private ProgressBar DodgeBar;
  private Label DodgeLabel;
  private Label livesLabel;
  private float healthWidth = 1000f;
  private Table maxLivesAlert;
  private Label maxLivesLabel;

  /**
   * Creates reusable ui styles and adds actors to the stage.
   */
  @Override
  public void create() {
    super.create();
    addActors();

    entity.getEvents().addListener("updateHealth", this::updatePlayerHealthUI);
    entity.getEvents().addListener("updateDodgeCooldown", this::updateDodgeBarUI);
    entity.getEvents().addListener("updateLives", this::updatePlayerLives);
    entity.getEvents().addListener("maxLivesAlert", this::maxLivesReached);
  }

  /**
   * Creates actors and positions them on the stage using a table.
   * @see Table for positioning options
   */
  private void addActors() {
    healthTable = new Table();
    livesTable = new Table();
    dodgeTable = new Table();
    healthTable.top().left();
    livesTable.top().left();
    healthTable.setFillParent(true);
    healthTable.padTop(45f).padLeft(5f);
    livesTable.setFillParent(true);
    livesTable.padTop(205f).padLeft(5f);
    dodgeTable = new Table();
    dodgeTable.top().left();
    dodgeTable.setFillParent(true);
    dodgeTable.padTop(165f).padLeft(5f);

    //health Bar
    int health = entity.getComponent(CombatStatsComponent.class).getHealth();
    healthBar = new ProgressBar(0, 100, 1, false, skin);

    //setting initial value of health Bar
    healthBar.setValue(100);

    //setting the position of health Bar
    healthBar.setWidth(healthWidth);
    healthBar.setDebug(true);
    healthBar.setPosition(10, Gdx.graphics.getHeight()  - healthBar.getHeight());

    // Heart image
    float heartSideLength = 30f;
    heartImage = new Image(ServiceLocator.getResourceService().getAsset("images/heart.png", Texture.class));

    // Health text
    CharSequence healthText = String.format("Health: %d", health);
    healthLabel = new Label(healthText, skin, "small");

    // Dodge Text for cool down
    int dodge = entity.getComponent(KeyboardPlayerInputComponent.class).triggerDodgeEvent();
    CharSequence dodgeText = String.format("Dodge Cool down : %d" , dodge);
    DodgeLabel = new Label(dodgeText, skin, "small");


    // Dodge Cool down Bar
    DodgeBar = new ProgressBar(0, 100, 1, false, skin);

    //Setting initial value of Dodge Cool down  bar
    DodgeBar.setValue(100);

    // setting the position of Dodge Cool down Bar
    DodgeBar.setPosition(0, Gdx.graphics.getHeight() - healthBar.getHeight());
    DodgeBar.setWidth(200f);
    DodgeBar.setDebug(true);

    //Player lives text
    int lives = entity.getComponent(CombatStatsComponent.class).getLives();
    CharSequence livesText = String.format("Lives Left: %d", lives);
    livesLabel = new Label(livesText, skin, "small");
    healthTable.add(heartImage).size(heartSideLength).pad(5);
    healthTable.add(healthLabel);
    healthTable.add(healthBar).padLeft(20);

//    dodgeTable.add(DodgeLabel);
//    dodgeTable.add(DodgeBar);
    livesTable.add(livesLabel);
    stage.addActor(healthTable);
    stage.addActor(dodgeTable);
    stage.addActor(livesTable);
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

  public void updatePlayerLives(int lives) {
    CharSequence livesText = String.format("Lives Left: %d", lives);
    livesLabel.setText(livesText);
  }

  /**
   * Alert for when maximum number of lives (3) has been reached. Is placed in the left corner below
   * number of lives player stats.
   */
  private void maxLivesAlert() {
    maxLivesAlert = new Table();
    maxLivesAlert.top().left();
    maxLivesAlert.setFillParent(true);
    maxLivesLabel = new Label("Max Player Lives Reached", skin, "small");

    maxLivesAlert.add(maxLivesLabel).padTop(250).padLeft(5f);
    //launch the table onto the screen
    stage.addActor(maxLivesAlert);
  }

  /**
   * Creates an alert for if the maximum number of lives (3) has been reached.
   * Used when player picks up Powerup ('Plus one life').
   */
  public void maxLivesReached() {
    maxLivesAlert(); // indicates to player that max number of lives has been reached
    final Timer timer = new Timer();
    TimerTask removeAlert = new TimerTask() {
      @Override
      public void run() {
        maxLivesLabel.remove();
        timer.cancel();
        timer.purge();
      }
    };
    timer.schedule(removeAlert, 3000); // removes alert after 1 second
  }

    @Override
  public void dispose() {
    super.dispose();
    heartImage.remove();
    healthLabel.remove();
    healthBar.remove();
    DodgeLabel.remove();
    DodgeBar.remove();
    maxLivesLabel.remove();
    livesLabel.remove();
  }
}

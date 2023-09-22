package com.csse3200.game.components.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.entities.configs.PlayerConfig;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;

import java.util.Timer;
import java.util.TimerTask;


/**
 * An ui component for displaying player stats, e.g. health, healthBar, Dodge Cool-down Bar.
 */
public class PlayerStatsDisplay extends UIComponent {
  Table container;
  Table statsTable;

  private int maxHealth;

  private float barWidth;

  private Label healthLabel;
  private Label dodgeLabel;
  private Label livesLabel;

  private Image healthBarFrame;
  private Image healthBarFill;
  private Image dodgeBarFrame;
  private Image livesBarFrame;

  private Table maxLivesAlert;
  private Label maxLivesLabel;

  public PlayerStatsDisplay(PlayerConfig config) {
    maxHealth = config.health;
    barWidth = 300f;

    //ADDING IMAGES
    healthBarFrame = new Image(ServiceLocator.getResourceService().getAsset("images/player/statbar.png", Texture.class));
    healthBarFill = new Image(ServiceLocator.getResourceService().getAsset("images/player/bar-fill.png", Texture.class));
    dodgeBarFrame = new Image(ServiceLocator.getResourceService().getAsset("images/player/statbar.png", Texture.class));
    livesBarFrame = new Image(ServiceLocator.getResourceService().getAsset("images/player/widestatbar.png", Texture.class));
  }

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

  @Override
  public void update() {
    super.update();
  }

  /**
   * Creates actors and positions them on the stage using a table.
   * @see Table for positioning options
   */
  private void addActors() {
    container = new Table();
    container.top().left();
    container.setFillParent(true);
    container.padTop(20f).padLeft(190f);

    statsTable = new Table();
    statsTable.top().left();
    container.setFillParent(true);

    //CREATING LABELS
    int health = entity.getComponent(CombatStatsComponent.class).getHealth();
    CharSequence healthText = String.format("%d", health);
    healthLabel = new Label(healthText, skin, "small");
    dodgeLabel = new Label("Ready!", skin, "small");
    livesLabel = new Label("Lives:", skin, "small");
    healthLabel.setFontScale(0.25f);
    dodgeLabel.setFontScale(0.25f);

    Table healthBarTable = new Table();
    healthBarTable.add(healthBarFill).size(260f, 30f).padRight(5).padTop(3);

    Stack healthStack = new Stack();
    healthStack.add(healthBarFrame);
    healthStack.add(healthBarTable);
    statsTable.add(healthStack).size(barWidth, 40f).pad(5);
    statsTable.add(healthLabel).left();

    statsTable.row();
    statsTable.add(dodgeBarFrame).size(300f, 40f).pad(5);
    statsTable.add(dodgeLabel).left();

    statsTable.row();
    statsTable.add(livesBarFrame).size(300f, 58f).pad(5);

    container.add(statsTable);
    stage.addActor(container);
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
    CharSequence text = String.format("%d", health);
    healthLabel.setText(health);
    barWidth = 260f * health / maxHealth;
    healthBarFill.setSize(barWidth, 30f);
  }

  /**
   * Updates the Player's Dodge on the UI
   * @param dodge player Dodge
   */
  public void updateDodgeBarUI (int dodge) {
    CharSequence text = String.format("Dodge Cool down : %d", dodge);
    //DodgeLabel.setText(text);
    //DodgeBar.setValue(dodge);
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
    healthLabel.remove();
    livesLabel.remove();
  }
}

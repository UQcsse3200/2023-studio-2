package com.csse3200.game.components.player;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.entities.configs.PlayerConfig;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;


/**
 * An ui component for displaying player stats, e.g. health, healthBar, Dodge Cool-down Bar.
 */
public class PlayerStatsDisplay extends UIComponent {
  Table container;
  Table statsTable;

  private int maxHealth;
  private int maxLives;

  private float barWidth;

  private Label healthLabel;
  private Label dodgeLabel;
  private Label livesLabel;

  private Image healthBarFrame;
  private Image healthBarFill;

  private Image dodgeBarFrame;
  private Image dodgeBarFill;

  private Image livesBarFrame;
  private Image livesHeart;

  private Table maxLivesAlert;
  private Label maxLivesLabel;

  private HashMap<Integer, Image> livesImages;

  public PlayerStatsDisplay(PlayerConfig config) {
    maxHealth = config.health;
    maxLives = config.lives;
    barWidth = 300f;
  }

  /**
   * Creates reusable ui styles and adds actors to the stage.
   */
  @Override
  public void create() {
    super.create();
    addActors();

    entity.getEvents().addListener("updateHealth", this::updatePlayerHealthUI);

    entity.getEvents().addListener("dodged", this::updateDodgeUsed);
    entity.getEvents().addListener("dodgeAvailable", this::updateDodgeRefreshed);

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

    createHealthBar(statsTable);
    statsTable.row();
    createDodgeBar(statsTable);
    statsTable.row();
    createLivesBar(statsTable);

    container.add(statsTable);
    stage.addActor(container);
  }

  public void createHealthBar(Table statsTable) {
    healthBarFrame = new Image(ServiceLocator.getResourceService().getAsset("images/player/statbar.png", Texture.class));
    healthBarFill = new Image(ServiceLocator.getResourceService().getAsset("images/player/bar-fill.png", Texture.class));

    Table healthBarTable = new Table();
    healthBarTable.add(healthBarFill).size(260f, 30f).padRight(5).padTop(3);

    Stack healthStack = new Stack();
    healthStack.add(healthBarFrame);
    healthStack.add(healthBarTable);

    statsTable.add(healthStack).size(barWidth, 40f).pad(5);
    statsTable.add(healthLabel).left();
  }

  public void createDodgeBar(Table statsTable) {
    dodgeBarFrame = new Image(ServiceLocator.getResourceService().getAsset("images/player/statbar.png", Texture.class));
    dodgeBarFill = new Image(ServiceLocator.getResourceService().getAsset("images/player/bar-fill2.png", Texture.class));

    Table dodgeBarTable = new Table();
    dodgeBarTable.add(dodgeBarFill).size(260f, 30f).padRight(5).padTop(3);

    Stack dodgeStack = new Stack();
    dodgeStack.add(dodgeBarFrame);
    dodgeStack.add(dodgeBarTable);
    statsTable.add(dodgeStack).size(barWidth, 40f).pad(5);
    statsTable.add(dodgeLabel).left();
  }

  public void createLivesBar(Table statsTable) {
    livesBarFrame = new Image(ServiceLocator.getResourceService().getAsset("images/player/widestatbar.png", Texture.class));
    livesImages = new HashMap<>();
    for (int lifeNum = 1; lifeNum <= maxLives; lifeNum++) {
      Image lifeImage = new Image(ServiceLocator.getResourceService().getAsset("images/player/heart.png", Texture.class));
      livesImages.put(lifeNum, lifeImage);
    }

    Table livesTable = new Table();
    for (Image lifeImage : livesImages.values()) {
      livesTable.add(lifeImage).size(30f, 26f).padRight(5).padTop(3);
    }

    Stack livesStack = new Stack();
    livesStack.add(livesBarFrame);
    livesStack.add(livesTable);
    statsTable.add(livesStack).size(300f, 58f).pad(5);

    System.out.println("#####################################################################################");
    System.out.println(maxLives);
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
    healthLabel.setText(health);
    barWidth = 260f * health / maxHealth;
    healthBarFill.setSize(barWidth, 30f);
  }

  /**
   * Updates the Player's Dodge on the UI
   */
  public void updateDodgeUsed() {
    CharSequence dodgeText = String.format("");
    dodgeLabel.setText(dodgeText);
    dodgeBarFill.setSize(0, 30f);

    dodgeBarFill.addAction(
            Actions.sequence(
                    Actions.parallel(
                            Actions.sizeTo(260f, dodgeBarFill.getHeight(), 0.7f, Interpolation.linear)
                    )
            )
    );
  }

  public void updateDodgeRefreshed() {
    CharSequence dodgeText = String.format("Ready!");
    dodgeLabel.setText(dodgeText);
    dodgeBarFill.setSize(260f, 30f);
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

    // TODO
  }
}

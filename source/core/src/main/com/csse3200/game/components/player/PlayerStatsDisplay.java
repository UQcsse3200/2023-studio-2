package com.csse3200.game.components.player;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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

import static java.util.Collections.max;


/**
 * An ui component for displaying player stats, e.g. health, dodge cool down and player lives
 */
public class PlayerStatsDisplay extends UIComponent {
  Table container;
  Table statsTable;
  private final int maxHealth;
  private int playerLives;
  private float barWidth;
  private Label healthLabel;
  private Label dodgeLabel;
  private Image healthBarFill;
  private Image dodgeBarFill;
  private Label maxLivesLabel;
  private Texture heartsTexture;
  private TextureRegion hearts;
  private Image livesBarFill;

  /**
   * Constructor for the PlayerStatsDisplay
   * @param config the player config file
   */
  public PlayerStatsDisplay(PlayerConfig config) {
    maxHealth = config.health;
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

    // Creating player HP and dodge labels
    int health = entity.getComponent(CombatStatsComponent.class).getHealth();
    CharSequence healthText = String.format("%d", health);
    String small = "small";
    healthLabel = new Label(healthText, skin, small);
    dodgeLabel = new Label("Ready!", skin, small);
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

  /**
   * Creates the heath bar for the player HUD
   * @param statsTable the table which the health bar is contained within
   */
  public void createHealthBar(Table statsTable) {
    Image healthBarFrame;
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

  /**
   * Creates the dodge bar for the HUD
   * @param statsTable the table which the dodge bar is contained within
   */
  public void createDodgeBar(Table statsTable) {
    Image dodgeBarFrame;
    dodgeBarFrame = new Image(ServiceLocator.getResourceService().getAsset("images/player/statbar.png", Texture.class));
    dodgeBarFill = new Image(ServiceLocator.getResourceService().getAsset("images/player/bar-fill2.png", Texture.class));

    Table dodgeBarTable = new Table();
    dodgeBarTable.add(dodgeBarFill).size((barWidth - 40f), 30f).padRight(5).padTop(3);

    Stack dodgeStack = new Stack();
    dodgeStack.add(dodgeBarFrame);
    dodgeStack.add(dodgeBarTable);
    statsTable.add(dodgeStack).size(barWidth, 40f).pad(5);
    statsTable.add(dodgeLabel).left();
  }

  /**
   * Creates the player lives UI for the HUD
   * @param statsTable the table which the player lives information is contained within
   */
  public void createLivesBar(Table statsTable) {
    playerLives = entity.getComponent(CombatStatsComponent.class).getLives();
    Image livesBarFrame;
    livesBarFrame = new Image(ServiceLocator.getResourceService().getAsset("images/player/widestatbar.png", Texture.class));

    heartsTexture = ServiceLocator.getResourceService().getAsset("images/player/hearts.png", Texture.class);
    hearts = new TextureRegion(heartsTexture,
            playerLives * 15,
            13);
    livesBarFill = new Image(hearts);

    Table heartsTable = new Table();
    heartsTable.left().padTop(10).padLeft(17);
    heartsTable.add(livesBarFill).size(playerLives * 30f, 26f);

    Table livesTable = new Table();
    livesTable.add(livesBarFrame).size(150f, 65f);

    Stack livesStack = new Stack();
    livesStack.add(livesTable);
    livesStack.add(heartsTable);
    statsTable.add(livesStack).left().pad(5);
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
   * Updates the visible avaliability of the dodge movement when used by the player
   */
  public void updateDodgeUsed() {
    CharSequence dodgeText = "";
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

  /**
   * Updates the visible avaliability of the dodge when it becomes available
   */
  public void updateDodgeRefreshed() {
    CharSequence dodgeText = "Ready!";
    dodgeLabel.setText(dodgeText);
    dodgeBarFill.setSize(260f, 30f);
  }

  /**
   * Updates the number of lives displayed in the player HUD
   * @param lives the number that is being updated to
   */
  public void updatePlayerLives(int lives) {
    hearts.setRegionWidth(lives*15);
    livesBarFill.setWidth(lives*30);
  }

  /**
   * Alert for when maximum number of lives (3) has been reached. Is placed in the left corner below
   * number of lives player stats.
   */
  private void maxLivesAlert() {
    Table maxLivesAlert;
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
    dodgeLabel.remove();
    healthBarFill.remove();
    dodgeBarFill.remove();
    livesBarFill.remove();
  }
}

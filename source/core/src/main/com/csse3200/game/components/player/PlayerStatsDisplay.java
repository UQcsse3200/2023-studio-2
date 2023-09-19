package com.csse3200.game.components.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;


/**
 * An ui component for displaying player stats, e.g. health, healthBar, Dodge Cool-down Bar.
 */
public class PlayerStatsDisplay extends UIComponent {
  Table container;
  Table planetTable;
  Table statsTable;
  //Table table2;
  //Table table1;
  private Image heartImage;
  public ProgressBar healthBar;
  private ProgressBar DodgeBar;
  private float healthWidth = 1000f;

  private Label mapLabel;
  private Label healthLabel;
  private Label dodgeLabel;
  private Label livesLabel;

  private Image planetImageFrame;
  private Image planetImage;
  private Image healthBarFrame;
  private Image healthBarFill;
  private Image dodgeBarFrame;
  private Image livesBarFrame;

  public PlayerStatsDisplay(String mapName) {
    mapLabel = new Label(mapName, skin, "small");
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
  }

  /**
   * Creates actors and positions them on the stage using a table.
   * @see Table for positioning options
   */
  private void addActors() {
    container = new Table();
    container.top().left();
    container.setFillParent(true);
    container.padTop(5f).padLeft(5f);

    planetTable = new Table();

    statsTable = new Table();
    statsTable.top().left();
    container.setFillParent(true);

    //table2 = new Table();
    //table2.top().left();
    //statsTable.setFillParent(true);
    //statsTable.padTop(45f).padLeft(5f);
    //table2.setFillParent(true);
    //table2.padTop(205f).padLeft(5f);
    //table1 = new Table();
    //table1.top().left();
    //table1.setFillParent(true);
    //table1.padTop(165f).padLeft(5f);

    //ADDING IMAGES
    planetImageFrame = new Image(ServiceLocator.getResourceService().getAsset("images/player/planet-frame.png", Texture.class));
    planetImage = new Image(ServiceLocator.getResourceService().getAsset("images/space_navigation_planet_0.png", Texture.class));
    healthBarFrame = new Image(ServiceLocator.getResourceService().getAsset("images/player/statbar.png", Texture.class));
    healthBarFill = new Image(ServiceLocator.getResourceService().getAsset("images/player/bar-fill.png", Texture.class));
    dodgeBarFrame = new Image(ServiceLocator.getResourceService().getAsset("images/player/statbar.png", Texture.class));
    livesBarFrame = new Image(ServiceLocator.getResourceService().getAsset("images/player/widestatbar.png", Texture.class));


    //health Bar
    int health = entity.getComponent(CombatStatsComponent.class).getHealth();
    healthBar = new ProgressBar(0, 100, 1, false, skin);

    //CREATING LABELS
    CharSequence healthText = String.format("%d", health);
    healthLabel = new Label(healthText, skin, "small");
    dodgeLabel = new Label("Ready!", skin, "small");
    livesLabel = new Label("Lives:", skin, "small");
    //healthLabel.setAlignment(Align.left);
    //dodgeLabel.setAlignment(Align.left);

    //setting initial value of health Bar
    healthBar.setValue(100);

    //setting the position of health Bar
    healthBar.setWidth(healthWidth);
    healthBar.setDebug(true);
    healthBar.setPosition(10, Gdx.graphics.getHeight()  - healthBar.getHeight());

    // Dodge Text for cool down
    //int dodge = entity.getComponent(KeyboardPlayerInputComponent.class).triggerDodgeEvent();
    //CharSequence dodgeText = String.format("Dodge Cool down : %d" , dodge);
    //DodgeLabel = new Label(dodgeText, skin, "small");

    // Dodge Cool down Bar
    //DodgeBar = new ProgressBar(0, 100, 1, false, skin);

    //Setting initial value of Dodge Cool down  bar
    //DodgeBar.setValue(100);

    // setting the position of Dodge Cool down Bar
    //DodgeBar.setPosition(0, Gdx.graphics.getHeight() - healthBar.getHeight());
    //DodgeBar.setWidth(200f);
    //DodgeBar.setDebug(true);

    //Player lives text
    //int lives = entity.getComponent(CombatStatsComponent.class).getLives();
    //CharSequence livesText = String.format("Lives Left: %d", lives);
    //livesLabel = new Label(livesText, skin, "small");



    Table labelTable = new Table();
    labelTable.add(planetImage).size(140f).padLeft(5f).padBottom(8f).padTop(-2f);
    labelTable.row();
    labelTable.add(mapLabel);

    Stack planetStack = new Stack();
    planetStack.add(planetImageFrame);
    planetStack.add(labelTable);
    planetTable.add(planetStack).size(180f, 225f);
    planetTable.row();

    Stack healthStack = new Stack();
    healthStack.add(healthBarFrame);
    healthStack.add(healthBarFill);
    statsTable.add(healthStack).size(300f, 40f).pad(5);
    statsTable.add(healthLabel).left();

    statsTable.row();
    statsTable.add(dodgeBarFrame).size(300f, 40f).pad(5);
    statsTable.add(dodgeLabel).left();

    statsTable.row();
    statsTable.add(livesBarFrame).size(300f, 58f).pad(5);


    container.add(planetTable);
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
    healthLabel.setText(text);
    healthBar.setValue(health);
  }

  /**
   * Updates the Player's Dodge on the UI
   * @param dodge player Dodge
   */
  public void updateDodgeBarUI (int dodge) {
    CharSequence text = String.format("Dodge Cool down : %d", dodge);
    //DodgeLabel.setText(text);
    DodgeBar.setValue(dodge);
  }

  public void updatePlayerLives(int lives) {
    CharSequence livesText = String.format("Lives Left: %d", lives);
    livesLabel.setText(livesText);
  }

  @Override
  public void dispose() {
    super.dispose();
    heartImage.remove();
    healthLabel.remove();
    healthBar.remove();
    //DodgeLabel.remove();
    DodgeBar.remove();
    livesLabel.remove();
  }
}

package com.csse3200.game.components.player;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.Weapons.WeaponType;
import com.csse3200.game.components.upgradetree.UpgradeDisplay;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.PlayerConfig;
import com.csse3200.game.entities.configs.WeaponConfig;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;
import java.util.Timer;
import java.util.TimerTask;


/**
 * An ui component for displaying player stats, e.g. health, dodge cool down and player lives
 */
public class PlayerStatsDisplay extends UIComponent {
  private final String labelStyle;
  private Table container;
  private Table statsTable;
  private final int maxHealth;
  private float healthBarWidth;
  private float dodgeBarWidth;
  private Label healthLabel;
  private Label dodgeLabel;
  private Image healthBarFill;
  private Image dodgeBarFill;
  private Label maxLivesLabel;
  private TextureRegion hearts;
  private Image livesBarFill;
  private InventoryComponent inventory;
  private Label ammoLabel;
  private Table weaponImageTable;

  /**
   * Constructor for the PlayerStatsDisplay
   * @param config the player config file
   */
  public PlayerStatsDisplay(PlayerConfig config) {
    labelStyle = "small";
    maxHealth = config.health;
    healthBarWidth = 320f;
    dodgeBarWidth = 320f;
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
    entity.getEvents().addListener("updateAmmo", this::updateAmmo);
    entity.getEvents().addListener("changeWeapon", this::updateWeapon);

    InventoryComponent invComp = entity.getComponent(InventoryComponent.class);
    this.updateAmmo(invComp.getCurrentAmmo(),invComp.getCurrentMaxAmmo(), invComp.getCurrentAmmoUse());
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
    healthLabel = new Label(healthText, skin, labelStyle);
    dodgeLabel = new Label("Ready!", skin, labelStyle);
    healthLabel.setFontScale(0.25f);
    dodgeLabel.setFontScale(0.25f);

    createHealthBar(statsTable);
    statsTable.row();
    createDodgeBar(statsTable);
    statsTable.row();
    Table innerTable = new Table();
    createLivesBar(innerTable);
    createAmmoBar(innerTable);
    statsTable.add(innerTable).left();

    Stack planetDisplay = new Stack();
    container.add(statsTable);
    container.add(planetDisplay).size(170f,225f);
    container.row();
    createUpgradeTreeButton(container);
    stage.addActor(container);
  }

  /**
   * @param table - Used to add Column/Rows and define the actors
   * createUpgradeTreeButton() - creating button and defining it on the top left
   *                              also playing the sound when on tapping it
   *
   */

  public void createUpgradeTreeButton(Table table) {
    TextButton button = new TextButton("Upgrade Tree", skin);

    button.addListener(new ChangeListener() {
      @Override
      public void changed(ChangeEvent event, Actor actor) {

        if (event instanceof ChangeEvent) {
          KeyboardPlayerInputComponent keys =
                  ServiceLocator.getEntityService().getPlayer().getComponent(KeyboardPlayerInputComponent.class);
          keys.clearWalking();
          UpgradeDisplay display = UpgradeDisplay.createUpgradeDisplay();
          ServiceLocator.getRenderService().getStage().addActor(display);

          entity.getEvents().trigger("playSound", "upgradeTree");
        }
      }
    });
    table.add(button).left();
  }

  /**
   * Creates the heath bar for the player HUD
   * @param parentTable the table which the health bar is contained within
   */
  public void createHealthBar(Table parentTable) {
    Image healthBarFrame;
    healthBarFrame = new Image(ServiceLocator.getResourceService().getAsset("images/player/statbar.png", Texture.class));
    healthBarFill = new Image(ServiceLocator.getResourceService().getAsset("images/player/bar-fill.png", Texture.class));

    Table healthBarTable = new Table();
    healthBarTable.add(healthBarFill).size(healthBarWidth - 40f, 30f).padRight(5).padTop(3);

    Stack healthStack = new Stack();
    healthStack.add(healthBarFrame);
    healthStack.add(healthBarTable);

    parentTable.add(healthStack).size(healthBarWidth, 40f).pad(5);
    parentTable.add(healthLabel).left();
  }

  /**
   * Creates the dodge bar for the HUD
   * @param parentTable the table which the dodge bar is contained within
   */
  public void createDodgeBar(Table parentTable) {
    Image dodgeBarFrame;
    dodgeBarFrame = new Image(ServiceLocator.getResourceService().getAsset("images/player/statbar.png", Texture.class));
    dodgeBarFill = new Image(ServiceLocator.getResourceService().getAsset("images/player/bar-fill2.png", Texture.class));

    Table dodgeBarTable = new Table();
    dodgeBarTable.add(dodgeBarFill).size((dodgeBarWidth - 40f), 30f).padRight(5).padTop(3);

    Stack dodgeStack = new Stack();
    dodgeStack.add(dodgeBarFrame);
    dodgeStack.add(dodgeBarTable);
    parentTable.add(dodgeStack).size(dodgeBarWidth, 40f).pad(5).padTop(10);
    parentTable.add(dodgeLabel).left();
  }

  /**
   * Creates the player lives UI for the HUD
   * @param parentTable the table which the player lives information is contained within
   */
  public void createLivesBar(Table parentTable) {
    int playerLives = entity.getComponent(CombatStatsComponent.class).getLives();
    Image livesBarFrame;
    livesBarFrame = new Image(ServiceLocator.getResourceService().getAsset("images/player/widestatbar.png", Texture.class));

    Texture heartsTexture = ServiceLocator.getResourceService().getAsset("images/player/hearts.png", Texture.class);
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
    parentTable.add(livesStack).left().padLeft(5).padRight(5);
  }

  /**
   * Creates the weapons ammo UI for the HUD
   * @param parentTable the table which the ammo information is contained within
   */
  public void createAmmoBar(Table parentTable) {
    Entity player = ServiceLocator.getEntityService().getPlayer();
    inventory = player.getComponent(InventoryComponent.class);

    Image ammoBarFrame;
    ammoBarFrame = new Image(ServiceLocator.getResourceService().getAsset("images/player/widestatbar.png", Texture.class));

    WeaponConfig config = inventory.getConfigs().GetWeaponConfig(inventory.getEquippedType());
    Image weaponImage = new Image( new Texture(config.imagePath));
    int currentAmmo = inventory.getCurrentAmmo();
    int maxAmmo = inventory.getCurrentMaxAmmo();
    CharSequence ammoText = String.format("%d / %d", currentAmmo, maxAmmo);
    ammoLabel = new Label(ammoText, skin, labelStyle);
    ammoLabel.setFontScale(0.21f);

    Table ammoFrameTable = new Table();
    ammoFrameTable.add(ammoBarFrame).size(150f, 65f);

    weaponImageTable = new Table();
    weaponImageTable.add(weaponImage).size(25f);

    Table ammoInfo = new Table();
    ammoInfo.add(weaponImageTable).left().pad(5).padTop(10);
    ammoInfo.add(ammoLabel).size(80f).right().pad(5).padTop(10);

    Stack ammoStack = new Stack();
    ammoStack.add(ammoFrameTable);
    ammoStack.add(ammoInfo);
    parentTable.add(ammoStack).left().padLeft(5).padRight(5);
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
    healthBarWidth = 280f * health / maxHealth;
    healthBarFill.setSize(healthBarWidth, 30f);
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
                            Actions.sizeTo(dodgeBarWidth - 40f, dodgeBarFill.getHeight(), 0.7f, Interpolation.linear)
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
    dodgeBarFill.setSize(dodgeBarWidth - 40f, 30f);
  }

  /**
   * Updates the number of lives displayed in the player HUD
   * @param lives the number that is being updated to
   */
  public void updatePlayerLives(int lives) {
    hearts.setRegionWidth(lives * 15);
    livesBarFill.setWidth(lives * 30f);
  }

  /**
   * Updates the current and max ammo displaying in the player HUD
   * @param currentAmmo the current ammo of the weapon equipped
   * @param maxAmmo the max ammo of the weapon equipped
   */
  public void updateAmmo(int currentAmmo, int maxAmmo, int ammoUse) {
    CharSequence ammoText = String.format("%d / %d", currentAmmo, maxAmmo);
    if (ammoUse == 0) {
      ammoText = "    -";
    }
    ammoLabel.setText(ammoText);
  }

  /**
   * Updates the weapon equipped in the player HUD
   * @param weapon the new weapon
   */
  public void updateWeapon(WeaponType weapon) {
    WeaponConfig config = inventory.getConfigs().GetWeaponConfig(weapon);
    Image weaponImage = new Image( new Texture(config.imagePath));
    weaponImageTable.clear();
    weaponImageTable.add(weaponImage).size(30f);
    updateAmmo(inventory.getCurrentAmmo(), inventory.getCurrentMaxAmmo(), inventory.getCurrentAmmoUse());
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
    maxLivesLabel = new Label("Max Player Lives Reached", skin, labelStyle);

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

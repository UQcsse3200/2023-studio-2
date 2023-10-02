package com.csse3200.game.components.Companion;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.CompanionWeapons.CompanionWeaponType;
import com.csse3200.game.components.Weapons.WeaponType;
import com.csse3200.game.components.player.InventoryComponent;
import com.csse3200.game.components.player.KeyboardPlayerInputComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.CompanionWeaponConfig;
import com.csse3200.game.entities.configs.WeaponConfig;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;


/**
 * A UI component for displaying Companion stats, e.g., health.
 */
public class CompanionStatsDisplay extends UIComponent {
    Table companionStatisticsUI;
    private Table weaponImageTable;
    Table container;
    Table statsTable;

    private boolean update = false;

    Table playerLowHealthAlert;

    Table titleTable;
    private long duration;
    private CompanionInventoryComponent inventory;



    /**
     * The player entity associated with this CompanionStatsDisplay.
     */
    public Entity player = ServiceLocator.getEntityService().getPlayer();
    public Entity companion = ServiceLocator.getEntityService().getCompanion();

    /**
     * The UI playerLowHealthLabel for displaying the companion's health.
     */
    public Label playerLowHealthLabel;

    public Label companionHealthLabel; //this is the label for the companions health displayed

    public Label companionUIHeaderLabel; // label for the header of the UI component.
    public Label companionModeLabel; // label for the companions mode

    private boolean isInvincible = true;
    private boolean isInfiniteHealth = true;
    private Label ammoLabel;

    /**
     * Default constructor for CompanionStatsDisplay.
     */
    public CompanionStatsDisplay() {
    }
    /**
     * Creates reusable UI styles and adds actors to the stage.
     */
    @Override
    public void create() {
        super.create();
        addActors();

        // Listen for events related to health updates
        entity.getEvents().addListener("updateHealth", this::updateCompanionHealthUI);
        entity.getEvents().addListener("companionModeChange", this::updateCompanionModeUI);
        entity.getEvents().addListener("updateAmmo", this::updateAmmo);
//        entity.getEvents().addListener("changeWeapon", this::updateWeapon);
    }
    public void updateAmmo(int currentAmmo, int maxAmmo) {
        CharSequence ammoText = String.format("%d / %d", currentAmmo, maxAmmo);
        if (maxAmmo == 100) {  // todo: make non-ammo things not have ammo
            ammoText = "    -";
        }
        ammoLabel.setText(ammoText);
    }
    private CompanionInventoryComponent Inventory;

//    public void updateWeapon(CompanionWeaponType weapon) {
//        CompanionWeaponConfig config = CompanionInventoryComponent.getConfigs().GetWeaponConfig(weapon);
//        Image weaponImage = new Image( new Texture(config.imagePath));
//        weaponImageTable.clear();
//        weaponImageTable.add(weaponImage).size(30f);
//        updateAmmo(Inventory.GetCurrentAmmo(), Inventory.GetCurrentMaxAmmo());
//    }

    public void createInventoryButton(Table statsTable) {
        TextButton button = new TextButton("Inventory", skin);

        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                KeyboardPlayerInputComponent keys =
                        ServiceLocator.getEntityService().getPlayer().getComponent(KeyboardPlayerInputComponent.class);
                /*keys.clearWalking();*/
                CompanionInventoryComponent inventoryComponent = new CompanionInventoryComponent();
                CompanionInventoryDisplay display = CompanionInventoryDisplay.createUpgradeDisplay(inventoryComponent);
                ServiceLocator.getRenderService().getStage().addActor(display);
            }
        });

        statsTable.add(button);
    }

    /**
     * Creates actors and positions them on the stage using a companionStatisticsUI.
     * This means that the UI components are initialised and their locations are set, as well as their starting values
     * See {@link Table} for positioning options.
     */
    private void addActors() {
        companionStatisticsUI = new Table();
        companionStatisticsUI.top().right();
        companionStatisticsUI.setFillParent(true);
        //placing the companionStatisticsUI/UI on a certain portion of the screen!
        /*companionStatisticsUI.padTop(85f).padRight(5f);*/

        container = new Table();
        container.top().right();
        container.setFillParent(true);
        //container.padTop(20f).padLeft(190f);

        statsTable = new Table();
        statsTable.padTop(230f).padRight(20f);
        container.setFillParent(true);

        // ADD A COMPANION UI HEADER
        CharSequence companionUIHeader = "Companion";
        companionUIHeaderLabel = new Label(companionUIHeader, skin, "title");
        companionStatisticsUI.add(companionUIHeaderLabel);
        companionStatisticsUI.row();

        companionStatisticsUI.padTop(100f).padRight(5f);

        // ADD THE COMPANIONS HEALTH INFORMATION
        int companionHealth = entity.getComponent(CombatStatsComponent.class).getHealth();
        CharSequence companionHealthText = String.format("Health: %d", companionHealth);
        companionHealthLabel = new Label(companionHealthText, skin, "small");
        companionStatisticsUI.add(companionHealthLabel);
        companionStatisticsUI.row();


        // ADD THE COMPANIONS MODE INFORMATION
        CharSequence companionModeText = "Mode: Normal";
        companionModeLabel = new Label(companionModeText, skin, "small");
        companionStatisticsUI.add(companionModeLabel);
        companionStatisticsUI.row();

        //finally
        stage.addActor(companionStatisticsUI);

        createInventoryButton(statsTable);

        container.add(statsTable);
        stage.addActor(container);
//        Table innerTable = new Table();
//        createAmmoBar(innerTable);
//        statsTable.add(innerTable).left();
//
//        container.add(statsTable);
//        stage.addActor(container);

    }



//    public void createAmmoBar(Table parentTable) {
//        companion = ServiceLocator.getEntityService().getCompanion();
//        inventory = companion.getComponent(CompanionInventoryComponent.class);
//
//        Image ammoBarFrame;
//        ammoBarFrame = new Image(ServiceLocator.getResourceService().getAsset("images/player/widestatbar.png", Texture.class));
//// Assuming PlayerStatsDisplay is the class with the getConfigs() method
//        CompanionStatsDisplay companionStatsDisplay = new CompanionStatsDisplay(); // Create an instance
//
//// Now, you can call getConfigs() on the instance
//        CompanionWeaponConfig weaponConfig = companionStatsDisplay.getConfigs().GetWeaponConfig(weaponType);
//
//        CompanionWeaponConfig config = CompanionInventoryComponent.getConfigs().GetWeaponConfig(inventory.getEquippedType());
//        Image weaponImage = new Image( new Texture(config.imagePath));
//        int currentAmmo = Inventory.GetCurrentAmmo();
//        int maxAmmo = Inventory.GetCurrentMaxAmmo();
//        CharSequence ammoText = String.format("%d / %d", currentAmmo, maxAmmo);
//        ammoLabel = new Label(ammoText, skin, "small");
//        ammoLabel.setFontScale(0.21f);
//
//        Table ammoFrameTable = new Table();
//        ammoFrameTable.add(ammoBarFrame).size(150f, 65f);
//
//        weaponImageTable = new Table();
//        weaponImageTable.add(weaponImage).size(25f);
//
//        Table ammoInfo = new Table();
//        ammoInfo.add(weaponImageTable).left().pad(5).padTop(10);
//        ammoInfo.add(ammoLabel).size(80f).right().pad(5).padTop(10);
//
//        Stack ammoStack = new Stack();
//        ammoStack.add(ammoFrameTable);
//        ammoStack.add(ammoInfo);
//        parentTable.add(ammoStack).left().padLeft(5).padRight(5);
//    }

    /**
     * Set the companion's image to an invincible state.
     */
    /*public void setInvincibleImage() {
        AnimationRenderComponent infanimator = ServiceLocator.getGameArea().getCompanion().getComponent(AnimationRenderComponent.class);
        infanimator.startAnimation("LEFT_1");
    }*/

    /**
     * Toggle invincibility for the companion.
     */
    public void toggleInvincibility() {
        if (isInvincible) {
            /*setInvincibleImage();*/
            isInvincible = false;

            // Schedule a task to reset the image after a delay (e.g., 10 seconds)
            Timer.schedule(new Task() {
                @Override
                public void run() {
                    resetImage();
                }
            }, 10.0f);
        }
    }

    /**
     * Reset the companion's image.
     */
    public void resetImage() {
        AnimationRenderComponent animator = ServiceLocator.getEntityService().getCompanion().getComponent(AnimationRenderComponent.class);
        animator.startAnimation("RIGHT");
    }

    /**
     * Toggle infinite health for the companion.
     */
    public void toggleInfiniteHealth() {
        if (isInfiniteHealth) {
            companion.getComponent(CombatStatsComponent.class).setImmunity(true);
            player.getComponent(CombatStatsComponent.class).setImmunity(true);
            isInfiniteHealth = false;
            this.setDuration(6000);
            // Schedule a task to reset health to a normal value after a delay (e.g., 10 seconds)
            java.util.TimerTask health = new java.util.TimerTask()  {
                @Override
                public void run() {
                    companion.getComponent(CombatStatsComponent.class).setImmunity(false);
                    player.getComponent(CombatStatsComponent.class).setImmunity(false);

                }
            };
            new java.util.Timer().schedule(health, getDuration());
        }
    }


    @Override
    public void draw(SpriteBatch batch) {
        // Code for drawing UI elements and updating the projection matrix.
    }

    /**
     * Updates the companion's health UI.
     *
     * @param health The updated health value to display.
     */
    public void updateCompanionHealthUI(int health) {
        CharSequence text = String.format("Health: %d", health);
        companionHealthLabel.setText(text);
    }

    /**
     * updating the companion UI to include the mode
     * @param newMode - the mode sent by the CompanionActions trigger to be put on screen
     */
    public void updateCompanionModeUI(String newMode) {
        CharSequence companionModeText = String.format("Mode: %s", newMode);
        companionModeLabel.setText(companionModeText);
    }

    /**
     * remove all labels form the screen when disposing
     */
    @Override
    public void dispose() {
        super.dispose();
        companionHealthLabel.remove();
        if (playerLowHealthLabel != null) playerLowHealthLabel.remove();
        /*companionUIHeaderLabel.remove();*/
    }
    public void setDuration(long duration) {
        this.duration = duration;
    }
    public long getDuration() {
        return duration;
    }
}

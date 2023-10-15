package com.csse3200.game.components.Companion;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.player.KeyboardPlayerInputComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.CompanionConfig;
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
    private final String labelStyle;
    private Image healthBarFill;


    Table companionStatisticsUI;
    Table container;

    private final int maxHealth;
    private float healthBarWidth;

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
    private CompanionInventoryComponent Inventory;

    /**
     * Default constructor for CompanionStatsDisplay.
     */
    public CompanionStatsDisplay(CompanionConfig config) {
        labelStyle = "small";
        maxHealth = config.health;
        healthBarWidth = 320f;
    }
    /**
     * Creates reusable UI styles and adds actors to the stage.
     * Check this
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



//    public void updateWeapon(CompanionWeaponType weapon) {
//        CompanionWeaponConfig config = CompanionInventoryComponent.getConfigs().GetWeaponConfig(weapon);
//        Image weaponImage = new Image( new Texture(config.imagePath));
//        weaponImageTable.clear();
//        weaponImageTable.add(weaponImage).size(30f);
//        updateAmmo(Inventory.GetCurrentAmmo(), Inventory.GetCurrentMaxAmmo());
//    }



    /**
     * Creates actors and positions them on the stage using a companionStatisticsUI.
     * This means that the UI components are initialised and their locations are set, as well as their starting values
     * See {@link Table} for positioning options.
     */
    private void addActors() {
        //container is fitting all the UI elements
        //CONTAINER GOES ON THE BOTTOM LEFT
        container = new Table();
        container.bottom().left();
        container.setFillParent(true);

        //this is the table involving all the stats for the companion
        companionStatisticsUI = new Table();
        companionStatisticsUI.top().left();
        container.setFillParent(true);
        companionStatisticsUI.padTop(10f).padRight(5f).padLeft(15f).padBottom(15f);


        // ADD A COMPANION UI HEADER
        CharSequence companionUIHeader = "Companion";
        companionUIHeaderLabel = new Label(companionUIHeader, skin, labelStyle);
        companionStatisticsUI.add(companionUIHeaderLabel);
        companionStatisticsUI.row();

        //Create the numeric HP of the companion
        int health = entity.getComponent(CombatStatsComponent.class).getHealth();
        CharSequence healthText = String.format("%d", health);
        companionHealthLabel = new Label(healthText, skin, labelStyle);
        companionHealthLabel.setFontScale(0.25f);


        //Add the companions health bar
        createHealthBar(companionStatisticsUI);
        companionStatisticsUI.row();
        //Add the companions Mode section
        createModesBar(companionStatisticsUI);
        companionStatisticsUI.row();
        //Add the inventory button below
        createInventoryButton(companionStatisticsUI);

        // Place all smaller UI objects into container
        container.add(companionStatisticsUI);

        // Place container on the screen
        stage.addActor(container);

    }

    /**
     * Creates the Companions health bar
     * @param statsTable - the table to add the row to
     */
    public void createHealthBar(Table statsTable) {
        Image healthBarFrame;
        healthBarFrame = new Image(ServiceLocator.getResourceService().getAsset("images/player/statbar.png", Texture.class));
        healthBarFill = new Image(ServiceLocator.getResourceService().getAsset("images/player/bar-fill.png", Texture.class));

        Table healthBarTable = new Table();
        healthBarTable.add(healthBarFill).size(healthBarWidth - 40f, 30f).padRight(5).padTop(3);

        Stack healthStack = new Stack();
        healthStack.add(healthBarFrame);
        healthStack.add(healthBarTable);

        statsTable.add(healthStack).size(healthBarWidth, 40f).pad(5);
        statsTable.add(companionHealthLabel).left();
    }

    /**
     * This function will create the MODES seciton of the UI
     * It has a grey background, and can dynamically update the MODES
     * @param statsTable - the table to push to
     */
    public void createModesBar(Table statsTable) {
        //PART 1
        //get the background image
        Image backgroundBarFrame;
        backgroundBarFrame = new Image(ServiceLocator.getResourceService().getAsset("images/player/longandwidestatbar.png", Texture.class));

        //create the modes text
        CharSequence companionModeText = "Mode: Normal";
        companionModeLabel = new Label(companionModeText, skin, labelStyle);
        companionModeLabel.setFontScale(0.3f);

        //PART 2
        //create the mode table
        Table modesTable = new Table();
        //add the background
        modesTable.add(backgroundBarFrame).size(healthBarWidth, 65f);

        // add text to a table
        Table modesTextTable = new Table();
        modesTextTable.padTop(10).padLeft(20);
        modesTextTable.add(companionModeLabel);

        //PART 3
        //CREATE THE STACK TO LAY THE THINGS OVER EACH OTHER
        Stack modesStack = new Stack();
        modesStack.add(modesTable);
        modesStack.add(modesTextTable);
        //add the text over the background

        //send to the parent
        statsTable.add(modesStack).pad(5).left();


    }

    /**
     * Creates the inventory button which gives access to the things inside the companions inventory!!!
     * @param statsTable - the Table UI element to print to essentially
     */
    public void createInventoryButton(Table statsTable) {
        TextButton button = new TextButton("Inventory", skin);

        //How to detect when the button is pressed, and raise the inventory page
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
     * For weapons with a set number of ammo/pieces
     * Function which updates the amount of ammo seen on screen, for weapons with limited number of uses
     * @param currentAmmo - amount of ammo in the weapon left
     * @param maxAmmo - amount of total ammo storage in that weapon
     */
    public void updateAmmo(int currentAmmo, int maxAmmo) {
        CharSequence ammoText = String.format("%d / %d", currentAmmo, maxAmmo);
        if (maxAmmo == 100) {  // todo: make non-ammo things not have ammo
            ammoText = "    -";
        }
        ammoLabel.setText(ammoText);
    }

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


    /**
     * Draw
     * @param batch Batch to render to.
     */
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
        CharSequence text = String.format("%d", health);
        companionHealthLabel.setText(text);
        healthBarWidth = 280f * health / maxHealth;
        healthBarFill.setSize(healthBarWidth, 30f);
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

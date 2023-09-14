package com.csse3200.game.components.Companion;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

/**
 * A UI component for displaying Companion stats, e.g., health.
 */
public class CompanionStatsDisplay extends UIComponent {
    Table companionStatisticsUI;

    private boolean update = false;
    Table playerLowHealthAlert;


    /**
     * The player entity associated with this CompanionStatsDisplay.
     */
    public Entity playerEntity;

    /**
     * The UI playerLowHealthLabel for displaying the companion's health.
     */
    public Label playerLowHealthLabel;

    public Label companionHealthLabel; //this is the label for the companions health displayed

    public Label companionUIHeaderLabel; // label for the header of the UI component.
    public Label companionModeLabel; // label for the companions mode

    private boolean isInvincible = true;
    private boolean isInfiniteHealth = true;

    /**
     * Default constructor for CompanionStatsDisplay.
     */
    public CompanionStatsDisplay() {
    }

    /**
     * Constructor for CompanionStatsDisplay with a player entity.
     *
     * @param playerEntity The player entity to associate with this UI component.
     */
    public CompanionStatsDisplay(Entity playerEntity) {
        this.playerEntity = playerEntity;
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
        playerEntity.getEvents().addListener("updateHealth", this::updatePlayerHealthUI);
        entity.getEvents().addListener("companionModeChange", this::updateCompanionModeUI);
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
        companionStatisticsUI.padTop(85f).padRight(5f);

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
    }

    /**
     * Set the companion's image to an invincible state.
     */
    public void setInvincibleImage() {
        AnimationRenderComponent infanimator = ServiceLocator.getGameArea().getCompanion().getComponent(AnimationRenderComponent.class);
        infanimator.startAnimation("LEFT");
    }

    /**
     * Toggle invincibility for the companion.
     */
    public void toggleInvincibility() {
        if (isInvincible) {
            setInvincibleImage();
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
        AnimationRenderComponent animator = ServiceLocator.getGameArea().getCompanion().getComponent(AnimationRenderComponent.class);
        animator.startAnimation("RIGHT");
    }

    /**
     * Toggle infinite health for the companion.
     */
    public void toggleInfiniteHealth() {
        if (isInfiniteHealth) {
            int maxHealth = Integer.MAX_VALUE;
            ServiceLocator.getGameArea().getCompanion().getComponent(CombatStatsComponent.class).setHealth(maxHealth);
            isInfiniteHealth = false;

            // Schedule a task to reset health to a normal value after a delay (e.g., 10 seconds)
            Timer.schedule(new Task() {
                @Override
                public void run() {
                    ServiceLocator.getGameArea().getCompanion().getComponent(CombatStatsComponent.class).setHealth(50);
                }
            }, 10.0f);
        }
    }

    /**
     * Add an alert when the player's health is low.
     *
     * @param health The current health value.
     */
    private void addAlert(int health) {
        //FIND WHERE THE COMPANION IS
        PhysicsComponent companionPhysics = entity.getComponent(PhysicsComponent.class);
        Vector2 compPos = companionPhysics.getBody().getPosition();
        playerLowHealthAlert = new Table();
        playerLowHealthAlert.top().left();
        playerLowHealthAlert.setFillParent(true);
        //place the label where the companion is plus an offset
        playerLowHealthAlert.setPosition(compPos.x + 550f, compPos.y - 200F);

        // plate up the low player health string
        CharSequence playerLowHealthString = String.format("Player Low Health: %d", health);
        playerLowHealthLabel = new Label(playerLowHealthString, skin, "small");


        playerLowHealthAlert.add(playerLowHealthLabel);

        //launch the table onto the screen
        stage.addActor(playerLowHealthAlert);
    }

    @Override
    public void draw(SpriteBatch batch) {
        // Code for drawing UI elements and updating the projection matrix.
    }

    /**
     * Update the player's health UI.
     *
     * @param health The current health value of the player.
     */
    public void updatePlayerHealthUI(int health) {
        if (health <= 50 && !update) {
            addAlert(health);
            update = true;
            return;
        }

        if (update) {
            // Schedule a task to remove the playerLowHealthLabel after a delay (e.g., 3 seconds)
            Timer.schedule(new Task() {
                @Override
                public void run() {
                    playerLowHealthLabel.remove();
                    update = false;
                }
            }, 3.0f);
        }
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
        playerLowHealthLabel.remove();
        companionUIHeaderLabel.remove();
    }
}

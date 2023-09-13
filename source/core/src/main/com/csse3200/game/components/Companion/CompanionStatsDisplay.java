package com.csse3200.game.components.Companion;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

/**
 * A UI component for displaying Companion stats, e.g., health.
 */
public class CompanionStatsDisplay extends UIComponent {
    Table table;
    private boolean update = false;
    Table table2;

    /**
     * The player entity associated with this CompanionStatsDisplay.
     */
    public Entity playerEntity;

    /**
     * The UI label for displaying the companion's health.
     */
    public Label messageLabel;
    public Label label;

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
    }

    /**
     * Creates actors and positions them on the stage using a table.
     * See {@link Table} for positioning options.
     */
    private void addActors() {
        table = new Table();
        table.top().left();
        table.setFillParent(true);
        table.padTop(85f).padLeft(5f);

        // Health text
        int companionHealth = entity.getComponent(CombatStatsComponent.class).getHealth();
        CharSequence healthText = String.format("Companion Health: %d", companionHealth);
        messageLabel = new Label(healthText, skin, "large");
        table.add(messageLabel);
        stage.addActor(table);
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
        PhysicsComponent companionPhysics = entity.getComponent(PhysicsComponent.class);
        Vector2 compPos = companionPhysics.getBody().getPosition();
        table2 = new Table();
        table2.top().left();
        table2.setFillParent(true);
        table2.setPosition(compPos.x + 550f, compPos.y - 200F);

        CharSequence healthText2 = String.format("Low Health: %d", health);
        label = new Label(healthText2, skin, "large");
        table2.add(label);
        stage.addActor(table2);
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
            // Schedule a task to remove the label after a delay (e.g., 3 seconds)
            Timer.schedule(new Task() {
                @Override
                public void run() {
                    label.remove();
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
        CharSequence text = String.format("Companion Health: %d", health);
        messageLabel.setText(text);
    }

    @Override
    public void dispose() {
        super.dispose();
        messageLabel.remove();
        label.remove();
    }
}

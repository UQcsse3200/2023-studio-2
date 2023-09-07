package com.csse3200.game.components.Companion;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;

/**
 * A UI component for displaying Companion stats, e.g. health.
 */
public class CompanionStatsDisplay extends UIComponent {
    Table table;

    private static final float SCALE_REDUCTION = 70;
    private static final float MESSAGE_DISPLAY_TIME = 5.0f; // Time in seconds

    // Configuration variables for positioning
    private final int health;
    private final boolean center;
    private final float offsetY;

    // UI element for displaying the message
    public Label messageLabel;

    // Remaining time for message display
    public float messageDisplayTimeLeft;

    /**
     * Constructor for CompanionStatsDisplay.
     *
     * @param center  Whether to center the display.
     * @param offsetY Offset in the Y-axis for positioning.
     * @param health  The initial health value to display.
     */
    public CompanionStatsDisplay(boolean center, float offsetY, int health) {
        this.center = center;
        this.offsetY = offsetY;
        this.health = health;
    }

    /**
     * Creates reusable UI styles and adds actors to the stage.
     */
    @Override
    public void create() {
        super.create();
        addActors();
        entity.getEvents().addListener("updateHealth", this::updateCompanionHealthUI);
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

        // Heart image
        float heartSideLength = 30f;

        // Health text
        int health = entity.getComponent(CombatStatsComponent.class).getHealth();
        CharSequence healthText = String.format("Companion Health: %d", health);
        messageLabel = new Label(healthText, skin, "large");

        table.add(messageLabel);
        stage.addActor(table);
    }

    private void updateUIPosition() {
        float yPos = offsetY;
        if (center) {
            yPos += entity.getCenterPosition().y;
        } else {
            yPos += entity.getPosition().y;
        }

        // Calculate the scaled position for the UI element
        var position = new Vector3(entity.getCenterPosition().x, yPos, 0);
        position.scl(SCALE_REDUCTION);
        messageLabel.setPosition(position.x, position.y);
    }

    @Override
    public void draw(SpriteBatch batch) {
        // Code for drawing UI elements and updating the projection matrix.
    }

    /**
     * Updates the UI component based on the elapsed time.
     *
     * @param deltaTime The time elapsed since the last frame.
     */
    public void update(float deltaTime) {
        super.update();

        if (messageDisplayTimeLeft > 0) {
            messageDisplayTimeLeft -= deltaTime;
            if (messageDisplayTimeLeft <= 0) {
                messageLabel.setText(""); // Clear the message when time expires
            }
        }
    }

    /**
     * Updates the companion's health on the UI.
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
    }
}

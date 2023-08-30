package com.csse3200.game.components.Companion;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.csse3200.game.ui.UIComponent;
import com.csse3200.game.entities.Entity;

/**
 * A ui component for displaying Companion stats, e.g. health.
 */
public class CompanionStatsDisplay extends UIComponent {
    Table table;
    public CharSequence healthText;

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

    public CompanionStatsDisplay(boolean center, float offsetY, int health) {
        this.center = center;
        this.offsetY = offsetY;
        this.health = health;
    }

    /**
     * Creates reusable ui styles and adds actors to the stage.
     */
    @Override
    public void create() {
        super.create();
        addActors();

        //entity.getEvents().addListener("updateHealth", this::updateCompanionHealthUI);
    }

    /**
     * Creates actors and positions them on the stage using a table.
     * @see Table for positioning options
     */
    private void addActors() {
        table = new Table();
        table.top().left();
        table.setFillParent(true);
        table.padTop(15f).padLeft(5f);

        // Heart image
        float heartSideLength = 20f;
       // heartImage = new Image(ServiceLocator.getResourceService().getAsset("images/heart.png", Texture.class));

        // Health text
       // messageLabel = new Label(healthText, skin, "small");
       // int health = playerEntity.getComponent(CombatStatsComponent.class).getHealth();
       // CharSequence healthText = String.format("Health: %d", health);
       // CharSequence healthText = String.format("Low Health: %d", health);
        if(health == 100){
           // messageLabel = new Label(healthText, skin, "small");
            displayMessage();
            //entity.getEvents().addListener("displayMessage", this::displayMessage);
            //messageLabel = new Label(healthText, skin, "small");
        }


       // table.add(heartImage).size(heartSideLength).pad(5);
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
    public void draw(SpriteBatch batch)  {

        updateUIPosition();
        Matrix4 originalMatrix = batch.getProjectionMatrix().cpy();

        Vector3 newScale = new Vector3();
        originalMatrix.getScale(newScale);
        newScale.scl(1 / SCALE_REDUCTION);

        Vector3 originalPosition = originalMatrix.getTranslation(new Vector3());
        Quaternion originalRotation = new Quaternion();
        Matrix4 newMatrix = new Matrix4();
        newMatrix.idt(); // Reset to identity matrix
        newMatrix.translate(originalPosition);
        newMatrix.scale(newScale.x, newScale.y, newScale.z);

        // Apply rotation manually using the rotate method
        newMatrix.rotate(originalRotation);

        batch.setProjectionMatrix(newMatrix);


        if (messageDisplayTimeLeft > 0) {
            //messageLabel.draw(batch, 1.0f);

        }

        batch.setProjectionMatrix(originalMatrix);

    }

    public void displayMessage() {
        //CharSequence healthText = String.format("Low Health: %d", health);
       // messageLabel.setText("Player health is low");
       // messageLabel = new Label(healthText, skin, "small");
        messageLabel = new Label("Low Health", skin, "small");


        messageDisplayTimeLeft = MESSAGE_DISPLAY_TIME;
    }

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
     * Updates the player's health on the ui.
     * @param health player health
     */
    public void updateCompanionHealthUI(int health) {
        CharSequence text = String.format("Health: %d", health);
        //messageLabel.setText(text);
    }

    @Override
    public void dispose() {
        super.dispose();
        //heartImage.remove();
        messageLabel.remove();
    }
}



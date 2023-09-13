package com.csse3200.game.components;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.csse3200.game.ui.UIComponent;

/**
 * This UI Component for displaying PowerUp.
 * It will display the current Power Up which is selected .
 */
public class PowerUpDisplayHUD extends UIComponent {
    Table table;

    public PowerupType powerUpEntity;
    private Label PowerUpLabel;

    public PowerUpDisplayHUD(PowerupType powerUpEntity) {
        this.powerUpEntity = powerUpEntity;
    }

    public void create() {
        super.create();
        addActors();

        entity.getEvents().addListener("Current PowerUp", this::updatePowerUpDisplayUi);
    }

    /**
     * Creates actors and position them on the stage using a table
     * @see Table for positining options
     */
    private void addActors() {
        table = new Table();
        table.top().left();
        table.setFillParent(true);
        table.padTop(125f).padLeft(5f);

        String PowerUp = entity.getComponent(PowerupComponent.class).getPowerupType();

        //Power Up display text
        CharSequence powerUpText = String.format("Current PowerUp : %s", PowerUp);
        PowerUpLabel = new Label(powerUpText,skin,"large");

        table.add(PowerUpLabel);
        stage.addActor(table);
    }

    /**
     * @param batch Batch to render to.
     */

    @Override
    protected void draw(SpriteBatch batch) {
    }

    /**
     * Updates the Player PowerUp display name
     * @param powerUp contains the string value of the updated powerUp Type
     */
    public void updatePowerUpDisplayUi(String powerUp) {
        CharSequence text = String.format("Current PowerUp: %s", powerUp);
        PowerUpLabel.setText(text);
    }

    public void dispose() {
        super.dispose();
        PowerUpLabel.remove();
    }
}

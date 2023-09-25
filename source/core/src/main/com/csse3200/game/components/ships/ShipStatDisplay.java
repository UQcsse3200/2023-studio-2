package com.csse3200.game.components.ships;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.csse3200.game.ui.UIComponent;

/**
 * A ui component for displaying the ship stats, e.g. speed.
 */
public class ShipStatDisplay extends UIComponent {
    Table table;
    private Label fuelLabel;
    private Label healthLabel;

    private int health;
    private int fuel;
    private ShipActions shipActions;

    /**
     * Creates reusable ui styles and adds actors to the stage.
     */
    @Override
    public void create() {
        super.create();
        this.health = this.shipActions.getMaxHealth();
        this.fuel = this.shipActions.getMaxFuel();
        addActors();
    }

    /**
     * Creates actors and positions them on the stage using a table.
     * @see Table for positioning options
     */
    private void addActors() {
        table = new Table();
        table.top().left();
        table.setFillParent(true);
        table.padTop(45f).padLeft(45f);
        entity.getEvents().addListener("updateShipHealth", this::updateShipHealthUI);
        entity.getEvents().addListener("updateShipFuel", this::updateShipFuelUI);

        //currently leave them here for the UI, may adjust after further implementation
        CharSequence fuelText = "Fuel: " + Integer.toString(this.fuel);
        CharSequence healthText = "Health: " + Integer.toString(this.health);

        float labelWidth = 200f;  // Adjust as necessary

        fuelLabel = new Label(fuelText, skin, "thick");
        healthLabel = new Label(healthText, skin, "thick");

        table.add(fuelLabel).width(labelWidth).pad(5f).left().row();
        table.add(healthLabel).width(labelWidth).pad(5f).left().row();


        stage.addActor(table);

    }

    @Override
    public void draw(SpriteBatch batch)  {
        // draw is handled by the stage
    }

    /**
     * Updates the ship's health on the ui.
     */
    public void updateShipHealthUI() {

        this.health = shipActions.getMaxHealth();
        CharSequence healthText = String.format("Health: %d", this.health);
        healthLabel.setText(healthText);


    }

    /**
     * Updates ship's fuel on the ui.
     */
    public void updateShipFuelUI() {

        this.fuel = shipActions.getMaxFuel();
        CharSequence fuelText = String.format("Fuel: %d", this.fuel);
        fuelLabel.setText(fuelText);


    }

    @Override
    public void dispose() {
        super.dispose();
        fuelLabel.remove();
        healthLabel.remove();
    }

    public void setShipActions(ShipActions shipActions) {
        this.shipActions = shipActions;
    }

}

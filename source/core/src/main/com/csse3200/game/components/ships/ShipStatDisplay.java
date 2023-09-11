package com.csse3200.game.components.ships;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;

public class ShipStatDisplay extends UIComponent {
    Table table;
    private Label speedLabel;

    private Label healthLabel;
    private Label attackLabel;
    @Override
    public void create() {
        super.create();
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

        CharSequence speedText = "Speed: 100";
        CharSequence healthText = "Health: 100";
        CharSequence attackText = "Attack: 100";

        float labelWidth = 200f;  // Adjust as necessary

        speedLabel = new Label(speedText, skin, "thick");
        healthLabel = new Label(healthText, skin, "thick");
        attackLabel = new Label(attackText, skin, "thick");

        table.add(speedLabel).width(labelWidth).pad(5f).left().row();
        table.add(healthLabel).width(labelWidth).pad(5f).left().row();
        table.add(attackLabel).width(labelWidth).pad(5f).left().row();

        stage.addActor(table);

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
        CharSequence text = String.format("Health: 100");
        speedLabel.setText(text);
    }

    @Override
    public void dispose() {
        super.dispose();
        speedLabel.remove();
    }

}

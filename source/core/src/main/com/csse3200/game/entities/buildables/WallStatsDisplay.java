package com.csse3200.game.entities.buildables;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;

public class WallStatsDisplay extends UIComponent {

    Table table;
    private Image heartImage;
    private Label healthLabel;

    /**
     * Creates reusable ui styles and adds actors to the stage.
     */
    @Override
    public void create() {
        super.create();
        addActors();

        entity.getEvents().addListener("updateHealth", this::updateWallHealthUI);
    }

    /**
     * Creates actors and positions them on the stage using a table.
     * @see Table for positioning options
     */
    private void addActors() {
        table = new Table();
        table.top().bottom();
        table.setFillParent(true);
        table.padTop(20f).padLeft(5f);

        // Heart image
        float heartSideLength = 30f;
        heartImage = new Image(ServiceLocator.getResourceService().getAsset("images/heart.png", Texture.class));

        // Health text
        int health = entity.getComponent(CombatStatsComponent.class).getHealth();
        CharSequence healthText = String.format("Health: %d", health);
        healthLabel = new Label(healthText, skin, "large");

        table.add(heartImage).size(heartSideLength).pad(5);
        table.add(healthLabel);
        stage.addActor(table);
    }

    @Override
    public void draw(SpriteBatch batch)  {
        // draw is handled by the stage
    }

    /**
     * Updates the Wall's health on the ui.
     * @param health player health
     */
    public void updateWallHealthUI(int health) {
        CharSequence text = String.format("Health: %d", health);
        healthLabel.setText(text);
    }

    @Override
    public void dispose() {
        super.dispose();
        heartImage.remove();
        healthLabel.remove();
    }

}

package com.csse3200.game.components.ships;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.csse3200.game.ui.UIComponent;

/**
 * Class for creating the UI Component
 * for displaying distance
 */
public class DistanceDisplay extends UIComponent {
    private Label distanceLabel;

    /**
     * Method for creating
     * actors to go on stage
     */
    @Override
    public void create() {
        super.create();
        addActors();
    }

    /**
     * Method for displaying the distance in the minigame
     */
    private void addActors() {
        Table table = new Table();

        table.center().bottom();
        table.setFillParent(true);
        table.padTop(45f).padLeft(5f);

        // Distance text
        CharSequence distanceText = "Distance: N/A";
        distanceLabel = new Label(distanceText, skin, "title");

        table.add(distanceLabel);
        stage.addActor(table);
    }

    /**
     * Method for drawing handled by the stage
     * @param batch Batch to render to.
     */
    @Override
    public void draw(SpriteBatch batch) {
        // Drawing is handled by the stage
    }

    /**
     * Method for updating distance UI in the minigame
     * @param distance Distance to be displayed
     */
    public void updateDistanceUI(float distance) {
        CharSequence text = String.format("Distance: %.2f", distance);
        distanceLabel.setText(text);
    }

    /**
     * Method for disposing and
     * removing the distance label
     */
    @Override
    public void dispose() {
        super.dispose();
        distanceLabel.remove();
    }
}


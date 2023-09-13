package com.csse3200.game.components.ships;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.csse3200.game.ui.UIComponent;
import com.csse3200.game.services.ServiceLocator;

public class DistanceDisplay extends UIComponent {
    private Table table;
    private Label distanceLabel;

    @Override
    public void create() {
        super.create();
        addActors();
    }

    private void addActors() {
        table = new Table();
        //table.top().left();
        table.center().bottom();
        table.setFillParent(true);
        table.padTop(45f).padLeft(5f);

        // Distance text
        CharSequence distanceText = "Distance: N/A";
        //distanceLabel = new Label(distanceText, skin, "large");
        distanceLabel = new Label(distanceText, skin, "title");

        table.add(distanceLabel);
        stage.addActor(table);
    }

    @Override
    public void draw(SpriteBatch batch) {
        // Drawing is handled by the stage
    }

    public void updateDistanceUI(float distance) {
        CharSequence text = String.format("Distance: %.2f", distance);
        distanceLabel.setText(text);
    }

    @Override
    public void dispose() {
        super.dispose();
        distanceLabel.remove();
    }
}


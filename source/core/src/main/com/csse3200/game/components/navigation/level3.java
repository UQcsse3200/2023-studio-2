package com.csse3200.game.components.navigation;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;

/**
 * The {@code level3} class represents a specialized UI component used to display and interact with
 * level 3 in the game. It extends {@code UIComponent} and provides functionality for creating and
 * managing the UI elements associated with level 3.
 *
 * <p>This class includes methods for creating the UI components, handling user interactions, and
 * disposing of resources when no longer needed.
 */
public class level3 extends UIComponent {
    private final String PlanetImage;
    private final float x;
    private final float y;
    private Table table;

    /**
     * Constructs a new {@code level3} instance with the specified planet image path, x and y
     * coordinates.
     *
     * @param PlanetImage The path to the image representing the planet.
     * @param x           The x-coordinate position of the UI component.
     * @param y           The y-coordinate position of the UI component.
     */
    public level3(String PlanetImage, float x, float y) {
        this.PlanetImage = PlanetImage;
        this.x = x;
        this.y = y;
    }

    /**
     * Creates the UI components for level 3, including the planet image, label, and button for
     * interaction. This method sets up the layout and positioning of these components on the screen.
     */
    @Override
    public void create() {
        super.create();
        table = new Table();
        Image planetImage = new Image(ServiceLocator.getResourceService().getAsset(PlanetImage, Texture.class));
        Label label = new Label("Level 3 ", skin, "large");
        Button button = new Button(label, skin);
        button.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        entity.getEvents().trigger("Navigateee");
                    }
                });

        table.add(planetImage).align(Align.top).size(100f);
        table.row();
        table.add(button);
        table.setPosition(x, y);
        table.setSize(20, 20);
        stage.addActor(table);
    }

    /**
     * This method is responsible for drawing UI elements but is currently empty as there is no
     * custom drawing logic.
     *
     * @param batch The SpriteBatch used for rendering.
     */
    @Override
    protected void draw(SpriteBatch batch) {
        // Currently no custom drawing logic.
    }

    /**
     * Disposes of the resources associated with this UI component. This includes clearing the table
     * and removing it from the stage.
     */
    @Override
    public void dispose() {
        table.clear();
        super.dispose();
        table.remove();
    }
}

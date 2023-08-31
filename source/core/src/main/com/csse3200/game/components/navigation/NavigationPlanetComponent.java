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
 * The `PlanetComponent` class represents a navigational component in the game, allowing players
 * to interact with and navigate to different game areas represented as planets.
 */
public class NavigationPlanetComponent extends UIComponent {

    // The path to the planet image
    private final String PlanetImage;

    // The x and y coords
    private final float x;
    private final float y;

    // Table of UI elements
    private Table table;

    // The name of the planet
    private final String name;

    /**
     * Constructs a `PlanetComponent` instance with the specified planet image and coordinates.
     *
     * @param PlanetImage The path to the image representing the planet.
     * @param x The x-coordinate of the planet's position.
     * @param y The y-coordinate of the planet's position.
     */
    public NavigationPlanetComponent(String PlanetImage, float x, float y, String name) {
        this.PlanetImage = PlanetImage;
        this.x = x;
        this.y = y;
        this.name = name;
    }

    /**
     * Creates the planet UI component, including the planet image and navigation button.
     * Sets up event listeners for navigation triggers.
     */
    @Override
    public void create() {
        super.create();

        // Create and configure the UI elements
        table = new Table();
        Image planetImage = new Image(ServiceLocator.getResourceService().getAsset(PlanetImage, Texture.class));
        Label label = new Label(name, skin, "large");
        Button button = new Button(label, skin);

        // Add a listener to the navigation button
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                // Trigger navigation event when the button is clicked
                entity.getEvents().trigger("Navigate"+name);
            }
        });

        // Configure the layout of UI elements within the table
        table.add(planetImage).align(Align.top).size(100f);
        table.row();
        table.add(button);

        // Set the position and size of the table
        table.setPosition(x, y);
        table.setSize(20, 20);

        // Add the table to the stage for rendering
        stage.addActor(table);
    }

    /**
     * Draws the UI component. This method is intentionally left empty as the drawing
     * is handled by the LibGDX scene2d framework.
     *
     * @param batch The sprite batch used for rendering.
     */
    @Override
    protected void draw(SpriteBatch batch) {
        // Drawing is managed by the LibGDX scene2d framework
    }

    /**
     * Disposes of resources used by the `PlanetComponent`. Clears the internal table
     * and performs the parent class disposal. Removes the table from the stage.
     */
    @Override
    public void dispose() {
        // Clear and remove the table from the stage
        table.clear();
        super.dispose();
        table.remove();
    }
}
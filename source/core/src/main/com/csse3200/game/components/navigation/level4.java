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
 * The level4 class represents a UI component for Level 4 in a game's navigation system.
 * It displays a planet image and a button for navigation to a different level.
 */
public class level4 extends UIComponent {

    // The path to the planet image
    private final String PlanetImage;

    // The x and y coords
    private final float x;
    private final float y;

    // Table of UI elements
    private Table table;

    /**
     * Constructs a new level4 instance.
     *
     * @param PlanetImage The path to the planet image.
     * @param x           The x-coordinate for the position of the planet component.
     * @param y           The y-coordinate for the position of the planet component.
     */
    public level4(String PlanetImage,float x,float y) {
        this.PlanetImage=PlanetImage;
        this.x=x;
        this.y=y;
    }

    /**
     * Creates the UI elements for Level 4 component.
     */
    @Override
    public void create() {
        super.create();
        table=new Table();
        Image planetImage = new Image(ServiceLocator.getResourceService().getAsset(PlanetImage, Texture.class));
        Label label=new Label("Level 4 ",skin,"large");
        Button button=new Button(label,skin);
        button.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {

                        entity.getEvents().trigger("Navigateeee");
                    }
                });

        table.add(planetImage).align(Align.top).size(100f);
        table.row();
        table.add(button);
        table.setPosition(x,y);
        table.setSize(20,20);
        stage.addActor(table);
    }

    /**
     * Draws the Level 4 component (not implemented in this class).
     *
     * @param batch The SpriteBatch to use for drawing.
     */
    @Override
    protected void draw(SpriteBatch batch) {

    }

    /**
     * Disposes of resources and clears the table.
     */
    @Override
    public void dispose() {
        table.clear();
        super.dispose();
        table.remove();
    }
}




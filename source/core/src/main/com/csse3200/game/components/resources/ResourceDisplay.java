package com.csse3200.game.components.resources;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;
import org.w3c.dom.css.Rect;

import java.util.HashMap;
import java.util.Map;

/**
 * A ui component for displaying resources in the bottom right corner of the screen.
 */
public class ResourceDisplay extends UIComponent {
    Table table;
    TextureRegion lightsTextureRegion;
    HashMap<String, TextureRegion> resourceBars;
    HashMap<String, Float> barWidths;
    HashMap<String, Image> resourceBarImages;
    int scale = 5;
    int maxResource = 1000; // TODO make this a reference to gamestate and actually work

    /**
     * Constructor for the ResourceDisplay.
     * Initializes the table and maps used for storing resource bar data.
     */
    public ResourceDisplay() {
        this.table = new Table();
        this.resourceBars = new HashMap<>();
        this.barWidths = new HashMap<>();
        this.resourceBarImages = new HashMap<>();
    }

    /**
     * Creates reusable ui styles and adds actors to the stage.
     */
    @Override
    public void create() {
        super.create();
        addActors();
    }

    /**
     * Adjusts the width of the resource bar based on a given percentage.
     *
     * @param resource The type of resource to adjust.
     * @param pct The percentage to which the bar should be filled.
     */
    void setWidth(Resource resource, double pct) {
        TextureRegion region = this.resourceBars.get(resource.toString());
        float originalWidth = this.barWidths.get(resource.toString());
        region.setRegionWidth((int) (pct * originalWidth));
        Image resourceBarImage = this.resourceBarImages.get(resource.toString());
        resourceBarImage.setSize((float) (originalWidth * pct * 5.0), resourceBarImage.getImageHeight());
    }

    /**
     * Adds a resource type to the display.
     *
     * @param resource The type of resource to add.
     * @return Returns the updated ResourceDisplay object.
     */
    public ResourceDisplay withResource(Resource resource) {
        Image barForegroundImage= new Image(ServiceLocator.getResourceService().getAsset("images/resourcebar_foreground.png", Texture.class));
        Image barBackgroundImage= new Image(ServiceLocator.getResourceService().getAsset("images/resourcebar_background.png", Texture.class));

        Texture lightsTexture = ServiceLocator.getResourceService().getAsset("images/resourcebar_lights.png", Texture.class);
        lightsTextureRegion = new TextureRegion(lightsTexture, lightsTexture.getWidth(), lightsTexture.getHeight());
        Image lightsImage = new Image(lightsTextureRegion);

        String barPath = "images/resourcebar_" + resource.toString().toLowerCase() + ".png";
        Texture resourceBarTexture = ServiceLocator.getResourceService().getAsset(barPath, Texture.class);
        TextureRegion resourceBarTextureRegion = new TextureRegion(resourceBarTexture,
                (int) (resourceBarTexture.getWidth() * 1.0),
                resourceBarTexture.getHeight());
        resourceBars.put(resource.toString(), resourceBarTextureRegion);
        barWidths.put(resource.toString(), (float) resourceBarTexture.getWidth());
        Image resourceBar = new Image(resourceBarTextureRegion);
        resourceBarImages.put(resource.toString(), resourceBar);

        Stack barStack = new Stack();
        barStack.add(barBackgroundImage);
        barStack.add(resourceBar);
        barStack.add(lightsImage);
        barStack.add(barForegroundImage);

        table.row();
        table.add(barStack).size(resourceBarTexture.getWidth()*scale, resourceBarTexture.getHeight()*scale).pad(5);

        return this;
    }

    /**
     * Creates actors and positions them on the stage using a table.
     * @see Table for positioning options
     */
    private void addActors() {
        table.bottom().right();
        table.setFillParent(true);
        table.padBottom(45f).padRight(45f);
        stage.addActor(table);
    }


    /**
     * Draws the resource bars on the screen.
     * The drawing is managed by the stage.
     *
     * @param batch The SpriteBatch used for drawing.
     */
    @Override
    public void draw(SpriteBatch batch)  {
        // draw is handled by the stage
        for (Resource resource : new Resource[]{Resource.Durasteel, Resource.Nebulite, Resource.Solstite}) {
            Object quantity = ServiceLocator.getGameStateObserverService().getStateData("resource/" + resource.toString());
            if (quantity != null) {
                int value = (int) quantity;
                setWidth(resource, Math.min((float) value / (float) maxResource, maxResource));
            } else {
                setWidth(resource, 0.0);
            }
        }
    }
}

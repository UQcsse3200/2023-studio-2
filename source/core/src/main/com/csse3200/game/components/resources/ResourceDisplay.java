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
    HashMap<String, TextureRegion> resourceBars;
    HashMap<String, Float> barWidths;
    HashMap<String, Image> resourceBarImages;
    HashMap<String, TextureRegion> extractorBars;
    HashMap<String, Float> extractorBarWidths;
    HashMap<String, Image> extractorBarImages;
    int scale = 5;
    int maxResource = 1000; // TODO make this a reference to gamestate and actually work
    int maxExtractors = 4; // Defines the maximum amount of extractors per bar
    int steps = 64; // The number of intervals on the progress bar (rounds the percentages to this amount of steps)

    /**
     * Constructor for the ResourceDisplay.
     * Initializes the table and maps used for storing resource bar data.
     */
    public ResourceDisplay(int scale, int steps, int maxResource) {
        this.table = new Table();
        this.resourceBars = new HashMap<>();
        this.extractorBars = new HashMap<>();
        this.barWidths = new HashMap<>();
        this.extractorBarWidths = new HashMap<>();
        this.resourceBarImages = new HashMap<>();
        this.extractorBarImages = new HashMap<>();
        this.scale = scale;
        this.steps = steps;
        this.maxResource = maxResource;
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

    void setExtractorCount(Resource resource, int count) {
        TextureRegion region = this.extractorBars.get(resource.toString());
        float originalWidth = this.extractorBarWidths.get(resource.toString());
        region.setRegionWidth((int) (((float) count / (float) maxExtractors) * originalWidth));
        Image extractorBarImage = this.extractorBarImages.get(resource.toString());
        extractorBarImage.setSize((float) (originalWidth * ((float) count / (float) maxExtractors) * 5.0), extractorBarImage.getImageHeight());
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

        Texture extractorBarTexture = ServiceLocator.getResourceService().getAsset("images/resourcebar_lights.png", Texture.class);
        TextureRegion extractorBarTextureRegion = new TextureRegion(extractorBarTexture,
                (int) (extractorBarTexture.getWidth() * 1.0),
                extractorBarTexture.getHeight());
        extractorBars.put(resource.toString(), extractorBarTextureRegion);
        extractorBarWidths.put(resource.toString(), (float) extractorBarTexture.getWidth());
        Image extractorBar = new Image(extractorBarTextureRegion);
        extractorBarImages.put(resource.toString(), extractorBar);

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
        barStack.add(extractorBar);
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
                setWidth(resource, Math.min((double) Math.round((float) value / (float) maxResource * this.steps) / this.steps, 1.0));
                setExtractorCount(resource, 1);
            } else {
                setWidth(resource, 0.0);
            }
        }
    }
}

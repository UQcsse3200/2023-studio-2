package com.csse3200.game.components.resources;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;

import java.util.HashMap;

/**
 * A ui component for displaying player stats, e.g. health.
 */
public class ResourceDisplay extends UIComponent {
    Table table;
    TextureRegion lightsTextureRegion;
    HashMap<String, TextureRegion> resourceBars;
    HashMap<String, Float> barWidths;
    int scale = 5;

    public ResourceDisplay() {
        this.table = new Table();
        this.resourceBars = new HashMap<>();
        this.barWidths = new HashMap<>();
    }

    /**
     * Creates reusable ui styles and adds actors to the stage.
     */
    @Override
    public void create() {
        super.create();
        addActors();
    }

    void setWidth(Resource resource, double pct) {
        this.resourceBars.get(resource.toString()).setRegionWidth((int) (pct * this.barWidths.get(resource.toString())));
    }

    public ResourceDisplay withResource(Resource resource) {
        Image barForegroundImage= new Image(ServiceLocator.getResourceService().getAsset("images/resourcebar_foreground.png", Texture.class));
        Image barBackgroundImage= new Image(ServiceLocator.getResourceService().getAsset("images/resourcebar_background.png", Texture.class));

        Texture lightsTexture = ServiceLocator.getResourceService().getAsset("images/resourcebar_lights.png", Texture.class);
        lightsTextureRegion = new TextureRegion(lightsTexture, lightsTexture.getWidth(), lightsTexture.getHeight());
        Image lightsImage = new Image(lightsTextureRegion);

        String barPath = "images/resourcebar_" + resource.toString().toLowerCase() + ".png";
        Texture resourceBarTexture = ServiceLocator.getResourceService().getAsset(barPath, Texture.class);
        TextureRegion resourceBarTextureRegion = new TextureRegion(resourceBarTexture, (int) (resourceBarTexture.getWidth() * 0.75), resourceBarTexture.getHeight());
        resourceBars.put(resource.toString(), resourceBarTextureRegion);
        barWidths.put(resource.toString(), (float) resourceBarTexture.getWidth());
        Image resourceBar = new Image(resourceBarTextureRegion);
//        subTable.add(resourceBar).pad(0.0F).width(10).fill(); //.padLeft((float) (0.0 * resourceBar.getWidth()));

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

    @Override
    public void draw(SpriteBatch batch)  {
        // draw is handled by the stage
        setWidth(Resource.Durasteel, 0.4);
    }
}

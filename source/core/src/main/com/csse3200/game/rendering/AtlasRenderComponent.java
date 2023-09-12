package com.csse3200.game.rendering;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.services.ServiceLocator;

public class AtlasRenderComponent extends RenderComponent {
    private TextureAtlas atlas;
    private TextureAtlas.AtlasRegion currentRegion;

    public AtlasRenderComponent(String atlasPath, String region) {
        this(ServiceLocator.getResourceService().getAsset(atlasPath, TextureAtlas.class), region);
    }

    public AtlasRenderComponent(TextureAtlas atlas, String region) {
        this.atlas = atlas;
        this.currentRegion = atlas.findRegion(region);
    }

    /**
     * Changes the atlas being used to a new atlas and sets the region to be
     * the given region.
     *
     * @param atlas - the new atlas
     * @param region - the new region
     */
    public void updateTextureAtlas(TextureAtlas atlas, String region) {
        this.atlas = atlas;

        currentRegion = atlas.findRegion(region);
    }

    /** Scale the entity to a width of 1 and a height matching the texture's ratio */
    public void scaleEntity() {
        entity.setScale(1f, ((float)currentRegion.getRegionHeight()) / ((float)currentRegion.getRegionWidth()));
    }

    /**
     * Sets the region to use when drawing the texture.
     *
     * @param region - the region to use
     * @param scaleEntity - whether the scale should be updated.
     */
    public void setRegion(String region, boolean scaleEntity) {
        this.currentRegion = atlas.findRegion(region);

        if (scaleEntity) {
            scaleEntity();
        }
    }

    @Override
    protected void draw(SpriteBatch batch) {
        Vector2 position = entity.getPosition();
        Vector2 scale = entity.getScale();
        batch.draw(currentRegion, position.x, position.y, scale.x, scale.y);
    }
}

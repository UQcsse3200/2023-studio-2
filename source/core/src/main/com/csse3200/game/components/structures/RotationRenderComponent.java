package com.csse3200.game.components.structures;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.csse3200.game.rendering.AtlasRenderComponent;

/**
 * This component can be used to display different AtlasRegions depending on the
 * compass rotation of the entity.
 */
public class RotationRenderComponent extends AtlasRenderComponent {
    protected Rotation rotation;

    /**
     * Creates a RotationRenderComponent with the given atlasPath and rotation.
     * @param atlasPath - the path to the atlas to use.
     * @param rotation - the rotation to begin with.
     */
    public RotationRenderComponent(String atlasPath, Rotation rotation) {
        super(atlasPath, rotation.toString());
        this.rotation = rotation;
    }

    public RotationRenderComponent(TextureAtlas atlas, Rotation rotation) {
        super(atlas, rotation.toString());
        this.rotation = rotation;
    }

    /**
     * Sets the current rotation of the atlas.
     * @param rotation - the rotation to set the atlas too.
     */
    public void setRotation(Rotation rotation) {
        super.setRegion(rotation.toString(), false);
        this.rotation = rotation;
    }

    /**
     * Gets the current rotation of the atlas.
     * @return the current rotation.
     */
    public Rotation getRotation() {
        return this.rotation;
    }
}
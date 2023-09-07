package com.csse3200.game.components.structures;

import com.csse3200.game.rendering.AtlasRenderComponent;

public class RotationRenderComponent extends AtlasRenderComponent {
    private Rotation rotation;
    public RotationRenderComponent(String atlasPath, Rotation rotation) {
        super(atlasPath, rotation.toString());
    }

    public void setRotation(Rotation rotation) {
        super.setRegion(rotation.toString(), false);
    }

    public Rotation getRotation() {
        return this.rotation.getRotationPos();
    }
}
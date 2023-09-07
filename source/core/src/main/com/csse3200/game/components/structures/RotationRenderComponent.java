package com.csse3200.game.components.structures;

import com.csse3200.game.rendering.AtlasRenderComponent;

public class RotationRenderComponent extends AtlasRenderComponent {
    public RotationRenderComponent(String atlasPath, Rotation rotation) {
        super(atlasPath, rotation.toString());
    }

    public void setRotation(Rotation rotation) {
        super.setRegion(rotation.toString(), false);
    }
}
package com.csse3200.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;


public class Gun {
    private Texture gunTexture;
    private Vector2 gunOffset;

    public Gun(String texturePath, Vector2 gunOffset) {
        gunTexture = new Texture(texturePath);
        this.gunOffset = gunOffset;
    }

    public Texture getGunTexture() {
        return gunTexture;
    }

    public Vector2 getGunOffset() {
        return gunOffset;
    }


}


package com.csse3200.game.entities;

import com.csse3200.game.components.CameraComponent;
import com.csse3200.game.components.ProximityActivationComponent;

public class Portal extends Entity {

    private float x;
    private float y;


    public Portal(Entity player) {
        super();
        addComponent(new ProximityActivationComponent(0.5f, player, this::teleport, this::teleport));
    }

    /**
     * Changes the texture to resemble a closed gate and enables the collision.
     *
     * @param player - the player who opened the gate
     */
    public void teleport(Entity player) {
        player.setPosition(x, y);
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

}

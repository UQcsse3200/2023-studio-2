package com.csse3200.game.entities;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.csse3200.game.components.ProximityActivationComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.TextureRenderComponent;


public class Portal extends Entity {

    private float x;
    private float y;


    public Portal(Entity player) {
        super();
        addComponent(new ProximityActivationComponent(0.5f, player, this::teleport, this::teleport));
    }

    /**
     * Teleports the player to a fixed destination
     *
     * @param player - the player entering the portal
     */
    public void teleport(Entity player) {
        player.setPosition(x, y);
    }

    @Override
    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }
}

package com.csse3200.game.entities;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.csse3200.game.components.ProximityActivationComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.TextureRenderComponent;

public class Portal extends Entity {
    public Portal(Entity player) {
        super();
        
        addComponent(new ProximityActivationComponent(0.5f, player, this::teleport, this::teleport));
        addComponent(new PhysicsComponent().setBodyType(BodyDef.BodyType.StaticBody));
        addComponent(new TextureRenderComponent("map/portal.png"));
    }

    /**
     * Teleports the player to a fixed destination
     *
     * @param player - the player entering the portal
     */
    public void teleport(Entity player) {
        player.setPosition(5, 5);
    }
}

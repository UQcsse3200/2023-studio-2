package com.csse3200.game.entities.factories;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.rendering.TextureRenderComponent;
import com.csse3200.game.physics.PhysicsUtils;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.TextureRenderComponent;


public class GoldMineFactory {
    public static Entity createGoldMine(){
        Entity GoldMine =
                new Entity()
                        .addComponent(new TextureRenderComponent("images/goldmine.png"))
                        .addComponent(new ColliderComponent().setLayer(PhysicsLayer.GoldMine))
                        .addComponent(new PhysicsComponent().setBodyType(BodyDef.BodyType.StaticBody))
                        .addComponent(new HitboxComponent().setLayer(PhysicsLayer.GoldMine));
        GoldMine.getComponent(PhysicsComponent.class).setBodyType(BodyDef.BodyType.StaticBody);
        GoldMine.getComponent(TextureRenderComponent.class).scaleEntity();
        GoldMine.setScale(4f, 4.5f);
        PhysicsUtils.setScaledCollider(GoldMine, 0.9f, 0.7f);
        GoldMine.scaleHeight(2.0f);
        return GoldMine;
    }
    private GoldMineFactory() {
        throw new IllegalStateException("Instantiating static util class");
    }
}

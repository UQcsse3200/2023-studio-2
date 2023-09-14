package com.csse3200.game.entities.factories;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.csse3200.game.LabWindow;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.DistanceCheckComponent;
import com.csse3200.game.components.InteractLabel;
import com.csse3200.game.components.InteractableComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.PhysicsUtils;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.TextureRenderComponent;
import com.csse3200.game.services.ServiceLocator;


public class LaboratoryFactory {
    public static Entity createLaboratory(){
        Entity Laboratory =
                new Entity()
                        .addComponent(new TextureRenderComponent("images/laboratory.png"))
                        .addComponent(new ColliderComponent().setLayer(PhysicsLayer.LABORATORY))
                        .addComponent(new PhysicsComponent().setBodyType(BodyDef.BodyType.StaticBody))
                        .addComponent(new HitboxComponent().setLayer(PhysicsLayer.LABORATORY))
                        .addComponent( new CombatStatsComponent(4,0,0,false));
        Laboratory.getComponent(PhysicsComponent.class).setBodyType(BodyDef.BodyType.StaticBody);
        Laboratory.getComponent(TextureRenderComponent.class).scaleEntity();
        Laboratory.setScale(4f, 4.0f);
        PhysicsUtils.setScaledCollider(Laboratory, 0.6f, 0.4f);
        Laboratory.scaleHeight(2.0f);
        Laboratory.getComponent(CombatStatsComponent.class).setHealth(0);

        InteractLabel interactLabel = new InteractLabel(); //code for interaction prompt
        Laboratory.addComponent(new DistanceCheckComponent(5f, interactLabel));
        ServiceLocator.getRenderService().getStage().addActor(interactLabel);

        Laboratory.addComponent(new InteractableComponent(entity -> {
            CombatStatsComponent healthStats = Laboratory.getComponent(CombatStatsComponent.class);


            if (healthStats.isDead()) {
                LabWindow minigame = LabWindow.MakeNewLaboratory(Laboratory);
                ServiceLocator.getRenderService().getStage().addActor(minigame);
            }
        }, 5f));
        return Laboratory;
    }
    private LaboratoryFactory() {
        throw new IllegalStateException("Instantiating static util class");
    }
}
package com.csse3200.game.entities.factories;

import com.csse3200.game.components.ships.ShipActions;
import com.csse3200.game.components.ships.ShipStatDisplay;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.BaseEntityConfig;
import com.csse3200.game.entities.configs.MinigameConfigs;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.input.InputComponent;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.PhysicsUtils;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.components.player.InteractionControllerComponent;
import com.csse3200.game.rendering.TextureRenderComponent;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.entities.configs.ShipConfig;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import com.csse3200.game.rendering.AnimationRenderComponent;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.csse3200.game.components.ships.ShipAnimationController;
import com.badlogic.gdx.graphics.g2d.Animation;


public class MinigameShipFactory {

    private static final ShipConfig stats = FileLoader.readClass(ShipConfig.class, "configs/ship.json");
    /**
     * Creates a new minigame ship to match the config file
     * @param config Configuration file to match ship to
     * @return Created minigame ship
     */
    public static Entity createMinigameShip() {
        InputComponent inputComponent =
                ServiceLocator.getInputService().getInputFactory().createForShip();

        AnimationRenderComponent animator = new AnimationRenderComponent(
                ServiceLocator.getResourceService().getAsset("images/ship.atlas", TextureAtlas.class));

        animator.addAnimation("Ship_LeftStill", 0.1f);
        animator.addAnimation("Ship_UpStill", 0.1f);
        animator.addAnimation("Ship_DownStill", 0.1f);
        animator.addAnimation("Ship_RightStill", 0.1f);
        animator.addAnimation("Ship_RightUp", 0.1f);
        animator.addAnimation("Ship_RightDown", 0.1f);
        animator.addAnimation("Ship_LeftDown", 0.1f);
        animator.addAnimation("Ship_LeftUp", 0.1f);
        animator.addAnimation("Ship_RotateUpLeft", 0.3f, Animation.PlayMode.NORMAL);

        Entity ship =
                new Entity()

                        //.addComponent(new TextureRenderComponent("images/Ship.png"))
                        //.addComponent(new TextureRenderComponent("images/LeftShip.png"))Dont add 2 of the same component class

                        .addComponent(animator)
                        .addComponent(new ShipAnimationController())
                        .addComponent(new PhysicsComponent())
                        .addComponent(new ColliderComponent())
                        .addComponent(new HitboxComponent().setLayer(PhysicsLayer.SHIP))
                        .addComponent(new ShipActions(stats.health, stats.fuel, stats.acceleration))
                        .addComponent(new ShipStatDisplay())
                        .addComponent(inputComponent)
                        .addComponent(new InteractionControllerComponent(false));
                        //.addComponent(new ShipStatsComponent(stats.health))
                        //.addComponent(new InventoryComponent(stats.gold))
                        //.addComponent(new TextureRenderComponent("images/Ship.png"))
                        //.addComponent(new TextureRenderComponent("images/LeftShip.png"))Dont add 2 of the same component class

                        //.addComponent(new TextureRenderComponent(config.spritePath))


        PhysicsUtils.setScaledCollider(ship, 0.6f, 0.3f);
        ship.getComponent(ColliderComponent.class).setDensity(1.5f);
        ship.getComponent(ShipStatDisplay.class).setShipActions(ship.getComponent(ShipActions.class));
        //ship.getComponent(TextureRenderComponent.class).scaleEntity();

        //Edited by Foref, changes physics to reflect space environment
        //With fixed rotation off, ship will spin without additional customization of shipactions
        //ship.getComponent(PhysicsComponent.class).getBody().setFixedRotation(false);
        //rotation too janky
        ship.getComponent(PhysicsComponent.class).getBody().setGravityScale(0);

        animator.startAnimation("Ship_UpStill");
        ship.setEntityType("ship");

        return ship;
    }

    private MinigameShipFactory() {
        throw new IllegalStateException("Instantiating static util class");
    }
}



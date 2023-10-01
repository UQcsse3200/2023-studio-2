package com.csse3200.game.entities.factories;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.Car.KeyboardCarInputComponent;
import com.csse3200.game.components.Car.CarActions;
import com.csse3200.game.components.Car.CarAnimationController;
import com.csse3200.game.entities.Entity;

import com.csse3200.game.input.InputComponent;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.ServiceLocator;

public class CarFactory {
    public static Entity createCar(Entity player) {

        AnimationRenderComponent animator = setupCarAnimations();
        InputComponent inputComponent = ServiceLocator.getInputService().getInputFactory().createForCar();

        Entity car = new Entity()
                .addComponent(new PhysicsComponent())
                .addComponent(new CarAnimationController())
                .addComponent(animator)
                .addComponent(inputComponent)
                .addComponent(new CarActions());

        car.getComponent(AnimationRenderComponent.class).scaleEntity();
        car.getComponent(CarActions.class).setPlayer(player);
        car.getComponent(KeyboardCarInputComponent.class).setActions(car.getComponent(CarActions.class));
        return car;
    }

    private static AnimationRenderComponent setupCarAnimations() {
        AnimationRenderComponent animator = new AnimationRenderComponent(
                ServiceLocator.getResourceService().getAsset("images/car/car.atlas", TextureAtlas.class));

        addCarAnimations(animator, "left");
        addCarAnimations(animator, "right");
        addCarAnimations(animator, "up");
        addCarAnimations(animator, "down");
        addStopAnimations(animator, "normal");
        addMoveAnimations(animator, "normal");

        return animator;
    }

    private static void addCarAnimations(AnimationRenderComponent animator, String direction) {
        animator.addAnimation("car_" + direction, 0.2f, Animation.PlayMode.LOOP);
    }

    private static void addStopAnimations(AnimationRenderComponent animator, String mode) {
        for (String direction : new String[]{"left", "right", "up", "down"}) {
            animator.addAnimation("car_stop_" + direction + "_" + mode, 0.2f, Animation.PlayMode.LOOP);
        }
    }

    private static void addMoveAnimations(AnimationRenderComponent animator, String mode) {
        for (String direction : new String[]{"left", "right", "up", "down"}) {
            animator.addAnimation("car_move_" + direction + "_" + mode, 0.2f, Animation.PlayMode.LOOP);
        }
    }
}
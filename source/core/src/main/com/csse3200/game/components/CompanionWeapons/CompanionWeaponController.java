package com.csse3200.game.components.CompanionWeapons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.Component;
import com.csse3200.game.entities.configs.CompanionWeaponConfig;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.ServiceLocator;

public class CompanionWeaponController extends Component {
    CompanionWeaponType weaponType;
    /* Variable to hold the remaining life (in game ticks) of a weapon) */
    int remainingDuration;
    /* Variable to hold the speed at which the projectile moves */
    float speed;
    /* The rotational speed of the weapon*/
    int rotationSpeed;
    /* The rotation of the weapon*/
    float currentRotation;
    /* number degrees of positive x-axis image is */
    int imageRotationOffset;
    /* Determined by # of directions in sprite sheet */
    int animationType;

    /**
     * Class to store variables of a spawned weapon
     */
    public CompanionWeaponController(CompanionWeaponType weaponType,
                                     int weaponDuration,
                                     float currentRotation,
                                     float speed,
                                     int rotationSpeed,
                                     int animationType,
                                     int imageRotationOffset) {
        this.weaponType = weaponType;
        this.remainingDuration = weaponDuration;
        this.currentRotation = currentRotation;
        this.speed = speed;
        this.rotationSpeed = rotationSpeed;
        this.animationType = animationType;
        this.imageRotationOffset = imageRotationOffset;
    }


    public void create() {
        this.entity.getEvents().addListener("death", this::setDuration);
    }

    /**
     * Sets the remaining duration
     * @param duration duration to set reamining duration
     */
    private void setDuration(int duration) {
        this.remainingDuration = duration;
    }

    /**
     * Function to control projectile movement once it has spawned
     */

    public void update() {
//        if (--this.remainingDuration <= 0) {
//            this.despawn();
//            return;
//        }
        //switch statement to define weapon movement based on type (a projectile
        Vector2 movement = switch (this.weaponType) {
            case Death_Potion -> update_swing();

            default -> null;
        };

        //Reference to current position of the projectile
        Vector2 position = entity.getPosition();
        //Update position and rotation of projectile
        entity.setPosition(new Vector2(position.x + movement.x, position.y + movement.y));


    }
//    private void despawn() {
//        AnimationRenderComponent animator = entity.getComponent(AnimationRenderComponent.class);
//        animator.stopAnimation();
//        Gdx.app.postRunnable(entity::dispose);
//    }
//



    private Vector2 update_swing() {
        CompanionWeaponTargetComponent weaponTargetComponent = entity.getComponent(CompanionWeaponTargetComponent.class);
        Vector2 targetPosition = weaponTargetComponent.get_pos_of_target();

        // Calculate the direction vector towards the target
        Vector2 direction = targetPosition.cpy().sub(entity.getPosition()).nor();

        // Update the position based on the direction and speed
        Vector2 movement = direction.scl(this.speed * Gdx.graphics.getDeltaTime());

        return movement;
    }
}

package com.csse3200.game.components.Weapons;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.csse3200.game.components.Weapons.WeaponType;
import com.csse3200.game.components.Component;
import com.csse3200.game.entities.Entity;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.rendering.AnimationRenderComponent;

import java.util.*;

/**
 * Class to control the movement of weapons that have been spawned
 */
public class WeaponControllerComponent extends Component {
    /* Weapon Type to be controlled*/
    WeaponType weaponType;
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

    /**
     * Class to store variables of a spawned weapon
     */
    public WeaponControllerComponent(WeaponType weaponType,
                                     float currentRotation,
                                     float speed,
                                     int remainingDuration,
                                     int rotationSpeed,
                                     int imageRotationOffset) {
        this.weaponType = weaponType;
        this.remainingDuration = remainingDuration;
        this.speed = speed;
        this.rotationSpeed = rotationSpeed;
        this.currentRotation = currentRotation;
        this.imageRotationOffset = imageRotationOffset;
    }

    /**
     * Function to control projectile movement once it has spawned
     */
    @Override
    public void update() {
        //switch statement to define weapon movement based on type (a projectile
        Vector2 movement = switch (this.weaponType) {
            case SLING_SHOT -> weapon_target_update();
            case ELEC_WRENCH -> weapon_1_update();
            case THROW_ELEC_WRENCH -> weapon_2_update();
            case STICK -> null; // todo
            case LASERGUN -> null;
            case KATANA -> null;
            case WOODHAMMER -> null;
            case STONEHAMMER -> null;
        };


        //Reference to current position of the projectile
        Vector2 position = entity.getPosition();
        //Update position and rotation of projectile
        entity.setPosition(new Vector2(position.x + movement.x, position.y + movement.y));
        entity.setRotation(this.currentRotation - this.imageRotationOffset);
        if (--this.remainingDuration == 0) {this.despawn();}
    }

    private void despawn() {
        AnimationRenderComponent animator = entity.getComponent(AnimationRenderComponent.class);
        animator.stopAnimation();
        ServiceLocator.getEntityService().unregister(entity);
    }

    //TODO fix this forbidden code
    private Vector2 weapon_x_update() {
        //Edit this.currentRotation and return movement vector
        Vector2 movement = new Vector2(0,0);
        //double radians = Math.toRadians(currentRotation);
        return movement;
    }

    private Vector2 weapon_1_update() {
        Vector2 movement = new Vector2(0,0);
        this.currentRotation -= this.rotationSpeed;
        double radians = Math.toRadians(currentRotation);
        movement.x = (float) Math.cos(radians) * 0.015f * this.speed;
        movement.y = (float) Math.sin(radians) * 0.015f * this.speed;
        return movement;
    }

    private Vector2 weapon_2_update() {
        Vector2 movement = new Vector2(0,0);
        double radians = Math.toRadians(currentRotation);
        movement.x = (float) Math.cos(radians) * 0.015f * this.speed;
        movement.y = (float) Math.sin(radians) * 0.015f * this.speed;
        return movement;
    }

    private Vector2 weapon_target_update() {
        Vector2 movement = new Vector2(0,0);
        WeaponTargetComponent weaponTargetComponent = entity.getComponent(WeaponTargetComponent.class);
        Vector2 target_pos = weaponTargetComponent.get_pos_of_target();
        Vector2 weapon_pos = entity.getPosition();

        double radians = Math.atan2(target_pos.y - weapon_pos.y, target_pos.x - weapon_pos.x);
        movement.x = (float) Math.cos(radians) * 0.015f * this.speed;
        movement.y = (float) Math.sin(radians) * 0.015f * this.speed;
        return movement;
    }
}


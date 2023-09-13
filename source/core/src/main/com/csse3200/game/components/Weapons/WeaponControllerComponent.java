package com.csse3200.game.components.Weapons;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.Weapons.WeaponType;
import com.csse3200.game.components.Component;
import com.csse3200.game.entities.Entity;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.physics.components.HitboxComponent;
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
    /* Determined by # of directions in sprite sheet */
    int animationType;

    /**
     * Class to store variables of a spawned weapon
     */
    public WeaponControllerComponent(WeaponType weaponType,
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

    @Override
    public void create() {
        this.entity.getEvents().addListener("death", this::setDuration);
    }

    private void setDuration(int duration) {
        this.remainingDuration = duration;
    }

    /**
     * Function to control projectile movement once it has spawned
     */
    @Override
    public void update() {
        //switch statement to define weapon movement based on type (a projectile
        Vector2 movement = switch (this.weaponType) {
            case MELEE_WRENCH, MELEE_KATANA, MELEE_BEE_STING, RANGED_BOOMERANG -> update_swing();
            case RANGED_SLINGSHOT -> update_ranged_slingshot();
            case RANGED_HOMING -> update_ranged_homing();
            case STATIC_WEAPON -> update_static();
            default -> throw new IllegalStateException("Unexpected value: " + this.weaponType);
        };

        //Reference to current position of the projectile
        Vector2 position = entity.getPosition();
        //Update position and rotation of projectile
        entity.setPosition(new Vector2(position.x + movement.x, position.y + movement.y));
        entity.setRotation(this.currentRotation - this.imageRotationOffset);
        if (--this.remainingDuration <= 0) {this.despawn();}
    }



    private void despawn() {
        AnimationRenderComponent animator = entity.getComponent(AnimationRenderComponent.class);
        animator.stopAnimation();
        Gdx.app.postRunnable(entity::dispose);
    }

    //TODO fix this forbidden code
    private Vector2 weapon_x_update() {
        //Edit this.currentRotation and return movement vector
        Vector2 movement = new Vector2(0,0);
        //double radians = Math.toRadians(currentRotation);
        return movement;
    }

    private Vector2 update_melee_wrench() {
        return update_swing();
    }
    private Vector2 update_melee_katana() {
        return update_swing();
    }
    private Vector2 update_melee_bee_sting() {
        return update_swing();
    }

    private Vector2 update_swing() {
        WeaponTargetComponent weaponTargetComponent = entity.getComponent(WeaponTargetComponent.class);
        Vector2 movement = weaponTargetComponent.get_pos_of_target();

        this.currentRotation -= this.rotationSpeed;
        double radians = Math.toRadians(currentRotation);
        movement.x += (float) Math.cos(radians) * 0.01f * this.speed;
        movement.y += (float) Math.sin(radians) * 0.01f * this.speed;
        return movement;
    }

    private Vector2 update_ranged_slingshot() {
        return update_stright();
    }
    private Vector2 update_stright() {
        Vector2 movement = new Vector2(0,0);
        double radians = Math.toRadians(currentRotation);
        movement.x = (float) Math.cos(radians) * 0.015f * this.speed;
        movement.y = (float) Math.sin(radians) * 0.015f * this.speed;
        return movement;
    }

    private Vector2 update_ranged_boomerang() {
        return update_swing();
    }

    private Vector2 update_ranged_homing() {

        AnimationRenderComponent animator = entity.getComponent(AnimationRenderComponent.class);

        int dir = (int) Math.floor((this.currentRotation + 22.5) / 45);
        System.out.println(dir);
        switch (dir) {
            case 0, 8 -> animator.startAnimation("RIGHT2");
            case 1 -> animator.startAnimation("RIGHT1");
            case 2 -> animator.startAnimation("UP");
            case 3 -> animator.startAnimation("LEFT1");
            case 4, -4 -> animator.startAnimation("LEFT2");
            case -3 -> animator.startAnimation("LEFT3");
            case -2 -> animator.startAnimation("DOWN");
            case -1 -> animator.startAnimation("RIGHT3");
        }

        return update_mousehoming();
    }

    private Vector2 update_mousehoming() {
        Vector2 movement = new Vector2(0,0);
        WeaponTargetComponent weaponTargetComponent = entity.getComponent(WeaponTargetComponent.class);
        Vector2 target_pos = weaponTargetComponent.get_pos_of_target();
        Vector2 weapon_pos = entity.getPosition();

        var angle = (float) Math.atan2(target_pos.y - weapon_pos.y, target_pos.x - weapon_pos.x);
        movement.x = (float) Math.cos(angle) * 0.015f * this.speed;
        movement.y = (float) Math.sin(angle) * 0.015f * this.speed;
        this.currentRotation = (float) Math.toDegrees(angle);
        return movement;
    }

    private Vector2 update_static() {
        WeaponTargetComponent weaponTargetComponent = entity.getComponent(WeaponTargetComponent.class);
        return weaponTargetComponent.get_pos_of_target();
    }
}


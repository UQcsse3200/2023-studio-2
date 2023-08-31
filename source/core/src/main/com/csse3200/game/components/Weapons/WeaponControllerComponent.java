package com.csse3200.game.components.Weapons;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.csse3200.game.components.Weapons.WeaponType;
import com.csse3200.game.components.Component;
import com.csse3200.game.entities.Entity;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.services.ServiceLocator;

import java.util.*;

/**
 * Class to control the movement of weapons that have been spawned
 */
public class WeaponControllerComponent extends Component {
    /* Weapon Type to be controlled*/
    WeaponType weaponType;
    /* Variable to hold the remaining life (in game ticks) of a weapon) */
    int remainigLife;
    /* Variable to hold the speed at which the projectile moves */
    int speed;
    /* The rotational speed of the weapon*/
    int rotationSpeed;
    /* The rotation of the weapon*/
    float currentRotation;
    /* number degrees of positive x-axis image is */
    int imageRotationOffset;

    /**
     * Class to store variables of a spawned weapon
     */
    public WeaponControllerComponent(WeaponType weaponType, float currentRotation,
                                     int remainigLife,int speed, int rotationSpeed, int imageRotationOffset) {
        this.weaponType = weaponType;
        this.remainigLife = remainigLife;
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
        //Reference to current position of the projectile
        Vector2 position = entity.getPosition();
        float xMovement = 0;
        float yMovement = 0;
        double radians;

        //switch statement to define weapon movement based on type (a projectile
        switch (this.weaponType) {
            case ELEC_WRENCH:
                this.currentRotation -= this.rotationSpeed;
                radians = Math.toRadians(currentRotation);
                xMovement = (float) Math.cos(radians) * 0.015f * this.speed;
                yMovement = (float) Math.sin(radians) * 0.015f * this.speed;
                break;
            case THROW_ELEC_WRENCH:
                radians = Math.toRadians(currentRotation);
                xMovement = (float) Math.cos(radians) * 0.015f * this.speed;
                yMovement = (float) Math.sin(radians) * 0.015f * this.speed;
        }

        //Update position and rotation of projectile
        entity.setRotation(this.currentRotation - this.imageRotationOffset);
        entity.setPosition(new Vector2(position.x + xMovement, position.y + yMovement));

        //Function to despawn Projectile after left is zero
        //Todo this needs to be changed to actually work
        if (--this.remainigLife == 0) {
            Gdx.app.postRunnable(entity::dispose);
        }
    }
}


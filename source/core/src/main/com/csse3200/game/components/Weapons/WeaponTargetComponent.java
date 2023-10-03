package com.csse3200.game.components.Weapons;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.player.KeyboardPlayerInputComponent;
import com.csse3200.game.entities.Entity;

/**
 * Class to control the movement of weapons that have been spawned
 */
public class WeaponTargetComponent extends Component {
    /* Weapon Type to be controlled*/
    Entity entity;
    WeaponType weaponType;
    Vector2 trackPrev;

    //For the shield rotations
    /**
     * offsets are a value between
     * -100 and 100
     * It gets divided down by 20, so that the final offsets are between
     * -5 and 5
     *
     * The rotation_directions are an integer, -1 or 1
     * 0 represents growing negative, 1 represents growing positive
     */
    float x_rotate_offset;
    int x_rotation_direction;
    float y_rotate_offset;
    int y_rotation_direction;



    /**
     * Class to store variables of a spawned weapon
     */
    public WeaponTargetComponent(WeaponType weaponType, Entity entity) {
        this.weaponType = weaponType;
        this.entity = entity;
        this.trackPrev = entity.getPosition();

        //if it is a shield, adjust the rotation offsets
        //start shield in the far right corner
        if (this.weaponType == WeaponType.SHIELD) {
            x_rotate_offset = 100;
            x_rotation_direction = 1;
            y_rotate_offset = 0;
            y_rotation_direction = 1;
        }
    }

    /**
     * Function to control projectile movement once it has spawned
     * It gets called every single frame
     */
    public Vector2 get_pos_of_target() {
        //switch statement to define weapon movement based on type (a projectile
        Vector2 pos;

        switch (this.weaponType) {
            case RANGED_HOMING:
                KeyboardPlayerInputComponent keyboardPlayerInputComponent = entity.getComponent(KeyboardPlayerInputComponent.class);
                //if (keyboardPlayerInputComponent == null) {return null;}
                pos = keyboardPlayerInputComponent.getLastMousePos();
                Vector2 eScl = entity.getScale();
                return new Vector2(pos.x + eScl.x/2 - 0.1f, pos.y + eScl.y/2 - 0.1f);
            case STATIC_WEAPON, MELEE_WRENCH, MELEE_KATANA, MELEE_BEE_STING, RANGED_BOOMERANG:
                var delta = entity.getPosition().sub(this.trackPrev);
                this.trackPrev = entity.getPosition();
                return delta;
            case SHIELD:
                return rotate_shield_around_entity();
            default:
                return new Vector2(0, 0);
        }


    }

    public Vector2 rotate_shield_around_entity() {
        //companion position
        var companion = entity.getPosition().sub(this.trackPrev);

        float scalaingFactor = 7000;

        // Grab the current offsets, and scale down the offset down to between
        // -5 and 5
        float x_offset = x_rotate_offset / scalaingFactor;
        float y_offset = y_rotate_offset / scalaingFactor;

        //float x_offset = (float) 0.002;
        //float y_offset = (float) 0.002;
        // create the offset vector
        var offsetVector = new Vector2(x_offset, y_offset);

        //offset the  companion position
        companion.add(offsetVector);

        //update the offset values
        update_shield_rotation_offsets();

        //update the latest trackPrev to the new entity position
        this.trackPrev = entity.getPosition();
        return companion;
    }

    /**
     * This function cycles x and y values from -100 to 100
     * Using an incrementor, whi
     */
    public void update_shield_rotation_offsets() {
        // if the offset has reached 100 yet, flip the direction
        if (Math.abs(x_rotate_offset) == 100) {
            x_rotation_direction  = x_rotation_direction*-1;
        }
        x_rotate_offset += x_rotation_direction;

        //y
        // if the offset has reached 100 yet, flip the direction
        if (Math.abs(y_rotate_offset) == 100) {
            y_rotation_direction  = y_rotation_direction*-1;
        }
        y_rotate_offset += y_rotation_direction;
    }


}



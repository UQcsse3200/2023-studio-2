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

    /**
     * Class to store variables of a spawned weapon
     */
    public WeaponTargetComponent(WeaponType weaponType, Entity entity) {
        this.weaponType = weaponType;
        this.entity = entity;
        this.trackPrev = entity.getPosition();
    }

    /**
     * Function to control projectile movement once it has spawned
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

            default:
                return new Vector2(0, 0);
        }
    }
}



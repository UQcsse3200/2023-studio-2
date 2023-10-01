package com.csse3200.game.components.CompanionWeapons;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.player.KeyboardPlayerInputComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.components.Companion.KeyboardCompanionInputComponent;

public class CompanionWeaponTargetComponent extends Component {
    Entity entity;
    CompanionWeaponType weaponType;
    Vector2 trackPrev;

    /**
     * Class to store variables of a spawned weapon
     */
    public CompanionWeaponTargetComponent(CompanionWeaponType weaponType, Entity entity) {
        this.weaponType = weaponType;
        this.entity = entity;
        this.trackPrev = entity.getPosition();
    }

    public Vector2 get_pos_of_target() {
        //switch statement to define weapon movement based on type (a projectile
        Vector2 pos;

        switch (this.weaponType) {
            case Death_Potion:
                KeyboardCompanionInputComponent keyboardCompanionInputComponent = entity.getComponent(KeyboardCompanionInputComponent.class);
                //if (keyboardPlayerInputComponent == null) {return null;}
                pos = keyboardCompanionInputComponent.getLastMousePos();
                Vector2 eScl = entity.getScale();
                return new Vector2(pos.x + eScl.x/2 - 0.1f, pos.y + eScl.y/2 - 0.1f);

//                var delta = entity.getPosition().sub(this.trackPrev);
//                this.trackPrev = entity.getPosition();
//                return delta;

            default:
                return new Vector2(0, 0);
        }

    }
}
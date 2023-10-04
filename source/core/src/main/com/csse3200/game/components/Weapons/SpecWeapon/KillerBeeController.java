package com.csse3200.game.components.Weapons.SpecWeapon;

import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.WeaponConfig;
import com.csse3200.game.rendering.AnimationRenderComponent;

public class KillerBeeController extends ProjectileController {

    public KillerBeeController(WeaponConfig config,
                               float attackDirection,
                               Entity player, int attackNum) {
        super(config, attackDirection, player, attackNum);
    }

    @Override
    protected void initial_rotation() {
        currentRotation = ((int) (((currentRotation + 22.5f) % 360) / 45f)) * 45f;
        currentRotation = (currentRotation + (float) ((attackNum * (90 / 7) - 45))) % 360;
    }

    @Override
    protected void initial_animation(AnimationRenderComponent animator) {
        int dir = Math.round(currentRotation / 45);
        switch (dir) {
            case 0, 7 -> animator.startAnimation("RIGHT1");
            case 2, 1, 3 -> animator.startAnimation("UP");
            case 4, 5 -> animator.startAnimation("LEFT1");
            case 6 -> animator.startAnimation("DOWN");
        }
    }
}
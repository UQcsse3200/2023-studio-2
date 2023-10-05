package com.csse3200.game.components.Weapons.SpecWeapon;

import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.WeaponConfig;
import com.csse3200.game.rendering.AnimationRenderComponent;

public class KillerBeeController extends ProjectileController {
    private float speed;
    public KillerBeeController(WeaponConfig config,
                               float attackDirection,
                               Entity player, int attackNum) {
        super(config, attackDirection, player, attackNum);
        speed = config.weaponSpeed;
        speed *= switch (attackNum) {
            case 0 -> 0.7f;
            case 1 -> 1.5f;
            case 2 -> 0.9f;
            case 3 -> 1.2f;
            case 4 -> 1.1f;
            case 5 -> 1.3f;
            case 6 -> 0.8f;
            case 7 -> 1.2f;
            default -> 1;
        };
    }

    @Override
    public Integer get_spawn_delay() {
        return switch (attackNum) {
            case 0 -> 50;
            case 1 -> 85;
            case 2 -> 85;
            case 3 -> 0;
            case 4 -> 90;
            case 5 -> 50;
            case 6 -> 25;
            case 7 -> 100;
            default -> 0;
        };
    }

    @Override
    protected void initial_rotation() {
        currentRotation = ((int) (((currentRotation + 22.5f) % 360) / 45f)) * 45f;
        currentRotation = ((currentRotation + (float) ((attackNum * (90 / 7) - 45))) + 360) % 360;
    }

    @Override
    protected void initial_animation(AnimationRenderComponent animator) {
        int dir = (int) (((currentRotation + 22.5f + 360f) % 360) / 45);
        switch (dir) {
            case 0 -> animator.startAnimation("RIGHT2");
            case 1 -> animator.startAnimation("RIGHT1");
            case 2 -> animator.startAnimation("UP");
            case 3 -> animator.startAnimation("LEFT1");
            case 4 -> animator.startAnimation("LEFT2");
            case 5 -> animator.startAnimation("LEFT3");
            case 6 -> animator.startAnimation("DOWN");
            case 7 -> animator.startAnimation("RIGHT3");
        }
    }

    @Override
    protected void move() {
        entity.setPosition(entity.getPosition()
                .add(positionInDirection(currentRotation, 0.01f * speed))
        );
    }
}
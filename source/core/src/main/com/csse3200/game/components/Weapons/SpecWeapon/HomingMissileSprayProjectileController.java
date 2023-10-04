package com.csse3200.game.components.Weapons.SpecWeapon;

import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.WeaponConfig;

public class HomingMissileSprayProjectileController extends HomingProjectileController {
    private float speed;

    public HomingMissileSprayProjectileController(WeaponConfig config,
                                                  float attackDirection,
                                                  Entity player, int attackNum) {
        super(config, attackDirection, player, attackNum);
        this.speed = 2;
    }

    @Override
    public Integer get_spawn_delay() {
        return switch (attackNum) {
            case 0 -> 100;
            case 1 -> 400;
            case 2 -> 650;
            case 3 -> 850;
            case 4 -> 1050;
            case 5 -> 1200;
            case 6 -> 1300;
            default -> (attackNum - 6) * 70 + 1300;
        };
    }

    @Override
    protected void initial_rotation() {
        currentRotation = (currentRotation + (float) ((Math.random() - 0.5) * 30)) % 360;
    }

    @Override
    protected void move() {
        speed += 0.3;
        entity.setPosition(entity.getPosition()
                .add(positionInDirection(currentRotation, 0.01f * this.speed))
        );
    }
}
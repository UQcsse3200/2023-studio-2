package com.csse3200.game.components.Weapons.SpecWeapon.Projectile;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.explosives.ExplosiveComponent;
import com.csse3200.game.components.explosives.ExplosiveConfig;
import com.csse3200.game.components.player.KeyboardPlayerInputComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.WeaponConfig;

public class HomingProjectileController extends ProjectileController {
    public HomingProjectileController(WeaponConfig config,
                                      float attackDirection,
                                      Entity player, int attackNum) {
        super(config, attackDirection, player, attackNum);
    }

    @Override
    public void create() {
        super.create();
        var explosiveConfig = new ExplosiveConfig();
        explosiveConfig.chainable = false;
        explosiveConfig.baseAttack = 20;
        explosiveConfig.damageRadius = 2.5f;
        explosiveConfig.chainRadius = 3.0f;
        explosiveConfig.effectPath = "particle-effects/explosion/explosion.effect";
        explosiveConfig.soundPath = "sounds/explosion/grenade.mp3";

        var explode = new ExplosiveComponent(explosiveConfig);
        entity.addComponent(explode);
        explode.create();
    }


    @Override
    protected void rotate() {
        //kbPIComp = kiloBytes of π Comparator
        KeyboardPlayerInputComponent kbPIComp = player.getComponent(KeyboardPlayerInputComponent.class);
        if (kbPIComp == null) {
            return;
        }
        Vector2 targetpos = kbPIComp.getLastMousePos().sub(entity.getScale().scl(0.5f));
        Vector2 weaponpos = entity.getPosition();

        float targetAngle = (float) Math.toDegrees(Math.atan2(targetpos.y - weaponpos.y, targetpos.x - weaponpos.x));
        targetAngle = (targetAngle + 360) % 360;

        float dir = targetAngle - currentRotation;
        if (dir > 180) {
            dir -= 360;
        } else if (dir < -180) {
            dir += 360;
        }

        currentRotation = ((
                (currentRotation +
                        ((dir > 0) ? Math.min(config.rotationSpeed, dir) : Math.max(-config.rotationSpeed, dir)))
        ) + 360) % 360;
    }

    @Override
    protected void reanimate() {
        animator.setRotation(currentRotation);
    }

    @Override
    protected void despawn() {
        entity.getEvents().trigger("explode");
    }
}
package com.csse3200.game.components.Weapons.SpecWeapon;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.Weapons.WeaponType;
import com.csse3200.game.components.explosives.ExplosiveComponent;
import com.csse3200.game.components.explosives.ExplosiveConfig;
import com.csse3200.game.components.player.KeyboardPlayerInputComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.WeaponConfig;
import com.csse3200.game.rendering.AnimationRenderComponent;

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
        explosiveConfig.damage = 20;
        explosiveConfig.damageRadius = 2.5f;
        explosiveConfig.chainRadius = 3.0f;
        explosiveConfig.effectPath = "particle-effects/explosion/explosion.effect";
        explosiveConfig.soundPath = "sounds/explosion/grenade.mp3";

        entity.addComponent(new ExplosiveComponent(explosiveConfig));
    }

    @Override
    protected void initial_animation(AnimationRenderComponent animator) {
        reanimate();
    }

    @Override
    protected void rotate() {
        //kbPIComp = kiloBytes of π Comparator
        KeyboardPlayerInputComponent kbPIComp = player.getComponent(KeyboardPlayerInputComponent.class);
        if (kbPIComp == null) {
            return;
        }
        Vector2 target_pos = kbPIComp.getLastMousePos().sub(entity.getScale().scl(0.5f));
        Vector2 weapon_pos = entity.getPosition();

        float targetAngle = (float) Math.toDegrees(Math.atan2(target_pos.y - weapon_pos.y, target_pos.x - weapon_pos.x));
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
        AnimationRenderComponent animator = entity.getComponent(AnimationRenderComponent.class);
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
    protected void despawn() {
        entity.getEvents().trigger("explode");
    }
}
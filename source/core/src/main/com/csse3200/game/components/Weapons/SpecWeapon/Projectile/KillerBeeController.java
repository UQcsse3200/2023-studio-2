package com.csse3200.game.components.Weapons.SpecWeapon.Projectile;

import com.badlogic.gdx.graphics.g2d.Animation;
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
    public int get_spawn_delay() {
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
    protected void set_animations() {
        animator = entity.getComponent(AnimationRenderComponent.class);
        animator.addAnimation("ATTACK1", 0.07f, Animation.PlayMode.NORMAL);
        animator.addAnimation("ATTACK2", 0.07f, Animation.PlayMode.NORMAL);
    }

    @Override
    protected void initial_rotation() {
        currentRotation = ((int) (((currentRotation + 22.5f) % 360) / 45f)) * 45f;
        currentRotation = ((currentRotation + (float) (attackNum * ((double) 90 / 7) - 45)) + 360) % 360;
    }

    @Override
    protected void initial_animation() {
        if (currentRotation < 90 || currentRotation > 270) {
            animator.startAnimation("ATTACK1");
            animator.setRotation(currentRotation);
        } else {
            animator.startAnimation("ATTACK2");
            animator.setRotation(currentRotation + 180);
        }
    }

    @Override
    protected void move() {
        entity.setPosition(entity.getPosition()
                .add(positionInDirection(currentRotation, 0.01f * speed))
        );
    }
}
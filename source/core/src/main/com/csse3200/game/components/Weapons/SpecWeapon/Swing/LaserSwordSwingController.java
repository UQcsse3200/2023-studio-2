package com.csse3200.game.components.Weapons.SpecWeapon.Swing;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.Weapons.WeaponControllerComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.WeaponConfig;
import com.csse3200.game.rendering.AnimationRenderComponent;

public class LaserSwordSwingController extends MeleeSwingController {
    private boolean charged = false;
    protected int attackSide;

    public LaserSwordSwingController(WeaponConfig config,
                                     float attackDirection,
                                     Entity player) {
        super(config, attackDirection, player);

    }

    @Override
    protected void set_animations() {
        animator = entity.getComponent(AnimationRenderComponent.class);
        animator.addAnimation("PRE_ATTACK1", 0.2f, Animation.PlayMode.NORMAL);
        animator.addAnimation("ATTACK1", 0.04f, Animation.PlayMode.NORMAL);
        animator.addAnimation("PRE_ATTACK2", 0.2f, Animation.PlayMode.NORMAL);
        animator.addAnimation("ATTACK2", 0.04f, Animation.PlayMode.NORMAL);
    }


    @Override
    protected void initial_rotation() {
        return;
    }


    @Override
    protected void initial_animation() {
        if (attackSide == -1) {
            animator.startAnimation("PRE_ATTACK1");
            animator.setRotation(currentRotation);
        } else {
            animator.startAnimation("PRE_ATTACK2");
            animator.setRotation(currentRotation + 180);
        }
    }


    @Override
    protected void move() {
        Vector2 player_delta = player.getPosition().sub(player_last_pos);
        this.player_last_pos = player.getPosition();

        entity.setPosition(entity.getPosition()
                .add(player_delta.cpy())
                .add(positionInDirection(currentRotation + (float) attackSide * 90f,
                        0.01f * config.weaponSpeed).scl(charged && remainingDuration > 15 ? 1 : 0))
        );
    }

    @Override
    protected void reanimate() {
        if (!charged && remainingDuration < 30) {
            if (attackSide == -1) {
                animator.startAnimation("ATTACK1");
            } else {
                animator.startAnimation("ATTACK2");
            }
            charged = true;
        }
    }
}
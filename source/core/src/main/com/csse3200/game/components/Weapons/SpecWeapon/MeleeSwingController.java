package com.csse3200.game.components.Weapons.SpecWeapon;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.Weapons.WeaponControllerComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.WeaponConfig;
import com.csse3200.game.rendering.AnimationRenderComponent;

public class MeleeSwingController extends WeaponControllerComponent {
    private Vector2 playerlastpos;
    private int attackSide;

    public MeleeSwingController(WeaponConfig config,
                                float attackDirection,
                                Entity player) {
        super(config, attackDirection, player);
        this.playerlastpos = player.getPosition();
        attackSide = (currentRotation > 90 && currentRotation < 270) ? 1 : -1;
    }

    @Override
    public void create() {
        super.create();
    }

    @Override
    protected void set_animations() {
        animator = entity.getComponent(AnimationRenderComponent.class);
        animator.addAnimation("ATTACK1", 0.07f, Animation.PlayMode.NORMAL);
        animator.addAnimation("ATTACK2", 0.07f, Animation.PlayMode.NORMAL);
    }


    @Override
    protected void initial_rotation() {
    }

    @Override
    protected void initial_position() {
        entity.setPosition(player.getCenterPosition()
                .mulAdd(entity.getScale(), -0.5f)
                .add(positionInDirection(currentRotation - (float) attackSide * 25f, 0.8f))
        );
    }

    @Override
    protected void initial_animation() {
        if (attackSide == -1) {
            animator.startAnimation("ATTACK1");
            animator.setRotation(currentRotation);
        } else {
            animator.startAnimation("ATTACK2");
            animator.setRotation(currentRotation + 180);
        }
    }

    @Override
    protected void rotate() {
    }

    @Override
    protected void move() {
        Vector2 player_delta = player.getPosition().sub(playerlastpos);
        this.playerlastpos = player.getPosition();

        entity.setPosition(entity.getPosition()
                .add(player_delta.cpy())
                .add(positionInDirection(currentRotation + (float) attackSide * 90f,
                        0.01f * config.weaponSpeed))
        );
    }

    @Override
    protected void reanimate() {
    }
}
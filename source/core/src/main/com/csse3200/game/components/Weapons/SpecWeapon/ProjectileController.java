package com.csse3200.game.components.Weapons.SpecWeapon;

import com.csse3200.game.components.Weapons.WeaponControllerComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.WeaponConfig;
import com.csse3200.game.rendering.AnimationRenderComponent;

public class ProjectileController extends WeaponControllerComponent {

    public ProjectileController(WeaponConfig config,
                                float attackDirection,
                                Entity player) {
        super(config, attackDirection, player);
    }

    @Override
    public void create() {
        super.create();
    }

    @Override
    protected void initial_rotation() {
        return;
    }

    @Override
    protected void initial_position() {
        entity.setPosition(player.getCenterPosition()
                .mulAdd(entity.getScale(), -0.5f)
                .add(positionInDirection(currentRotation, 0.5f))
        );
    }

    @Override
    protected void initial_animation(AnimationRenderComponent animator) {
        animator.startAnimation("UP");
    }

    @Override
    protected void rotate() {
        return;
    }

    @Override
    protected void move() {
        entity.setPosition(entity.getPosition()
                .add(positionInDirection(currentRotation, 0.01f * config.weaponSpeed))
        );
    }

    @Override
    protected void reanimate() {
        return;
    }
}
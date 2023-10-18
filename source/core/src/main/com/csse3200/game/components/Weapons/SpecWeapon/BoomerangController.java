package com.csse3200.game.components.Weapons.SpecWeapon;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.Weapons.WeaponControllerComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.WeaponConfig;
import com.csse3200.game.rendering.AnimationRenderComponent;

public class BoomerangController extends WeaponControllerComponent {
    private Vector2 playerlastpos;

    public BoomerangController(WeaponConfig config,
                               float attackDirection,
                               Entity player, int attackNum) {
        super(config, attackDirection, player, attackNum);
        this.playerlastpos = player.getPosition();
    }

    @Override
    protected void set_animations() {
        animator = entity.getComponent(AnimationRenderComponent.class);
        animator.addAnimation("ATTACK", 0.07f, Animation.PlayMode.LOOP);
    }

    @Override
    protected void initial_rotation() {
        currentRotation += 75 * (attackNum % 2 == 0 ? 1 : -1);;
    }

    @Override
    protected void initial_position() {
        entity.setPosition(player.getPosition()
                .add(player.getScale().scl(0.5f))
                .sub(entity.getScale().scl(0.5f))
                .add(positionInDirection(currentRotation, 0.8f))
        );
    }

    @Override
    protected void initial_animation() {
        animator.startAnimation("ATTACK");
    }

    @Override
    protected void rotate() {
        this.currentRotation -= config.rotationSpeed * (attackNum % 2 == 0 ? 1 : -1);
    }


    @Override
    protected void move() {
        Vector2 player_delta = player.getPosition().sub(playerlastpos);
        this.playerlastpos = player.getPosition();

        entity.setPosition(entity.getPosition()
                .add(player_delta.cpy())
                .add(positionInDirection(currentRotation,0.01f * config.weaponSpeed))
        );
    }

    @Override
    protected void reanimate() {
    }
}
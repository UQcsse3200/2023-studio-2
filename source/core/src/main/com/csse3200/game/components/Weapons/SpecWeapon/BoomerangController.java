package com.csse3200.game.components.Weapons.SpecWeapon;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.Weapons.WeaponControllerComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.WeaponConfig;
import com.csse3200.game.rendering.AnimationRenderComponent;

public class BoomerangController extends WeaponControllerComponent {
    private Vector2 player_last_pos;

    public BoomerangController(WeaponConfig config,
                               float attackDirection,
                               Entity player, int attackNum) {
        super(config, attackDirection, player, attackNum);
        this.player_last_pos = player.getPosition();
    }

    @Override
    protected void add_animations(AnimationRenderComponent animator) {
        animator.addAnimation("UP", 0.07f, Animation.PlayMode.LOOP);
    }

    @Override
    protected void initial_rotation() {
        System.out.println(attackNum);
        currentRotation += 45 * (attackNum % 2 == 0 ? 1 : -1);;
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
    protected void initial_animation(AnimationRenderComponent animator) {
        animator.startAnimation("UP");
    }

    @Override
    protected void rotate() {
        this.currentRotation -= config.rotationSpeed * (attackNum % 2 == 0 ? 1 : -1);
    }


    @Override
    protected void move() {
        Vector2 player_delta = player.getPosition().sub(player_last_pos);
        this.player_last_pos = player.getPosition();

        entity.setPosition(entity.getPosition()
                .add(player_delta.cpy())
                .add(positionInDirection(currentRotation,0.01f * config.weaponSpeed))
        );
    }

    @Override
    protected void reanimate() {return;}
}
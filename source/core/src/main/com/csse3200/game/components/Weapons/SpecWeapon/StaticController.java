package com.csse3200.game.components.Weapons.SpecWeapon;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.SoundComponent;
import com.csse3200.game.components.Weapons.WeaponControllerComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.WeaponConfig;
import com.csse3200.game.rendering.AnimationRenderComponent;

public class StaticController extends WeaponControllerComponent {
    private Vector2 player_last_pos;

    public StaticController(WeaponConfig config,
                            float attackDirection,
                            Entity player) {
        super(config, attackDirection, player);
        this.remainingDuration = 25;
        this.player_last_pos = player.getPosition();
    }

    @Override
    protected void set_animations() {
    }

    @Override
    protected void set_sound() {
    }

    @Override
    protected void initial_rotation() {
    }

    @Override
    protected void initial_position() {
        entity.setPosition(player.getCenterPosition()
                .mulAdd(entity.getScale(), -0.5f)
                .add(positionInDirection(currentRotation, 0.5f))
        );
    }

    @Override
    protected void initial_animation() {
    }

    @Override
    protected void rotate() {
    }

    @Override
    protected void move() {
        Vector2 player_delta = player.getPosition().sub(player_last_pos);
        this.player_last_pos = player.getPosition();
        entity.setPosition(entity.getPosition().add(player_delta.cpy()));
    }

    @Override
    protected void reanimate() {
    }

    @Override
    protected void despawn () {
        Gdx.app.postRunnable(entity::dispose);
    }
}
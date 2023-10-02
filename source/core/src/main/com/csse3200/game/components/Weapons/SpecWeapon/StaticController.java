package com.csse3200.game.components.Weapons.SpecWeapon;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.Weapons.WeaponControllerComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.WeaponConfig;
import com.csse3200.game.rendering.AnimationRenderComponent;

public class StaticController extends WeaponControllerComponent {
    Entity player;
    Vector2 player_last_pos;

    public StaticController(WeaponConfig config,
                            Entity player) {
        super(config, 0);
        this.remainingDuration = Integer.MAX_VALUE;
        this.player = player;
        this.player_last_pos = player.getPosition();
    }

    @Override
    protected void initial_rotation() {
        return;
    }

    @Override
    protected void initial_position() {
        entity.setPosition(player.getPosition()
                .add(player.getScale().scl(0.5f))
                .sub(entity.getScale().scl(0f))
                .add(new Vector2(0.15f, -0.2f))
        );
    }

    @Override
    protected void initial_animation(AnimationRenderComponent animator) {
        return;
    }

    @Override
    protected void rotate() {
        return;
    }

    @Override
    protected void move() {
        Vector2 player_delta = player.getPosition().sub(player_last_pos);
        this.player_last_pos = player.getPosition();
        entity.setPosition(entity.getPosition().add(player_delta.cpy()));
    }

    @Override
    protected void reanimate() {
        return;
    }
}

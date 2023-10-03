package com.csse3200.game.components.Weapons.SpecWeapon;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.Weapons.WeaponControllerComponent;
import com.csse3200.game.components.explosives.ExplosiveComponent;
import com.csse3200.game.components.explosives.ExplosiveConfig;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.WeaponConfig;
import com.csse3200.game.rendering.AnimationRenderComponent;

public class GrenadeController extends WeaponControllerComponent {
    private Vector2 player_last_pos;
    private float rotation;

    private float attackDirection;

    public GrenadeController(WeaponConfig config,
                             float attackDirection,
                             Entity player, int attackNum) {
        super(config, attackDirection, player, attackNum);
        this.player_last_pos = player.getPosition();
        this.attackDirection = attackDirection;
        this.rotation = config.rotationSpeed * (attackDirection < 90 || attackDirection > 270 ? 1 : -1);
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

        var explode = new ExplosiveComponent(explosiveConfig);
        entity.addComponent(explode);
        explode.create();
    }

    @Override
    protected void add_animations(AnimationRenderComponent animator) {
        animator.addAnimation("UP", 0.07f, Animation.PlayMode.LOOP);
    }

    @Override
    protected void initial_rotation() {
        System.out.println(attackNum);
        currentRotation += 45 * (attackNum % 2 == 0 ? 1 : -1) * (attackDirection < 90 || attackDirection > 270 ? 1 : -1);
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
        this.currentRotation -= this.rotation * (attackNum % 2 == 0 ? 1 : -1);
    }


    @Override
    protected void move() {
        Vector2 player_delta = player.getPosition().sub(player_last_pos);
        this.player_last_pos = player.getPosition();

        entity.setPosition(entity.getPosition()
                .add(positionInDirection(currentRotation,0.01f * config.weaponSpeed))
        );
    }

    @Override
    protected void reanimate() {return;}

    @Override
    protected void despawn() {
        AnimationRenderComponent animator = entity.getComponent(AnimationRenderComponent.class);

        if (animator != null) {
            //entity.getEvents().trigger("playSound", "stop");
            animator.stopAnimation();
        }

        entity.getEvents().trigger("explode");
    }
}
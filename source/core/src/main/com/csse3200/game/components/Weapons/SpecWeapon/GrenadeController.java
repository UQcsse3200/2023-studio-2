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
    private int attackSide;

    public GrenadeController(WeaponConfig config,
                             float attackDirection,
                             Entity player, int attackNum) {
        super(config, attackDirection, player, attackNum);
        attackSide = (currentRotation > 90 && currentRotation < 270) ? -1 : 1;
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
    protected void set_animations() {
        animator = entity.getComponent(AnimationRenderComponent.class);
        animator.addAnimation("ATTACK", 0.07f, (attackSide == 1 ? Animation.PlayMode.LOOP : Animation.PlayMode.LOOP_REVERSED));


    }

    @Override
    protected void initial_rotation() {
        currentRotation += 45 * attackSide;
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
        this.currentRotation -= config.rotationSpeed * attackSide;
    }


    @Override
    protected void move() {
        entity.setPosition(entity.getPosition()
                .add(positionInDirection(currentRotation,0.01f * config.weaponSpeed))
        );
    }

    @Override
    protected void reanimate() {return;}

    @Override
    protected void despawn() {
        //entity.getEvents().trigger("playSound", "stop");
        animator.stopAnimation();
        entity.getEvents().trigger("explode");
    }
}
package com.csse3200.game.components.Weapons.SpecWeapon;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.Weapons.WeaponControllerComponent;
import com.csse3200.game.components.Weapons.WeaponType;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.WeaponConfig;
import com.csse3200.game.rendering.AnimationRenderComponent;

public class MeleeSwingController extends WeaponControllerComponent {
    private final Entity player;
    private Vector2 player_last_pos;

    public MeleeSwingController(WeaponConfig config,
                                float attackDirection,
                                Entity player) {
        super(config, attackDirection);
        this.player = player;
        this.player_last_pos = player.getPosition();
    }

    @Override
    public void create() {
        super.create();
    }

    @Override
    protected void initial_rotation() {
        //Discritise rotation into 4directions
        currentRotation = ((int) (((currentRotation + 45f) % 360) / 90)) * 90f;
        if (config.type == WeaponType.MELEE_BEE_STING) {
            currentRotation = (currentRotation + (float) ((Math.random() - 0.5) * 45)) % 360;
        }
    }

    @Override
    protected void initial_position() {
        entity.setPosition(player.getPosition()
                .add(player.getScale().scl(0.5f))
                .sub(entity.getScale().scl(0.5f))
                .add(positionInDirection(currentRotation - (float) getMoveDirection() * 25f, 0.8f))
        );
    }

    @Override
    protected void initial_animation(AnimationRenderComponent animator) {
        switch (config.type) {
            //All melee weapons should implement 8 directional attacking
            case MELEE_KATANA, MELEE_BEE_STING:
                int dir = Math.round(currentRotation / 45);
                switch (dir) {
                    case 0, 1, 7 -> animator.startAnimation("RIGHT1");
                    case 2 -> animator.startAnimation("UP");
                    case 3, 4, 5 -> animator.startAnimation("LEFT1");
                    case 6 -> animator.startAnimation("DOWN");
                }
                break;
            default:
                animator.startAnimation("UP");
        }
    }

    @Override
    protected void rotate() {
        this.currentRotation -= config.rotationSpeed;
    }

    @Override
    protected void move() {
        Vector2 player_delta = player.getPosition().sub(player_last_pos);
        this.player_last_pos = player.getPosition();

        entity.setPosition(entity.getPosition()
                .add(player_delta.cpy())
                .add(positionInDirection(currentRotation + (float) getMoveDirection() * 90f,
                        0.01f * config.weaponSpeed))
        );
    }

    private int getMoveDirection() {
        if (config.type == WeaponType.MELEE_BEE_STING) {return 0;}
        return switch (Math.round(currentRotation / 45)) {
            case  0, 1, 7, 2 -> -1;
            default -> 1;
        };
    }

    @Override
    protected void reanimate() {
        return;
    }
}

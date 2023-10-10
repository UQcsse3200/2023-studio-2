package com.csse3200.game.components.CompanionWeapons;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.explosives.ExplosiveComponent;
import com.csse3200.game.components.explosives.ExplosiveConfig;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.CompanionWeaponConfig;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.ServiceLocator;

public class PowerUpController extends CompanionWeaponController {
    private Vector2 companion_pos;
    private float rotation;

    private float attackDirection;

    Entity companion = ServiceLocator.getEntityService().getCompanion();

    public PowerUpController(CompanionWeaponConfig config) {
        super(config.type, config.weaponDuration, config.currentRotation, config.weaponSpeed, config.rotationSpeed, config.animationType, config.imageRotationOffset);
        this.companion_pos = companion.getPosition();
        this.rotation = config.rotationSpeed * (attackDirection < 90 || attackDirection > 270 ? 1 : -1);
    }

    @Override
    public void create(){
        super.create();
        ExplosiveConfig explosiveConfig = new ExplosiveConfig();
        explosiveConfig.chainable = false;
        explosiveConfig.damage = 20;
        explosiveConfig.damageRadius = 2.5f;
        explosiveConfig.chainRadius = 3.0f;
        explosiveConfig.effectPath = "particle-effects/deathpotion/Death.effect";

        var explode = new ExplosiveComponent(explosiveConfig);
        entity.addComponent(explode);
        explode.create();
    }
}

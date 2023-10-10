package com.csse3200.game.entities.factories;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.Companion.CompanionInventoryComponent;
import com.csse3200.game.components.CompanionWeapons.PowerUpController;
import com.csse3200.game.components.TouchAttackComponent;
import com.csse3200.game.components.CompanionWeapons.CompanionWeaponController;
import com.csse3200.game.components.CompanionWeapons.CompanionWeaponTargetComponent;
import com.csse3200.game.components.CompanionWeapons.CompanionWeaponType;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.CompanionWeaponConfig;
import com.csse3200.game.entities.configs.CompanionWeaponConfigs;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.ServiceLocator;

public class CompanionAttackFactory {
    private static final CompanionWeaponConfigs configs =
            FileLoader.readClass(CompanionWeaponConfigs.class, "configs/CompanionWeapon.json");

    public static Entity createAttack(CompanionWeaponType weaponType, float initRot, Entity Companion) {
        CompanionWeaponConfig config = configs.GetWeaponConfig(weaponType);
        CompanionInventoryComponent CompanionInventory = ServiceLocator.getEntityService().getCompanion().getComponent(CompanionInventoryComponent.class);
        CompanionInventory.setEquippedCooldown(config.attackCooldown);
        CompanionInventory.changeEquippedAmmo(-config.ammoUse);

        int direction = 1;
        switch (weaponType) {
            case Death_Potion:
                if (initRot > 120 && initRot < 300) {
                    direction = -1;
                }
                break;
            default:
        }

        CompanionWeaponController weaponController = new CompanionWeaponController(weaponType,
                config.weaponDuration,
                initRot + config.initialRotationOffset,
                config.weaponSpeed * direction,
                config.rotationSpeed * direction,
                config.animationType,
                config.imageRotationOffset);

        Entity attack = new Entity()
                .addComponent(new PhysicsComponent())
                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.WEAPON))
                .addComponent(new TouchAttackComponent((short)
                        (PhysicsLayer.ENEMY_RANGE | PhysicsLayer.ENEMY_MELEE)))
                .addComponent(weaponController);
        attack.setEntityType("ComapanionWeapon");

        TextureAtlas atlas = new TextureAtlas(config.textureAtlas);
        AnimationRenderComponent animator = new AnimationRenderComponent(atlas);

        switch (config.animationType) {
            case 8:
                animator.addAnimation("LEFT3", 0.07f, Animation.PlayMode.NORMAL);
                animator.addAnimation("RIGHT3", 0.07f, Animation.PlayMode.NORMAL);
            case 6:
                animator.addAnimation("LEFT2", 0.07f, Animation.PlayMode.NORMAL);
                animator.addAnimation("RIGHT2", 0.07f, Animation.PlayMode.NORMAL);
            case 4:
                animator.addAnimation("LEFT1", 0.07f, Animation.PlayMode.NORMAL);
                animator.addAnimation("RIGHT1", 0.07f, Animation.PlayMode.NORMAL);
                animator.addAnimation("DOWN", 0.07f, Animation.PlayMode.NORMAL);
            default:
                animator.addAnimation("UP", 0.07f, Animation.PlayMode.NORMAL);
                animator.addAnimation("STATIC", 0.07f, Animation.PlayMode.NORMAL);
        }

        attack.addComponent(animator)
                .addComponent(new CombatStatsComponent(30, 10, 1, false));

        if (weaponType == CompanionWeaponType.Death_Potion) {
            int dir = (int) Math.floor(initRot / 60);
            switch (dir) {
                case 0, 5 -> animator.startAnimation("RIGHT1");
                case 1 -> animator.startAnimation("UP");
                case 2, 3 -> animator.startAnimation("LEFT1");
                case 4 -> animator.startAnimation("DOWN");
            }}
            attack.scaleWidth(config.imageScale);
            // Create the PowerUpController for the Death Potion
            PowerUpController powerUpController = new PowerUpController(config);

            // Set up the PowerUpController
            attack.addComponent(powerUpController);
            attack.addComponent(new CompanionWeaponTargetComponent(weaponType, Companion));
            return attack;
        }

  private CompanionAttackFactory() {
            throw new IllegalStateException("Instantiating static util class");
        }
    }

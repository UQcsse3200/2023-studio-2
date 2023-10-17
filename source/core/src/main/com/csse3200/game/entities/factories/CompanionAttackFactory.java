package com.csse3200.game.entities.factories;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.companion.CompanionInventoryComponent;
import com.csse3200.game.components.companionweapons.PowerUpController;
import com.csse3200.game.components.ParticleComponent;
import com.csse3200.game.components.TouchAttackComponent;
import com.csse3200.game.components.companionweapons.CompanionWeaponController;
import com.csse3200.game.components.companionweapons.CompanionWeaponTargetComponent;
import com.csse3200.game.components.companionweapons.CompanionWeaponType;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.CompanionWeaponConfig;
import com.csse3200.game.entities.configs.CompanionWeaponConfigs;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.services.ServiceLocator;

public class CompanionAttackFactory {
    private static final CompanionWeaponConfigs configs = new CompanionWeaponConfigs();

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
                config.imageRotationOffset, config.textureAtlas);

        Entity attack = new Entity()
                .addComponent(new PhysicsComponent())
                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.WEAPON))
                .addComponent(new TouchAttackComponent((short)
                        (PhysicsLayer.ENEMY_RANGE | PhysicsLayer.ENEMY_MELEE)))
                .addComponent(weaponController);
        attack.setEntityType("ComapanionWeapon");

        attack.addComponent(new ParticleComponent(config.effects));


                attack.addComponent(new CombatStatsComponent(30,40,10,1,false));


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

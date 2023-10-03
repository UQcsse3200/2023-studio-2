package com.csse3200.game.entities.factories;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.SoundComponent;
import com.csse3200.game.components.TouchAttackComponent;
import com.csse3200.game.components.Weapons.SpecWeapon.*;
import com.csse3200.game.components.Weapons.WeaponControllerComponent;
import com.csse3200.game.components.Weapons.WeaponType;
import com.csse3200.game.components.player.InventoryComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.WeaponConfig;
import com.csse3200.game.entities.configs.WeaponConfigs;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.AnimationRenderComponent;

import java.util.ArrayList;

/**
 * Class to create weapons when the player attacks
 */
public class AttackFactory {
    private static final WeaponConfigs configs =
            FileLoader.readClass(WeaponConfigs.class, "configs/weapons.json");

    //TODO: REMOVE - LEGACY

    /**
     * function to generate multiple attacks in a sequence attack
     * @param weaponType
     * @param attackDirection
     * @param player
     * @return
     */
    public static ArrayList<Entity> createAttacks(WeaponType weaponType, float attackDirection, Entity player) {
        WeaponConfig config = configs.GetWeaponConfig(weaponType);
        int numberOfAttacks = config.projectiles;

        //Check usage requirements
        InventoryComponent invComp = player.getComponent(InventoryComponent.class);
        if (invComp.getCurrentAmmo() - config.ammoUse < 0) {
            return null;
        }
        invComp.setEquippedCooldown(config.attackCooldown);
        invComp.changeEquippedAmmo(-config.ammoUse);

        ArrayList<Entity> attacks = new ArrayList<>();
        for (int i = 0; i < numberOfAttacks; i++) {
            Entity attack = createAttack(config, attackDirection, player, i);
            attacks.add(attack);
        }
        return attacks;
    }

    /**
     * Static function to create a new weapon entity
     * @param config config to weapon to create
     * @param attackDirection - the initial rotation of the player
     * @param player          - the player using this attack
     * @param attackNum - the number of the attack in the sequence
     * @return A reference to the created weapon entity
     */
    public static Entity createAttack(WeaponConfig config, float attackDirection, Entity player, int attackNum) {
        WeaponControllerComponent wepCon = switch (config.type) {
            case MELEE_WRENCH, MELEE_KATANA ->
                    new MeleeSwingController(config, attackDirection, player);
            case MELEE_BEE_STING -> new KillerBeeController(config, attackDirection, player, attackNum);
            case RANGED_BOOMERANG -> new BoomerangController(config, attackDirection, player);
            case RANGED_SLINGSHOT -> new ProjectileController(config, attackDirection, player);
            case RANGED_HOMING -> new HomingProjectileController(config, attackDirection, player);
            default -> throw new IllegalArgumentException("No controller defined for weapon type: " + config.type);
        };

        //Creating the Attack entity with require components
        Entity attack = new Entity()
                .addComponent(new PhysicsComponent())
                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.WEAPON))
                .addComponent(new TouchAttackComponent((short)
                        (PhysicsLayer.ENEMY_RANGE | PhysicsLayer.ENEMY_MELEE)))
                .addComponent(new AnimationRenderComponent(new TextureAtlas(config.textureAtlas)))
                .addComponent(new CombatStatsComponent(30, (int) config.damage, 1, false))
                .addComponent(new SoundComponent(config.sound))
                .addComponent(wepCon);

        //Final configurations on entity
        attack.setEntityType("playerWeapon");
        attack.scaleWidth(config.imageScale);
        return attack;
    }

    private AttackFactory() {
        throw new IllegalStateException("Instantiating static util class");
    }
}
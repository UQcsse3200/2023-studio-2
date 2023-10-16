package com.csse3200.game.entities.factories;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.SoundComponent;
import com.csse3200.game.components.TouchAttackComponent;
import com.csse3200.game.components.Weapons.SpecWeapon.*;
import com.csse3200.game.components.Weapons.SpecWeapon.GrenadeController;
import com.csse3200.game.components.Weapons.SpecWeapon.Projectile.HomingMissileSprayProjectileController;
import com.csse3200.game.components.Weapons.SpecWeapon.Projectile.HomingProjectileController;
import com.csse3200.game.components.Weapons.SpecWeapon.Projectile.KillerBeeController;
import com.csse3200.game.components.Weapons.SpecWeapon.Projectile.ProjectileController;
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
import net.dermetfan.utils.Pair;

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
     * @param clickPos
     * @param player
     * @return
     */
    public static ArrayList<Pair<Entity, Integer>> createAttacks(WeaponType weaponType, Vector2 clickPos, Entity player) {
        WeaponConfig config = configs.GetWeaponConfig(weaponType);
        int numberOfAttacks = config.projectiles;

        //Check usage requirements
        InventoryComponent invComp = player.getComponent(InventoryComponent.class);
        if (invComp.getCurrentAmmo() - config.ammoUse < 0) {
            return null;
        }
        invComp.setEquippedCooldown(config.attackCooldown);
        invComp.changeEquippedAmmo(-config.ammoUse);

        ArrayList<Pair<Entity, Integer>> attacks = new ArrayList<>();
        for (int i = 0; i < numberOfAttacks; i++) {
            Pair<Entity, Integer> attack = createAttack(config, clickPos, player, i);
            attacks.add(attack);
        }

        return attacks;
    }

    /**
     * Static function to create a new weapon entity
     * @param config config to weapon to create
     * @param clickPos - the click pos of the player
     * @param player          - the player using this attack
     * @param attackNum - the number of the attack in the sequence
     * @return A reference to the created weapon entity
     */
    public static Pair<Entity, Integer> createAttack(WeaponConfig config, Vector2 clickPos, Entity player, int attackNum) {
        float attackDirection = calcRotationAngleInDegrees(player.getCenterPosition(), clickPos);

        WeaponControllerComponent wepCon = switch (config.type) {
            case MELEE_WRENCH, MELEE_KATANA ->
                    new MeleeSwingController(config, attackDirection, player);
            case MELEE_BEE_STING -> new KillerBeeController(config, attackDirection, player, attackNum);
            case RANGED_BOOMERANG, RANGED_BLUEMERANG -> new BoomerangController(config, attackDirection, player, attackNum);
            case RANGED_GRENADE ->  new GrenadeController(config, attackDirection, player, attackNum);
            case RANGED_SLINGSHOT -> new ProjectileController(config, attackDirection, player, attackNum);
            case RANGED_HOMING -> new HomingProjectileController(config, attackDirection, player, attackNum);
            case RANGED_MISSILES -> new HomingMissileSprayProjectileController(config, attackDirection, player, attackNum);
            case RANGED_NUKE -> new NukeController(config, clickPos, player, attackNum);
            default -> throw new IllegalArgumentException("No controller defined for weapon type: " + config.type);
        };

        //Creating the Attack entity with require components
        Entity attack = new Entity()
                .addComponent(new PhysicsComponent())
                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.WEAPON))
                .addComponent(new TouchAttackComponent((short)
                        (PhysicsLayer.ENEMY_RANGE | PhysicsLayer.ENEMY_MELEE)))
                .addComponent(new AnimationRenderComponent(new TextureAtlas(config.textureAtlas)))
                .addComponent(new CombatStatsComponent(config.health, (int) config.damage, 1, false))
                .addComponent(new SoundComponent(config.sound))
                .addComponent(wepCon);

        //Final configurations on entity
        attack.setEntityType("playerWeapon");
        attack.scaleWidth(config.imageScale);
        return new Pair<>(attack, wepCon.get_spawn_delay());
    }
    private AttackFactory() {
        throw new IllegalStateException("Instantiating static util class");
    }


    /**
     * Calcuate angle between 2 points from the center point to the target point,
     * angle is
     * in degrees with 0degrees being in the direction of the positive x-axis going
     * counter clockwise
     * up to 359.9... until wrapping back around
     *
     * @param centerPt - point from where angle is calculated from
     * @param targetPt - Tart point to where angle is calculated to
     * @return return angle between points in degrees from the positive x-axis
     */
    private static float calcRotationAngleInDegrees(Vector2 centerPt, Vector2 targetPt) {
        double angle = Math.toDegrees(Math.atan2(targetPt.y - centerPt.y, targetPt.x - centerPt.x));
        if (angle < 0) {
            angle += 360;
        }
        return (float) angle;
    }
}
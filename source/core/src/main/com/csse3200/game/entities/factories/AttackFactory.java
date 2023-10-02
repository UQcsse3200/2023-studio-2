package com.csse3200.game.entities.factories;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.csse3200.game.components.CombatStatsComponent;
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

/**
 * Class to create weapons when the player attacks
 */
public class AttackFactory {
  private static final WeaponConfigs configs =
          FileLoader.readClass(WeaponConfigs.class, "configs/weapons.json");

  //TODO: REMOVE - LEGACY
  /**
   * Static function to create a new weapon entity
   * @param weaponType - the type of weapon entity to be made
   * @param attackDirection - the initial rotation of the player
   * @param player - the player using this attack
   * @return A reference to the created weapon entity
   */
  public static Entity createAttack(WeaponType weaponType, float attackDirection, Entity player) {
    WeaponConfig config = configs.GetWeaponConfig(weaponType);
    //Play modifications
    InventoryComponent playerInventory = player.getComponent(InventoryComponent.class);
    playerInventory.setEquippedCooldown(config.attackCooldown);
    playerInventory.changeEquippedAmmo(-config.ammoUse);

    //Switch case for different weapon types:
    WeaponControllerComponent wepCon = switch (weaponType) {
      case MELEE_WRENCH, MELEE_KATANA, MELEE_BEE_STING
              -> new MeleeSwingController(config, attackDirection, player);
      case RANGED_BOOMERANG
              -> new BoomerangController(config, attackDirection, player);
      case RANGED_SLINGSHOT
              -> new ProjectileController(config, attackDirection, player);
      case RANGED_HOMING
              -> new HomingProjectileController(config, attackDirection, player);
      default -> new MeleeSwingController(config, attackDirection, player);
    };

    //Creating the Attack entity
    TextureAtlas atlas = new TextureAtlas(config.textureAtlas);

    Entity attack =new Entity()
                    .addComponent(new PhysicsComponent())
                    .addComponent(new HitboxComponent().setLayer(PhysicsLayer.WEAPON))
                    .addComponent(new TouchAttackComponent((short)
                            (PhysicsLayer.ENEMY_RANGE | PhysicsLayer.ENEMY_MELEE)))
                    .addComponent(new AnimationRenderComponent(atlas))
                    .addComponent(wepCon)
                    .addComponent(new CombatStatsComponent(30, (int) config.damage, 1, false));
    attack.setEntityType("playerWeapon");

    //Final stuff
    attack.scaleWidth(config.imageScale);
    return attack;
  }

  private AttackFactory() {
    throw new IllegalStateException("Instantiating static util class");
  }
}



package com.csse3200.game.entities.factories;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.TouchAttackComponent;
import com.csse3200.game.components.Weapons.WeaponControllerComponent;
import com.csse3200.game.components.Weapons.WeaponTargetComponent;
import com.csse3200.game.components.Weapons.WeaponType;
import com.csse3200.game.components.explosives.ExplosiveComponent;
import com.csse3200.game.components.player.WeaponComponent;
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
   * @param initRot - the initial rotation of the player
   * @param player - the player using this attack
   * @return A reference to the created weapon entity
   */
  public static Entity createAttack(WeaponType weaponType, float initRot, Entity player) {
    WeaponConfig config = configs.GetWeaponConfig(weaponType);
    WeaponComponent weaponComponent = player.getComponent(WeaponComponent.class);
    weaponComponent.setAttackCooldown(config.attackCooldown);

    int direction = 1;
    switch (weaponType) {
      case MELEE_WRENCH, MELEE_KATANA:
        if (initRot > 120 && initRot < 300) {direction = -1;}
        break;
      default:
    }

    WeaponControllerComponent weaponController = new WeaponControllerComponent(weaponType,
            config.weaponDuration,
            initRot + config.initialRotationOffset,
            config.weaponSpeed * direction,
            config.rotationSpeed * direction,
            config.animationType,
            config.imageRotationOffset);

    Entity attack =new Entity()
                    .addComponent(new PhysicsComponent())
                    .addComponent(new HitboxComponent().setLayer(PhysicsLayer.WEAPON))
                    .addComponent(new TouchAttackComponent((short)
                            (PhysicsLayer.ENEMY_RANGE | PhysicsLayer.ENEMY_MELEE)))
                    .addComponent(weaponController);
    attack.setEntityType("playerWeapon");

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

    if (weaponType == WeaponType.MELEE_KATANA || weaponType == WeaponType.MELEE_BEE_STING) {
      int dir = (int) Math.floor(initRot / 60);
      switch (dir) {
        case 0, 5 -> animator.startAnimation("RIGHT1");
        case 1 -> animator.startAnimation("UP");
        case 2, 3 -> animator.startAnimation("LEFT1");
        case 4 -> animator.startAnimation("DOWN");
      }
    } else if (weaponType == WeaponType.RANGED_BOOMERANG || weaponType == WeaponType.RANGED_GRENADE) {
      animator.removeAnimation("UP");
      animator.addAnimation("UP", 0.07f, Animation.PlayMode.LOOP);
      animator.startAnimation("UP");
    } else {
      animator.startAnimation("UP");
    }

    if (weaponType == WeaponType.RANGED_GRENADE || weaponType == WeaponType.RANGED_HOMING) {
      attack.addComponent(new ExplosiveComponent("particle-effects/explosion/explosion.effect",
              null, 3, true));
    }

    attack.scaleWidth(config.imageScale);
    attack.addComponent(new WeaponTargetComponent(weaponType, player));
    return attack;
  }

  private AttackFactory() {
    throw new IllegalStateException("Instantiating static util class");
  }
}



package com.csse3200.game.entities.factories;
/**different types of attacks
 * Each weapon: melee component: rotation, life, swing speed
 *              Animation:    image atlas
 *              Combat stats: damage, (Health = 1, Is immue = True, attack multiplier = 1)
 *              //Or use player combat stast
*/

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.TouchAttackComponent;
import com.csse3200.game.components.Weapons.WeaponControllerComponent;
import com.csse3200.game.components.Weapons.WeaponTargetComponent;
import com.csse3200.game.components.Weapons.WeaponType;
import com.csse3200.game.components.player.PlayerActions;
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
public class PlayerWeaponFactory {
  private static final WeaponConfigs configs =
          FileLoader.readClass(WeaponConfigs.class, "configs/weapons.json");
  /**
   * Static function to create a new weapon entity
   * @param weaponType - the type of weapon entity to be made
   * @param initRot - the initial rotation of the player
   * @return A reference to the created weapon entity
   */
  public static Entity createPlayerWeapon(WeaponType weaponType, Entity player) {
    WeaponConfig config = configs.GetWeaponConfig(weaponType);

    WeaponControllerComponent weaponController = new WeaponControllerComponent(WeaponType.STATIC_WEAPON,
            Integer.MAX_VALUE,0,0,0,0,0);

    Entity attack = new Entity().addComponent(weaponController);
    attack.setEntityType("playerStaticWeapon");

    TextureAtlas atlas = new TextureAtlas(config.textureAtlas);
    AnimationRenderComponent animator = new AnimationRenderComponent(atlas);

    animator.addAnimation("STATIC", 0.1f, Animation.PlayMode.NORMAL);
    //animator.addAnimation("nonstatic", 0.1f, Animation.PlayMode.NORMAL);

    attack.addComponent(animator)
            .addComponent(new CombatStatsComponent(1, 0, 0, true));

    animator.startAnimation("STATIC");
    attack.scaleWidth(config.imageScale / 2);
    attack.addComponent(new WeaponTargetComponent(WeaponType.MELEE_WRENCH, player));
    return attack;
  }

  private PlayerWeaponFactory() {
    throw new IllegalStateException("Instantiating static util class");
  }
}

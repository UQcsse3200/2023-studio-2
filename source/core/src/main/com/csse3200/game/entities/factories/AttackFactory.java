package com.csse3200.game.entities.factories;
/**different types of attacks
 * Each weapon: melee component: rotation, life, swing speed
 *              Animation:    image atlas
 *              Combat stats: damage, (Health = 1, Is immue = True, attack multiplier = 1)
 *              //Or use player combat stast
*/
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.TouchAttackComponent;
import com.csse3200.game.components.Weapons.WeaponControllerComponent;
import com.csse3200.game.components.Weapons.WeaponTargetComponent;
import com.csse3200.game.components.Weapons.WeaponType;
import com.csse3200.game.components.player.PlayerActions;
import com.csse3200.game.components.tasks.ChaseTask;
import com.csse3200.game.components.tasks.WanderTask;
import com.csse3200.game.entities.configs.*;
import com.csse3200.game.entities.enemies.EnemyBehaviour;
import com.csse3200.game.entities.enemies.EnemyType;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.PhysicsUtils;
import com.csse3200.game.rendering.TextureRenderComponent;
import com.csse3200.game.services.ServiceLocator;

/**
 * Class to create weapons when the player attacks
 */
public class AttackFactory {
  private static final WeaponConfigs configs =
          FileLoader.readClass(WeaponConfigs.class, "configs/weapons.json");
  /**
   * Static function to create a new weapon entity
   * @param weaponType - the type of weapon entity to be made
   * @param initRot - the initial rotation of the player
   * @return A reference to the created weapon entity
   */
  public static Entity createAttack(WeaponType weaponType, float initRot, Entity player) {
    WeaponConfig config = configs.GetWeaponConfig(weaponType);

    PlayerActions playerActions = player.getComponent(PlayerActions.class);
    playerActions.setAttackCooldown(config.attackCooldown);

    WeaponControllerComponent weaponController = new WeaponControllerComponent(
            weaponType,
            initRot + config.initialRotationOffset,
            config.weaponSpeed,
            config.weaponDuration,
            config.rotationSpeed,
            config.imageRotationOffset);

    Entity attack =
            new Entity()
                    .addComponent(new PhysicsComponent())
                    .addComponent(new HitboxComponent().setLayer(PhysicsLayer.WEAPON))
                    .addComponent(new TouchAttackComponent((short)
                            (PhysicsLayer.ENEMY_RANGE | PhysicsLayer.NPC)))
                    .addComponent(weaponController);

    AnimationRenderComponent animator = new AnimationRenderComponent(
            ServiceLocator.getResourceService().getAsset(config.textureAtlas, TextureAtlas.class));

    animator.addAnimation("attack", 0.1f, Animation.PlayMode.LOOP_PINGPONG);

    //TODO make this use player
    attack
            .addComponent(animator)
            .addComponent(new CombatStatsComponent(1, 10, 1, true));

    //TODO animations to control rotational apperance
    animator.startAnimation("attack");
    attack.scaleWidth(config.imageScale);

    switch (weaponType) {
      case SLING_SHOT:
        attack.addComponent(new WeaponTargetComponent(weaponType, player));
    }

    return attack;
  }

  private AttackFactory() {
    throw new IllegalStateException("Instantiating static util class");
  }
}

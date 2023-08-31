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
import com.csse3200.game.components.Weapons.WeaponType;
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
  /**
   * Static function to create a new weapon entity
   * @param weaponType - the type of weapon entity to be made
   * @param initRot - the initial rotation of the player
   * @return A reference to the created weapon entity
   */
  public static Entity createAttack(WeaponType weaponType, float initRot) {
    //TODO make this based on an enum/configs
    //-> WeaponControllerComponent values will be based on config
    //-> Assest will be based on config if possible
    //-> CombatStatsComponent values will be based on confit and combatStat values
    //Tomporary test for changing weapon
    WeaponControllerComponent change = new WeaponControllerComponent(weaponType, initRot, 25, 10, 10, 45-90);
    switch (weaponType) {
      case ELEC_WRENCH:
        change = new WeaponControllerComponent(weaponType, initRot + 45 + 45, 25, 10, 10, 45-90);
        break;
      case THROW_ELEC_WRENCH:
        change = new WeaponControllerComponent(weaponType, initRot, 25, 10, 10, 45);
        break;
    }

    Entity attack =
            new Entity()
                    .addComponent(new PhysicsComponent())
                    .addComponent(new HitboxComponent().setLayer(PhysicsLayer.PLAYER))
                    .addComponent(new TouchAttackComponent(PhysicsLayer.NPC, 1f))
                    .addComponent(new TextureRenderComponent("images/wrench.png"))
                    .addComponent(change);

    //AnimationRenderComponent animator =
            // new AnimationRenderComponent(     ServiceLocator.getResourceService().getAsset("images/wrench.atlas", TextureAtlas.class));
    //animator.addAnimation("attack", 0.1f, Animation.PlayMode.LOOP);
      attack
        .addComponent(new CombatStatsComponent(1, 5, 1, false));
        //.addComponent(animator);
    return attack;
  }

  private AttackFactory() {
    throw new IllegalStateException("Instantiating static util class");
  }
}

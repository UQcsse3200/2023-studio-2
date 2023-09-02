package com.csse3200.game.components;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.BodyUserData;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.DialogComponent;
import com.csse3200.game.ui.TitleBox;
import com.csse3200.game.ui.DialogueBox;
import com.csse3200.game.areas.ForestGameArea;

import static com.csse3200.game.ui.UIComponent.skin;

import java.util.logging.Logger;

/**
 * TouchAttackComponent is responsible for dealing damage and applying knockback to entities when
 * this entity collides with a valid enemy's hitbox.
 *
 * <p>This component requires the presence of CombatStatsComponent and HitboxComponent on this entity.
 *
 * <p>Damage is only applied if the target entity has a CombatStatsComponent. Knockback is only applied
 * if the target entity has a PhysicsComponent.
 */
public class TouchAttackComponent extends Component {

  private short targetLayer;
  private float knockbackForce = 0f;
  private CombatStatsComponent combatStats;
  private HitboxComponent hitboxComponent;

  /**
   * Creates a TouchAttackComponent that attacks entities on collision, without knockback.
   *
   * @param targetLayer The physics layer of the target's collider.
   */
  public TouchAttackComponent(short targetLayer) {
    this.targetLayer = targetLayer;
  }

  /**
   * Creates a TouchAttackComponent that attacks entities on collision, with knockback.
   *
   * @param targetLayer The physics layer of the target's collider.
   * @param knockback The magnitude of the knockback applied to the entity.
   */
  public TouchAttackComponent(short targetLayer, float knockback) {
    this.targetLayer = targetLayer;
    this.knockbackForce = knockback;
  }

  @Override
  public void create() {
    // Listen for collision events
    entity.getEvents().addListener("collisionStart", this::onCollisionStart);

    // Retrieve necessary components
    combatStats = entity.getComponent(CombatStatsComponent.class);
    hitboxComponent = entity.getComponent(HitboxComponent.class);
  }

  /**
   * Handles collision start events and applies damage and knockback to the target entity.
   *
   * @param me The fixture associated with this entity's hitbox.
   * @param other The fixture of the colliding entity.
   */
  private void onCollisionStart(Fixture me, Fixture other) {
    if (hitboxComponent.getFixture() != me) {
      // Not triggered by hitbox, ignore
      return;
    }

    if (!PhysicsLayer.contains(targetLayer, other.getFilterData().categoryBits)) {
      // Doesn't match our target layer, ignore
      return;
    }

    // Try to attack target.
    Entity target = ((BodyUserData) other.getBody().getUserData()).entity;
    Entity source = ((BodyUserData) me.getBody().getUserData()).entity;
    DialogComponent dialogue = target.getComponent(DialogComponent.class);
    CombatStatsComponent targetStats = target.getComponent(CombatStatsComponent.class);
    if (targetStats != null) {
      if(dialogue != null) {
        dialogue.showdialogue("You hit a Ghost");
      }
      targetStats.hit(combatStats);
    }

    // Apply knockback
    PhysicsComponent physicsComponent = target.getComponent(PhysicsComponent.class);
    if (physicsComponent != null && knockbackForce > 0f) {
      Body targetBody = physicsComponent.getBody();
      Vector2 direction = target.getCenterPosition().sub(entity.getCenterPosition());
      Vector2 impulse = direction.setLength(knockbackForce);
      targetBody.applyLinearImpulse(impulse, targetBody.getWorldCenter(), true);
    }
  }
}

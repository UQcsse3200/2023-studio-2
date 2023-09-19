package com.csse3200.game.components;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.BodyUserData;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.ui.DialogComponent;
import com.badlogic.gdx.utils.Timer;

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
  private float knockbackForce = 1f;
  private CombatStatsComponent combatStats;
  private HitboxComponent hitboxComponent;
  private boolean leftContact;

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

  /**
   * Creates listener that checks if current entity and a target entity come into contact.
   */
  @Override
  public void create() {
    // Listen for collision events
    entity.getEvents().addListener("collisionStart", this::onCollisionStart);
    entity.getEvents().addListener("collisionEnd", this::onCollisionEnd);

    // Retrieve necessary components
    combatStats = entity.getComponent(CombatStatsComponent.class);
    hitboxComponent = entity.getComponent(HitboxComponent.class);
    leftContact = true;

  }
  
  /**
   * Initial collision between current entity and target entity.
   * Deals single instance of damage when hit by enemy.
   * Deals Damage over time if enemy hits a destructible and still in contact with entity.
   * @param me The current entity as a Fixture
   * @param other The Target entity as a Fixture
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
    // Has come into contact

    Entity target = ((BodyUserData) other.getBody().getUserData()).entity;
    Entity source = ((BodyUserData) me.getBody().getUserData()).entity;
    DialogComponent dialogue = target.getComponent(DialogComponent.class);
    CombatStatsComponent targetStats = target.getComponent(CombatStatsComponent.class);
    CombatStatsComponent sourceStats = source.getComponent(CombatStatsComponent.class);
    leftContact = false;
    // If No Hitbox
    if (target.getComponent(HitboxComponent.class) == null) {
      return;
    }

    // Targeting STRUCTURE entity type
    if (target.getComponent(HitboxComponent.class).getLayer() == PhysicsLayer.STRUCTURE) {
      // Schedule the trigger every 2 seconds
      Timer.Task task = new Timer.Task() {
        @Override
        public void run() {
          if (!leftContact) {
            hitOnce(target, source, sourceStats, targetStats);
          }
        }
      };
      Timer.schedule(task, 2000, 2000); // Initial delay: 2000, Repeat every 2000 milliseconds (2 seconds)
    } else {
      // hit once, push away
      hitOnce(target, source, sourceStats, targetStats);
    }
  }

  /**
   * Helper Method that deals damage and knockback to Target
   * @param target The Targeted Entity, usually the Player Entity
   * @param targetStats The Targeted Entity's stats
   */
  private void hitOnce(Entity target, Entity source, CombatStatsComponent sourceStats, CombatStatsComponent targetStats){
    if (targetStats != null && sourceStats != null) {
//      if(dialogue != null) {
//        dialogue.showdialogue("You hit a Ghost");
//      }
      //targetStats.hit(combatStats);

      // Valid damage dealt
      if (source.getComponent(HitboxComponent.class).getLayer() == PhysicsLayer.ENEMY_MELEE ||
              source.getComponent(HitboxComponent.class).getLayer() == PhysicsLayer.ENEMY_RANGE) {
        char attackDirection = getDirection(target.getPosition());
        if(attackDirection == '<'){
          entity.getEvents().trigger("attackLeft");
        }
        if(attackDirection == '>'||attackDirection == '='){
          entity.getEvents().trigger("enemyAttack");
        }
      }

      targetStats.hit(combatStats);

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

  /**
   * Indicates when current entity loses contact with target entity.
   * @param me The current Entity's Fixture
   * @param other The Target Entity's Fixture
   */
  private void onCollisionEnd(Fixture me, Fixture other) {
    // Stop dealing tick damage
    leftContact = true;
  }

  public char getDirection(Vector2 destination) {
    if (entity.getPosition().x - destination.x < 0) {
      return '>';
    }
    if (entity.getPosition().x - destination.x > 0) {
      return '<';
    }
    return '=';
  }

}
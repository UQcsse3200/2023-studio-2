package com.csse3200.game.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.BodyUserData;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.AnimationRenderComponent;

import java.util.Timer;
import java.util.TimerTask;

/**
 * When this entity touches a valid enemy's hitbox, deal damage to them and apply a knockback.
 *
 * <p>Requires CombatStatsComponent, HitboxComponent on this entity.
 *
 * <p>Damage is only applied if target entity has a CombatStatsComponent. Knockback is only applied
 * if target entity has a PhysicsComponent.
 */
public class ProjectileAttackComponent extends Component {
  private short targetLayer;
  private float knockbackForce = 0f;
  private CombatStatsComponent combatStats;
  private HitboxComponent hitboxComponent;
  private boolean leftContact;
  private Timer triggerTimer;
  /**
   * Create a component which attacks entities on collision, without knockback.
   * @param targetLayer The physics layer of the target's collider.
   */
  public ProjectileAttackComponent(short targetLayer) {
    this.targetLayer = targetLayer;
  }

  /**
   * Create a component which attacks entities on collision, with knockback.
   * @param targetLayer The physics layer of the target's collider.
   * @param knockback The magnitude of the knockback applied to the entity.
   */
  public ProjectileAttackComponent(short targetLayer, float knockback) {
    this.targetLayer = targetLayer;
    this.knockbackForce = knockback;
  }

  /**
   * Creates new listener waiting for projectile entity to interact with another entity
   */
  @Override
  public void create() {
    entity.getEvents().addListener("collisionStart", this::onCollisionStart);
    entity.getEvents().addListener("collisionEnd", this::onCollisionEnd);
    combatStats = entity.getComponent(CombatStatsComponent.class);
    hitboxComponent = entity.getComponent(HitboxComponent.class);
    leftContact = true;
  }

  /**
   * Runs when projectile collision listener initially makes contact with another entity.
   * Will deal damage to entity upon contact.
   * @param me The current entity's fixture
   * @param other The targeted entity's fixture
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
    CombatStatsComponent targetStats = target.getComponent(CombatStatsComponent.class);
    leftContact = false;
    // If No Hitbox
    if (target.getComponent(HitboxComponent.class) == null) {
      return;
    }
    if (target.getComponent(HitboxComponent.class).getLayer() != PhysicsLayer.PLAYER && target.getComponent(HitboxComponent.class).getLayer() != PhysicsLayer.STRUCTURE) {
      // Do nothing
      logger.debug("Non-target");
    } else if (target.getComponent(HitboxComponent.class).getLayer() == PhysicsLayer.PLAYER || target.getComponent(HitboxComponent.class).getLayer() == PhysicsLayer.STRUCTURE) {
      //Damage Structure while still in contact
      hitOnce(target, targetStats);
    }
  }

  /**
   * Helper Function that deals damage and knockback to target entity.
   * Will also despawn the projectile after hit has been made.
   * @param target The targeted entity
   * @param targetStats The targeted entity's stats
   */
  private void hitOnce(Entity target, CombatStatsComponent targetStats){
    if (targetStats != null) {
      // Valid damage dealt
      targetStats.hit(combatStats);
    }

    // Knockback
    PhysicsComponent physicsComponent = target.getComponent(PhysicsComponent.class);
    if (physicsComponent != null && knockbackForce > 0f) {
      Body targetBody = physicsComponent.getBody();
      Vector2 direction = target.getCenterPosition().sub(entity.getCenterPosition());
      Vector2 impulse = direction.setLength(knockbackForce);
      targetBody.applyLinearImpulse(impulse, targetBody.getWorldCenter(), true);
    }

    AnimationRenderComponent animator = entity.getComponent(AnimationRenderComponent.class);
    animator.stopAnimation();
    entity.getComponent(HitboxComponent.class).setLayer((short) 0);

    // Dispose Entity with animation
    entity.getEvents().trigger("explode");
    // Schedule a task to execute entity::dispose after a delay
    // Get the duration of the projectile explosion animation
    float deathAnimationDuration = animator.getAnimationDuration("explode");
    // Convert the duration from seconds to milliseconds for the Timer
    long delay = (long) (deathAnimationDuration * 1000);
    Timer timer = new Timer();

    timer.schedule(new TimerTask() {
      @Override
      public void run() {
        Gdx.app.postRunnable(entity::dispose);
      }
    }, delay); // Delay based on the dispose animation duration

  }

  /**
   * Will signal when projectile no longer is in contact with target entity
   * @param me The current entity's fixture
   * @param other The targeted entity's fixture
   */
  private void onCollisionEnd(Fixture me, Fixture other) {
    // Stop dealing tick damage
    leftContact = true;
  }
}

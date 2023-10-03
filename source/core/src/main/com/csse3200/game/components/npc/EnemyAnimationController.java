package com.csse3200.game.components.npc;

import com.csse3200.game.components.Component;
import com.csse3200.game.rendering.AnimationRenderComponent;

/**
 * This class listens to events relevant to all enemy entities' state and plays the animation when one
 * of the events is triggered.
 */
public class EnemyAnimationController extends Component {
  AnimationRenderComponent animator;

  /**
   * Add actions listener to the entity
   */

  @Override
  public void create() {
    super.create();
    animator = this.entity.getComponent(AnimationRenderComponent.class);
    entity.getEvents().addListener("wanderStart", this::animateWander);
    entity.getEvents().addListener("chaseStart", this::animateChase);
    entity.getEvents().addListener("wander_left", this::animateWanderLeft);
    entity.getEvents().addListener("standing", this::animateWanderStand);
    entity.getEvents().addListener("dispose", this::animateDeath);
    entity.getEvents().addListener("explode", this::animateExplosion);
    entity.getEvents().addListener("enemyAttack", this::animateAttack);
    entity.getEvents().addListener("chaseLeft", this::animateChaseLeft);
    entity.getEvents().addListener("attackLeft",this::animateAttackLeft);

    entity.getEvents().trigger("standing");
  }


  /**
   * Initialise animation
   */
  void animateWander() {
    animator.startAnimation("float");

  }

  /**
   * Initialise animation
   */
  void animateChase() {
    animator.startAnimation("angry_float");
  }

  /**
   * Initialise animation
   */
  void animateWanderLeft() {
    animator.startAnimation("left");
  }

  /**
   * Initialise animation
   */
  void animateWanderStand() {
    animator.startAnimation("stand");
  }

  /**
   * Initialise animation and
   * plays the sound when enemy dead
   */
  void animateDeath() {
    if (animator.hasAnimation("death")) {
      animator.startAnimation("death");
      if (entity != null) {
        entity.getEvents().trigger("playSound", "enemyDeath");
      }
    }
  }

  /**
   * Initialise animation
   */
  void animateAttack (){
    animator.startAnimation("attack");
  }

  /**
   * Initialise animation
   */
  void animateExplosion (){
    animator.startAnimation("explode");
  }
  void animateChaseLeft (){
    animator.startAnimation("chaseLeft");
  }
  void animateAttackLeft(){
    animator.startAnimation("attackLeft");
  }

}

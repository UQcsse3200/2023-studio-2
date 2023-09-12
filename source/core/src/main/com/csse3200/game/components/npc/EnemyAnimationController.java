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
   * Initialise animation
   */
  void animateDeath() {
    animator.startAnimation("death");
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

}

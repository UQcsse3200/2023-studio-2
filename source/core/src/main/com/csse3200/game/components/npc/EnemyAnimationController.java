package com.csse3200.game.components.npc;

import com.csse3200.game.components.Component;
import com.csse3200.game.rendering.AnimationRenderComponent;

/**
 * This class listens to events relevant to all enemy entities' state and plays the animation when one
 * of the events is triggered.
 */
public class EnemyAnimationController extends Component {
  AnimationRenderComponent animator;

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
  }

  void animateWander() {
    animator.startAnimation("float");
  }
  void animateChase() {
    animator.startAnimation("angry_float");
  }
  void animateWanderLeft() {
    animator.startAnimation("left");
  }
  void animateWanderStand() {
    animator.startAnimation("stand");
  }
  void animateDeath() {
    animator.startAnimation("death");
  }
  void animateExplosion (){
    animator.startAnimation("explode");
  }

}

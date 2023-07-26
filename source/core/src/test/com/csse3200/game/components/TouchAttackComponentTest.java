package com.csse3200.game.components;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(GameExtension.class)
class TouchAttackComponentTest {
  @BeforeEach
  void beforeEach() {
    ServiceLocator.registerPhysicsService(new PhysicsService());
  }

  @Test
  void shouldAttack() {
    short targetLayer = (1 << 3);
    Entity entity = createAttacker(targetLayer);
    Entity target = createTarget(targetLayer);

    Fixture entityFixture = entity.getComponent(HitboxComponent.class).getFixture();
    Fixture targetFixture = target.getComponent(HitboxComponent.class).getFixture();
    entity.getEvents().trigger("collisionStart", entityFixture, targetFixture);

    assertEquals(0, target.getComponent(CombatStatsComponent.class).getHealth());
  }

  @Test
  void shouldNotAttackOtherLayer() {
    short targetLayer = (1 << 3);
    short attackLayer = (1 << 4);
    Entity entity = createAttacker(attackLayer);
    Entity target = createTarget(targetLayer);

    Fixture entityFixture = entity.getComponent(HitboxComponent.class).getFixture();
    Fixture targetFixture = target.getComponent(HitboxComponent.class).getFixture();
    entity.getEvents().trigger("collisionStart", entityFixture, targetFixture);

    assertEquals(10, target.getComponent(CombatStatsComponent.class).getHealth());
  }

  @Test
  void shouldNotAttackWithoutCombatComponent() {
    short targetLayer = (1 << 3);
    Entity entity = createAttacker(targetLayer);
    // Target does not have a combat component
    Entity target =
        new Entity()
            .addComponent(new PhysicsComponent())
            .addComponent(new HitboxComponent().setLayer(targetLayer));
    target.create();

    Fixture entityFixture = entity.getComponent(HitboxComponent.class).getFixture();
    Fixture targetFixture = target.getComponent(HitboxComponent.class).getFixture();

    // This should not cause an exception, but the attack should be ignored
    entity.getEvents().trigger("collisionStart", entityFixture, targetFixture);
  }

  Entity createAttacker(short targetLayer) {
    Entity entity =
        new Entity()
            .addComponent(new TouchAttackComponent(targetLayer))
            .addComponent(new CombatStatsComponent(0, 10))
            .addComponent(new PhysicsComponent())
            .addComponent(new HitboxComponent());
    entity.create();
    return entity;
  }

  Entity createTarget(short layer) {
    Entity target =
        new Entity()
            .addComponent(new CombatStatsComponent(10, 0))
            .addComponent(new PhysicsComponent())
            .addComponent(new HitboxComponent().setLayer(layer));
    target.create();
    return target;
  }
}

package com.csse3200.game.physics;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.utils.math.Vector2Utils;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.physics.components.PhysicsComponent.AlignX;
import com.csse3200.game.physics.components.PhysicsComponent.AlignY;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * It is difficult to check whether the shape of a Fixture matches a given bounding box, so bbox
 * functionality is unfortunately not tested.
 */
@ExtendWith(GameExtension.class)
class ColliderComponentTest {
  @BeforeEach
  void beforeEach() {
    ServiceLocator.registerPhysicsService(new PhysicsService());
  }

  @Test
  void shouldSetFriction() {
    Entity entity = new Entity();
    entity.addComponent(new PhysicsComponent());
    ColliderComponent component = new ColliderComponent();
    entity.addComponent(component);

    component.setFriction(2f);
    entity.create();
    assertEquals(2f, component.getFixture().getFriction());
    component.setFriction(3f);
    assertEquals(3f, component.getFixture().getFriction());
  }

  @Test
  void shouldSetSensor() {
    Entity entity = new Entity();
    entity.addComponent(new PhysicsComponent());
    ColliderComponent component = new ColliderComponent();
    entity.addComponent(component);

    component.setSensor(true);
    entity.create();
    assertTrue(component.getFixture().isSensor());
    component.setSensor(false);
    assertFalse(component.getFixture().isSensor());
  }

  @Test
  void shouldSetDensity() {
    Entity entity = new Entity();
    entity.addComponent(new PhysicsComponent());
    ColliderComponent component = new ColliderComponent();
    entity.addComponent(component);

    component.setDensity(2f);
    entity.create();
    assertEquals(2f, component.getFixture().getDensity());
    component.setDensity(3f);
    assertEquals(3f, component.getFixture().getDensity());
  }

  @Test
  void shouldSetRestitution() {
    Entity entity = new Entity();
    entity.addComponent(new PhysicsComponent());
    ColliderComponent component = new ColliderComponent();
    entity.addComponent(component);

    component.setRestitution(2f);
    entity.create();
    assertEquals(2f, component.getFixture().getRestitution());
    component.setRestitution(3f);
    assertEquals(3f, component.getFixture().getRestitution());
  }

  @Test
  void shouldSetShape() {
    Entity entity = new Entity();
    entity.addComponent(new PhysicsComponent());
    ColliderComponent component = new ColliderComponent();
    entity.addComponent(component);

    Shape shape = new CircleShape();
    component.setShape(shape);
    entity.create();
    assertEquals(shape.getType(), component.getFixture().getShape().getType());
  }

  @Test
  void shouldSetBox() {
    Entity entity = new Entity();
    entity.addComponent(new PhysicsComponent());
    ColliderComponent component = new ColliderComponent();
    entity.addComponent(component);

    entity.setScale(4f, 4f);
    Vector2 box = new Vector2(2f, 2f);
    component.setAsBox(box);
    entity.create();

    PhysicsTestUtils.checkPolygonCollider(component, box);
    Vector2 pos = PhysicsTestUtils.getRectanglePosition(component);
    assertEquals(Vector2Utils.ONE, pos);
  }

  @Test
  void shouldSetAligned() {
    testAlignedBox(AlignX.LEFT, AlignY.BOTTOM, Vector2.Zero);
    testAlignedBox(AlignX.RIGHT, AlignY.TOP, new Vector2(2f, 2f));
    testAlignedBox(AlignX.CENTER, AlignY.CENTER, new Vector2(1f, 1f));
  }

  private static void testAlignedBox(AlignX alignX, AlignY alignY, Vector2 position) {
    Entity entity = new Entity();
    entity.addComponent(new PhysicsComponent());
    ColliderComponent component = new ColliderComponent();
    entity.addComponent(component);

    entity.setScale(4f, 4f);
    Vector2 box = new Vector2(2f, 2f);
    component.setAsBoxAligned(box, alignX, alignY);
    entity.create();

    Vector2 realPos = PhysicsTestUtils.getRectanglePosition(component);
    assertEquals(position, realPos);
  }
}

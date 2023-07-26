package com.csse3200.game.components;

import static org.junit.jupiter.api.Assertions.*;

import com.csse3200.game.components.Component;
import com.csse3200.game.components.ComponentType;
import com.csse3200.game.extensions.GameExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(GameExtension.class)
class ComponentTypeTest {
  @Test
  void shouldGiveSameIdForSameClass() {
    assertEquals(
        ComponentType.getFrom(Component.class).getId(),
        ComponentType.getFrom(Component.class).getId()
    );

    assertEquals(
        ComponentType.getFrom(TestComponent1.class).getId(),
        ComponentType.getFrom(TestComponent1.class).getId()
    );
  }

  @Test
  void shouldGiveUniqueIdPerClass() {
    ComponentType type1 = ComponentType.getFrom(Component.class);
    ComponentType type2 = ComponentType.getFrom(TestComponent1.class);
    ComponentType type3 = ComponentType.getFrom(TestComponent2.class);

    assertNotEquals(type1.getId(), type2.getId());
    assertNotEquals(type2.getId(), type3.getId());
  }

  static class TestComponent1 extends Component {}
  static class TestComponent2 extends TestComponent1 {}
}
package com.csse3200.game.components;

import com.csse3200.game.extensions.GameExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
class ComponentTest {
  @Test
  void shouldUpdateIfEnabled() {
    Component component = spy(Component.class);
    component.setEnabled(true);

    component.triggerUpdate();
    verify(component).update();

    component.triggerEarlyUpdate();
    verify(component).earlyUpdate();
  }

  @Test
  void shouldNotUpdateIfDisabled() {
    Component component = spy(Component.class);
    component.setEnabled(false);

    component.triggerUpdate();
    verify(component, times(0)).update();

    component.triggerEarlyUpdate();
    verify(component, times(0)).earlyUpdate();
  }
}

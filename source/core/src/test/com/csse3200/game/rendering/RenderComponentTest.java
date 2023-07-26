package com.csse3200.game.rendering;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import com.csse3200.game.entities.Entity;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)
class RenderComponentTest {
  @Mock
  RenderService service;

  @Test
  void shouldRegisterSelf() {
    ServiceLocator.registerRenderService(service);
    RenderComponent component = spy(RenderComponent.class);
    component.create();
    verify(service).register(component);
  }

  @Test
  void shouldUnregisterOnDispose() {
    ServiceLocator.registerRenderService(service);
    RenderComponent component = spy(RenderComponent.class);
    component.create();
    component.dispose();
    verify(service).unregister(component);
  }

  @Test
  void shouldDrawOnRender() {
    RenderComponent component = spy(RenderComponent.class);
    component.render(null);
    verify(component).draw(any());
  }

  @Test
  void shouldGiveCorrectRenderOrder() {
    RenderComponent component1 = spy(RenderComponent.class);
    RenderComponent component2 = spy(RenderComponent.class);
    assertEquals(component1.getLayer(), component2.getLayer());

    Entity entity1 = new Entity();
    Entity entity2 = new Entity();
    component1.setEntity(entity1);
    component2.setEntity(entity2);

    entity1.setPosition(0f, 1f);
    entity2.setPosition(0f, 2f);
    assertTrue(component1.getZIndex() > component2.getZIndex());

    entity2.setPosition(5f, -3f);
    assertTrue(component1.getZIndex() < component2.getZIndex());
  }
}
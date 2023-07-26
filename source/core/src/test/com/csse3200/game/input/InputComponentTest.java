package com.csse3200.game.input;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

@ExtendWith(GameExtension.class)
class InputComponentTest {
  @Test
  void shouldUpdatePriority() {
    int newPriority = 100;
    InputComponent inputComponent = spy(InputComponent.class);

    inputComponent.setPriority(newPriority);
    verify(inputComponent).setPriority(newPriority);

    int priority = inputComponent.getPriority();
    verify(inputComponent).getPriority();

    assertEquals(newPriority, priority);
  }

  @Test
  void shouldRegisterOnCreate() {
    InputService inputService = spy(InputService.class);
    ServiceLocator.registerInputService(inputService);

    InputComponent inputComponent = spy(InputComponent.class);
    inputComponent.create();
    verify(inputService).register(inputComponent);
  }

  @Test
  void shouldHandleKeyDown(){
    InputComponent inputComponent = spy(InputComponent.class);
    assertFalse(inputComponent.keyDown(1));
  }

  @Test
  void shouldHandleKeyTyped() {
    InputComponent inputComponent = spy(InputComponent.class);
    assertFalse(inputComponent.keyTyped('a'));
  }

  @Test
  void shouldHandleKeyUp() {
  InputComponent inputComponent = spy(InputComponent.class);
  assertFalse(inputComponent.keyUp(1));
  }

  @Test
  void shouldHandleMouseMoved() {
  InputComponent inputComponent = spy(InputComponent.class);
  assertFalse(inputComponent.mouseMoved( 5, 6));
  }

  @Test
  void shouldHandleScrolled() {
  InputComponent inputComponent = spy(InputComponent.class);
  assertFalse(inputComponent.scrolled( 5f, 6f));
  }

  @Test
  void shouldHandleTouchDown() {
  InputComponent inputComponent = spy(InputComponent.class);
  assertFalse(inputComponent.touchDown( 5, 6, 7, 8));
  }

  @Test
  void shouldHandleTouchDragged() {
  InputComponent inputComponent = spy(InputComponent.class);
  assertFalse(inputComponent.touchDragged(5, 6, 7));
  }

  @Test
  void shouldHandleTouchUp() {
 InputComponent inputComponent = spy(InputComponent.class);
  assertFalse(inputComponent.touchUp( 5, 6, 7, 8));
  }

  @Test
  void shouldHandleFling(){
    InputComponent inputComponent = spy(InputComponent.class);
    assertFalse(inputComponent.fling( 5f, 6f, 7));
  }

  @Test
  void shouldHandleLongPress(){
    InputComponent inputComponent = spy(InputComponent.class);
    assertFalse(inputComponent.longPress(5f, 6f));
  }

  @Test
  void shouldHandlePan(){
    InputComponent inputComponent = spy(InputComponent.class);
    assertFalse(inputComponent.pan( 5f, 6f, 7f, 8f));
  }

  @Test
  void shouldHandlePanStop(){
    InputComponent inputComponent = spy(InputComponent.class);
    assertFalse(inputComponent.panStop( 5f, 6f, 7, 8));
  }

  @Test
  void shouldHandlePinch(){
    InputComponent inputComponent = spy(InputComponent.class);
    assertFalse(inputComponent.pinch(Vector2.Zero, Vector2.Zero, Vector2.Zero, Vector2.Zero));
  }

  @Test
  void shouldHandleTap() {
    InputComponent inputComponent = spy(InputComponent.class);
    assertFalse(inputComponent.tap(5f, 6f, 7, 8));
  }

  @Test
  void shouldHandleTouchDownGesture(){
    InputComponent inputComponent = spy(InputComponent.class);
    assertFalse(inputComponent.touchDown(5f, 6f, 7, 8));
  }

  @Test
  void shouldHandleZoom(){
    InputComponent inputComponent = spy(InputComponent.class);
    assertFalse(inputComponent.zoom(5f, 6f));
  }
}

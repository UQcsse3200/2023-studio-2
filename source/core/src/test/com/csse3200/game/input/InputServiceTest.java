package com.csse3200.game.input;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.extensions.GameExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
class InputServiceTest {

  @Test
  void shouldRegisterInputHandler() {
    int keycode = 1;
    InputComponent inputComponent = spy(InputComponent.class);
    when(inputComponent.getPriority()).thenReturn(1);

    InputService inputService = new InputService();
    inputService.register(inputComponent);

    inputService.keyDown(keycode);
    verify(inputComponent).keyDown(keycode);
  }

  @Test
  void shouldUnregisterInputHandler() {
    int keycode = 1;
    InputComponent inputComponent = spy(InputComponent.class);
    when(inputComponent.getPriority()).thenReturn(1);

    InputService inputService = new InputService();
    inputService.register(inputComponent);
    inputService.unregister(inputComponent);

    inputService.keyDown(keycode);
    verify(inputComponent, times(0)).keyDown(keycode);
  }

  @Test
  void shouldHandleKeyDown()
      throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method method = InputComponent.class.getDeclaredMethod("keyDown", int.class);
    Method serviceMethod = InputService.class.getDeclaredMethod("keyDown", int.class);
    shouldCallInputHandlersInPriorityOrder(method, serviceMethod, 1);
  }

  @Test
  void shouldHandleKeyTyped()
      throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method method = InputComponent.class.getDeclaredMethod("keyTyped", char.class);
    Method serviceMethod = InputService.class.getDeclaredMethod("keyTyped", char.class);
    shouldCallInputHandlersInPriorityOrder(method, serviceMethod, 'a');
  }

  @Test
  void shouldHandleKeyUp()
      throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method method = InputComponent.class.getDeclaredMethod("keyUp", int.class);
    Method serviceMethod = InputService.class.getDeclaredMethod("keyUp", int.class);
    shouldCallInputHandlersInPriorityOrder(method, serviceMethod, 1);
  }

  @Test
  void shouldHandleMouseMoved()
      throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method method = InputComponent.class.getDeclaredMethod("mouseMoved", int.class, int.class);
    Method serviceMethod = InputService.class.getDeclaredMethod("mouseMoved", int.class, int.class);
    shouldCallInputHandlersInPriorityOrder(method, serviceMethod, 5, 6);
  }

  @Test
  void shouldHandleScrolled()
      throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method method = InputComponent.class.getDeclaredMethod("scrolled", float.class, float.class);
    Method serviceMethod =
        InputService.class.getDeclaredMethod("scrolled", float.class, float.class);
    shouldCallInputHandlersInPriorityOrder(method, serviceMethod, 5f, 6f);
  }

  @Test
  void shouldHandleTouchDown()
      throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method method =
        InputComponent.class.getDeclaredMethod(
            "touchDown", int.class, int.class, int.class, int.class);
    Method serviceMethod =
        InputService.class.getDeclaredMethod(
            "touchDown", int.class, int.class, int.class, int.class);
    shouldCallInputHandlersInPriorityOrder(method, serviceMethod, 5, 6, 7, 8);
  }

  @Test
  void shouldHandleTouchDragged()
      throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method method =
        InputComponent.class.getDeclaredMethod("touchDragged", int.class, int.class, int.class);
    Method serviceMethod =
        InputService.class.getDeclaredMethod("touchDragged", int.class, int.class, int.class);
    shouldCallInputHandlersInPriorityOrder(method, serviceMethod, 5, 6, 7);
  }

  @Test
  void shouldHandleTouchUp()
      throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method method =
        InputComponent.class.getDeclaredMethod(
            "touchUp", int.class, int.class, int.class, int.class);
    Method serviceMethod =
        InputService.class.getDeclaredMethod("touchUp", int.class, int.class, int.class, int.class);
    shouldCallInputHandlersInPriorityOrder(method, serviceMethod, 5, 6, 7, 8);
  }

  @Test
  void shouldHandleFling()
      throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method method =
        InputComponent.class.getDeclaredMethod("fling", float.class, float.class, int.class);
    Method serviceMethod =
        InputService.class.getDeclaredMethod("fling", float.class, float.class, int.class);
    shouldCallInputHandlersInPriorityOrder(method, serviceMethod, 5f, 6f, 7);
  }

  @Test
  void shouldHandleLongPress()
      throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method method = InputComponent.class.getDeclaredMethod("longPress", float.class, float.class);
    Method serviceMethod =
        InputService.class.getDeclaredMethod("longPress", float.class, float.class);
    shouldCallInputHandlersInPriorityOrder(method, serviceMethod, 5f, 6f);
  }

  @Test
  void shouldHandlePan()
      throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method method =
        InputComponent.class.getDeclaredMethod(
            "pan", float.class, float.class, float.class, float.class);
    Method serviceMethod =
        InputService.class.getDeclaredMethod(
            "pan", float.class, float.class, float.class, float.class);
    shouldCallInputHandlersInPriorityOrder(method, serviceMethod, 5f, 6f, 7f, 8f);
  }

  @Test
  void shouldHandlePanStop()
      throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method method =
        InputComponent.class.getDeclaredMethod(
            "panStop", float.class, float.class, int.class, int.class);
    Method serviceMethod =
        InputService.class.getDeclaredMethod(
            "panStop", float.class, float.class, int.class, int.class);
    shouldCallInputHandlersInPriorityOrder(method, serviceMethod, 5f, 6f, 7, 8);
  }

  @Test
  void shouldHandlePinch()
      throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method method =
        InputComponent.class.getDeclaredMethod(
            "pinch", Vector2.class, Vector2.class, Vector2.class, Vector2.class);
    Method serviceMethod =
        InputService.class.getDeclaredMethod(
            "pinch", Vector2.class, Vector2.class, Vector2.class, Vector2.class);
    shouldCallInputHandlersInPriorityOrder(
        method, serviceMethod, Vector2.Zero, Vector2.Zero, Vector2.Zero, Vector2.Zero);
  }

  @Test
  void shouldHandleTap()
      throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method method =
        InputComponent.class.getDeclaredMethod(
            "tap", float.class, float.class, int.class, int.class);
    Method serviceMethod =
        InputService.class.getDeclaredMethod("tap", float.class, float.class, int.class, int.class);
    shouldCallInputHandlersInPriorityOrder(method, serviceMethod, 5f, 6f, 7, 8);
  }

  @Test
  void shouldHandleTouchDownGesture()
      throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method method =
        InputComponent.class.getDeclaredMethod(
            "touchDown", float.class, float.class, int.class, int.class);
    Method serviceMethod =
        InputService.class.getDeclaredMethod(
            "touchDown", float.class, float.class, int.class, int.class);
    shouldCallInputHandlersInPriorityOrder(method, serviceMethod, 5f, 6f, 7, 8);
  }

  @Test
  void shouldHandleZoom()
      throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method method = InputComponent.class.getDeclaredMethod("zoom", float.class, float.class);
    Method serviceMethod = InputService.class.getDeclaredMethod("zoom", float.class, float.class);
    shouldCallInputHandlersInPriorityOrder(method, serviceMethod, 5f, 6f);
  }

  /**
   * This is a generic method that is used to test that each of the InputService's registered input handlers are called
   * by descending priority order. As well as, that the InputService returns as soon as the input is handled.
   *
   * @param method input component method
   * @param serviceMethod input service method
   * @param args method args
   * @throws InvocationTargetException
   * @throws IllegalAccessException
   */
  private void shouldCallInputHandlersInPriorityOrder(
      Method method, Method serviceMethod, Object... args)
      throws InvocationTargetException, IllegalAccessException {
    InputComponent inputComponent1 = spy(InputComponent.class);
    InputComponent inputComponent2 = spy(InputComponent.class);
    InputComponent inputComponent3 = spy(InputComponent.class);

    when(inputComponent1.getPriority()).thenReturn(100);
    when(inputComponent2.getPriority()).thenReturn(1);
    when(inputComponent3.getPriority()).thenReturn(10);

    when(method.invoke(inputComponent1, args)).thenReturn(false);
    when(method.invoke(inputComponent2, args)).thenReturn(true);
    when(method.invoke(inputComponent3, args)).thenReturn(true);

    InputService inputService = new InputService();
    inputService.register(inputComponent1);
    inputService.register(inputComponent2);
    inputService.register(inputComponent3);

    serviceMethod.invoke(inputService, args);
    method.invoke(verify(inputComponent1, times(1)), args);
    method.invoke(verify(inputComponent2, times(0)), args);
    method.invoke(verify(inputComponent3, times(1)), args);
  }
}

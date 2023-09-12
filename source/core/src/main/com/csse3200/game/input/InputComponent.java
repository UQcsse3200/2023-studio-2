package com.csse3200.game.input;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.Component;
import com.csse3200.game.services.ServiceLocator;

/**
 * An InputComponent that supports keyboard and touch input and touch gestures. By default an
 * InputComponent does not handle any input events.
 *
 * <p>Subclasses of InputComponent should override relevant methods to handle input. Ensure the
 * priority is set in the subclass' constructor.
 */
public abstract class InputComponent extends Component
    implements InputProcessor, GestureDetector.GestureListener {
  /** The priority that the input handler is visited in by InputService. */
  protected int priority;

  /**
   * Sets priority to the default value;
   */
  protected InputComponent() {
    this(0);
  }

  /**
   * Sets input handler priority to a given value.
   * @param priority input handler's priority
   */
  protected InputComponent(int priority) {
    this.priority = priority;
  }

  @Override
  public void create() {
    ServiceLocator.getInputService().register(this);
  }

  public void setPriority(int priority) {
    this.priority = priority;
  }

  public int getPriority() {
    return priority;
  }

  @Override
  public void dispose() {
    super.dispose();
    ServiceLocator.getInputService().unregister(this);
  }

  /**
   * Below methods are for supporting keyboard and touch.
   */

  /** @see InputProcessor#keyDown(int) */
  @Override
  public boolean keyDown(int keycode) {
    return false;
  }

  /** @see InputProcessor#keyTyped(char) */
  @Override
  public boolean keyTyped(char character) {
    return false;
  }

  /** @see InputProcessor#keyUp(int) */
  @Override
  public boolean keyUp(int keycode) {
    return false;
  }

  /** @see InputProcessor#mouseMoved(int, int) */
  @Override
  public boolean mouseMoved(int screenX, int screenY) {
    return false;
  }

  /** @see InputProcessor#scrolled(float, float) */
  @Override
  public boolean scrolled(float amountX, float amountY) {
    return false;
  }

  /** @see InputProcessor#touchDown(int, int, int, int) */
  @Override
  public boolean touchDown(int screenX, int screenY, int pointer, int button) {
    return false;
  }

  /** @see InputProcessor#touchDragged(int, int, int) */
  @Override
  public boolean touchDragged(int screenX, int screenY, int pointer) {
    return false;
  }

  /** @see InputProcessor#touchUp(int, int, int, int) */
  @Override
  public boolean touchUp(int screenX, int screenY, int pointer, int button) {
    return false;
  }

  /**
   * Below methods are for supporting touch gestures.
   */

  /** @see GestureDetector.GestureListener#fling(float, float, int) */
  @Override
  public boolean fling(float velocityX, float velocityY, int button) {
    return false;
  }

  /** @see GestureDetector.GestureListener#longPress(float, float) */
  @Override
  public boolean longPress(float x, float y) {
    return false;
  }

  /** @see GestureDetector.GestureListener#pan(float, float, float, float) */
  @Override
  public boolean pan(float x, float y, float deltaX, float deltaY) {
    return false;
  }

  /** @see GestureDetector.GestureListener#panStop(float, float, int, int) */
  @Override
  public boolean panStop(float x, float y, int pointer, int button) {
    return false;
  }

  /** @see GestureDetector.GestureListener#pinch(Vector2, Vector2, Vector2, Vector2) */
  @Override
  public boolean pinch(
      Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
    return false;
  }

  /** @see GestureDetector.GestureListener#pinchStop() */
  @Override
  public void pinchStop() {}

  /**
   * Wrapper for pinch stop that can be overridden.
   * This was created because the pinchStop() doesn't have a return value.
   *
   * @return whether the input was processed
   */
  public boolean pinchStopHandled() {
    this.pinchStop();
    return false;
  }

  /** @see GestureDetector.GestureListener#tap(float, float, int, int) */
  @Override
  public boolean tap(float x, float y, int count, int button) {
    return false;
  }

  /** @see GestureDetector.GestureListener#touchDown(float, float, int, int) */
  @Override
  public boolean touchDown(float x, float y, int pointer, int button) {
    return false;
  }

  /** @see GestureDetector.GestureListener#zoom(float, float) */
  @Override
  public boolean zoom(float initialDistance, float distance) {
    return false;
  }

}

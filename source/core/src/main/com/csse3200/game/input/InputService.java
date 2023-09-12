package com.csse3200.game.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Provides a global access point for handling user input and creating input handlers. All active
 * input handlers should be registered here.
 *
 * <p>When an input is received, it is passed to registered input handlers in descending priority
 * order and stops as soon as the input is handled.
 */
public class InputService implements InputProcessor, GestureDetector.GestureListener {
  private static final Logger logger = LoggerFactory.getLogger(InputService.class);
  private static final InputFactory.InputType inputType = InputFactory.InputType.KEYBOARD;

  private static final Comparator<InputComponent> comparator =
      Collections.reverseOrder(Comparator.comparingInt(InputComponent::getPriority));

  private final List<InputComponent> inputHandlers = new ArrayList<>();
  private final InputFactory inputFactory;

  public InputService() {
    this(InputFactory.createFromInputType(inputType));
  }

  public InputService(InputFactory inputFactory) {
    this.inputFactory = inputFactory;
    Gdx.input.setInputProcessor(this);
  }

  /**
   * Get the input factory to create input handlers
   *
   * @return input factory
   */
  public InputFactory getInputFactory() {
    return inputFactory;
  }

  /**
   * Register an input handler based on its priority and reorder inputHandlers.
   *
   * @param inputHandler input handler
   */
  public void register(InputComponent inputHandler) {
    logger.debug("Registering input handler {}", inputHandler);
    inputHandlers.add(inputHandler);
    inputHandlers.sort(comparator);
  }

  /**
   * Unregister an input handler
   *
   * @param inputHandler input handler
   */
  public void unregister(InputComponent inputHandler) {
    logger.debug("Unregistering input handler {}", inputHandler);
    inputHandlers.remove(inputHandler);
  }

  /**
   * Iterates over registered input handlers in descending priority and stops as soon as the input is
   * processed.
   *
   * @return whether the input was processed
   * @see InputProcessor#keyDown(int)
   */
  @Override
  public boolean keyDown(int keycode) {
    for (InputComponent inputHandler : inputHandlers) {
      if (inputHandler.keyDown(keycode)) {
        logger.debug("keyDown input handled by {}", inputHandler);
        return true;
      }
    }
    logger.debug("keyDown input was not handled");
    return false;
  }

  /**
   * Iterates over registered input handlers in descending priority and stops as soon as the input is
   * processed
   *
   * @return whether the input was processed
   * @see InputProcessor#keyTyped(char)
   */
  @Override
  public boolean keyTyped(char character) {
    for (InputComponent inputHandler : inputHandlers) {
      if (inputHandler.keyTyped(character)) {
        logger.debug("keyTyped input handled by {}", inputHandler);
        return true;
      }
    }
    logger.debug("keyTyped input was not handled");
    return false;
  }

  /**
   * Iterates over registered input handlers in descending priority and stops as soon as the input is
   * processed
   *
   * @return whether the input was processed
   * @see InputProcessor#keyUp(int)
   */
  @Override
  public boolean keyUp(int keycode) {
    for (InputComponent inputHandler : inputHandlers) {
      if (inputHandler.keyUp(keycode)) {
        logger.debug("keyUp input handled by {}", inputHandler);
        return true;
      }
    }
    logger.debug("keyUp input was not handled");
    return false;
  }

  /**
   * Iterates over registered input handlers in descending priority and stops as soon as the input is
   * processed
   *
   * @return whether the input was processed
   * @see InputProcessor#mouseMoved(int, int)
   */
  @Override
  public boolean mouseMoved(int screenX, int screenY) {
    for (InputComponent inputHandler : inputHandlers) {
      if (inputHandler.mouseMoved(screenX, screenY)) {
        logger.debug("mouseMoved input handled by {}", inputHandler);
        return true;
      }
    }
    logger.debug("mouseMoved input was not handled");
    return false;
  }

  /**
   * Iterates over registered input handlers in descending priority and stops as soon as the input is
   * processed
   *
   * @return whether the input was processed
   * @see InputProcessor#scrolled(float, float)
   */
  @Override
  public boolean scrolled(float amountX, float amountY) {
    for (InputComponent inputHandler : inputHandlers) {
      if (inputHandler.scrolled(amountX, amountY)) {
        logger.debug("scrolled input handled by {}", inputHandler);
        return true;
      }
    }
    logger.debug("scrolled input was not handled");
    return false;
  }

  /**
   * Iterates over registered input handlers in descending priority and stops as soon as the input is
   * processed
   *
   * @return whether the input was processed
   * @see InputProcessor#touchDown(int, int, int, int)
   */
  @Override
  public boolean touchDown(int screenX, int screenY, int pointer, int button) {
    for (InputComponent inputHandler : inputHandlers) {
      if (inputHandler.touchDown(screenX, screenY, pointer, button)) {
        logger.debug("touchDown input handled by {}", inputHandler);
        return true;
      }
    }
    logger.debug("touchDown input was not handled");
    return false;
  }

  /**
   * Iterates over registered input handlers in descending priority and stops as soon as the input is
   * processed
   *
   * @return whether the input was processed
   * @see InputProcessor#touchDragged(int, int, int)
   */
  @Override
  public boolean touchDragged(int screenX, int screenY, int pointer) {
    for (InputComponent inputHandler : inputHandlers) {
      if (inputHandler.touchDragged(screenX, screenY, pointer)) {
        logger.debug("touchDragged input handled by {}", inputHandler);
        return true;
      }
    }
    logger.debug("touchDragged input was not handled");
    return false;
  }

  /**
   * Iterates over registered input handlers in descending priority and stops as soon as the input is
   * processed
   *
   * @return whether the input was processed
   * @see InputProcessor#touchUp(int, int, int, int)
   */
  @Override
  public boolean touchUp(int screenX, int screenY, int pointer, int button) {
    for (InputComponent inputHandler : inputHandlers) {
      if (inputHandler.touchUp(screenX, screenY, pointer, button)) {
        logger.debug("touchUp input handled by {}", inputHandler);
        return true;
      }
    }
    logger.debug("touchUp input was not handled");
    return false;
  }

  /**
   * Iterates over registered input handlers in descending priority and stops as soon as the input is
   * processed
   *
   * @return whether the input was processed
   * @see GestureDetector.GestureListener#fling(float, float, int)
   */
  @Override
  public boolean fling(float velocityX, float velocityY, int button) {
    for (InputComponent inputHandler : inputHandlers) {
      if (inputHandler.fling(velocityX, velocityY, button)) {
        logger.debug("fling input handled by {}", inputHandler);
        return true;
      }
    }
    logger.debug("fling input was not handled");
    return false;
  }

  /**
   * Iterates over registered input handlers in descending priority and stops as soon as the input is
   * processed
   *
   * @return whether the input was processed
   * @see GestureDetector.GestureListener#longPress(float, float)
   */
  @Override
  public boolean longPress(float x, float y) {
    for (InputComponent inputHandler : inputHandlers) {
      if (inputHandler.longPress(x, y)) {
        logger.debug("longPress input handled by {}", inputHandler);
        return true;
      }
    }
    logger.debug("longPress input was not handled");
    return false;
  }

  /**
   * Iterates over registered input handlers in descending priority and stops as soon as the input is
   * processed
   *
   * @return whether the input was processed
   * @see GestureDetector.GestureListener#pan(float, float, float, float)
   */
  @Override
  public boolean pan(float x, float y, float deltaX, float deltaY) {
    for (InputComponent inputHandler : inputHandlers) {
      if (inputHandler.pan(x, y, deltaX, deltaY)) {
        logger.debug("pan input handled by {}", inputHandler);
        return true;
      }
    }
    logger.debug("pan input was not handled");
    return false;
  }

  /**
   * Iterates over registered input handlers in descending priority and stops as soon as the input is
   * processed
   *
   * @return whether the input was processed
   * @see GestureDetector.GestureListener#panStop(float, float, int, int)
   */
  @Override
  public boolean panStop(float x, float y, int pointer, int button) {
    for (InputComponent inputHandler : inputHandlers) {
      if (inputHandler.panStop(x, y, pointer, button)) {
        logger.debug("panStop input handled by {}", inputHandler);
        return true;
      }
    }
    logger.debug("panStop input was not handled");
    return false;
  }

  /**
   * Iterates over registered input handlers in descending priority and stops as soon as the input is
   * processed
   *
   * @return whether the input was processed
   * @see GestureDetector.GestureListener#pinch(Vector2, Vector2, Vector2, Vector2)
   */
  @Override
  public boolean pinch(
      Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
    for (InputComponent inputHandler : inputHandlers) {
      if (inputHandler.pinch(initialPointer1, initialPointer2, pointer1, pointer2)) {
        logger.debug("pinch input handled by {}", inputHandler);
        return true;
      }
    }
    logger.debug("pinch input was not handled");
    return false;
  }

  /**
   * Iterates over registered input handlers in descending priority and stops as soon as the input is
   * processed
   *
   * @see GestureDetector.GestureListener#pinchStop()
   */
  @Override
  public void pinchStop() {
    for (InputComponent inputHandler : inputHandlers) {
      if (inputHandler.pinchStopHandled()) {
        logger.debug("pinchStop input handled by {}", inputHandler);
        return;
      }
    }
    logger.debug("pinchStop input was not handled");
  }

  /**
   * Iterates over registered input handlers in descending priority and stops as soon as the input is
   * processed
   *
   * @return whether the input was processed
   * @see GestureDetector.GestureListener#tap(float, float, int, int)
   */
  @Override
  public boolean tap(float x, float y, int count, int button) {
    for (InputComponent inputHandler : inputHandlers) {
      if (inputHandler.tap(x, y, count, button)) {
        logger.debug("tap input handled by {}", inputHandler);
        return true;
      }
    }
    logger.debug("tap input was not handled");
    return false;
  }

  /**
   * Iterates over registered input handlers in descending priority and stops as soon as the input is
   * processed
   *
   * @return whether the input was processed
   * @see GestureDetector.GestureListener#touchDown(float, float, int, int)
   */
  @Override
  public boolean touchDown(float x, float y, int pointer, int button) {
    for (InputComponent inputHandler : inputHandlers) {
      if (inputHandler.touchDown(x, y, pointer, button)) {
        logger.debug("touchDown (gesture) input handled by {}", inputHandler);
        return true;
      }
    }
    logger.debug("touchDown input was not handled");
    return false;
  }

  /**
   * Iterates over registered input handlers in descending priority and stops as soon as the input is
   * processed
   *
   * @return whether the input was processed
   * @see GestureDetector.GestureListener#zoom(float, float)
   */
  @Override
  public boolean zoom(float initialDistance, float distance) {
    for (InputComponent inputHandler : inputHandlers) {
      if (inputHandler.zoom(initialDistance, distance)) {
        logger.debug("zoom input handled by {}", inputHandler);
        return true;
      }
    }
    logger.debug("zoom input was not handled");
    return false;
  }
}

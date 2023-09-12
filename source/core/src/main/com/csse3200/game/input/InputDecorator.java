package com.csse3200.game.input;

import com.badlogic.gdx.InputProcessor;

/**
 * Generic class to wrap an InputProcessor so that it acts like an InputComponent. This was
 * initially created to wrap the stage in, but can be used for any class that implements
 * InputProcessor.
 */
public class InputDecorator extends InputComponent {
  private final InputProcessor inputProcessor;

  public InputDecorator(InputProcessor inputProcessor, int priority) {
    super(priority);
    this.inputProcessor = inputProcessor;
  }

  @Override
  public boolean keyDown(int keycode) {
    return inputProcessor.keyDown(keycode);
  }

  @Override
  public boolean keyTyped(char character) {
    return inputProcessor.keyTyped(character);
  }

  @Override
  public boolean keyUp(int keycode) {
    return inputProcessor.keyUp(keycode);
  }

  @Override
  public boolean mouseMoved(int screenX, int screenY) {
    return inputProcessor.mouseMoved(screenX, screenY);
  }

  @Override
  public boolean scrolled(float amountX, float amountY) {
    return inputProcessor.scrolled(amountX, amountY);
  }

  @Override
  public boolean touchDown(int screenX, int screenY, int pointer, int button) {
    return inputProcessor.touchDown(screenX, screenY, pointer, button);
  }

  @Override
  public boolean touchDragged(int screenX, int screenY, int pointer) {
    return inputProcessor.touchDragged(screenX, screenY, pointer);
  }

  @Override
  public boolean touchUp(int screenX, int screenY, int pointer, int button) {
    return inputProcessor.touchUp(screenX, screenY, pointer, button);
  }
}

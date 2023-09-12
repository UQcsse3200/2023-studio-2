package com.csse3200.game.ui.terminal;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.csse3200.game.input.InputComponent;

/**
 * Input handler for the debug terminal for keyboard and touch (mouse) input.
 * This input handler only uses keyboard input.
 *
 * <p>The debug terminal can be opened and closed by pressing 'F1' and a message can be entered via
 * the keyboard.
 */
public class KeyboardTerminalInputComponent extends InputComponent {
  private static final int TOGGLE_OPEN_KEY = Input.Keys.F1;
  private Terminal terminal;

  public KeyboardTerminalInputComponent() {
    super(10);
  }

  public KeyboardTerminalInputComponent(Terminal terminal) {
    this();
    this.terminal = terminal;
  }


  @Override
  public void create() {
    super.create();
    terminal = entity.getComponent(Terminal.class);
  }

  /**
   * If the toggle key is pressed, the terminal will open / close.
   *
   * <p>Otherwise, handles input if the terminal is open. This is because keyDown events are
   * triggered alongside keyTyped events. If the user is typing in the terminal, the input shouldn't
   * trigger any other input handlers.
   *
   * @return whether the input was processed
   * @see InputProcessor#keyDown(int)
   */
  @Override
  public boolean keyDown(int keycode) {
    // handle open and close terminal
    if (keycode == TOGGLE_OPEN_KEY) {
      terminal.toggleIsOpen();
      return true;
    }

    return terminal.isOpen();
  }

  /**
   * Handles input if the terminal is open. If 'enter' is typed, the entered message will be
   * processed, otherwise the message will be updated with the new character.
   *
   * @return whether the input was processed
   * @see InputProcessor#keyTyped(char)
   */
  @Override
  public boolean keyTyped(char character) {
    if (!terminal.isOpen()) {
      return false;
    }

    if (character == '\b') {
      terminal.handleBackspace();
      return true;
    } else if (character == '\r' || character == '\n') {
      if (terminal.processMessage()) {
        terminal.toggleIsOpen();
      }
      terminal.setEnteredMessage("");
      return true;
    } else if(Character.isLetterOrDigit(character) || character == ' ') {
      // append character to message
      terminal.appendToMessage(character);
      return true;
    }
    return false;
  }

  /**
   * Handles input if the terminal is open. This is because keyUp events are
   * triggered alongside keyTyped events. If the user is typing in the terminal, the input shouldn't
   * trigger any other input handlers.
   *
   * @return whether the input was processed
   * @see InputProcessor#keyUp(int)
   */
  @Override
  public boolean keyUp(int keycode) {
    return terminal.isOpen();
  }
}

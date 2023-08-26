package com.csse3200.game.input;

import com.csse3200.game.components.player.TouchPlayerInputComponent;
import com.csse3200.game.components.Companion.TouchCompanionInputComponent;
import com.csse3200.game.ui.terminal.TouchTerminalInputComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TouchInputFactory extends InputFactory{
  private static final Logger logger = LoggerFactory.getLogger(TouchInputFactory.class);

  /**
   * Creates an input handler for the player
   *
   * @return Player input handler
   */
  @Override
  public InputComponent createForPlayer() {
    logger.debug("Creating player input handler");
    return new TouchPlayerInputComponent();
  }

  /**
   * Creates an input handler for the Companion
   *
   * @return Companion input handler
   */
  @Override
  public InputComponent createForCompanion() {
    logger.debug("Creating Companion input handler");
    return new TouchCompanionInputComponent();
  }

  /**
   * Creates an input handler for the terminal
   *
   * @return Terminal input handler
   */
  @Override
  public InputComponent createForTerminal() {
    logger.debug("Creating terminal input handler");
    return new TouchTerminalInputComponent();
  }
}

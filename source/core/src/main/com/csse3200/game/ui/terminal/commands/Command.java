package com.csse3200.game.ui.terminal.commands;

import java.util.ArrayList;

/**
 * A generic command class.
 */
public interface Command {
  /**
   * Action a command.
   * @param args command args
   * @return command was successful
   */
  boolean action(ArrayList<String> args);
}

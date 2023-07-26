package com.csse3200.game.ui.terminal;

import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.ui.terminal.commands.Command;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
class TerminalTest {
  Command command = mock(Command.class);

  @Test
  void shouldSetOpenClosed() {
    Terminal terminal = spy(Terminal.class);

    terminal.setClosed();
    assertFalse(terminal.isOpen());

    terminal.setOpen();
    assertTrue(terminal.isOpen());

    terminal.setClosed();
    assertFalse(terminal.isOpen());
  }

  @Test
  void shouldToggleIsOpen() {
    Terminal terminal = spy(Terminal.class);

    terminal.setClosed();

    terminal.toggleIsOpen();
    assertTrue(terminal.isOpen());
    terminal.toggleIsOpen();
    assertFalse(terminal.isOpen());
  }

  @Test
  void shouldAddCommand() {
    HashMap<String, Command> commands = new HashMap<>();
    Terminal terminal = new Terminal(commands);

    int startingSize = commands.size();

    terminal.addCommand("test1", command);
    assertEquals(startingSize + 1, commands.size());

    terminal.addCommand("test2", command);
    assertEquals(startingSize + 2, commands.size());

    // command for duplicate key should not be added
    terminal.addCommand("test2", command);
    assertEquals(startingSize + 2, commands.size());
  }

  @Test
  void shouldProcessMessageNoArgs() {
    Terminal terminal = new Terminal();

    Class<ArrayList<String>> captorClass = (Class<ArrayList<String>>) (Class)ArrayList.class;
    ArgumentCaptor<ArrayList<String>> captor = ArgumentCaptor.forClass(captorClass);

    terminal.addCommand("test1", command);

    terminal.setEnteredMessage("test1");
    terminal.processMessage();
    verify(command).action(captor.capture());

    assertEquals(0, captor.getValue().size());
  }

  @Test
  void shouldProcessMessageMultipleArgs() {
    Terminal terminal = new Terminal();

    Class<ArrayList<String>> captorClass = (Class<ArrayList<String>>) (Class)ArrayList.class;
    ArgumentCaptor<ArrayList<String>> captor = ArgumentCaptor.forClass(captorClass);

    terminal.addCommand("test1", command);

    terminal.setEnteredMessage("test1 1 2 3");
    terminal.processMessage();
    verify(command).action(captor.capture());

    ArrayList<String> capturedArg = captor.getValue();
    assertEquals(3, capturedArg.size());
    assertEquals("1", capturedArg.get(0));
    assertEquals("2", capturedArg.get(1));
    assertEquals("3", capturedArg.get(2));
  }

  @Test
  void shouldModifyEnteredMessage() {
    Terminal terminal = new Terminal();

    terminal.appendToMessage('a');
    terminal.appendToMessage('b');
    assertEquals("ab", terminal.getEnteredMessage());

    terminal.handleBackspace();
    assertEquals("a", terminal.getEnteredMessage());
  }
}

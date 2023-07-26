package com.csse3200.game.input;

import com.badlogic.gdx.InputProcessor;
import com.csse3200.game.extensions.GameExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)
class InputDecoratorTest {
  @Mock InputProcessor inputProcessor;

  @Test
  void shouldCallKeys() {
    InputDecorator inputDecorator = new InputDecorator(inputProcessor, 10);

    inputDecorator.keyDown(1);
    verify(inputProcessor).keyDown(1);

    inputDecorator.keyTyped('c');
    verify(inputProcessor).keyTyped('c');

    inputDecorator.keyUp(2);
    verify(inputProcessor).keyUp(2);
  }

  @Test
  void shouldCallMouse() {
    InputDecorator inputDecorator = new InputDecorator(inputProcessor, 10);

    inputDecorator.mouseMoved(100, 200);
    verify(inputProcessor).mouseMoved(100, 200);

    inputDecorator.scrolled(300, 400);
    verify(inputProcessor).scrolled(300, 400);
  }

  @Test
  void shouldcallTouch() {
    InputDecorator inputDecorator = new InputDecorator(inputProcessor, 10);

    inputDecorator.touchDown(1, 2, 3, 4);
    verify(inputProcessor).touchDown(1, 2, 3, 4);

    inputDecorator.touchDragged(5, 6, 7);
    verify(inputProcessor).touchDragged(5, 6, 7);

    inputDecorator.touchUp(8, 9, 10, 11);
    verify(inputProcessor).touchUp(8, 9, 10, 11);
  }
}

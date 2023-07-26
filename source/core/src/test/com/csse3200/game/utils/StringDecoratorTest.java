package com.csse3200.game.utils;

import com.csse3200.game.extensions.GameExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(GameExtension.class)
class StringDecoratorTest {
  @Test
  void shouldReturnGivenString() {
    Function<Integer, String> customToString = (Integer n) -> "hello";
    StringDecorator<Integer> decorator = new StringDecorator<>(5, customToString);

    assertEquals("hello", decorator.toString());
  }

  @Test
  void shouldPassCorrectObject() {
    Function<Integer, String> customToString = (Integer n) -> Integer.toString(n * 2);

    StringDecorator<Integer> decorator = new StringDecorator<>(5, customToString);
    assertEquals("10", decorator.toString());

    decorator = new StringDecorator<>(10, customToString);
    assertEquals("20", decorator.toString());
  }
}
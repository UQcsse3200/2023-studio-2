package com.csse3200.game.utils;

import com.csse3200.game.extensions.GameExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(GameExtension.class)
class SortedIntMapTest {
  @Test
  void shouldPutGetCorrectly() {
    SortedIntMap<Integer> map = new SortedIntMap<>(2);
    map.put(3, 1);
    map.put(0, -2);
    assertEquals(1, map.get(3));
    assertEquals(-2, map.get(0));
    map.put(3, -4);
    assertEquals(-4, map.get(3));
    assertNull(map.get(10));
  }

  @Test
  void shouldContains() {
    SortedIntMap<Integer> map = new SortedIntMap<>(2);
    assertFalse(map.containsKey(4));
    assertFalse(map.contains(5));

    map.put(2, 5);
    assertTrue(map.contains(5));
    assertTrue(map.containsKey(2));
  }

  @Test
  void shouldClear() {
    SortedIntMap<Integer> map = new SortedIntMap<>(2);
    map.put(3, 1);
    map.put(0, -2);
    map.clear();
    assertNull(map.get(3));
    assertNull(map.get(0));
  }
}
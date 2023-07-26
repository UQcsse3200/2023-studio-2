package com.csse3200.game.utils;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntArray;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * A map sorted by the value of key with O(1) iteration. put/get/contains are O(n). useful when we
 * don't modify values often, but want to iterate quickly.
 *
 * @param <V> Class type to map
 */
public class SortedIntMap<V> implements Iterable<V> {
  private final IntArray keys;
  private final Array<V> values;

  /**
   * Create a sorted intmap with the specified capacity
   *
   * @param capacity initial capacity
   */
  public SortedIntMap(int capacity) {
    keys = new IntArray(true, capacity);
    values = new Array<>(true, capacity);
  }

  /**
   * @param key map key
   * @return true if map contains key
   */
  public boolean containsKey(int key) {
    return keys.contains(key);
  }

  /**
   * @param value value in map
   * @return true if map contains value
   */
  public boolean contains(V value) {
    return values.contains(value, true);
  }

  /**
   * Put key, value pair in map
   *
   * @param key key
   * @param value value
   */
  public void put(int key, V value) {
    if (keys.size == 0) {
      insertAt(0, key, value);
      return;
    }

    for (int i = 0; i < keys.size; i++) {
      if (keys.get(i) >= key) {
        insertAt(i, key, value);
        return;
      }
    }

    keys.add(key);
    values.add(value);
  }

  /**
   * Get value from map
   *
   * @param key map key
   * @return map value or null if not found
   */
  public V get(int key) {
    int index = keys.indexOf(key);
    if (index == -1) {
      return null;
    }
    return values.get(index);
  }

  /** Clear the map */
  public void clear() {
    keys.clear();
    values.clear();
  }

  @Override
  public Iterator<V> iterator() {
    return values.iterator();
  }

  @Override
  public void forEach(Consumer<? super V> action) {
    values.forEach(action);
  }

  @Override
  public Spliterator<V> spliterator() {
    return values.spliterator();
  }

  private void insertAt(int i, int key, V value) {
    keys.insert(i, key);
    values.insert(i, value);
  }
}

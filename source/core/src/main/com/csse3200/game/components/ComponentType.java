package com.csse3200.game.components;

import com.badlogic.gdx.utils.ObjectMap;

/**
 * Internal type system for components. Used to generate unique IDs for each component type at
 * runtime, allowing entities to get components by type.
 */
public class ComponentType {
  private static final ObjectMap<Class<? extends Component>, ComponentType> componentTypes =
      new ObjectMap<>();
  private static int nextId = 0;

  private final int id;

  public static ComponentType getFrom(Class<? extends Component> type) {
    ComponentType componentType = componentTypes.get(type);
    if (componentType == null) {
      componentType = new ComponentType();
      componentTypes.put(type, componentType);
    }
    return componentType;
  }

  public int getId() {
    return id;
  }

  private ComponentType() {
    id = nextId;
    nextId++;
  }
}

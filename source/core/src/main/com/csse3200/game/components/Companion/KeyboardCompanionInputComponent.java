/**
 * This class represents the input component for controlling a companion character using the keyboard.
 * It implements the InputProcessor interface to handle keyboard input events.
 */
package com.csse3200.game.components.Companion;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.input.InputComponent;
import com.csse3200.game.utils.math.Vector2Utils;

public class KeyboardCompanionInputComponent extends InputComponent {
    private final Vector2 walkDirection = Vector2.Zero.cpy();

    /**
     * Constructs a new KeyboardCompanionInputComponent with a priority of 5.
     */
    public KeyboardCompanionInputComponent() {
        super(5);
    }

    /**
     * Handles key down events and triggers companion events based on specific keycodes.
     *
     * @param keycode The keycode of the pressed key.
     * @return True if the input was processed, false otherwise.
     * @see InputProcessor#keyDown(int)
     */
    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Keys.SPACE -> {
                entity.getEvents().trigger("attack");
                return true;
            }
            case Keys.I -> {
                walkDirection.add(Vector2Utils.UP);
                triggerWalkEvent();
                return true;
            }
            case Keys.J -> {
                walkDirection.add(Vector2Utils.LEFT);
                triggerWalkEvent();
                return true;
            }
            case Keys.K -> {
                walkDirection.add(Vector2Utils.DOWN);
                triggerWalkEvent();
                return true;
            }
            case Keys.L -> {
                walkDirection.add(Vector2Utils.RIGHT);
                triggerWalkEvent();
                return true;
            }
            default -> {
                return false;
            }
        }
    }

    /**
     * Handles key up events and triggers companion events based on specific keycodes.
     *
     * @param keycode The keycode of the released key.
     * @return True if the input was processed, false otherwise.
     * @see InputProcessor#keyUp(int)
     */
    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Keys.I -> {
                walkDirection.sub(Vector2Utils.UP);
                triggerWalkEvent();
                return true;
            }
            case Keys.J -> {
                walkDirection.sub(Vector2Utils.LEFT);
                triggerWalkEvent();
                return true;
            }
            case Keys.K -> {
                walkDirection.sub(Vector2Utils.DOWN);
                triggerWalkEvent();
                return true;
            }
            case Keys.L -> {
                walkDirection.sub(Vector2Utils.RIGHT);
                triggerWalkEvent();
                return true;
            }
            default -> {
                return false;
            }
        }
    }

    /**
     * Triggers the walk event for the companion based on the current walk direction.
     * If the walk direction is zero, it triggers the walkStop event.
     */
    private void triggerWalkEvent() {
        if (walkDirection.epsilonEquals(Vector2.Zero)) {
            entity.getEvents().trigger("walkStop");
        } else {
            entity.getEvents().trigger("walk", walkDirection);
        }
    }
}

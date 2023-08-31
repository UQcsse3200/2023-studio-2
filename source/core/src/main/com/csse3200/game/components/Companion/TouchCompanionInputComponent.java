/**
 * This class represents the input component for controlling a companion character using touch input (mouse).
 * It implements the InputProcessor interface to handle touch input events.
 */
package com.csse3200.game.components.Companion;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.input.InputComponent;
import com.csse3200.game.utils.math.Vector2Utils;
import com.badlogic.gdx.InputProcessor;

public class TouchCompanionInputComponent extends InputComponent {

    /**
     * Constructs a new TouchCompanionInputComponent with a priority of 5.
     */
    public TouchCompanionInputComponent() {
        super(5);
    }

    /**
     * Handles touch input events and triggers the Companion's attack.
     *
     * @param screenX The x-coordinate of the touch event on the screen.
     * @param screenY The y-coordinate of the touch event on the screen.
     * @param pointer The pointer ID for the touch event.
     * @param button  The button ID for the touch event.
     * @return True if the input was processed, false otherwise.
     * @see InputProcessor#touchDown(int, int, int, int)
     */
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        entity.getEvents().trigger("attack");
        return true;
    }
}


/*
*//**
 * Input handler for the Companion for keyboard and touch (mouse) input.
 * This input handler uses keyboard and touch input.
 *//*
public class TouchCompanionInputComponent extends InputComponent {
    private final Vector2 walkDirection = Vector2.Zero.cpy();

    public TouchCompanionInputComponent() {
        super(5);
    }

    *//**
     * Triggers Companion events on specific keycodes.
     *
     * @return whether the input was processed
     * @see InputProcessor#keyDown(int)
     *//*
    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.UP:
                walkDirection.add(Vector2Utils.UP);
                triggerWalkEvent();
                return true;
            case Input.Keys.LEFT:
                walkDirection.add(Vector2Utils.LEFT);
                triggerWalkEvent();
                return true;
            case Input.Keys.DOWN:
                walkDirection.add(Vector2Utils.DOWN);
                triggerWalkEvent();
                return true;
            case Input.Keys.RIGHT:
                walkDirection.add(Vector2Utils.RIGHT);
                triggerWalkEvent();
                return true;
            default:
                return false;
        }
    }

    *//**
     * Triggers Companion events on specific keycodes.
     *
     * @return whether the input was processed
     * @see InputProcessor#keyUp(int)
     *//*
    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Input.Keys.UP:
                walkDirection.sub(Vector2Utils.UP);
                triggerWalkEvent();
                return true;
            case Input.Keys.LEFT:
                walkDirection.sub(Vector2Utils.LEFT);
                triggerWalkEvent();
                return true;
            case Input.Keys.DOWN:
                walkDirection.sub(Vector2Utils.DOWN);
                triggerWalkEvent();
                return true;
            case Input.Keys.RIGHT:
                walkDirection.sub(Vector2Utils.RIGHT);
                triggerWalkEvent();
                return true;
            default:
                return false;
        }
    }

    *//**
     * Triggers the Companionattack.
     * @return whether the input was processed
     * @see InputProcessor#touchDown(int, int, int, int)
     *//*
    @Override

    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        entity.getEvents().trigger("attack");
        return true;
    }

    private void triggerWalkEvent() {
        if (walkDirection.epsilonEquals(Vector2.Zero)) {
            entity.getEvents().trigger("walkStop");
        } else {
            entity.getEvents().trigger("walk", walkDirection);
        }
    }
}*/







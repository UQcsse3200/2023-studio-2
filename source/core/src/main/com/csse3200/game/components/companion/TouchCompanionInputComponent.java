/**
 * This class represents the input component for controlling a companion character using touch input (mouse).
 * It implements the InputProcessor interface to handle touch input events.
 */
package com.csse3200.game.components.companion;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.input.InputComponent;
import com.csse3200.game.utils.math.Vector2Utils;

/**
 * TouchCompanionInputComponent handles touch input to control a companion character.
 */
public class TouchCompanionInputComponent extends InputComponent {

    private final Vector2 walkDirection = Vector2.Zero.cpy();



    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.I:
                walkDirection.add(Vector2Utils.UP);
                triggerWalkEvent();
                return true;
            case Input.Keys.J:
                walkDirection.add(Vector2Utils.LEFT);
                triggerWalkEvent();
                return true;
            case Input.Keys.K:
                walkDirection.add(Vector2Utils.DOWN);
                triggerWalkEvent();
                return true;
            case Input.Keys.L:
                walkDirection.add(Vector2Utils.RIGHT);
                triggerWalkEvent();
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Input.Keys.I:
                walkDirection.sub(Vector2Utils.UP);
                triggerWalkEvent();
                return true;
            case Input.Keys.J:
                walkDirection.sub(Vector2Utils.LEFT);
                triggerWalkEvent();
                return true;
            case Input.Keys.K:
                walkDirection.sub(Vector2Utils.DOWN);
                triggerWalkEvent();
                return true;
            case Input.Keys.L:
                walkDirection.sub(Vector2Utils.RIGHT);
                triggerWalkEvent();
                return true;
            default:
                return false;
        }
    }

    /**
     * Trigger the companion's walking event based on the input direction.
     */
    private void triggerWalkEvent() {
        // If no input direction was given, stop the companion's movement
        if (walkDirection.epsilonEquals(Vector2.Zero)) {
            entity.getEvents().trigger("moveStop");
        } else {
            // Update the companion's position based on the direction
            entity.getEvents().trigger("move", walkDirection);
        }
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        // Trigger the companion's attack event when touched
        entity.getEvents().trigger("attack");
        return true;
    }
}

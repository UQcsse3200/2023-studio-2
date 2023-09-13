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

    private final Vector2 walkDirection = Vector2.Zero.cpy();
    public TouchCompanionInputComponent() {
        super(5);
    }

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
     * Trigger all the MOVING of the companion.
     */
    private void triggerWalkEvent() {
        // if no input direction was given, stop the companions movement
        if (walkDirection.epsilonEquals(Vector2.Zero)) {
            entity.getEvents().trigger("moveStop");
        } else {
            // update the companions position based off the direction
            entity.getEvents().trigger("move", walkDirection);
        }
    }


    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        entity.getEvents().trigger("attack");
        return true;
    }
}






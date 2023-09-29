package com.csse3200.game.components.Car;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.files.UserSettings;
import com.csse3200.game.input.InputComponent;
import com.csse3200.game.utils.math.Vector2Utils;

public class KeyboardCarInputComponent extends InputComponent implements InputProcessor {
    private final Vector2 movementDirection = new Vector2();
    private com.csse3200.game.components.Car.CarActions carActions;
    private UserSettings walkDirection;

    public KeyboardCarInputComponent() {
        super(5);
    }

    @Override
    public boolean keyDown(int keycode) {
        if (!carActions.isSilent()) {
            Vector2 direction = getKeyDirection(keycode);
            if (direction != null) {
                movementDirection.add(direction);
                triggerMoveEvent();
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (!carActions.isSilent()) {
            Vector2 direction = getKeyDirection(keycode);
            if (direction != null) {
                movementDirection.sub(direction);
                triggerMoveEvent();
                return true;
            }
        }
        return false;
    }

    private Vector2 getKeyDirection(int keycode) {
        switch (keycode) {
            case Input.Keys.G:
                return Vector2Utils.UP;
            case Input.Keys.C:
                return Vector2Utils.LEFT;
            case Input.Keys.V:
                return Vector2Utils.DOWN;
            case Input.Keys.B:
                return Vector2Utils.RIGHT;
            case Input.Keys.P:
                return Vector2.Zero;
            default:
                return null;
        }
    }

    private void triggerMoveEvent() {
        if (movementDirection.isZero()) {
            entity.getEvents().trigger("walkStop");
        } else {
            entity.getEvents().trigger("walk", movementDirection);
        }
    }

    private void triggerExitEvent() {
        entity.getEvents().trigger("exitCar");
    }

    public void setActions(com.csse3200.game.components.Car.CarActions actions) {
        this.carActions = actions;
    }

    public void setWalkDirection(Vector2 direction) {
        UserSettings.set(direction);
    }

    public Vector2 getWalkDirection() {
        return null;
    }
}

package com.csse3200.game.components.Companion;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.input.InputComponent;
import com.csse3200.game.utils.math.Vector2Utils;

public class KeyboardCompanionInputComponent extends InputComponent {
    private final Vector2 walkDirection = Vector2.Zero.cpy();

    public KeyboardCompanionInputComponent() {
        super(5);
    }

    /**
     * Triggers Companion events on specific keycodes.
     *
     * @return whether the input was processed
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
     * Triggers Companion events on specific keycodes.
     *
     * @return whether the input was processed
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
    private void triggerWalkEvent() {
        if (walkDirection.epsilonEquals(Vector2.Zero)) {
            entity.getEvents().trigger("walkStop");
        } else {
            entity.getEvents().trigger("walk", walkDirection);
        }
    }
}

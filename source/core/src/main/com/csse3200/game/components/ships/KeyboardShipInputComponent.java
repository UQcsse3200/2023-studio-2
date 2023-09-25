package com.csse3200.game.components.ships;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.player.InteractionControllerComponent;
import com.csse3200.game.input.InputComponent;
import com.csse3200.game.utils.math.Vector2Utils;

/**
 * Input handler for the ship for keyboard.
 * This input handler only uses keyboard input.
 */
public class KeyboardShipInputComponent extends InputComponent {
    private final Vector2 flyDirection = Vector2.Zero.cpy();

    public KeyboardShipInputComponent() { super(5); }


    /**
     * Triggers ship events on specific keycodes.
     *
     * @return whether the input was processed
     * @see InputProcessor#keyDown(int)
     */
    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Keys.W, Keys.UP -> {
                flyDirection.add(Vector2Utils.UP);
                triggerFlyEvent();
                return true;
            }
            case Keys.A,Keys.LEFT -> {
                flyDirection.add(Vector2Utils.LEFT);
                triggerFlyEvent();
                return true;
            }
            case Keys.S,Keys.DOWN -> {
                flyDirection.add(Vector2Utils.DOWN);
                triggerFlyEvent();
                return true;
            }
            case Keys.D,Keys.RIGHT -> {
                flyDirection.add(Vector2Utils.RIGHT);
                triggerFlyEvent();
                return true;
            }

            case Keys.F -> {
                InteractionControllerComponent interactionController = entity
                        .getComponent(InteractionControllerComponent.class);

                if (interactionController != null) {
                    interactionController.interact();
                }
                return true;
            }

            default -> {
                return false;
            }
        }
    }


    /**
     * Triggers player events on specific keycodes.
     *
     * @return whether the input was processed
     * @see InputProcessor#keyUp(int)
     */
    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {

            case Keys.W, Keys.UP -> {
                flyDirection.sub(Vector2Utils.UP);
                triggerFlyEvent();
                return true;
            }
            case Keys.A,Keys.LEFT-> {
                flyDirection.sub(Vector2Utils.LEFT);
                triggerFlyEvent();

                return true;
            }
            case Keys.S,Keys.DOWN -> {
                flyDirection.sub(Vector2Utils.DOWN);
                triggerFlyEvent();
                return true;
            }
            case Keys.D,Keys.RIGHT-> {
                flyDirection.sub(Vector2Utils.RIGHT);
                triggerFlyEvent();
                return true;
            }



            case Keys.B -> {
                triggerBrakeOnEvent();
                return true;
            }

            case Keys.V -> {
                triggerBrakeOffEvent();
                return true;
            }

            default -> {
                return false;
            }
        }
    }


    /**
     * Trigger the fly and flyStop events
     */
    private void triggerFlyEvent() {
        if (flyDirection.epsilonEquals(Vector2.Zero)) {
            entity.getEvents().trigger("flyStop");
        } else {
            entity.getEvents().trigger("fly", flyDirection);
        }
    }

    /**
     * Calls the event brakeOn to activate brakes
     */
    private void triggerBrakeOnEvent() {
        entity.getEvents().trigger("brakeOn");
    }

    /**
     * Calls the event brakeOff to deactivate brake
     */
    private void triggerBrakeOffEvent() {
        entity.getEvents().trigger("brakeOff");
    }

    /* Rotation attempt 1 was a Failure
    private void triggerLeftEvent() {
        entity.getEvents().trigger("turnLeft");
    }
     */
}


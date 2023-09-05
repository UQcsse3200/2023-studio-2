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
import java.util.Timer;

public class KeyboardCompanionInputComponent extends InputComponent {
    private final Vector2 walkDirection = Vector2.Zero.cpy();
    private boolean dodge_available = true;
    private int flagW = 0;
    private int flagA = 0;
    private int flagS = 0;
    private int flagD = 0;

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
            case Keys.O -> {
                if (dodge_available) {
                    if (flagW == 1) {
                        walkDirection.add(Vector2Utils.DODGE_UP);
                    } else if (flagA == 1) {
                        walkDirection.add(Vector2Utils.DODGE_LEFT);
                    } else if (flagS == 1) {
                        walkDirection.add(Vector2Utils.DODGE_DOWN);
                    } else {
                        walkDirection.add(Vector2Utils.DODGE_RIGHT);
                    }
                    triggerDodgeEvent();
                    dodge();
                }
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
    /**
     * Triggers dodge event.
     * Immunity is applied for 200 milliseconds whilst player moves.
     */
    private void triggerDodgeEvent() {
        final Timer timer = new Timer();
        entity.getEvents().trigger("walk", walkDirection);
        entity.getEvents().trigger("dodged");
        java.util.TimerTask stopDodge = new java.util.TimerTask() {
            @Override
            public void run() {
                entity.getEvents().trigger("walkStop");
                entity.getEvents().trigger("dodged");
                timer.cancel();
                timer.purge();
            }
        };
        timer.schedule(stopDodge, 150);
    }

    /**
     * Responsible for dodge action
     * Triggers when the spacebar is clicked.
     * Cooldown of 3000 ms.
     */
    private void dodge() {
        dodge_available = false;
        final Timer timer = new Timer();
        java.util.TimerTask makeDodgeAvailable = new java.util.TimerTask() {
            @Override
            public void run() {
                dodge_available = true;
                timer.cancel();
                timer.purge();
            }
        };
        timer.schedule(makeDodgeAvailable, 3000);
    }
}


/**
 * This class represents the input component for controlling a companion character using the keyboard.
 * It implements the InputProcessor interface to handle keyboard input events.
 */
package com.csse3200.game.components.Companion;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.csse3200.game.components.player.InteractionControllerComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.input.InputComponent;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.utils.math.Vector2Utils;

import java.util.HashMap;

/**
 * The KeyboardCompanionInputComponent class handles keyboard input for controlling a companion character.
 */
public class KeyboardCompanionInputComponent extends InputComponent implements InputProcessor {

    AnimationRenderComponent animator;
    private static final String CHANGEWEAPON = "changeWeapon";
    private final Vector2 walkDirection = Vector2.Zero.cpy();
    private int flagW = 0;
    private int flagA = 0;
    private int flagS = 0;
    private int flagD = 0;
    private Entity Companion;
    private CompanionInventoryComponent CompanionInventory;
    private int testing = 0;

    static HashMap<Integer, Integer> keyFlags = new HashMap<>();
    Vector2 lastMousePos = new Vector2(0, 0);

    public Vector2 getLastMousePos() {
        return this.lastMousePos.cpy();
    }


//    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
//        Vector2 position = mouseToGamePos(screenX, screenY);
//        this.lastMousePos = position.cpy();
//       Companion = ServiceLocator.getEntityService().getCompanion();
//       CompanionInventory = Companion.getComponent(CompanionInventoryComponent.class);
//        CompanionWeaponComponent weaponComponent = entity.getComponent(CompanionWeaponComponent.class);
//        int cooldown = CompanionInventory.getEquippedCooldown();
//        if (cooldown > 0) {
//            return false;
//        }
//
//        switch (CompanionInventory.getEquipped()) {
//
//
//            case "ranged" :
//                System.out.println(CompanionInventory.GetCurrentAmmo());
//                if (CompanionInventory.GetCurrentAmmo() <= 0) {return false;}
//
//                if (button == Input.Buttons.LEFT) {
//                    CompanionInventoryComponent invComp = entity.getComponent(CompanionInventoryComponent.class);
//                    CompanionWeaponType weapon = invComp.getEquippedType();
//
//                    if (weapon == CompanionWeaponType.Death_Potion) {
//                        for (int i = 0; i < 7; i++)
//                            ServiceLocator.getEntityService().getCompanion().getEvents().trigger("weaponAttack", weapon, position);
//                    }
//                    ServiceLocator.getEntityService().getCompanion().getEvents().trigger("weaponAttack", weapon, position);
//                }
//                break;
//            default:
//                return false;
//        }
//        return true;
//    }


//    public boolean touchDragged(int screenX, int screenY, int pointer) {
//        Vector2 position = mouseToGamePos(screenX, screenY);
//        this.lastMousePos = position.cpy();
//        return false;
//    }

    /**
     * Update location of known mouse position
     * @param screenX, screenY Location of mouse press
     * @return - false
     */

//    public boolean mouseMoved(int screenX, int screenY) {
//        Vector2 position = mouseToGamePos(screenX, screenY);
//        this.lastMousePos = position.cpy();
//        return false;
//    }

//    private Vector2 mouseToGamePos(int screenX, int screenY) {
//        Vector2 entityScale = entity.getScale();
//        Vector2 mouse = ServiceLocator.getTerrainService().ScreenCoordsToGameCoords(screenX, screenY);
//        return new Vector2(mouse.x / 2 - entityScale.x / 2, (mouse.y) / 2 - entityScale.y / 2);
//    }




    /**
     * Returns the value for testing.
     *
     * @return The testing value.
     */
    public int getTesting() {
        return testing;
    }

    /**
     * Sets the testing value.
     *
     * @param testing The value to set for testing.
     */
    public void setTesting(int testing) {
        this.testing = testing;
    }

    private final boolean leftCtrlFlag = false;

    /**
     * Returns the sum of movement flags (W, A, S, D).
     *
     * @return The sum of movement flags.
     */
    private int getMovFlagSum() {
        return flagW + flagA + flagS + flagD;
    }

    /**
     * Handles diagonal movement.
     */
    private void diagonal() {
        int movFlagSum = getMovFlagSum();
        if (movFlagSum >= 3) {
            walkDirection.set(Vector2.Zero);
        }
        int flagMul = 0;
        if (movFlagSum == 2) {
            flagMul = 1;
            walkDirection.scl(new Vector2(0.707f, 0.707f));
        } else if (movFlagSum == 1) {
            if (flagW == 1) {
                walkDirection.set(Vector2Utils.UP);
            } else if (flagA == 1) {
                walkDirection.set(Vector2Utils.LEFT);
            } else if (flagS == 1) {
                walkDirection.set(Vector2Utils.DOWN);
            } else if (flagD == 1) {
                walkDirection.set(Vector2Utils.RIGHT);
            }
            triggerWalkEvent();
        } else if (movFlagSum == 0) {
            flagMul = 0;
            walkDirection.scl(0);
        }
    }

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
     */
    private long lastNKeyPressTime = 0;
    private static final long COOLDOWN_TIME = 7000; // 7 seconds in milliseconds

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Keys.N -> {
                //ATTACK IS A SOUND THING - AND IT ISN'T  ACTIVATED
                ServiceLocator.getEntityService().getCompanion().getEvents().trigger("attack");
                long currentTime = TimeUtils.millis();
                if (currentTime - lastNKeyPressTime >= COOLDOWN_TIME) {
                    //TRY TO USE THE DEATH POTION
                    //OLD INVENTORY SPAWNER
                    //ServiceLocator.getEntityService().getCompanion().getComponent(CompanionInventoryComponent.class).useNextPowerup();

                    // Use new inventory to spawn a brand new death potion weapon
                    ServiceLocator.getEntityService().getCompanion().getComponent(CompanionPowerupInventoryComponent.class).tryCreateDeathPowerupFromPowerupInventory();
                    //ServiceLocator.getEntityService().getCompanion().getComponent(CompanionPowerupActivationDisplay.class).decreaseIndexInPowerupActivationList();
                    lastNKeyPressTime = currentTime;
                }else {logger.debug("powerup cooldown");}
                return true;
            }

            case Keys.B -> {
                entity.getEvents().trigger("CompanionSwitchMode");
                return true;
            }

            case Keys.I -> {
                flagW = 1;
                if (getMovFlagSum() == 1) {
                    walkDirection.scl(0);
                    walkDirection.add(Vector2Utils.UP);
                } else {
                    walkDirection.add(Vector2Utils.UP);
                    diagonal();
                }
                triggerWalkEvent();
                return true;
            }
            case Keys.J -> {
                flagA = 1;
                if (getMovFlagSum() == 1) {
                    walkDirection.scl(0);
                    walkDirection.add(Vector2Utils.LEFT);
                } else {
                    walkDirection.add(Vector2Utils.LEFT);
                    diagonal();
                }
                triggerWalkEvent();
                return true;
            }
            case Keys.K -> {
                flagS = 1;
                if (getMovFlagSum() == 1) {
                    walkDirection.scl(0);
                    walkDirection.add(Vector2Utils.DOWN);
                } else {
                    walkDirection.add(Vector2Utils.DOWN);
                    diagonal();
                }
                triggerWalkEvent();
                return true;
            }
            case Keys.L -> {
                flagD = 1;
                if (getMovFlagSum() == 1) {
                    walkDirection.scl(0);
                    walkDirection.add(Vector2Utils.RIGHT);
                } else {
                    walkDirection.add(Vector2Utils.RIGHT);
                    diagonal();
                }
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
     */
    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Keys.I -> {
                flagW = 0;
                diagonal();
                if (getMovFlagSum() == 2) {
                    diagonal();
                }
                if (getMovFlagSum() == 0) {


                    walkDirection.scl(0);
                }
                triggerWalkEvent();
                return true;
            }
            case Keys.J -> {
                flagA = 0;
                diagonal();
                if (getMovFlagSum() == 2) {
                    diagonal();
                }
                if (getMovFlagSum() == 0) {
                    walkDirection.scl(0);
                }
                triggerWalkEvent();
                return true;
            }
            case Keys.K -> {
                flagS = 0;
                diagonal();
                if (getMovFlagSum() == 2) {
                    diagonal();
                }
                if (getMovFlagSum() == 0) {
                    walkDirection.scl(0);
                }
                triggerWalkEvent();
                return true;
            }
            case Keys.L -> {
                flagD = 0;
                diagonal();
                if (getMovFlagSum() == 2) {
                    diagonal();
                }
                if (getMovFlagSum() == 0) {
                    walkDirection.scl(0);
                }
                triggerWalkEvent();
                return true;
            }
            default -> {
                return false;
            }
        }
    }

    /**
     * Gets the current walking direction.
     *
     * @return The walking direction vector.
     */
    public Vector2 getDirection() {
        return this.walkDirection;
    }

    /**
     * Triggers the walk event for the companion based on the current walk direction.
     * If the walk direction is zero, it triggers the walkStop event.
     */
    private void triggerWalkEvent() {;
        //Sound attackSound = ServiceLocator.getResourceService().getAsset("sounds/companionwalksound.wav", Sound.class);
        if (this.getTesting() == 0) {
            if (walkDirection.epsilonEquals(Vector2.Zero)) {
                entity.getEvents().trigger("walkStop");
                //attackSound.stop();
            } else {
                if (walkDirection.epsilonEquals(Vector2Utils.UP_LEFT)) {
                    entity.getEvents().trigger("walkUpLeft");
                } else if (walkDirection.epsilonEquals(Vector2Utils.UP_RIGHT)) {
                    entity.getEvents().trigger("walkUpRight");
                } else if (walkDirection.epsilonEquals(Vector2Utils.UP)) {
                    entity.getEvents().trigger("walkUp");
                } else if (walkDirection.epsilonEquals(Vector2Utils.DOWN)) {
                    entity.getEvents().trigger("walkDown");
                } else if (walkDirection.epsilonEquals(Vector2Utils.DOWN_LEFT)) {
                    entity.getEvents().trigger("walkDownLeft");
                } else if (walkDirection.epsilonEquals(Vector2Utils.DOWN_RIGHT)) {
                    entity.getEvents().trigger("walkDownRight");
                } else if (walkDirection.epsilonEquals(Vector2Utils.LEFT)) {
                    entity.getEvents().trigger("walkLeft");
                } else if (walkDirection.epsilonEquals(Vector2Utils.RIGHT)) {
                    entity.getEvents().trigger("walkRight");
                }
                entity.getEvents().trigger("walk", walkDirection);
                //attackSound.play();

            }
        }
    }

    private void triggerInventoryEvent(String slot) {
        CompanionInventoryComponent invComp = ServiceLocator.getEntityService().getCompanion().getComponent(CompanionInventoryComponent.class);
        invComp.setEquipped(slot);

        ServiceLocator.getEntityService().getCompanion().getEvents().trigger(CHANGEWEAPON, invComp.getEquippedType());

    }
}

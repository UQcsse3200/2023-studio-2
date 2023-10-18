package com.csse3200.game.components.player;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.Weapons.WeaponType;
import com.csse3200.game.components.maingame.MainGameActions;
import com.csse3200.game.components.structures.StructureToolPicker;
import com.csse3200.game.components.upgradetree.UpgradeDisplay;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.input.InputComponent;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.utils.math.Vector2Utils;
import java.util.HashMap;
import java.util.Timer;

/**
 * Input handler for the player for keyboard and touch (mouse) input.
 * This input handler only uses keyboard input.
 */
public class KeyboardPlayerInputComponent extends InputComponent {
    private static final float ROOT2INV = 1f / (float) Math.sqrt(2f);
    private static final float DODGE_SPEED = 3f;
    private static final float WALK_SPEED = 1f;
    private static final int DODGE_COOLDOWN = 1000;
    private static final int DODGE_DURATION = 300;
    private static final String CHANGEWEAPON = "changeWeapon";
    private static final String WALKSTOP = "walkStop";
    private static final String BUILDING = "building";
    private Vector2 walkDirection = Vector2.Zero.cpy();
    private boolean dodgeAvailable = true;
    private Entity player;
    private InventoryComponent playerInventory;
    private int testing = 0;
    static HashMap<Integer, Integer> keyFlags = new HashMap<>();
    Vector2 lastMousePos = new Vector2(0, 0);

    public KeyboardPlayerInputComponent() {
        super(5);
    }

    /**
     * Checks if any Window is currently open and visible on the stage.
     *
     * @return true if any window is open and visible; false otherwise.
     */
    public boolean isWindowOpen() {
        for (Actor actor : ServiceLocator.getRenderService().getStage().getActors()) {
            if (actor instanceof Window && actor.isVisible()) {
                return true;
            }
        }
        return false;
    }

    public void clearWalking() {
        keyFlags.clear();
        triggerWalkEvent();
    }

    /**
     * Returns the last known position of the users cursor
     */
    public Vector2 getLastMousePos() {
        return this.lastMousePos.cpy();
    }

    /**
     * Returns value for testing.
     *
     * @return int
     */
    public int getTesting() {
        return testing;
    }

    /**
     * Sets value for testing.
     *
     * @param testing
     */
    public void setTesting(int testing) {
        this.testing = testing;
    }

    /**
     * Triggers player events on specific keycodes.
     *
     * @return whether the input was processed
     * @see InputProcessor#keyDown(int)
     */
    @Override
    public boolean keyDown(int keycode) {
        keyFlags.put(keycode, 1);
        player = ServiceLocator.getEntityService().getPlayer();
        playerInventory = player.getComponent(InventoryComponent.class);
        if (Boolean.TRUE.equals(entity.getComponent(CombatStatsComponent.class).isDead())) {
            return false;
        }
        switch (keycode) {
            case Keys.SPACE -> {
                if (!dodgeAvailable ||
                        walkDirection.epsilonEquals(Vector2.Zero)) {
                    return false;
                }
                triggerDodgeEvent();
                dodge();
                return true;
            }
            case Keys.ESCAPE -> {
                for (Entity mainGame : ServiceLocator.getEntityService().getEntitiesByComponent(MainGameActions.class)) {
                    mainGame.getEvents().trigger("pause");
                }
                return true;
            }
            case Keys.F -> {
                InteractionControllerComponent interactionController = entity
                        .getComponent(InteractionControllerComponent.class);
                if (interactionController != null) {
                    interactionController.interact();
                }

                // Stop movement if a menu is open
                if (isWindowOpen()) {
                    keyFlags.clear();
                    triggerWalkEvent();
                }
                return true;
            }
            case Keys.T -> {
                if (playerInventory.getEquipped().equals(BUILDING)) {
                    entity.getEvents().trigger("change_structure");
                }
                return true;
            }
            case Keys.M -> {
                triggerInventoryEvent("melee");
                entity.getComponent(StructureToolPicker.class).hide();
                return true;
            }
            case Keys.R -> {
                triggerInventoryEvent("ranged");
                entity.getComponent(StructureToolPicker.class).hide();
                return true;
            }
            case Keys.H -> {
                triggerInventoryEvent("building");
                entity.getComponent(StructureToolPicker.class).show();
                return true;
            }
            case Keys.U -> {
                UpgradeDisplay display = UpgradeDisplay.createUpgradeDisplay();
                ServiceLocator.getRenderService().getStage().addActor(display);
                return true;
            }
            case Keys.NUM_0, Keys.NUM_1, Keys.NUM_2, Keys.NUM_3, Keys.NUM_4,
                    Keys.NUM_5, Keys.NUM_6, Keys.NUM_7, Keys.NUM_8, Keys.NUM_9 -> {
                int index = keycode - (Keys.NUM_0 + 1) % 10;
                entity.getEvents().trigger("selectToolIndex", index);
                entity.getEvents().trigger("selectWeaponIndex", index);
                return true;
            }
            case Keys.W, Keys.S, Keys.A, Keys.D -> {
                triggerWalkEvent();
                return true;
            }
            case Keys.Q -> {
                if (!playerInventory.getReloading()) {
                    playerInventory.reloadWeapon();
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
        keyFlags.put(keycode, 0);
        return switch (keycode) {
            case Keys.W, Keys.S, Keys.A, Keys.D -> {
                triggerWalkEvent();
                yield true;
            }
            default -> false;
        };
    }

    /**
     * Function to respond to player mouse press
     *
     * @param screenX - X position on screen that mouse was pressed
     * @param screenY - Y position on screen that mouse was pressed
     * @param pointer -
     * @param button  - Button that pas pressed
     * @return - True or false based on if an action occurred
     */
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector2 clickPos = mouseToGamePos(screenX, screenY);

        player = ServiceLocator.getEntityService().getPlayer();
        playerInventory = player.getComponent(InventoryComponent.class);
        int cooldown = playerInventory.getEquippedCooldown();
        if (playerInventory.getReloading() || cooldown > 0) {
            return false;
        }
        InventoryComponent invComp = entity.getComponent(InventoryComponent.class);
        WeaponType weapon = invComp.getEquippedType();

        switch (playerInventory.getEquipped()) {
            // melee/ranged
            case "melee", "ranged":
                if (button == Input.Buttons.LEFT) {
                    entity.getEvents().trigger("weaponAttack", weapon, clickPos);
                }
                break;
            // building
            case BUILDING:
                if (button == Input.Buttons.LEFT) {
                    entity.getEvents().trigger("place", screenX, screenY);
                } else if (button == Input.Buttons.RIGHT) {
                    entity.getEvents().trigger("remove", screenX, screenY);
                }
                entity.getEvents().trigger("weaponPlace", weapon, clickPos);
                break;

            default:
                return false;
        }
        return true;
    }

    /**
     * Update location of known mouse position
     *
     * @param screenX, screenY Location of mouse press
     * @return - false
     */
    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        mouseToGamePos(screenX, screenY);
        return false;
    }

    /**
     * Update location of known mouse position
     *
     * @param screenX, screenY Location of mouse press
     * @return - false
     */
    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        mouseToGamePos(screenX, screenY);
        return false;
    }

    /**
     * Returns the direction of the player.
     *
     * @return Vector2
     */
    public Vector2 getDirection() {
        return this.walkDirection;
    }

    /**
     * Triggers walk event
     * and also trigger the walking sound
     */
    void triggerWalkEvent() {
        Entity companion = ServiceLocator.getEntityService().getCompanion();
        Vector2 lastDir = this.walkDirection.cpy();
        this.walkDirection = keysToVector().scl(WALK_SPEED);
        if (this.getTesting() == 0) {
            Directions dir = keysToDirection();
            if (dir == Directions.NONE) {
                entity.getEvents().trigger(WALKSTOP);
                entity.getEvents().trigger("walkStopAnimation", lastDir);

                entity.getEvents().trigger("stopSound", "footstep");
                return;
            }

            switch (dir) {
                case UP -> {
                    entity.getEvents().trigger("walkUp");
                    companion.getEvents().trigger("walkUp");
                }
                case DOWN -> {
                    entity.getEvents().trigger("walkDown");
                    companion.getEvents().trigger("walkDown");
                }
                case LEFT -> {
                    entity.getEvents().trigger("walkLeft");
                    companion.getEvents().trigger("walkLeft");
                }
                case RIGHT -> {
                    entity.getEvents().trigger("walkRight");
                    companion.getEvents().trigger("walkRight");
                }
                case UPLEFT -> {
                    entity.getEvents().trigger("walkUpLeft");
                    companion.getEvents().trigger("walkUpLeft");
                }
                case UPRIGHT -> {
                    entity.getEvents().trigger("walkUpRight");
                    companion.getEvents().trigger("walkUpRight");
                }
                case DOWNLEFT -> {
                    entity.getEvents().trigger("walkDownLeft");
                    companion.getEvents().trigger("walkDownLeft");
                }
                case DOWNRIGHT -> {
                    entity.getEvents().trigger("walkDownRight");
                    companion.getEvents().trigger("walkDownRight");
                }
                default -> entity.getEvents().trigger(WALKSTOP);
            }
            if (entity != null) {
                entity.getEvents().trigger("walk", walkDirection);

                // play the sound when player starts walking
                entity.getEvents().trigger("loopSound", "footstep");

            }
        }
    }

    /**
     * Triggers dodge event.
     * Immunity is applied for 200 milliseconds whilst player moves.
     *
     * @return
     */
    public int triggerDodgeEvent() {

        final Timer timer = new Timer();
        this.walkDirection = keysToVector().scl(DODGE_SPEED);
        Directions dir = keysToDirection();

        if (dir == Directions.NONE) {
            entity.getEvents().trigger(WALKSTOP);
            return DODGE_COOLDOWN;
        }
        switch (dir) {
            case UP -> entity.getEvents().trigger("dodgeUp");
            case DOWN -> entity.getEvents().trigger("dodgeDown");
            case LEFT, DOWNLEFT, UPLEFT -> entity.getEvents().trigger("dodgeLeft");
            case RIGHT, DOWNRIGHT, UPRIGHT -> entity.getEvents().trigger("dodgeRight");
            default -> entity.getEvents().trigger(WALKSTOP);
        }
        if (entity != null) {
            entity.getEvents().trigger("walk", walkDirection);
            entity.getEvents().trigger("dodged");

            // play the sound when player starts dodging
            entity.getEvents().trigger("playSound", "dodge");
        }

        java.util.TimerTask stopDodge = new java.util.TimerTask() {
            @Override
            public void run() {
                triggerWalkEvent();
                entity.getEvents().trigger("dodged");
                timer.cancel();
                timer.purge();
            }
        };
        timer.schedule(stopDodge, DODGE_DURATION);
        return DODGE_DURATION;
    }

    /**
     * Triggers inventory events
     */
    private void triggerInventoryEvent(String slot) {
        InventoryComponent invComp = entity.getComponent(InventoryComponent.class);
        invComp.setEquipped(slot);
        player.getEvents().trigger("updateHotbar");
        entity.getEvents().trigger(CHANGEWEAPON, invComp.getEquippedType());
        entity.getEvents().trigger("updateAmmo", invComp.getCurrentAmmo(),
                invComp.getCurrentMaxAmmo(), invComp.getCurrentAmmoUse());
    }

    /**
     * Responsible for dodge action
     * Triggers when the spacer is clicked.
     * Cool down of 3000 ms.
     */
    public void dodge() {
        dodgeAvailable = false;
        final Timer timer = new Timer();
        java.util.TimerTask makeDodgeAvailable = new java.util.TimerTask() {
            @Override
            public void run() {
                dodgeAvailable = true;
                entity.getEvents().trigger("dodgeAvailable");
                timer.cancel();
                timer.purge();
            }
        };
        timer.schedule(makeDodgeAvailable, DODGE_COOLDOWN);
    }

    /**
     * FUnction to known if a key is currently being pressed
     *
     * @param keycode - key to test if pressed
     * @return true if the key is pressed otherwise false
     */
    private boolean isPressed(int keycode) {
        return keyFlags.getOrDefault(keycode, 0) == 1;
    }

    /**
     * Function to convert a mouse position to a game location
     *
     * @param screenX x of mouse location
     * @param screenY y of mouse location
     * @return game position of mouse location
     */
    private Vector2 mouseToGamePos(int screenX, int screenY) {
        this.lastMousePos = ServiceLocator.getTerrainService()
                .screenCoordsToGameCoords(screenX, screenY).scl(0.5f);
        return this.lastMousePos.cpy();
    }

    /**
     * returns a scaled vector the direction should be moving based on current key
     * presses
     *
     * @return direction play should move
     */
    private Vector2 keysToVector() {
        float xCom = (isPressed(Keys.D) ? Vector2Utils.RIGHT.x : 0f) + (isPressed(Keys.A) ? Vector2Utils.LEFT.x : 0f);
        float yCom = (isPressed(Keys.W) ? Vector2Utils.UP.y : 0f) + (isPressed(Keys.S) ? Vector2Utils.DOWN.y : 0f);
        float mag = (Math.abs(Math.abs(xCom) - Math.abs(yCom)) < 0.1f ? ROOT2INV : 1f);
        return new Vector2(xCom, yCom).scl(mag);
    }

    /**
     * Convert the keys currently being pressed to a direction
     *
     * @return direction enum based on current key pressed
     */
    private Directions keysToDirection() {
        int dirFlags = 0b0101 +
                ((isPressed(Keys.W) ? 1 : 0) << 2) - ((isPressed(Keys.S) ? 1 : 0) << 2) +
                (isPressed(Keys.D) ? 1 : 0) - (isPressed(Keys.A) ? 1 : 0);
        return switch (dirFlags) {
            case 0b1001 -> Directions.UP;
            case 0b1010 -> Directions.UPRIGHT;
            case 0b1000 -> Directions.UPLEFT;
            case 0b0001 -> Directions.DOWN;
            case 0b0010 -> Directions.DOWNRIGHT;
            case 0b0000 -> Directions.DOWNLEFT;
            case 0b0110 -> Directions.RIGHT;
            case 0b0100 -> Directions.LEFT;
            default -> Directions.NONE;
        };

    }

    /**
     * Enum of each possible direction including no direction
     */
    private enum Directions {
        NONE,
        UP,
        DOWN,
        LEFT,
        RIGHT,
        UPLEFT,
        UPRIGHT,
        DOWNLEFT,
        DOWNRIGHT
    }
}
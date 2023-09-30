package com.csse3200.game.components.player;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.Weapons.WeaponType;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.WeaponConfig;
import com.csse3200.game.entities.configs.WeaponConfigs;
import com.csse3200.game.input.InputComponent;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.utils.math.Vector2Utils;

import java.util.HashMap;
import java.util.Timer;


/**d
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
                if (playerInventory.getEquipped().equals("building")) {
                    entity.getEvents().trigger("change_structure");
                }
                return true;
            }
            case Keys.NUM_1 -> {
                triggerInventoryEvent("melee");
                return true;
            }
            case Keys.NUM_2 -> {
                triggerInventoryEvent("ranged");
                return true;
            }
            case Keys.NUM_3 -> {
                triggerInventoryEvent("building");
                return true;
            }
            case Keys.W, Keys.S, Keys.A, Keys.D -> {
                triggerWalkEvent();
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
   * @param screenX - X position on screen that mouse was pressed
   * @param screenY - Y position on screen that mouse was pressed
   * @param pointer -
   * @param button  - Button that pas pressed
   * @return - True or false based on if an action occurred
   */
  @Override
  public boolean touchDown(int screenX, int screenY, int pointer, int button) {
    Vector2 position = mouseToGamePos(screenX, screenY);
    this.lastMousePos = position.cpy();
    player = ServiceLocator.getEntityService().getPlayer();
    playerInventory = player.getComponent(InventoryComponent.class);
    WeaponComponent weaponComponent = entity.getComponent(WeaponComponent.class);
    int cooldown = playerInventory.getEquippedCooldown();
    if (cooldown > 0) {
      return false;
    }

    switch (playerInventory.getEquipped()) {
      // melee/ranged

        case "melee", "ranged":
            System.out.println(playerInventory.getCurrentAmmo());
            if (playerInventory.getCurrentAmmo() <= 0) {return false;}

            if (button == Input.Buttons.LEFT) {
                InventoryComponent invComp = entity.getComponent(InventoryComponent.class);
                WeaponType weapon = invComp.getEquippedType();

                if (weapon == WeaponType.MELEE_BEE_STING) {
                    for (int i = 0; i < 7; i++)
                        entity.getEvents().trigger("weaponAttack", weapon, position);
                }
                entity.getEvents().trigger("weaponAttack", weapon, position);
            }
            break;
      // building
      case "building":
        if (button == Input.Buttons.LEFT) {
          entity.getEvents().trigger("place", screenX, screenY);
        } else if (button == Input.Buttons.RIGHT) {
          entity.getEvents().trigger("remove", screenX, screenY);
        }
        break;

      default:
        return false;
    }
    return true;
  }

  /**
   * Update location of known mouse position
   * @param screenX, screenY Location of mouse press
   * @return - false
   */
  @Override
  public boolean touchDragged(int screenX, int screenY, int pointer) {
    Vector2 position = mouseToGamePos(screenX, screenY);
    this.lastMousePos = position.cpy();
    return false;
  }

    /**
     * Update location of known mouse position
     * @param screenX, screenY Location of mouse press
     * @return - false
     */
    @Override
  public boolean mouseMoved(int screenX, int screenY) {
    Vector2 position = mouseToGamePos(screenX, screenY);
    this.lastMousePos = position.cpy();
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
  private void triggerWalkEvent() {
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
          case UP -> {entity.getEvents().trigger("walkUp");
            companion.getEvents().trigger("walkUp");}
          case DOWN -> {entity.getEvents().trigger("walkDown");
            companion.getEvents().trigger("walkDown");}
          case LEFT -> {
            entity.getEvents().trigger("walkLeft");
            companion.getEvents().trigger("walkLeft");}
          case RIGHT -> {entity.getEvents().trigger("walkRight");
            companion.getEvents().trigger("walkRight");}
          case UPLEFT -> {entity.getEvents().trigger("walkUpLeft");
            companion.getEvents().trigger("walkUpLeft");}
          case UPRIGHT -> {entity.getEvents().trigger("walkUpRight");
          companion.getEvents().trigger("walkUpRight");}
          case DOWNLEFT ->{ entity.getEvents().trigger("walkDownLeft");
            companion.getEvents().trigger("walkDownLeft");}
          case DOWNRIGHT ->{ entity.getEvents().trigger("walkDownRight");
            companion.getEvents().trigger("walkDownLeft");}
          default -> entity.getEvents().trigger(WALKSTOP);
      }
      entity.getEvents().trigger("walk", walkDirection);

      // play the sound when player starts walking
        entity.getEvents().trigger("loopSound", "footstep");
    }
  }

  /**
   * Triggers dodge event.
   * Immunity is applied for 200 milliseconds whilst player moves.
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

    entity.getEvents().trigger("walk", walkDirection);
    entity.getEvents().trigger("dodged");

//  play the sound when player starts dodging
    entity.getEvents().trigger("playSound", "dodge");

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
    entity.getEvents().trigger("updateAmmo", invComp.getCurrentAmmo(), invComp.getCurrentMaxAmmo());
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
     * @param keycode - key to test if pressed
     * @return true if the key is pressed otherwise false
     */
  private boolean isPressed(int keycode) {
    return keyFlags.getOrDefault(keycode, 0) == 1;
  }

    /**
     * Function to convert a mouse position to a game location
     * @param screenX x of mouse  location
     * @param screenY y of mouse location
     * @return game position of mouse location
     */
  private Vector2 mouseToGamePos(int screenX, int screenY) {
    Vector2 entityScale = entity.getScale();
    Vector2 mouse = ServiceLocator.getTerrainService().ScreenCoordsToGameCoords(screenX, screenY);
    return new Vector2(mouse.x / 2 - entityScale.x / 2, (mouse.y) / 2 - entityScale.y / 2);
  }

    /**
     * returns a scaled vector the direction should be moving based on current key presses
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
     * @return direction enum based on current key pressed
     */
  private Directions keysToDirection() {
    int dirFlags =  0b0101 +
            ((isPressed(Keys.W) ? 1 : 0) << 2) - ((isPressed(Keys.S) ? 1 : 0) << 2) +
            (isPressed(Keys.D) ? 1 : 0)      - (isPressed(Keys.A) ? 1 : 0);
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

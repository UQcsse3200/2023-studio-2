package com.csse3200.game.components.player;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.csse3200.game.input.InputComponent;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.utils.math.Vector2Utils;

//Testing:
import com.csse3200.game.components.Weapons.WeaponType;

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
  private static final int DODGE_COOLDOWN = 300;
  private static final int DODGE_DURATION = 300;

  private Vector2 walkDirection = Vector2.Zero.cpy();
  private boolean dodge_available = true;

  private int equippedItem = 1;
  private int testing = 0;

  static HashMap<Integer, Integer> keyFlags = new HashMap<Integer, Integer>();
  Vector2 lastMousePos = new Vector2(0, 0);

  /**
   * Checks if any Window is currently open and visible on the stage.
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

  /**
   *
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


  public KeyboardPlayerInputComponent() {
    super(5);
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


    switch (keycode) {
      case Keys.SPACE:
        if (!dodge_available ||
                walkDirection.epsilonEquals(Vector2.Zero)) { return false; }
        triggerDodgeEvent();
        dodge();
        return true;

      case Keys.F:
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
      case Keys.NUM_1:
        triggerInventoryEvent(1);
        return true;
      case Keys.NUM_2:
        triggerInventoryEvent(2);
        return true;
      case Keys.NUM_3:
        triggerInventoryEvent(3);
        return true;
      case Keys.TAB:
        triggerInventoryEvent(0);
        return true;
      case Keys.W, Keys.S, Keys.A, Keys.D:
        triggerWalkEvent();
        return true;

      default:
        return false;
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
    switch (keycode) {
      case Keys.W, Keys.S, Keys.A, Keys.D:
        triggerWalkEvent();
        return true;
      default:
        return false;
    }
  }

  /**
   * TODO this is barely works
   * Function to repond to player mouse press
   *
   * @param screenX - X position on screen that mouse was pressed
   * @param screenY - Y position on screen that mouse was pressed
   * @param pointer -
   * @param button  - Button that pas pressed
   * @return - True or false based on if an acction occured
   */
  @Override
  public boolean touchDown(int screenX, int screenY, int pointer, int button) {
    Vector2 position = mouseToGamePos(screenX, screenY);
    this.lastMousePos = position.cpy();
    PlayerActions playerActions = entity.getComponent(PlayerActions.class);
    int cooldown = playerActions.getAttackCooldown();

    if (cooldown != 0) {
      return false;
    }

    double initRot = calcRotationAngleInDegrees(entity.getPosition(), position);

    switch (equippedItem) {
      // melee
      case 1:
        if (button == Input.Buttons.LEFT) {
          entity.getEvents().trigger("weaponAttack", entity.getPosition(), WeaponType.ELEC_WRENCH, (float) initRot);
        }
        break;

      // ranged
      case 2:
        if (button == Input.Buttons.LEFT) {
          entity.getEvents().trigger("weaponAttack", entity.getPosition(), WeaponType.THROW_ELEC_WRENCH, (float) initRot);
        }
        break;

      // building
      case 3:
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

  public boolean touchDragged(int screenX, int screenY, int pointer) {
    Vector2 position = mouseToGamePos(screenX, screenY);
    this.lastMousePos = position.cpy();
    return false;
  }

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
   */
  private void triggerWalkEvent() {
    Vector2 lastDir = this.walkDirection.cpy();
    this.walkDirection = keysToVector().scl(WALK_SPEED);
    if (this.getTesting() == 0) {
      directions dir = keysToDirection();
      if (dir == directions.None) {
        entity.getEvents().trigger("walkStop");
        entity.getEvents().trigger("walkStopAnimation", lastDir);
        return;
      }

      switch (dir) {
        case Up -> entity.getEvents().trigger("walkUp");
        case Down -> entity.getEvents().trigger("walkDown");
        case Left -> entity.getEvents().trigger("walkLeft");
        case Right -> entity.getEvents().trigger("walkRight");
        case UpLeft -> entity.getEvents().trigger("walkUpLeft");
        case UpRight -> entity.getEvents().trigger("walkUpRight");
        case DownLeft -> entity.getEvents().trigger("walkDownLeft");
        case DownRight -> entity.getEvents().trigger("walkDownRight");
      }
      entity.getEvents().trigger("walk", walkDirection);
    }
  }

  /**
   * Triggers dodge event.
   * Immunity is applied for 200 milliseconds whilst player moves.
   */
  private void triggerDodgeEvent() {
    final Timer timer = new Timer();
    this.walkDirection = keysToVector().scl(DODGE_SPEED);
    directions dir = keysToDirection();

    if (dir == directions.None) {
      entity.getEvents().trigger("walkStop");
      return;
    }
    switch (dir) {
      case Up -> entity.getEvents().trigger("dodgeUp");
      case Down -> entity.getEvents().trigger("dodgeDown");
      case Left, DownLeft, UpLeft -> entity.getEvents().trigger("dodgeLeft");
      case Right, DownRight, UpRight -> entity.getEvents().trigger("dodgeRight");
    }

    entity.getEvents().trigger("walk", walkDirection);
    entity.getEvents().trigger("dodged");

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
  }

  
  /** 
   * Triggers inventory events
   * @param i - sets inventory event type
   */
  private void triggerInventoryEvent(int i) {
    entity.getEvents().trigger("inventory", i);
    this.equippedItem = entity.getComponent(InventoryComponent.class).getEquipped();
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
    timer.schedule(makeDodgeAvailable, DODGE_COOLDOWN);
  }

  /**
   * Calcuate angle between 2 points from the center point to the target point,
   * angle is
   * in degrees with 0degrees being in the direction of the positive x-axis going
   * counter clockwise
   * up to 359.9... until wrapping back around
   * 
   * @param centerPt - point from where angle is calculated from
   * @param targetPt - Tart point to where angle is calculated to
   * @return return angle between points in degrees from the positive x-axis
   */
  // https://stackoverflow.com/questions/9970281/java-calculating-the-angle-between-two-points-in-degrees
  private double calcRotationAngleInDegrees(Vector2 centerPt, Vector2 targetPt) {
    double angle = Math.toDegrees(Math.atan2(targetPt.y - centerPt.y, targetPt.x - centerPt.x));
    if (angle < 0) {
      angle += 360;
    }
    return angle;
  }

  private boolean is_pressed(int keycode) {
    return keyFlags.getOrDefault(keycode, 0) == 1;
  }

  // TODO this code needs to be looked over
  private Vector2 mouseToGamePos(int screenX, int screenY) {
    Vector2 entityScale = entity.getScale();
    Vector2 mouse = ServiceLocator.getTerrainService().ScreenCoordsToGameCoords(screenX, screenY);
    return new Vector2(mouse.x / 2 - entityScale.x / 2, (mouse.y) / 2 - entityScale.y / 2);
  }

  private Vector2 keysToVector() {
    float xCom = (is_pressed(Keys.D) ? Vector2Utils.RIGHT.x : 0f) + (is_pressed(Keys.A) ? Vector2Utils.LEFT.x : 0f);
    float yCom = (is_pressed(Keys.W) ? Vector2Utils.UP.y : 0f) + (is_pressed(Keys.S) ? Vector2Utils.DOWN.y : 0f);
    float mag = (Math.abs(Math.abs(xCom) - Math.abs(yCom)) < 0.1f ? ROOT2INV : 1f);
    return new Vector2(xCom, yCom).scl(mag);
  }

  private directions keysToDirection() {
    int dirFlags =  0b0101 +
            ((is_pressed(Keys.W) ? 1 : 0) << 2) - ((is_pressed(Keys.S) ? 1 : 0) << 2) +
            ((is_pressed(Keys.D) ? 1 : 0))      - ((is_pressed(Keys.A) ? 1 : 0));
      return switch (dirFlags) {
          case 0b1001 -> directions.Up;
          case 0b1010 -> directions.UpRight;
          case 0b1000 -> directions.UpLeft;
          case 0b0001 -> directions.Down;
          case 0b0010 -> directions.DownRight;
          case 0b0000 -> directions.DownLeft;
          case 0b0110 -> directions.Right;
          case 0b0100 -> directions.Left;
          default -> directions.None;
      };
  }

  private enum directions {
    None,
    Up,
    Down,
    Left,
    Right,
    UpLeft,
    UpRight,
    DownLeft,
    DownRight
  }
}

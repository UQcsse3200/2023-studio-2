package com.csse3200.game.components.player;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.input.InputComponent;
import com.csse3200.game.utils.math.Vector2Utils;
import com.csse3200.game.services.ServiceLocator;

//Testing:
import com.csse3200.game.components.Weapons.WeaponType;

/**
 * Input handler for the player for keyboard and touch (mouse) input.
 * This input handler only uses keyboard input.
 */
public class KeyboardPlayerInputComponent extends InputComponent {

  private final Vector2 walkDirection = Vector2.Zero.cpy();

  private int flagW = 0;
  private int flagA = 0;
  private int flagS = 0;
  private int flagD = 0;
  private int flagMul = 0;

  /**
   * @return int
   */
  private int movFlagSum() {
    return flagW + flagA + flagS + flagD;
  }

  private boolean dodge_available = true;
  private boolean dodged_up = false;
  private boolean dodged_left = false;
  private boolean dodged_down = false;
  private boolean dodged_right = false;

  /**
   * Triggers when keys are pressed or released.
   * Responsible for Diagonal movement when specific keys are pressed
   */
  private void diagonal() {
    int movFlagSum = movFlagSum();
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
    diagonal();
    switch (keycode) {
      case Keys.W:
        flagW = 1;
        if (movFlagSum() == 1) {
          walkDirection.scl(0);
          walkDirection.add(Vector2Utils.UP);
          entity.getEvents().trigger("walkUp");
        } else {
          walkDirection.add(Vector2Utils.UP);
          diagonal();
        }
        triggerWalkEvent();
        return true;
      case Keys.A:
        flagA = 1;
        if (movFlagSum() == 1) {
          walkDirection.scl(0);
          walkDirection.add(Vector2Utils.LEFT);
          entity.getEvents().trigger("walkLeft");
        } else {
          walkDirection.add(Vector2Utils.LEFT);
          diagonal();
        }
        triggerWalkEvent();
        return true;
      case Keys.S:
        flagS = 1;
        if (movFlagSum() == 1) {
          walkDirection.scl(0);
          walkDirection.add(Vector2Utils.DOWN);
          entity.getEvents().trigger("walkDown");
        } else {
          walkDirection.add(Vector2Utils.DOWN);
          diagonal();
        }
        triggerWalkEvent();
        return true;
      case Keys.D:
        flagD = 1;
        if (movFlagSum() == 1) {
          walkDirection.scl(0);
          walkDirection.add(Vector2Utils.RIGHT);
          entity.getEvents().trigger("walkRight");
        } else {
          walkDirection.add(Vector2Utils.RIGHT);
          diagonal();
        }
        triggerWalkEvent();
        return true;
      case Keys.P:
        entity.getEvents().trigger("attack");
        System.out.println("Attack");
        if (flagW == 1) {
          System.out.println("Multiple");
        }
        return true;
      case Keys.SPACE:
        if (dodge_available) {
          if (flagW == 1) {
            walkDirection.add(Vector2Utils.DODGE_UP);
            dodged_up = true;
          } else if (flagA == 1) {
            walkDirection.add(Vector2Utils.DODGE_LEFT);
            dodged_left = true;
          } else if (flagS == 1) {
            walkDirection.add(Vector2Utils.DODGE_DOWN);
            dodged_down = true;
          } else {
            walkDirection.add(Vector2Utils.DODGE_RIGHT);
            dodged_right = true;
          }
          triggerDodgeEvent();
        }
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
    switch (keycode) {
      case Keys.W:
        flagW = 0;
        diagonal();
        if (movFlagSum() == 2) {
          diagonal();
        }
        if (movFlagSum() == 0) {
          walkDirection.scl(0);
        }
        triggerWalkEvent();
        return true;
      case Keys.A:
        flagA = 0;
        diagonal();
        if (movFlagSum() == 2) {
          diagonal();
        }
        if (movFlagSum() == 0) {
          walkDirection.scl(0);
        }
        triggerWalkEvent();
        return true;
      case Keys.S:
        flagS = 0;
        diagonal();
        if (movFlagSum() == 2) {
          diagonal();
        }
        if (movFlagSum() == 0) {
          walkDirection.scl(0);
        }
        triggerWalkEvent();
        return true;
      case Keys.D:
        flagD = 0;
        diagonal();
        if (movFlagSum() == 2) {
          diagonal();
        }
        if (movFlagSum() == 0) {
          walkDirection.scl(0);
        }
        triggerWalkEvent();
        return true;
      case Keys.SPACE:
        if (dodge_available) {
          if (dodged_up) {
            walkDirection.sub(Vector2Utils.DODGE_UP);
            dodged_up = false;
          } else if (dodged_left) {
            walkDirection.sub(Vector2Utils.DODGE_LEFT);
            dodged_left = false;
          } else if (dodged_down) {
            walkDirection.sub(Vector2Utils.DODGE_DOWN);
            dodged_down = false;
          } else if (dodged_right) {
            walkDirection.sub(Vector2Utils.DODGE_RIGHT);
            dodged_right = false;
          }
          triggerWalkEvent();
          dodge();
        }
      default:
        return false;
    }
  }

  /**
   * Triggers walk event
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
    entity.getEvents().trigger("walk", walkDirection);
    entity.getEvents().trigger("dodged");
    java.util.TimerTask stopDodge = new java.util.TimerTask() {
      @Override
      public void run() {
        entity.getEvents().trigger("walkStop");
        entity.getEvents().trigger("dodged");
      }
    };
    new java.util.Timer().schedule(stopDodge, 200);
  }

  /**
   * Responsible for dodge action
   * Triggers when the spacebar is clicked.
   * Cooldown of 3s.
   */
  private void dodge() {
    dodge_available = false;
    java.util.TimerTask makeDodgeAvailable = new java.util.TimerTask() {
      @Override
      public void run() {
        dodge_available = true;
      }
    };
    new java.util.Timer().schedule(makeDodgeAvailable, 3000);
  }

  /** TODO this is barely works
   * Function to repond to player mouse press
   * @param screenX - X position on screen that mouse was pressed
   * @param screenY - Y position on screen that mouse was pressed
   * @param pointer -
   * @param button  - Button that pas pressed
   * @return - True or false based on if an acction occured
   */
  @Override
  public boolean touchDown(int screenX, int screenY, int pointer, int button) {
    if(button != Input.Buttons.LEFT && button != Input.Buttons.RIGHT) {
      return false;
    }

    Vector2 mouse = ServiceLocator.getTerrainService().ScreenCoordsToGameCoords(screenX, screenY);
    //Problems with screen to game coord - this is a temporary fix
    Vector2 entityScale = entity.getScale();
    Vector2 position = new Vector2(mouse.x/2 - entityScale.x/2, (mouse.y) / 2 - entityScale.y/2);
    double initRot = calcRotationAngleInDegrees(entity.getPosition(), position);

    if(button == Input.Buttons.LEFT){
      entity.getEvents().trigger("weaponAttack", entity.getPosition(), WeaponType.ELEC_WRENCH, (float) initRot);
    } else if (button == Input.Buttons.RIGHT) {
      entity.getEvents().trigger("weaponAttack", entity.getPosition(), WeaponType.THROW_ELEC_WRENCH, (float) initRot);
    }

    return true;
  }

  /**
   * Calcuate angle between 2 points from the center point to the target point, angle is
   * in degrees with 0degrees being in the direction of the positive x-axis going counter clockwise
   * up to 359.9... until wrapping back around
   * @param centerPt - point from where angle is calculated from
   * @param targetPt - Tart point to where angle is calculated to
   * @return return angle between points in degrees from the positive x-axis
   */
  //https://stackoverflow.com/questions/9970281/java-calculating-the-angle-between-two-points-in-degrees
  private double calcRotationAngleInDegrees(Vector2 centerPt, Vector2 targetPt)  {
    double angle = Math.toDegrees(Math.atan2(targetPt.y - centerPt.y, targetPt.x - centerPt.x));
    if (angle < 0) {
      angle += 360;
    }
    return angle;
  }
}
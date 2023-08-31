package com.csse3200.game.components.player;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.csse3200.game.input.InputComponent;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.utils.math.Vector2Utils;
import com.csse3200.game.services.ServiceLocator;


//Testing:
import com.csse3200.game.components.Weapons.WeaponType;

import java.util.Timer;


/**
 * Input handler for the player for keyboard and touch (mouse) input.
 * This input handler only uses keyboard input.
 */
public class KeyboardPlayerInputComponent extends InputComponent {

  private final Vector2 walkDirection = Vector2.Zero.cpy();
  private boolean dodge_available = true;
  private int flagW = 0;
  private int flagA = 0;
  private int flagS = 0;
  private int flagD = 0;
  private int flagMul = 0;

  private int testing = 0;

  /** 
   * Returns value for testing.
   * @return int
   */
  public int getTesting() {
    return testing;
  }

  /** 
   * Sets value for testing.
   * @param testing
   */
  public void setTesting(int testing) {
    this.testing = testing;
  }

  private boolean leftCtrlFlag = false;

  /**
   * @return int
   */
  private int getMovFlagSum() {
    return flagW + flagA + flagS + flagD;
  }

  /**
   * Triggers when keys are pressed or released.
   * Responsible for Diagonal movement when specific keys are pressed
   */
  private void diagonal() {
    int movFlagSum = getMovFlagSum();
    if (movFlagSum >= 3){
      walkDirection.set(Vector2.Zero);
    }
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
        if (getMovFlagSum() == 1) {
          walkDirection.scl(0);
          walkDirection.add(Vector2Utils.UP);
        } else {
          walkDirection.add(Vector2Utils.UP);
          diagonal();
        }
        triggerWalkEvent();
        return true;
      case Keys.A:
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
      case Keys.S:
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
      case Keys.D:
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
      case Keys.P:
        entity.getEvents().trigger("attack");
        if (flagW == 1) {  
        }
        return true;
      case Keys.CONTROL_LEFT:
        leftCtrlFlag = true;
        return true;
      case Keys.SPACE:
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
      case Keys.F:
        InteractionControllerComponent interactionController = entity.getComponent(InteractionControllerComponent.class);
        if (interactionController != null) {
          interactionController.interact();
        }
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
    switch (keycode) {
      case Keys.W:
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
      case Keys.A:
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
      case Keys.S:
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
      case Keys.D:
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
      case Keys.CONTROL_LEFT:
        leftCtrlFlag = false;
        return true;
      default:
        return false;
    }
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
    if(button == Input.Buttons.LEFT){
      if (leftCtrlFlag) {
        entity.getEvents().trigger("ctrl_place", screenX, screenY);
      } else {
        entity.getEvents().trigger("place", screenX, screenY);
      }
      return true;
    }
    if (button == Input.Buttons.RIGHT) {
      entity.getEvents().trigger("remove", screenX, screenY);
      return true;
    }


    Vector2 mouse = ServiceLocator.getTerrainService().ScreenCoordsToGameCoords(screenX, screenY);
    //Problems with screen to game coord - this is a temporary fix
    Vector2 entityScale = entity.getScale();
    Vector2 position = new Vector2(mouse.x/2 - entityScale.x/2, (mouse.y) / 2 - entityScale.y/2);
    double initRot = calcRotationAngleInDegrees(entity.getPosition(), position);

    if((button == Input.Buttons.MIDDLE) && this.flagD != 1){
      entity.getEvents().trigger("weaponAttack", entity.getPosition(), WeaponType.ELEC_WRENCH, (float) initRot);
    }

    if (button == Input.Buttons.MIDDLE && this.flagD == 1) {
     entity.getEvents().trigger("weaponAttack", entity.getPosition(), WeaponType.THROW_ELEC_WRENCH, (float) initRot);
    }


    return true;
  }

  
  /** 
   * Returns the direction of the player.
   * @return Vector2
   */
  public Vector2 getDirection(){
    return this.walkDirection;
  }

  /**
   * Triggers walk event
   */
  private void triggerWalkEvent() {
    if (this.getTesting() == 0){
      if (walkDirection.epsilonEquals(Vector2.Zero)) {
        entity.getEvents().trigger("walkStop");
      } else {
        if (walkDirection.epsilonEquals(Vector2Utils.UP_LEFT)) {
          entity.getEvents().trigger("walkUpLeft");  
        }
        else if (walkDirection.epsilonEquals(Vector2Utils.UP_RIGHT)) {
          entity.getEvents().trigger("walkUpRight");
        }
        else if (walkDirection.epsilonEquals(Vector2Utils.UP)) {
          entity.getEvents().trigger("walkUp");
        }
        else if (walkDirection.epsilonEquals(Vector2Utils.DOWN)) {
          entity.getEvents().trigger("walkDown");
        }
        else if (walkDirection.epsilonEquals(Vector2Utils.DOWN_LEFT)) {
          entity.getEvents().trigger("walkDownLeft");
        }
        else if (walkDirection.epsilonEquals(Vector2Utils.DOWN_RIGHT)) {
          entity.getEvents().trigger("walkDownRight");
        }
        else if (walkDirection.epsilonEquals(Vector2Utils.LEFT)) {
          entity.getEvents().trigger("walkLeft");
        }
        else if (walkDirection.epsilonEquals(Vector2Utils.RIGHT)) {
          entity.getEvents().trigger("walkRight");  
        }
        entity.getEvents().trigger("walk", walkDirection);
      }
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

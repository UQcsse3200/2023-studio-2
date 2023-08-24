package com.csse3200.game.components.player;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.input.InputComponent;
import com.csse3200.game.utils.math.Vector2Utils;

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
  private int movFlagSum(){
    return flagW + flagA + flagS + flagD;
  }
  private boolean dodge_available = true;
  private boolean dodged_up = false;
  private boolean dodged_left = false;
  private boolean dodged_down = false;
  private boolean dodged_right = false;
  private void diagonal(){
    int movFlagSum = movFlagSum();
    if (movFlagSum == 2) {
      flagMul = 1;
      walkDirection.scl(new Vector2(0.707f,0.707f));
    }else if (movFlagSum == 1){
      if (flagW == 1){
        walkDirection.set(Vector2Utils.UP);
      }else if (flagA == 1){
        walkDirection.set(Vector2Utils.LEFT);
      }else if (flagS == 1){
        walkDirection.set(Vector2Utils.DOWN);
      }else if (flagD == 1){
        walkDirection.set(Vector2Utils.RIGHT);
      }
      triggerWalkEvent();
    }else if (movFlagSum == 0){
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
        }else{
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
        }else{
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
        }else{
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
        }else{
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

  private void triggerWalkEvent() {
    if (walkDirection.epsilonEquals(Vector2.Zero)) {
      entity.getEvents().trigger("walkStop");
    } else {
      entity.getEvents().trigger("walk", walkDirection);
    }
  }
  private void triggerDodgeEvent() {
    entity.getEvents().trigger("walk", walkDirection);
    java.util.TimerTask stopDodge = new java.util.TimerTask() {
      @Override
      public void run() {
        entity.getEvents().trigger("walkStop");
      }
    };
    new java.util.Timer().schedule(stopDodge, 100);
  }
  private void dodge() {
    dodge_available = false;
    java.util.TimerTask makeDodgeAvailable = new java.util.TimerTask() {
      @Override
      public void run() {
        entity.getEvents().trigger("dodge");
        dodge_available = true;
      }
    };
    new java.util.Timer().schedule(makeDodgeAvailable, 3000);
  }
}
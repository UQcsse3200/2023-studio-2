package com.csse3200.game.components.Car;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import com.csse3200.game.components.Component;
import com.csse3200.game.components.player.KeyboardPlayerInputComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.components.PhysicsComponent;

public class CarActions extends Component {
    private static final Vector2 MAX_SPEED = new Vector2(5f, 5f);
    private Entity player;
    private PhysicsComponent physicsComponent;
    private Vector2 walkDirection = Vector2.Zero.cpy();
    private float prevDirection = 300;

    private static final float TIME_STEP = 1.0f / 60.0f;

    private boolean isMoving = false;
    private boolean isSilent = false;
    private Control mode = Control.normal;

    @Override
    public void create() {
        physicsComponent = entity.getComponent(PhysicsComponent.class);
        entity.getEvents().addListener("walk", this::walk);
        entity.getEvents().addListener("walkStop", this::stopMoving);
        entity.getEvents().addListener("exitCar", this::exitCar);
    }

    @Override
    public void update() {
        if (!isSilent) { // Changed variable name from isMuted to isSilent
            if (isMoving) {
                updateSpeed();
            }
            updateAnimation();
        }
    }

    private void updateAnimation() {
        if (walkDirection.epsilonEquals(Vector2.Zero)) {
            entity.getEvents().trigger("stopMoving", getDirection(prevDirection), mode.toString());
        } else {
            entity.getEvents().trigger("startMoving", getDirection(walkDirection.angleDeg()), mode.toString());
        }
    }

    private String getDirection(float direction) {
        int sector = (int) ((direction + 45) / 90) % 4;

        switch (sector) {
            case 0:
                return "right";
            case 1:
                return "up";
            case 2:
                return "left";
            case 3:
                return "down";
            default:
                return "right"; // Default case in case direction is out of range
        }
    }


    private void updateSpeed() {
        Body body = physicsComponent.getBody();
        Vector2 velocity = body.getLinearVelocity();
        Vector2 desiredVelocity = walkDirection.cpy().scl(MAX_SPEED);
        Vector2 force = desiredVelocity.sub(velocity).scl(body.getMass() / TIME_STEP); // TIME_STEP is the time step of your physics simulation
        body.applyForceToCenter(force, true);
    }


    void walk(Vector2 direction) {
        this.walkDirection = direction;
        this.prevDirection = walkDirection.angleDeg();
        this.isMoving = true;
    }

    public void stopMoving() {
        this.walkDirection = Vector2.Zero.cpy();
        updateSpeed();
        this.isMoving = false;
    }

    public void setPlayer(Entity player) {
        this.player = player;
      //  player.getComponent(PlayerActions.class).setCar(this.entity);
    }

    void exitCar() {
        stopMoving();
        mode = Control.normal;
        isSilent = true;
        String prevDirectionStr = getDirection(prevDirection);
        entity.getEvents().trigger("car", prevDirectionStr);
        KeyboardCarInputComponent carInput = entity.getComponent(KeyboardCarInputComponent.class);
        KeyboardPlayerInputComponent playerInput = player.getComponent(KeyboardPlayerInputComponent.class);
      //  playerInput.setWalkDirection(carInput.getWalkDirection());
        player.setPosition(entity.getPosition());
    }


    public boolean isSilent() {
        return isSilent;
    }

    public void setSilent(boolean silent) {
        this.isSilent = silent;
    }
}
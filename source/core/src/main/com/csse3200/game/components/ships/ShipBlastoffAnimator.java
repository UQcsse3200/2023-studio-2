package com.csse3200.game.components.ships;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.csse3200.game.components.Component;
import com.badlogic.gdx.Gdx;
import com.csse3200.game.physics.components.PhysicsComponent;


public class ShipBlastoffAnimator extends Component {
    private static final Vector2 MAX_SPEED = new Vector2(0.1f, 0.1f); // Metres per second
    private static final float LAUNCH_DURATION = 12.0f; // Duration of the launch
    private static final float LAUNCH_ANGLE = 45f; // Launch angle (in degrees)

    private PhysicsComponent physicsComponent;
    private Vector2 launchDirection = Vector2.Zero.cpy();
    private boolean launching = false;
    private float launchTimer = 0;

    @Override
    public void create() {
        physicsComponent = entity.getComponent(PhysicsComponent.class);
        entity.getEvents().addListener("launch", this::launch);
    }

    @Override
    public void update() {
        if (launching) {
            updateLaunch();
        }
    }

    private void updateLaunch() {
        launchTimer += Gdx.graphics.getDeltaTime();

        // Calculate the launch position based on a logarithmic curve
        float newX = launchTimer * MAX_SPEED.x;
        float newY = (float) Math.log(1 + launchTimer) * 10; // Adjust the multiplier for the curve

        Vector2 launchPosition = new Vector2(newX, newY);
        entity.setPosition(launchPosition);

        if (launchTimer >= LAUNCH_DURATION) {
            launching = false;
            launchTimer = 0;
        }
    }

    /**
     * Launches the ship.
     *
     * @param direction Direction to launch the ship (normalized vector)
     */
    void launch(Vector2 direction) {
        if (!launching) {
            launchDirection = direction;
            launching = true;

            Body body = physicsComponent.getBody();
            Vector2 launchVelocity = launchDirection.cpy().scl(MAX_SPEED);
            launchVelocity.setAngle(LAUNCH_ANGLE);
            body.setLinearVelocity(launchVelocity);
        }
    }
}

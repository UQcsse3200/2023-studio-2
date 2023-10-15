package com.csse3200.game.components.CompanionWeapons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;
import com.csse3200.game.components.Component;

public class CompanionWeaponController extends Component {
    CompanionWeaponType weaponType;
    /* Variable to hold the remaining life (in game ticks) of a weapon) */
    int remainingDuration;
    /* Variable to hold the speed at which the projectile moves */
    float speed;
    /* The rotational speed of the weapon*/
    int rotationSpeed;
    /* The rotation of the weapon*/
    float currentRotation;
    /* number degrees of positive x-axis image is */
    int imageRotationOffset;
    /* Determined by # of directions in sprite sheet */
    int animationType;
    private static final float POTION_DISPOSE_DELAY = 6f;

    /**
     * Class to store variables of a spawned weapon
     */
    public CompanionWeaponController(CompanionWeaponType weaponType,
                                     int weaponDuration,
                                     float currentRotation,
                                     float speed,
                                     int rotationSpeed,
                                     int animationType,
                                     int imageRotationOffset) {
        this.weaponType = weaponType;
        this.remainingDuration = weaponDuration;
        this.currentRotation = currentRotation;
        this.speed = speed;
        this.rotationSpeed = rotationSpeed;
        this.animationType = animationType;
        this.imageRotationOffset = imageRotationOffset;
    }

    @Override
    public void create() {
        this.entity.getEvents().addListener("death", this::setDuration);
    }

    /**
     * Sets the remaining duration
     * @param duration duration to set reamining duration
     */
    private void setDuration(int duration) {
        this.remainingDuration = duration;
    }

    /**
     * Function to control projectile movement once it has spawned
     */

    @Override
    public void update() {
        //switch statement to define weapon movement based on type (a projectile
        Vector2 movement = switch (this.weaponType) {
            case DEATH_POTION -> updateSwing();
            //get some different input numbers
            case SHIELD -> updateStatic();

            default -> updateStatic();
        };

        //Reference to current position of the projectile
        Vector2 position = entity.getPosition();
        //Update position and rotation of projectile
        entity.setPosition(new Vector2(position.x + movement.x, position.y + movement.y));
        if (this.weaponType == CompanionWeaponType.DEATH_POTION) {
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    // Dispose of the entity (potion) here
                    entity.dispose();
                }
            }, POTION_DISPOSE_DELAY);
        }
    }

    private Vector2 updateSwing() {
        CompanionWeaponTargetComponent weaponTargetComponent = entity.getComponent(CompanionWeaponTargetComponent.class);
        Vector2 targetPosition = weaponTargetComponent.getPosOfTarget();

        // Calculate the direction vector towards the target
        Vector2 direction = targetPosition.cpy().sub(entity.getPosition()).nor();

        // Update the position based on the direction and speed
        return direction.scl(this.speed * Gdx.graphics.getDeltaTime());
    }

    /**
     * required movement for static weapon display - tracks companion
     * @return required movement
     */
    private Vector2 updateStatic() {
        CompanionWeaponTargetComponent weaponTargetComponent = entity.getComponent(CompanionWeaponTargetComponent.class);
        return weaponTargetComponent.getPosOfTarget();
    }
}

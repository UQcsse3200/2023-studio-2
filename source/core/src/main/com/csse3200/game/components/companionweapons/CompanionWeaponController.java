package com.csse3200.game.components.companionweapons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;
import com.csse3200.game.components.Component;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.CompanionWeaponConfig;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.ServiceLocator;

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

    public String textureAtlas;
    private static final CompanionWeaponConfig configs = new CompanionWeaponConfig(
            " SWORD",
            "SHORT DISTANCE",
            50.0f,
            2.0f,
            25,
            1,
            0,
            2,
            0,
            4,
            0,
            1f,
            "images/weapons/sword.atlas",
            "images/upgradetree/sword.png",
            CompanionWeaponType.SWORD,
            "melee"     ,
            4);
Entity companion = ServiceLocator.getEntityService().getCompanion();
    /**
     * Class to store variables of a spawned weapon
     */
    public CompanionWeaponController(CompanionWeaponType weaponType,
                                     int weaponDuration,
                                     float currentRotation,
                                     float speed,
                                     int rotationSpeed,
                                     int animationType,
                                     int imageRotationOffset,String textureAtlas) {
        this.weaponType = weaponType;
        this.remainingDuration = weaponDuration;
        this.currentRotation = currentRotation;
        this.speed = speed;
        this.rotationSpeed = rotationSpeed;
        this.animationType = animationType;
        this.imageRotationOffset = imageRotationOffset;
                this.textureAtlas = textureAtlas;
    }


    public void create() {

        this.entity.getEvents().addListener("death", this::setDuration);
        if(weaponType == CompanionWeaponType.SWORD){
            initial_rotation();
            initial_position();

            AnimationRenderComponent animator = entity.getComponent(AnimationRenderComponent.class);
            if (animator != null) {
                add_animations(animator);
                initial_animation(animator);
            }


        }
    }
    protected void add_animations(AnimationRenderComponent animator) {
        switch (configs.animationType) {
            case 8:
                animator.addAnimation("LEFT3", 0.07f, Animation.PlayMode.NORMAL);
                animator.addAnimation("RIGHT3", 0.07f, Animation.PlayMode.NORMAL);
            case 6:
                animator.addAnimation("LEFT2", 0.07f, Animation.PlayMode.NORMAL);
                animator.addAnimation("RIGHT2", 0.07f, Animation.PlayMode.NORMAL);
            case 4:
                animator.addAnimation("LEFT1", 0.07f, Animation.PlayMode.NORMAL);
                animator.addAnimation("RIGHT1", 0.07f, Animation.PlayMode.NORMAL);
                animator.addAnimation("DOWN", 0.07f, Animation.PlayMode.NORMAL);
            default:
                animator.addAnimation("UP", 0.07f, Animation.PlayMode.NORMAL);
                animator.addAnimation("STATIC", 0.07f, Animation.PlayMode.NORMAL);
        }
    }

    /**
     * Sets the remaining duration
     *
     * @param duration duration to set reamining duration
     */
    private void setDuration(int duration) {
        this.remainingDuration = duration;
    }

    /**
     * Function to control projectile movement once it has spawned
     */

    public void update() {
        if(weaponType == CompanionWeaponType.SWORD){
         rotate();
         move();
        reanimate();
        if (--this.remainingDuration <= 0) {
            despawn();
        }}
        //switch statement to define weapon movement based on type (a projectile
        Vector2 movement = switch (this.weaponType) {
            case Death_Potion -> update_swing();
            //get some different input numbers
            case SHIELD -> update_static();
            case  SHIELD_2 -> update_SHIELD();
            case SWORD -> update_sword();

            default -> null;
        };

        //Reference to current position of the projectile
        Vector2 position = entity.getPosition();
        //Update position and rotation of projectile
        entity.setPosition(new Vector2(position.x + movement.x, position.y + movement.y));
        if (this.weaponType == CompanionWeaponType.Death_Potion || this.weaponType==CompanionWeaponType. SHIELD_2 ) {
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    despawn();
                }
            }, POTION_DISPOSE_DELAY);
        }
    }

    /**
     * Despawn the weapon
     */
    private void despawn() {
        Gdx.app.postRunnable(entity::dispose);
    }


    private Vector2 update_swing() {
        CompanionWeaponTargetComponent weaponTargetComponent = entity.getComponent(CompanionWeaponTargetComponent.class);
        Vector2 targetPosition = weaponTargetComponent.get_pos_of_target();

        // Calculate the direction vector towards the target
        Vector2 direction = targetPosition.cpy().sub(entity.getPosition()).nor();

        // Update the position based on the direction and speed
        Vector2 movement = direction.scl(this.speed * Gdx.graphics.getDeltaTime());

        return movement;
    }
    private Vector2 update_sword() {
        CompanionWeaponTargetComponent weaponTargetComponent = entity.getComponent(CompanionWeaponTargetComponent.class);
        Vector2 targetPosition = weaponTargetComponent.get_pos_of_target();

        Vector2 currentPosition = entity.getPosition();

        // Calculate the distance between the sword and the target
        float distanceToTarget = currentPosition.dst(targetPosition);

        if (distanceToTarget <= 3.0f) {
            // Calculate the direction vector towards the target
            Vector2 direction = targetPosition.cpy().sub(currentPosition).nor();

            // Update the position based on the direction and speed
            Vector2 movement = direction.scl(this.speed * Gdx.graphics.getDeltaTime());

            return movement;
        } else {
            // If the distance is greater than 2, return no movement
            return Vector2.Zero;
        }
    }

    /**
     * required movement for static weapon display - tracks companion
     *
     * @return required movement
     */
    private Vector2 update_static() {
        CompanionWeaponTargetComponent weaponTargetComponent = entity.getComponent(CompanionWeaponTargetComponent.class);
        return weaponTargetComponent.get_pos_of_target();
    }

    /**
     * explain
     * @return
     */
    private Vector2 update_SHIELD() {

        CompanionWeaponTargetComponent weaponTargetComponent = entity.getComponent(CompanionWeaponTargetComponent.class);

        Vector2 targetPosition = weaponTargetComponent.get_pos_of_target();

        // Calculate the direction vector towards the target
        Vector2 direction = targetPosition.cpy().sub(entity.getPosition()).nor();

        // Update the position based on the direction and speed
        Vector2 movement = direction.scl(this.speed * Gdx.graphics.getDeltaTime());

        return movement;
    }
    protected void initial_rotation() {
        //Discritise rotation into 4directions
        currentRotation = ((int) (((currentRotation + 22.5f) % 360) / 45f)) * 45f;
    }


    protected void initial_position() {
        entity.setPosition(companion.getCenterPosition()
                .mulAdd(entity.getScale(), -0.5f)
                .add(positionInDirection(currentRotation - (float) getMoveDirection() * 25f, 0.8f))
        );
    }


    protected void initial_animation(AnimationRenderComponent animator) {
        switch (configs.animationType) {
            //All melee weapons should implement 4 or 8 directional attacking
            case 4, 8:
                int dir = Math.round(currentRotation / 45);
                switch (dir) {
                    case 0, 7 -> animator.startAnimation("RIGHT1");
                    case 2, 1 -> animator.startAnimation("UP");
                    case 3, 4, 5 -> animator.startAnimation("LEFT1");
                    case 6 -> animator.startAnimation("DOWN");
                }
                break;
            default:
                animator.startAnimation("UP");
        }
    }


    protected void rotate() {
        this.currentRotation -= configs.rotationSpeed;
    }
    private Vector2 companion_last_pos = companion.getPosition();
    protected void move() {
        Vector2 player_delta = companion.getPosition().sub(companion_last_pos);
        this.companion_last_pos = companion.getPosition();

        entity.setPosition(entity.getPosition()
                .add(player_delta.cpy())
                .add(positionInDirection(currentRotation + (float) getMoveDirection() * 90f,
                        0.01f * configs.weaponSpeed))
        );
    }

    private int getMoveDirection() {
        return switch (Math.round(currentRotation / 45)) {
            case 0, 1, 2, 7 -> -1;
            default -> 1;
        };
    }


    protected void reanimate() {
        return;
    }

    protected Vector2 positionInDirection(float direction, float distance) {
        var radians = Math.toRadians(direction);
        return new Vector2((float) Math.cos(radians), (float) Math.sin(radians)).scl(distance);
    }

}




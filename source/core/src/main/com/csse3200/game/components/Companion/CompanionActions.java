package com.csse3200.game.components.Companion;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.CompanionWeapons.CompanionWeaponType;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.FollowComponent;
import com.csse3200.game.components.ItemPickupComponent;
import com.csse3200.game.components.player.PlayerAnimationController;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.services.ServiceLocator;

import java.util.Objects;

/**
 * This class represents the action component for interacting with a Companion entity.
 * It handles various actions related to the Companion, such as movement, speed, and attacking.
 */
public class CompanionActions extends Component {

    private static Vector2 COMPANION_SPEED = new Vector2(4f, 4f); // Metres per second


    private static final float ROTATION_SPEED = 10.0f;
    private static final String CHANGEWEAPON = "changeWeapon";
    private float currentRotation = 5.0f;
    private Entity player = ServiceLocator.getEntityService().getPlayer();

    private PhysicsComponent physicsComponent;
    public Vector2 walkDirection = Vector2.Zero.cpy();
    public boolean moving = false;

    // ALL CODE PERTAINING TO COMPANION MODES
    public String companionMode;
    public final static String COMPANION_MODE_ATTACK = "COMPANION_MODE_ATTACK";
    public final static String COMPANION_MODE_NORMAL = "COMPANION_MODE_NORMAL";
    public final static String COMPANION_MODE_DEFENCE = "COMPANION_MODE_DEFENCE";

    public final static Vector2 COMPANION_ATTACK_MODE_SPEED = new Vector2(6f, 6f);
    public final static Vector2 COMPANION_NORMAL_MODE_SPEED = new Vector2(4f, 4f);



    /**
     * Initialise the companion to be facing the player.
     * It sets up event listeners for companion movements and actions.
     */
    @Override
    public void create() {
        physicsComponent = entity.getComponent(PhysicsComponent.class);
        entity.getEvents().addListener("walk", this::walk);
        entity.getEvents().addListener("walkStop", this::stopWalking);
        entity.getEvents().addListener("attack", this::attack);
        entity.getEvents().addListener("CompanionSwitchMode", this::CompanionSwitchMode);

        setCompanionMode(COMPANION_MODE_NORMAL);

        // Initialize currentRotation based on the initial orientation of the companion
        currentRotation = physicsComponent.getBody().getAngle()*MathUtils.radiansToDegrees;
        if (player != null) {
            Vector2 playerPosition = player.getComponent(PhysicsComponent.class).getBody().getPosition();
            physicsComponent.getBody().setTransform(playerPosition, currentRotation * MathUtils.degreesToRadians);
        }
    }


    /**
     * Set the companion mode, given a companion mode string
     *
     *      Normal = Normal speed, you follow the player
     *      Defence = Normal speed, you don't follow the player
     *      Attack = High speed, you don't follow the player
     *
     * @param mode - the mode to set to
     */
    public void setCompanionMode(String mode) {
        companionMode = mode;

        //now for specific modes
        if (Objects.equals(mode, COMPANION_MODE_NORMAL)) {
            COMPANION_SPEED.set(COMPANION_NORMAL_MODE_SPEED);
            entity.getEvents().trigger("companionModeChange","Normal");
            entity.getComponent(FollowComponent.class).setFollowSpeed(2.5f);
        } else if (Objects.equals(mode, COMPANION_MODE_DEFENCE)) {
            COMPANION_SPEED.set(COMPANION_NORMAL_MODE_SPEED);
            //entity.getComponent(CombatStatsComponent.class).addHealth(40);
            entity.getEvents().trigger("companionModeChange","Defence");
            triggerMakeCompanionShield();
        } else if (Objects.equals(mode, COMPANION_MODE_ATTACK)) {
            COMPANION_SPEED.set(COMPANION_ATTACK_MODE_SPEED);
            entity.getEvents().trigger("companionModeChange","Attack");
            triggerInventoryEvent("ranged");
        }
    }


    /**
     * TOGGLE between the modes of the companion based off its current ordering
     * cycle goes
     * Normal -> Defence
     * Defence -> Attack
     * Attack -> Normal
     */
    public void CompanionSwitchMode() {

        if (Objects.equals(this.companionMode, COMPANION_MODE_NORMAL)) {
            setCompanionMode(COMPANION_MODE_DEFENCE);
        } else if (Objects.equals(this.companionMode, COMPANION_MODE_DEFENCE)) {
            setCompanionMode(COMPANION_MODE_ATTACK);
        } else if (Objects.equals(this.companionMode, COMPANION_MODE_ATTACK)){
            setCompanionMode(COMPANION_MODE_NORMAL);
        } else {
            setCompanionMode(COMPANION_MODE_NORMAL);
        }
    }

    public boolean isCompanionBeingMoved() {
        return this.moving;
    }

    /**
     * Set the bullet texture path.
     *
     * @param path - Path????
     */
    public void setBulletTexturePath(String path) {
    }

    /**
     * Update.
     * This is called once per frame, and will update the companion state.
     * It ensures the companion follows the player and adjusts its speed if boost is activated.
     */
    @Override
    public void update() {
        updateSpeed();
    }

//    void updateInventory(int i) {
//        switch (i) {
//            case 1:
//                entity.getComponent(CompanionInventoryComponent.class).setEquipped(1);
//                break;
//            case 2:
//                entity.getComponent(CompanionInventoryComponent.class).setEquipped(2);
//                break;
//            case 3:
//                entity.getComponent(CompanionInventoryComponent.class).setEquipped(3);
//                break;
//            case 4:
//                entity.getComponent(CompanionInventoryComponent.class).setEquipped(4);
//                break;
//            default:
//                entity.getComponent(CompanionInventoryComponent.class).cycleEquipped();
//                break;
//        }
//    }

    private boolean isMovementKeyPressed() {
        // Check if any of the movement keys are pressed (I, J, K, L)
        return Gdx.input.isKeyPressed(Input.Keys.I) || Gdx.input.isKeyPressed(Input.Keys.J) ||
                Gdx.input.isKeyPressed(Input.Keys.K) || Gdx.input.isKeyPressed(Input.Keys.L);
    }

    public String getCompanionMode() {
        return companionMode;
    }

    public void updateSpeed() {
        Body body = physicsComponent.getBody();
        Vector2 velocity = body.getLinearVelocity();
        Vector2 desiredVelocity = walkDirection.cpy().scl(COMPANION_SPEED);
        // impulse = (desiredVel - currentVel) * mass
        Vector2 impulse = desiredVelocity.sub(velocity).scl(body.getMass());
        body.applyLinearImpulse(impulse, body.getWorldCenter(), true);
    }


    /**
     * Moves the Companion towards a given direction.
     *
     * @param direction direction to move in
     */
    void walk(Vector2 direction) {
        this.walkDirection = direction;
        moving = true;
    }

    /**
     * Stops the player from walking.
     */
    void stopWalking() {
        this.walkDirection = Vector2.Zero.cpy();
        updateSpeed();
        moving = false;
    }

    /**
     * Makes the companion attack.
     */
    void attack() {
//        Sound attackSound = ServiceLocator.getResourceService().getAsset("sounds/Impact4.ogg", Sound.class);
//        attackSound.play();

    }

    /**
     * Set the speed to a set number.
     *
     * @param x - how fast in x direction
     * @param y - how fast in y direction
     */
    public void setSpeed(float x, float y) {
        COMPANION_SPEED = new Vector2(x, y);
    }
    public Vector2 getSpeed() {
        return COMPANION_SPEED;
    }


    /**
     * This funciton is to spawn a spinning shield around the companion
     */
    public void triggerMakeCompanionShield() {
        ServiceLocator.getEntityService().getCompanion().getEvents().trigger(CHANGEWEAPON, CompanionWeaponType.SHIELD);
    }

    private void triggerInventoryEvent(String slot) {
        CompanionInventoryComponent invComp = ServiceLocator.getEntityService().getCompanion().getComponent(CompanionInventoryComponent.class);
        invComp.setEquipped(slot);
        ServiceLocator.getEntityService().getCompanion().getEvents().trigger(CHANGEWEAPON, invComp.getEquippedType());
    }

}
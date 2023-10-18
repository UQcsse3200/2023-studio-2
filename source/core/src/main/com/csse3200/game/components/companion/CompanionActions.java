package com.csse3200.game.components.companion;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.companionweapons.CompanionWeaponController;
import com.csse3200.game.components.companionweapons.CompanionWeaponType;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.FollowComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.EnemyFactory;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.ServiceLocator;

import java.util.List;
import java.util.Objects;

/**
 * This class represents the action component for interacting with a Companion entity.
 * It handles various actions related to the Companion, such as movement, speed, and attacking.
 */
public class CompanionActions extends Component {

    private static Vector2 COMPANION_SPEED = new Vector2(4f, 4f); // Metres per second
    Entity player = ServiceLocator.getEntityService().getPlayer();
    private static final String CHANGEWEAPON = "changeWeapon";
    private float currentRotation = 5.0f;
    private PhysicsComponent physicsComponent;
    public Vector2 walkDirection = Vector2.Zero.cpy();
    public boolean moving = false;

    // ALL CODE PERTAINING TO COMPANION MODES
    public String companionMode;
    public final static String COMPANION_MODE_ATTACK = "COMPANION_MODE_ATTACK";
    public final static String COMPANION_MODE_NORMAL = "COMPANION_MODE_NORMAL";
    public final static String COMPANION_MODE_DEFENCE = "COMPANION_MODE_DEFENCE";
    public final static String COMPANION_MODE_DOWN = "COMPANION_MODE_DOWN";

    public final static Vector2 COMPANION_ATTACK_MODE_SPEED = new Vector2(6f, 6f);
    public final static Vector2 COMPANION_NORMAL_MODE_SPEED = new Vector2(4f, 4f);
    Vector2 trackPrev;
    int currentTargetIndex = 0;
    boolean inCombat = false;


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
            entity.getComponent(CombatStatsComponent.class).setImmunity(false);
            entity.getComponent(FollowComponent.class).setFollowSpeed(2.5f);
        } else if (Objects.equals(mode, COMPANION_MODE_DEFENCE)) {
            COMPANION_SPEED.set(COMPANION_NORMAL_MODE_SPEED);
            entity.getEvents().trigger("companionModeChange","Defence");
            entity.getComponent(CombatStatsComponent.class).setImmunity(false);
            triggerMakeCompanionShield();
        } else if (Objects.equals(mode, COMPANION_MODE_ATTACK)) {
            COMPANION_SPEED.set(COMPANION_ATTACK_MODE_SPEED);
            entity.getComponent(CombatStatsComponent.class).setImmunity(true);
            entity.getEvents().trigger("companionModeChange","Attack");
        }
    }

    /**
     * This function will handle everything when the companion goes down
     */
    public void handleCompanionDownMode() {
        // Set animation of companion to be facing down
        entity.getComponent(CompanionAnimationController.class).animateDown();
        // Set the mode to be in down mode
        setCompanionMode(COMPANION_MODE_DOWN);

        // Send updated mode to the UI
        entity.getEvents().trigger("companionModeChange","Down");

        // set follow speed to zero
        entity.getComponent(FollowComponent.class).setFollowSpeed(0f);

        //OPTIONAL _ MUST IMPLEMENT: trigger death animation
        entity.getEvents().trigger("companionDeath");
        entity.getEvents().trigger("standUp");
    }

    /**
     * Function is called whenever the companion was DOWN, but is being revived
     */
    public void handleCompanionRevive() {
        //change the mode back to normal
        setCompanionMode(COMPANION_MODE_NORMAL);
        //reset the follow speed to 2.5
        entity.getComponent(FollowComponent.class).setFollowSpeed(2.5f);
        //update mode UI
        entity.getEvents().trigger("companionModeChange","Normal");


        //restore health back to full (50)
        entity.getComponent(CombatStatsComponent.class).setHealth(50);


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

    /**
     * Update.
     * This is called once per frame, and will update the companion state.
     * It ensures the companion follows the player and adjusts its speed if boost is activated.
     */
    @Override
    public void update() {
        updateSpeed();
        handleAttackMode();
    }

    public String getCompanionMode() {
        return companionMode;
    }

    public void updateSpeed() {
        Body body = physicsComponent.getBody();
        Vector2 velocity = body.getLinearVelocity();
        Vector2 desiredVelocity = walkDirection.cpy().scl(COMPANION_SPEED);
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
     * Called every single frame,
     *
     * Basically, if you are in attack mode, it finds the closest enemy, and if there is one, it'll move you towards them
     * It will also create a meelee weapon event if you get very close
     */
    private void handleAttackMode(){
        if (companionMode.equals(COMPANION_MODE_ATTACK)) {
            Vector2 targetPosition = handleAttack();
            if (targetPosition != null) {
                // Calculate the direction vector towards the target
                Vector2 direction = targetPosition.cpy().sub(entity.getPosition());
                // Check the distance to the target
                float distanceToTarget = direction.len();
                if (distanceToTarget <= 1f) {
                    // When the companion is within the desired distance, trigger the melee attack
                    triggerInventoryEvent("melee");
                } else {
                    // Update the position based on the direction and speed
                    direction.nor();
                    Vector2 movement = direction.scl(COMPANION_SPEED.x * Gdx.graphics.getDeltaTime(), COMPANION_SPEED.y * Gdx.graphics.getDeltaTime());
                    entity.setPosition(new Vector2(entity.getPosition().x + movement.x, entity.getPosition().y + movement.y));
                }
            }
        }
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
    void attack() {}

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

    /**
     * trigger the creation of any
     * @param slot
     */
    public void triggerInventoryEvent(String slot) {
        CompanionInventoryComponent invComp = ServiceLocator.getEntityService().getCompanion().getComponent(CompanionInventoryComponent.class);
        invComp.setEquipped(slot);
        ServiceLocator.getEntityService().getCompanion().getEvents().trigger(CHANGEWEAPON, invComp.getEquippedType());
    }

    /**
     * handleAttack returns the closest enemy position in Vector2 form
     * @return - Vector2 of the closest enemies position
     */
    private Vector2 handleAttack() {
        List<Entity> enemies = EnemyFactory.getEnemyList();
        if (enemies.isEmpty()) {
            return entity.getPosition();
        }

        if (!inCombat) {
            Entity enemy = getNextLiveEnemy(enemies);
            if (enemy != null) {
                Vector2 playerPosition = player.getComponent(PhysicsComponent.class).getBody().getPosition();
                Vector2 enemyPosition = enemy.getComponent(PhysicsComponent.class).getBody().getPosition();
                float distanceToPlayer = playerPosition.dst(enemyPosition);

                if (distanceToPlayer <= 5.0f) {
                    inCombat = true;
                    trackPrev = enemyPosition;

                    float distanceToEnemy = entity.getPosition().dst(enemyPosition);

                    if (distanceToEnemy <= 3.0f) {
                        triggerInventoryEvent("melee");
                    }

                    return enemyPosition;
                }
            }
        }
        return inCombat ? enemies.get(currentTargetIndex).getPosition() : trackPrev;
    }


    /**
     * Get the first live enemy available. Then, once that one is dead, it finds a new target.
     *
     * @param enemies - list of enemies on the map
     * @return - the enemy that it is currently targeting
     */
    private Entity getNextLiveEnemy(List<Entity> enemies) {
        int numEnemies = enemies.size();
        int originalTargetIndex = currentTargetIndex;

        while (true) {
            Entity enemy = enemies.get(currentTargetIndex);
            if (enemy != null && enemy.getComponent(CombatStatsComponent.class).getHealth() > 0) {
                return enemy;
            }
            currentTargetIndex = (currentTargetIndex + 1) % numEnemies;

            if (currentTargetIndex == originalTargetIndex) {
                break;
            }
        }
        return null;
    }}
package com.csse3200.game.components.enemy;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.components.tasks.MovementTask;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.PhysicsEngine;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.raycast.RaycastHit;
import com.csse3200.game.rendering.DebugRenderer;
import com.csse3200.game.services.ServiceLocator;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/** Chases a target entity until they get too far away or line of sight is lost */
public class Invisible extends DefaultTask implements PriorityTask {
    private final Entity target;
    private final int priority;
    private final float viewDistance;
    private final float maxChaseDistance;
    private float shootDistance;
    private final PhysicsEngine physics;
    private final DebugRenderer debugRenderer;
    private final RaycastHit hit = new RaycastHit();
    private MovementTask movementTask;
    private char direction;
    private boolean isInvis = true;


    /**
     * @param target The entity to chase.
     * @param priority Task priority when chasing (0 when not chasing).
     * @param viewDistance Maximum distance from the entity at which chasing can start.
     * @param maxChaseDistance Maximum distance from the entity while chasing before giving up.
     */
    public Invisible (Entity target, int priority, float viewDistance, float maxChaseDistance) {
        this.target = target;
        this.priority = priority;
        this.viewDistance = viewDistance;
        this.maxChaseDistance = maxChaseDistance;
        this.shootDistance = 0;
        physics = ServiceLocator.getPhysicsService().getPhysics();
        debugRenderer = ServiceLocator.getRenderService().getDebug();
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(this::toggleSpeed, 0, 2, TimeUnit.SECONDS);
    }

    /**
     * Switches the speed variable
     */
    private void toggleSpeed() {
        // Toggle the speed field
        isInvis = !isInvis;
    }

    /**
     * Creates a new chase task which will stop once the entity is within a certain distance of the target.
     *
     * @param target The entity to chase.
     * @param priority Task priority when chasing (0 when not chasing).
     * @param viewDistance Maximum distance from the entity at which chasing can start.
     * @param maxChaseDistance Maximum distance from the entity while chasing before giving up.
     * @param shootDistance The distance where the entity stops to shoot at the target.
     */
    public Invisible(Entity target, int priority, float viewDistance, float maxChaseDistance, float shootDistance) {
        this.target = target;
        this.priority = priority;
        this.viewDistance = viewDistance;
        this.maxChaseDistance = maxChaseDistance;
        this.shootDistance = shootDistance;
        physics = ServiceLocator.getPhysicsService().getPhysics();
        debugRenderer = ServiceLocator.getRenderService().getDebug();
    }

    /**
     * Start moving
     */
    @Override
    public void start() {
        super.start();
        movementTask = new MovementTask(target.getPosition());
        movementTask.create(owner);
        movementTask.start();
        direction = getDirection(target.getPosition());
        if(isInvis){
            if(direction == '<'){
                this.owner.getEntity().getEvents().trigger("chaseLeft");
            }
            if(direction == '>'||direction == '='){
                this.owner.getEntity().getEvents().trigger("chaseStart");
            }
        }
        else{
            this.owner.getEntity().getEvents().trigger("goInvisible");
        }

    }

    @Override
    public void update() {
        char direction2 = getDirection(target.getPosition());
        movementTask.setTarget(target.getPosition());
        if(isInvis){
            movementTask.setSpeed(new Vector2(3f,3f));
        }else{
            movementTask.setSpeed(new Vector2(0.5f,0.5f));
        }
        movementTask.update();
        if (movementTask.getStatus() != Status.ACTIVE) {
            movementTask.start();
        }
        if (direction != direction2){
            start();
        }
    }

    @Override
    public void stop() {
        super.stop();
        movementTask.stop();
    }

    @Override
    public int getPriority() {
        if (status == Status.ACTIVE) {
            return getActivePriority();
        }
        return getInactivePriority();
    }

    private float getDistanceToTarget() {
        return owner.getEntity().getPosition().dst(target.getPosition());
    }

    private int getActivePriority() {
        float dst = getDistanceToTarget();
        if (dst > maxChaseDistance || dst < shootDistance || !isTargetVisible()) {
            return -1; // Too far, stop chasing
        }
        return priority;
    }

    private int getInactivePriority() {
        float dst = getDistanceToTarget();
        if (dst < viewDistance && dst > shootDistance && isTargetVisible()) {
            return priority;
        }
        return -1;
    }

    private boolean isTargetVisible() {
        Vector2 from = owner.getEntity().getCenterPosition();
        Vector2 to = target.getCenterPosition();

        // If there is an obstacle in the path to the player, not visible.
        if (physics.raycast(from, to, PhysicsLayer.OBSTACLE, hit)) {
            debugRenderer.drawLine(from, hit.point);
            return false;
        }
        debugRenderer.drawLine(from, to);
        return true;
    }
    /**
     * This get method returns a char indicating the position of the target relative to the enemy.
     * @param destination target position in grid
     * @return character
     */
    public char getDirection(Vector2 destination) {
        if (owner.getEntity().getPosition().x - destination.x < 0) {
            return '>';
        }
        if (owner.getEntity().getPosition().x - destination.x > 0) {
            return '<';
        }
        return '=';
    }
}
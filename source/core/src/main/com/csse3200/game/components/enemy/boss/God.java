package com.csse3200.game.components.enemy.boss;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;

import com.csse3200.game.ai.tasks.Task;
import com.csse3200.game.components.tasks.MovementTask;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.ProjectileFactory;
import com.csse3200.game.services.ServiceLocator;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


/**
 * This task involves firing bullets in a sequential manner with
 * incrementing angles to create a spiraling effect.
 *
 */
public class God extends DefaultTask implements PriorityTask {
    private boolean switchMode = false;
    private boolean Mode = false;
    private final Entity target;
    private MovementTask movementTask;
    private Vector2[] locations;


    /**
     *
     * @param target target entity
     * @param priority  priority tas
     * @param viewDistance  viewing distance for chasing
     * @param maxChaseDistance max chasing distance
     */
    public God(Entity target, int priority, float viewDistance, float maxChaseDistance){
        this.target = target;
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(this::toggleSpeed, 0, 2, TimeUnit.SECONDS);

        ScheduledExecutorService scheduler2 = Executors.newScheduledThreadPool(1);
        scheduler2.scheduleAtFixedRate(this::toggleMode, 0, 2, TimeUnit.SECONDS);
    }


    /**
     * Switches the speed variable
     */
    private void toggleSpeed() {
        // Toggle the speed field
        Mode = true;
    }

    /**
     * Toggle the firing mode. If true fires and false otherwise
     */
    private void toggleMode() {
        // Toggle the speed field
        switchMode = true;
    }


    /**
     * Initialise enemy movement task and set speed variable
     */
    @Override
    public void start() {
        super.start();
        movementTask = new MovementTask(target.getPosition());
        movementTask.create(owner);
        movementTask.start();
        movementTask.setSpeed(new Vector2(0.5f,0.5f));
    }


    /**
     * Firing single with angle incremented each time to create spiral attack
     * Update movement task
     */

    @Override
    public void update() {
        movementTask.setTarget(target.getPosition());
        movementTask.update();
        movementTask.start();

            if(Mode){
                locations = generateAngle(owner.getEntity().getCenterPosition());
                final int[] index = {0}; // Initialize an array to keep track of the current index

                // Define a task that will run after a delay
                Timer.Task spawnBulletTask = new Timer.Task() {
                    @Override
                    public void run() {
                        if (index[0] < locations.length) {
                            Vector2 location = locations[index[0]];
                            Entity bullet1 = ProjectileFactory.createEnemyBullet(location, owner.getEntity());
                            ServiceLocator.getStructurePlacementService()
                                .spawnEntityAtVector(bullet1, owner.getEntity().getPosition());
                            index[0]++;
                        }
                    }
                };

                float delayBetweenIterations = 0.1f; // Delay between each iteration in seconds
                float initialDelay = 0.0f; // Initial delay before the first iteration in seconds

                Timer.schedule(spawnBulletTask, initialDelay, delayBetweenIterations);
                Mode = false;
            }
    }


    /**
     * Get the direction of each bullet divided evenly across 360 degree.
     * @param owner position of enemy
     * @return  bullet vector location
     */
    private Vector2[] generateAngle(Vector2 owner ){
        float radius = 10f;

        // Define the number of positions you want
        int numPositions = 20;

        // Calculate the angle between each position
        float angleIncrement = 360.0f / numPositions;

        // Create an array to store the positions
        Vector2[] positions = new Vector2[numPositions];
        // Create an array to store the spawn positions
        Vector2[] spawns = new Vector2[numPositions];

        // Calculate the positions
        for (int i = 0; i < numPositions; i++) {
            // Calculate the angle in radians
            float angle = (float) Math.toRadians(i * angleIncrement);

            // Calculate the coordinates of the position
            float x = owner.x + radius * (float) Math.cos(angle);
            float y = owner.y + radius * (float) Math.sin(angle);

            // Calculate where to spawn attack
            float spawnX = owner.x + 0.25f * (float) Math.cos(angle);
            float spawnY = owner.y + 0.25f * (float) Math.sin(angle);

            // Create a Vector2 for the position
            positions[i] = new Vector2(x, y);
            spawns[i] = new Vector2(spawnX, spawnY);
        }

        return positions;
    }

    /**
     * Return the priority for this task
     * @return integer represent priority
     */
    @Override
    public int getPriority() {
        return 10;
    }
}

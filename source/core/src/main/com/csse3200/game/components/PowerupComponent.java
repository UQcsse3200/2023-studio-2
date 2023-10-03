package com.csse3200.game.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Timer;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.components.Companion.CompanionActions;
import com.csse3200.game.components.player.PlayerActions;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.enemies.EnemyType;
import com.csse3200.game.entities.factories.EnemyFactory;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.services.ServiceLocator;

import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Represents a power-up component within the game.
 */
public class PowerupComponent extends Component {

    private PowerupType type;
    private final Entity player = ServiceLocator.getEntityService().getPlayer();
    private final Entity companion = ServiceLocator.getEntityService().getCompanion();
    private long duration;

    private ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    /**
     * Assigns a type and targetLayer value to a given Powerup
     */
    public PowerupComponent(PowerupType type) {
        this.type = type;
    }

    /**
     * Overrides the Component create() function
     */
    @Override
    public void create() {
    }

    /**
     * Applies the effects of the Powerup to the specified target entity.
     *
     *
     *
     */
    public void applyEffect() {
        switch (type) {
            case HEALTH_BOOST:
                player.getComponent(CombatStatsComponent.class).setHealth(100);
                companion.getComponent(CombatStatsComponent.class).setHealth(50);
                player.getEvents().trigger("playSound", "healthPowerup"); // plays sound when health powerup selected
                break;

            case SPEED_BOOST:
                if (player.getComponent(PlayerActions.class) == null) {
                    return;
                } else {
                    player.getComponent(PlayerActions.class).setSpeed(6, 6);
                    companion.getComponent(CompanionActions.class).setSpeed(7, 7);
                    companion.getComponent(FollowComponent.class).setFollowSpeed(5);

                    // Set the duration for speed effect
                    setDuration(8000);

                    // Schedule a task to reset the speed values after the specified duration
                    executorService.schedule(() -> {
                        player.getComponent(PlayerActions.class).setSpeed(3, 3);
                        companion.getComponent(CompanionActions.class).setSpeed(4, 4);
                    }, getDuration(), TimeUnit.MILLISECONDS);
                }
                break;

            case EXTRA_LIFE:
                player.getComponent(CombatStatsComponent.class).addLife();
                break;

            case TEMP_IMMUNITY:
                if (player.getComponent(PlayerActions.class) == null) {
                    return;
                } else {
                    companion.getComponent(CombatStatsComponent.class).setImmunity(true);
                    player.getComponent(CombatStatsComponent.class).setImmunity(true);
                    setDuration(6000);
                    executorService.schedule(() -> {
                        companion.getComponent(CombatStatsComponent.class).setImmunity(false);
                        player.getComponent(CombatStatsComponent.class).setImmunity(false);
                    }, getDuration(), TimeUnit.MILLISECONDS);
                }
                break;

            case DOUBLE_DAMAGE:
                if (player.getComponent(PlayerActions.class) == null) {
                    return;
                } else {
                    player.getComponent(CombatStatsComponent.class).setAttackMultiplier(2);
                    setDuration(12000);
                    executorService.schedule(() -> {
                        player.getComponent(CombatStatsComponent.class).setAttackMultiplier(1);
                    }, getDuration(), TimeUnit.MILLISECONDS);
                }
                break;
            case DOUBLE_CROSS:
                if (player.getComponent(PlayerActions.class) == null) {
                    return;
                } else {
                    Random random = new Random();
                    List<Entity> enemies = EnemyFactory.getEnemyList();
                    int nextInt = random.nextInt(enemies.size()) - 1;
                    Entity enemy = enemies.get(nextInt);
                    while (enemy == null) {
                        nextInt = random.nextInt(enemies.size()) - 1;
                        enemy = enemies.get(nextInt);
                    }
                    enemies.remove(nextInt);
                    for (Entity enemytarget : enemies) {
                        if ((enemy != null) && (enemy != enemytarget)) {
                            EnemyFactory.targetSet(
                                    enemytarget,
                                    enemy.getComponent(HitboxComponent.class).getLayer(),
                                    enemy.getComponent(AITaskComponent.class));
                        }
                    }
                    setDuration(12000);
                    enemy.dispose();
                }
                break;
            case SNAP:
                if (player.getComponent(PlayerActions.class) == null) {
                    return;
                } else {
                    Random random = new Random();
                    List<Entity> enemies = EnemyFactory.getEnemyList();
                    int enemyCount = enemies.size() / 2;
                    for (int i = 0; i <= enemyCount; i++) {
                        int nextInt = random.nextInt(enemies.size()) - 1;
                        Entity enemy = enemies.get(nextInt);
                        while (enemy == null) {
                            nextInt = random.nextInt(enemies.size()) - 1;
                            enemy = enemies.get(nextInt);
                        }
                        enemy.dispose();
                    }
                }

            default:
                throw new IllegalArgumentException("You must specify a valid PowerupType");
        }

        if (entity != null) {
            Gdx.app.postRunnable(entity::dispose);
        }
    }

    /**
     * Sets the duration for which the Powerup effect should last.
     *
     * @param duration Duration in milliseconds.
     */
    public void setDuration(long duration) {
        this.duration = duration;
    }

    /**
     * Retrieves the duration for which the Powerup effect should last.
     *
     * @return Duration in milliseconds.
     */
    public long getDuration() {
        return duration;
    }

    /**
     * Retrieves the type of the Powerup.
     *
     * @return The current Powerup type.
     */
    public PowerupType getType() {
        return type;
    }

    /**
     * Sets the type of the Powerup.
     *
     * @param type The type to set.
     */
    public void setType(PowerupType type) {
        this.type = type;
    }
}

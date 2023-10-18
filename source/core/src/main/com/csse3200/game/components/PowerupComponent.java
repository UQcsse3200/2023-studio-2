package com.csse3200.game.components;

import com.badlogic.gdx.Gdx;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.components.companion.CompanionActions;
import com.csse3200.game.components.companion.CompanionInventoryComponent;
import com.csse3200.game.components.companion.CompanionPowerupInventoryComponent;
import com.csse3200.game.components.player.PlayerActions;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.EnemyFactory;
import com.csse3200.game.services.ServiceLocator;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Represents a power-up component within the game.
 */
public class PowerupComponent extends Component {

    private PowerupType type;
    private final Entity player = ServiceLocator.getEntityService().getPlayer();
    private final Entity companion = ServiceLocator.getEntityService().getCompanion();
    private long duration;
    private final Random random = new Random();
    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    Entity companionEntity = ServiceLocator.getEntityService().getCompanion();

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
        // creates the powerup component.
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
                applyHealthBoost();
                break;

            case SPEED_BOOST:
                applySpeedBoost();
                break;

            case EXTRA_LIFE:
                applyExtraLife();
                break;

            case TEMP_IMMUNITY:
                applyTempImmunity();
                break;

            case DOUBLE_DAMAGE:
                applyDoubleDamage();
                break;
            case DOUBLE_CROSS:
                applyDoubleCross();
                break;
            case SNAP:
                if (player.getComponent(PlayerActions.class) == null) {
                    return;
                } else {
                    List<Entity> enemies = EnemyFactory.getEnemyList();
                    int enemyCount = enemies.size() / 2;
                    for (int i = 1; i <= enemyCount; i++) {
                        int nextInt = random.nextInt(enemies.size()) - 1;
                        Entity enemy = enemies.get(nextInt);
                        while (enemy == null) {
                            nextInt = random.nextInt(enemies.size()) - 1;
                            enemy = enemies.get(nextInt);
                        }
                        enemy.dispose();
                    }
                    break;
                }
            case DEATH_POTION:
                applyDeath();
            default:
                throw new IllegalArgumentException("You must specify a valid PowerupType");
        }

    }

    /**
     * NEW UPDATE POWERUP INVENTORY
     *
     * It will get the new powerup inventory
     *
     *
     * It will add the powerup type to the new powerup inventory
     *
     * It will then remove the power-up from the ground
     */
    public void updatePowerupInventory() {
        //get the new powerup inventory
        CompanionPowerupInventoryComponent companionPowerupInventory = companionEntity.getComponent(CompanionPowerupInventoryComponent.class);

        //if the powerup inventory is not null, add it to the new inventory
        if (companionPowerupInventory != null) {
            // within the powerup inventory, add one of this type to the inventory
            companionPowerupInventory.addPowerupToPowerupInventory(getType());
        }

        //Now that this power-up has been added to the inventory, REMOVE IT FROM THE GRAPHICS SYSTEM
        if (entity != null) {
            Gdx.app.postRunnable(entity::dispose);
        }
    }

    private void applyHealthBoost() {
        player.getComponent(CombatStatsComponent.class).setHealth(100);
        if (!companion.getComponent(CombatStatsComponent.class).isDead()) {
            companion.getComponent(CombatStatsComponent.class).setHealth(50);
        }
        player.getEvents().trigger("playSound", "healthPowerup"); // plays sound when health powerup selected
    }

    private void applySpeedBoost() {
        if (player.getComponent(PlayerActions.class) == null) {
            return;
        } else {
            player.getComponent(PlayerActions.class).setSpeed(6, 6);
            if (!companion.getComponent(CombatStatsComponent.class).isDead())
            {
                companion.getComponent(CompanionActions.class).setSpeed(7, 7);
                companion.getComponent(FollowComponent.class).setFollowSpeed(5);
            }

            // Set the duration for speed effect
            setDuration(8000);

            // Schedule a task to reset the speed values after the specified duration
            executorService.schedule(() -> {
                player.getComponent(PlayerActions.class).setSpeed(3, 3);
                companion.getComponent(CompanionActions.class).setSpeed(4, 4);
            }, getDuration(), TimeUnit.MILLISECONDS);
        }
    }

    /**
     * extra life powerup
     *
     * if the companion is down, bring the companion back to life with full health
     *
     * if the companion is not down, give the player an extra life
     */
    private void applyExtraLife() {

        if (companion != null && Objects.equals(companion.getComponent(CompanionActions.class).getCompanionMode(), "COMPANION_MODE_DOWN")) {
            companion.getComponent(CompanionActions.class).handleCompanionRevive();
        } else {
            player.getComponent(CombatStatsComponent.class).addLife();
        }
    }

    private void applyTempImmunity() {
        if (player.getComponent(PlayerActions.class) != null) {
            companion.getComponent(CombatStatsComponent.class).setImmunity(true);
            player.getComponent(CombatStatsComponent.class).setImmunity(true);
            setDuration(6000);
            executorService.schedule(() -> {
                companion.getComponent(CombatStatsComponent.class).setImmunity(false);
                player.getComponent(CombatStatsComponent.class).setImmunity(false);
            }, getDuration(), TimeUnit.MILLISECONDS);
        }
    }

    private void applyDoubleDamage() {
        if (player.getComponent(PlayerActions.class) != null) {
            player.getComponent(CombatStatsComponent.class).setAttackMultiplier(2);
            setDuration(12000);
            executorService.schedule(() -> {
                player.getComponent(CombatStatsComponent.class).setAttackMultiplier(1);
            }, getDuration(), TimeUnit.MILLISECONDS);
        }
    }

    private void applyDoubleCross() {
        if (player.getComponent(PlayerActions.class) != null) {
            List<Entity> enemies = EnemyFactory.getEnemyList();
            int nextInt = random.nextInt(enemies.size()) - 1;
            Entity enemy = enemies.get(nextInt);
            while (enemy == null) {
                nextInt = random.nextInt(enemies.size()) - 1;
                enemy = enemies.get(nextInt);
            }
            enemies.remove(nextInt);
            for (Entity enemyTarget : enemies) {
                if (enemy != enemyTarget) {
                    EnemyFactory.targetSet(enemyTarget, enemy.getComponent(AITaskComponent.class));
                }
            }
            setDuration(12000);
            enemy.dispose();
        }
    }

    private void applySnap() {
        if (player.getComponent(PlayerActions.class) != null) {
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
    }
    private void applyDeath(){if (player.getComponent(PlayerActions.class) == null) {
        return;
    } else {
        companion.getComponent(CompanionActions.class).triggerInventoryEvent("ranged");
        return;
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

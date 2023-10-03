package com.csse3200.game.components.CompanionWeapons;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.player.KeyboardPlayerInputComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.components.Companion.KeyboardCompanionInputComponent;
import com.csse3200.game.entities.factories.EnemyFactory;

import java.util.List;
import java.util.Random;

public class CompanionWeaponTargetComponent extends Component {
    Entity entity;
    CompanionWeaponType weaponType;
    Vector2 trackPrev;

    /**
     * Class to store variables of a spawned weapon
     */
    public CompanionWeaponTargetComponent(CompanionWeaponType weaponType, Entity entity) {
        this.weaponType = weaponType;
        this.entity = entity;
        this.trackPrev = entity.getPosition();
    }

    public Vector2 get_pos_of_target() {
        Vector2 pos;

        switch (this.weaponType) {
            case Death_Potion:
                List<Entity> enemies = EnemyFactory.getEnemyList();
                if (!enemies.isEmpty()) {
                    Random random = new Random();
                    int randomIndex = random.nextInt(enemies.size());
                    Entity enemy = enemies.get(randomIndex);
                    if (enemy != null) {
                        // Get the position of the selected enemy
                        pos = enemy.getPosition();

                        // Schedule a task to dispose of the enemy after 5 seconds
                        Timer.schedule(new Timer.Task() {
                            @Override
                            public void run() {
                                if (enemies.contains(enemy)) {
                                    enemies.remove(enemy);
                                    enemy.dispose();

                                }
                            }
                        }, 7f);

                        // Update the companion's position for tracking
                        this.trackPrev = entity.getPosition();
                    } else {
                        // No valid enemy found, use previous position
                        pos = this.trackPrev;
                    }
                } else {
                    // No enemies available, use previous position
                    pos = this.trackPrev;
                }
                break;

            default:
                pos = new Vector2(0, 0);
                break;
        }

        return pos;
    }
}
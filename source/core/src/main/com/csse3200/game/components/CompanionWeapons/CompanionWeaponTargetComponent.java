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

    //For the shield rotations
    /**
     * offsets are a value between
     * -100 and 100
     * It gets divided down by 20, so that the final offsets are between
     * -5 and 5
     *
     * The rotation_directions are an integer, -1 or 1
     * 0 represents growing negative, 1 represents growing positive
     */
    float x_rotate_offset;
    int x_rotation_direction;
    float y_rotate_offset;
    int y_rotation_direction;

    /**
     * Class to store variables of a spawned weapon
     */
    public CompanionWeaponTargetComponent(CompanionWeaponType weaponType, Entity entity) {
        this.weaponType = weaponType;
        this.entity = entity;
        this.trackPrev = entity.getPosition();

        //if it is a shield, adjust the rotation offsets
        //start shield in the far right corner
        if (this.weaponType == CompanionWeaponType.SHIELD) {
            x_rotate_offset = 100;
            x_rotation_direction = 1;
            y_rotate_offset = 0;
            y_rotation_direction = 1;
        }

    }

    public Vector2 get_pos_of_target() {
        Vector2 pos;

        switch (this.weaponType) {
            case SHIELD:
                return rotate_shield_around_entity();
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

    /**
     * This function will rotate the shield around this entity
     * @return - vector
     */
    public Vector2 rotate_shield_around_entity() {
        //companion position
        var companion = entity.getPosition().sub(this.trackPrev);

        float scalaingFactor = 7000;

        // Grab the current offsets, and scale down the offset down to between
        // -5 and 5
        float x_offset = x_rotate_offset / scalaingFactor;
        float y_offset = y_rotate_offset / scalaingFactor;

        //float x_offset = (float) 0.002;
        //float y_offset = (float) 0.002;
        // create the offset vector
        var offsetVector = new Vector2(x_offset, y_offset);

        //offset the  companion position
        companion.add(offsetVector);

        //update the offset values
        update_shield_rotation_offsets();

        //update the latest trackPrev to the new entity position
        this.trackPrev = entity.getPosition();
        return companion;
    }

    /**
     * This function cycles x and y values from -100 to 100
     * Using an incrementor.
     */
    public void update_shield_rotation_offsets() {
        // if the offset has reached 100 yet, flip the direction
        if (Math.abs(x_rotate_offset) == 100) {
            x_rotation_direction  = x_rotation_direction*-1;
        }
        x_rotate_offset += x_rotation_direction;

        //y
        // if the offset has reached 100 yet, flip the direction
        if (Math.abs(y_rotate_offset) == 100) {
            y_rotation_direction  = y_rotation_direction*-1;
        }
        y_rotate_offset += y_rotation_direction;
    }
}
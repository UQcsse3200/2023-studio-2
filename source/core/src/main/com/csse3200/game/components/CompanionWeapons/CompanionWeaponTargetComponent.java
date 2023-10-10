package com.csse3200.game.components.CompanionWeapons;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.Component;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.EnemyFactory;

import java.util.List;
import java.util.Random;

public class CompanionWeaponTargetComponent extends Component {
    Entity entity;
    CompanionWeaponType weaponType;
    Vector2 trackPrev;
    int currentTargetIndex = 0;
    boolean inCombat = false;

    // For the shield rotations
    float x_rotate_offset;
    int x_rotation_direction;
    float y_rotate_offset;
    int y_rotation_direction;

    public CompanionWeaponTargetComponent(CompanionWeaponType weaponType, Entity entity) {
        this.weaponType = weaponType;
        this.entity = entity;
        this.trackPrev = entity.getPosition();
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
                if (!inCombat) {
                    List<Entity> enemies = EnemyFactory.getEnemyList();
                    if (!enemies.isEmpty()) {
                        Entity enemy = enemies.get(currentTargetIndex);
                        if (enemy != null) {
                            inCombat = true;
                            pos = enemy.getPosition();
                            Timer.schedule(new Timer.Task() {
                                @Override
                                public void run() {
                                    if (enemies.contains(enemy)) {
                                        enemy.getComponent(CombatStatsComponent.class).setHealth(0);
                                        inCombat = false;
                                    }
                                }
                            }, 2f);
                            trackPrev = pos;
                            currentTargetIndex = (currentTargetIndex + 1) % enemies.size();
                            return pos;
                        }
                    }
                }
                return inCombat ? EnemyFactory.getEnemyList().get(currentTargetIndex).getPosition() : trackPrev;
            default:
                return new Vector2(0, 0);
        }
    }

    public Vector2 rotate_shield_around_entity() {
        var companion = entity.getPosition().sub(trackPrev);
        float scalingFactor = 7000;
        float x_offset = x_rotate_offset / scalingFactor;
        float y_offset = y_rotate_offset / scalingFactor;
        var offsetVector = new Vector2(x_offset, y_offset);
        companion.add(offsetVector);
        update_shield_rotation_offsets();
        trackPrev = entity.getPosition();
        return companion;
    }

    public void update_shield_rotation_offsets() {
        if (Math.abs(x_rotate_offset) == 100) {
            x_rotation_direction *= -1;
        }
        x_rotate_offset += x_rotation_direction;

        if (Math.abs(y_rotate_offset) == 100) {
            y_rotation_direction *= -1;
        }
        y_rotate_offset += y_rotation_direction;
    }
}

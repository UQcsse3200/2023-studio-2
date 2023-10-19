package com.csse3200.game.components.companionweapons;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.explosives.PostrunnableTask;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.EnemyFactory;
import com.csse3200.game.services.ServiceLocator;

import java.util.List;

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
                return handleDeathPotion();
            case SWORD:
                return handleSword();
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
        updateSHIELD();
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

    /**
     * @return
     */
    private Vector2 handleDeathPotion() {
        List<Entity> enemies = EnemyFactory.getEnemyList();
        if (!inCombat) {
            if (!enemies.isEmpty()) {
                Entity enemy = getNextLiveEnemy(enemies);
                if (enemy != null) {
                    inCombat = true;
                    Vector2 pos = enemy.getPosition();
                    Timer.schedule(new Timer.Task() {
                        @Override
                        public void run() {
                            if (enemies.contains(enemy)) {
                                enemy.getComponent(CombatStatsComponent.class).setHealth(0);
                                inCombat = false;
                                enemies.remove(enemy);
                            }
                        }
                    }, 2f);
                    trackPrev = pos;
                    return pos;
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
    }
    Entity companion = ServiceLocator.getEntityService().getCompanion();

    public void updateSHIELD() {

        for (var otherEntity :  EnemyFactory.getEnemyList()) {
            if (otherEntity == entity) {
                continue;
            }

            var distance = companion.getCenterPosition().dst(otherEntity.getCenterPosition());

            if (distance <= 1.1f) {
                otherEntity.getEvents().trigger("chainExplode", distance/1.1f * 0.4f);
            }
        }



        for (Entity otherEntity : EnemyFactory.getEnemyList()) {
            if (otherEntity == entity ) {
                continue;
            }

            var distance = companion.getCenterPosition().dst(otherEntity.getCenterPosition());
            if (distance <= 1.1f) {
                otherEntity.getEvents().trigger("SHIELD_2", distance/1.1f * 0.4f);
            }
        }
        for (var otherEntity :  EnemyFactory.getEnemyList()) {
            var combatStatsComponent = otherEntity.getComponent(CombatStatsComponent.class);



            var distance = companion.getCenterPosition().dst(otherEntity.getCenterPosition());

            if (distance <= 1.1f) {
                combatStatsComponent.addHealth((int) (( 1.1f - distance)
                        / 1.1f * - 25 ));
            }
        }

    }
    private boolean isChained = false;
    protected void chainExplode(float delay) {
        if (isChained) {
            return;
        }

//        isChained = true;

        if (5==5) {
            Timer.schedule(new PostrunnableTask(this::updateSHIELD), delay);
        }
    }
    public void create() {

        this.entity.getEvents().addListener(" SHIELD_2", this::updateSHIELD);
        this.entity.getEvents().addListener("chainExplode", this::chainExplode);
        this.entity.getEvents().addListener("POTION", this::handleDeathPotion);
    }

    private Vector2 handleSword(){
        List<Entity> enemies = EnemyFactory.getEnemyList();
        if (!inCombat) {
            if (!enemies.isEmpty()) {
                Entity enemy = getNextLiveEnemy(enemies);
                if (enemy != null) {
                    inCombat = true;
                    Vector2 pos = enemy.getPosition();
                    Timer.schedule(new Timer.Task() {

                        public void run() {
                            if (enemies.contains(enemy)) {var distance = companion.getCenterPosition().dst(enemy.getCenterPosition());
                                if (distance <= 2.5f) {  enemy.getComponent(CombatStatsComponent.class).addHealth((int)-40.0f);

                                if(enemy.getComponent(CombatStatsComponent.class).getHealth() == 0)
                                {  enemies.remove(enemy);}
                                else if(enemy.getComponent(CombatStatsComponent.class).getHealth() != 0) { enemy.getComponent(CombatStatsComponent.class).getHealth();
                                }
                                }
                                else
                                { inCombat = false;} }
                        }
                    }, 1f);
                    trackPrev = pos;
                    return pos;
                }
            }
        }
        return inCombat ? enemies.get(currentTargetIndex).getPosition() : trackPrev;}
}
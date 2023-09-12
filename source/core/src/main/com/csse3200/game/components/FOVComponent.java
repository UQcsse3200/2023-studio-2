package com.csse3200.game.components;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.structures.Rotation;
import com.csse3200.game.components.structures.RotationRenderComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.entities.factories.EnemyFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FOVComponent extends ProximityActivationComponent {

    private RotationRenderComponent renderComponent;
    private List<Entity> enemies =  new ArrayList<>();
    private final Map<Entity, Boolean> enemyIsInFOV = new HashMap<>();
    EntityService entityService = new EntityService();

    public FOVComponent(float radius, List<Entity> enemies, ProximityFunc entered, ProximityFunc exited) {
        super(radius, entered, exited);
        this.enemies = enemies;
        enemies.forEach(entity -> enemyIsInFOV.put(entity, false));
    }

    public FOVComponent(float radius, Entity enemy, ProximityFunc entered, ProximityFunc exited) {
        super(radius, entered, exited);
        enemyIsInFOV.put(enemy, false);
        this.enemies.add(enemy);
    }

    @Override
    public void update() {

        for (Entity enemy : enemies) {
            boolean isInFOV = enemyIsInFOV.get(enemy);

            if (!isInFOV && enemyIsInFOV(enemy)) {
                enemyIsInFOV.put(enemy, true);
                entered.call(enemy);

            } else if (isInFOV && !enemyIsInFOV(enemy)) {
                enemyIsInFOV.put(enemy, false);
                exited.call(enemy);
            }
            else if (isInFOV) {
                entered.call(enemy);
            }
        }
    }

    public boolean enemyIsInFOV(Entity enemy) {

        Vector2 angleToEnemy = new Vector2(enemy.getPosition().x - entity.getPosition().x, enemy.getPosition().y - entity.getPosition().y);

        /*if (this.entityIsInProximity(enemy)) {
            if (renderComponent.getRotation() == Rotation.NORTH && 45 < angleToEnemy.angleDeg() && enemy.getPosition().angleDeg() < 135) {
                return true;
            } else if (renderComponent.getRotation() == Rotation.EAST && 135 < angleToEnemy.angleDeg() && enemy.getPosition().angleDeg() < 225) {
                return true;
            } else if (renderComponent.getRotation() == Rotation.SOUTH && 225 < angleToEnemy.angleDeg() && enemy.getPosition().angleDeg() < 315) {
                return true;
            } else if (renderComponent.getRotation() == Rotation.WEST && 315 < angleToEnemy.angleDeg() && enemy.getPosition().angleDeg() < 45) {
                return true;
            }
        }*/
        float distance = this.entity.getCenterPosition().dst(enemy.getCenterPosition());
        return distance <= this.radius;

    }

    public interface FOVFunc {
        void call(ArrayList<Entity> entities);
    }

    public void startAtlasRotation() {
        long lastRotationTime = System.currentTimeMillis();
        // Rotate the turret every 2 seconds (60 frames)
        if (System.currentTimeMillis() - lastRotationTime > 2000) {
            if (renderComponent.getRotation() == Rotation.NORTH) {
                renderComponent.setRotation(Rotation.EAST);
            } else if (renderComponent.getRotation() == Rotation.EAST) {
                renderComponent.setRotation(Rotation.SOUTH);
            } else if (renderComponent.getRotation() == Rotation.SOUTH) {
                renderComponent.setRotation(Rotation.WEST);
            } else if (renderComponent.getRotation() == Rotation.WEST) {
                renderComponent.setRotation(Rotation.NORTH);
            }
        }
    }
}

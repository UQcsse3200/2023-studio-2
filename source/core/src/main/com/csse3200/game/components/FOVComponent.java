package com.csse3200.game.components;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.structures.Rotation;
import com.csse3200.game.components.structures.RotationRenderComponent;
import com.csse3200.game.entities.Entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FOVComponent extends ProximityActivationComponent {

    private RotationRenderComponent renderComponent;
    private List<Entity> enemies;
    private final FOVFunc enter;
    private final FOVFunc exit;
    private final Map<Entity, Boolean> entityIsInFOV = new HashMap<>();

    public FOVComponent(float radius, List<Entity> enemies, ProximityFunc entered, ProximityFunc exited, FOVFunc enter1, FOVFunc exit1) {
        super(radius, entered, exited);
        this.enemies = enemies;
        this.enter = enter1;
        this.exit = exit1;
    }

    public FOVComponent(float radius, Entity enemy, ProximityFunc entered, ProximityFunc exited, FOVFunc enter, FOVFunc exit) {
        super(radius, entered, exited);
        this.enter = enter;
        this.exit = exit;
        this.enemies.add(enemy);
    }

    @Override
    public void update() {

        for (Entity enemy : enemies) {
            boolean isInFOV = entityIsInFOV.get(enemy);

            if (!isInFOV && enemyIsInFOV(enemy)) {
                entityIsInFOV.put(enemy, true);
                // override the existing entry method to enable shooting
                entered.call(enemy);

            } else if (isInFOV && !enemyIsInFOV(enemy)) {
                entityIsInFOV.put(enemy, false);
                startAtlasRotation();
                // override the existing exit method to disable shooting
                exited.call(enemy);
            }
        }
    }

    public boolean enemyIsInFOV(Entity enemy) {

        Vector2 angleToEnemy = new Vector2(enemy.getPosition().x - entity.getPosition().x, enemy.getPosition().y - entity.getPosition().y);

        if (this.entityIsInProximity(enemy)) {
            if (renderComponent.getRotation() == Rotation.NORTH && 45 < angleToEnemy.angleDeg() && enemy.getPosition().angleDeg() < 135) {
                return true;
            } else if (renderComponent.getRotation() == Rotation.EAST && 135 < angleToEnemy.angleDeg() && enemy.getPosition().angleDeg() < 225) {
                return true;
            } else if (renderComponent.getRotation() == Rotation.SOUTH && 225 < angleToEnemy.angleDeg() && enemy.getPosition().angleDeg() < 315) {
                return true;
            } else if (renderComponent.getRotation() == Rotation.WEST && 315 < angleToEnemy.angleDeg() && enemy.getPosition().angleDeg() < 45) {
                return true;
            }
        }
        return false;
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

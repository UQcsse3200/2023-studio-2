package com.csse3200.game.components;

import com.csse3200.game.components.structures.Rotation;
import com.csse3200.game.components.structures.RotationRenderComponent;
import com.csse3200.game.entities.Entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FOVComponent extends ProximityActivationComponent {

    private RotationRenderComponent renderComponent;
    private final FOVFunc shoot;
    private final FOVFunc stopShoot;
    private List<Entity> enemies;
    private final Map<Entity, Boolean> entityIsInFOV = new HashMap<>();

    public FOVComponent(float radius, List<Entity> enemies, ProximityFunc entered, ProximityFunc exited, FOVFunc shoot, FOVFunc stopShoot, RotationRenderComponent renderComponent) {
        super(radius, enemies, entered, exited);
        this.renderComponent = renderComponent;
        this.shoot = shoot;
        this.stopShoot = stopShoot;
    }

    public FOVComponent(float radius, Entity enemy, ProximityFunc entered, ProximityFunc exited, FOVFunc shoot, FOVFunc stopShoot, RotationRenderComponent renderComponent) {
        super(radius, enemy, entered, exited);
        this.renderComponent = renderComponent;
        this.shoot = shoot;
        this.stopShoot = stopShoot;
    }

    @Override
    public void update() {

        for (Entity enemy : enemies) {
            boolean isInFOV = entityIsInFOV.get(enemy);

            if (!isInFOV && enemyIsInFOV(enemy)) {
                entityIsInFOV.put(enemy, true);
                shoot.call(enemy);

            } else if (isInFOV && !enemyIsInFOV(enemy)) {
                entityIsInFOV.put(enemy, false);
                startAtlasRotation();
                stopShoot.call(enemy);
            }
        }
    }

    public boolean enemyIsInFOV(Entity enemy) {

        if (this.entityIsInProximity(enemy)) {
            if (renderComponent.getRotation() == Rotation.NORTH && enemy.getPosition().angleDeg() < 90) {
                return true;
            } else if (renderComponent.getRotation() == Rotation.SOUTH && enemy.getPosition().angleDeg() > 90) {
                return true;
            } else if (renderComponent.getRotation() == Rotation.EAST && enemy.getPosition().angleDeg() < 180) {
                return true;
            } else if (renderComponent.getRotation() == Rotation.WEST && enemy.getPosition().angleDeg() > 180) {
                return true;
            }
        }
        return false;
    }

    public interface FOVFunc {
        void call(Entity enemy);
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

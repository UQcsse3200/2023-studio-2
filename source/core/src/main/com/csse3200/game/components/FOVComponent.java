package com.csse3200.game.components;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.structures.Rotation;
import com.csse3200.game.components.structures.RotationRenderComponent;
import com.csse3200.game.components.structures.TurretTargetableComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.services.ServiceLocator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FOVComponent extends ProximityActivationComponent {
    private final Map<Entity, Boolean> enemyIsInFOV = new HashMap<>();

    public FOVComponent(float radius, ProximityFunc entered, ProximityFunc exited) {
        super(radius, entered, exited);
    }

    @Override
    public void update() {
        var entities = ServiceLocator.getEntityService().getEntitiesByComponent(TurretTargetableComponent.class);

        for (Entity enemy : entities) {
            var turretTargetableComponent = entity.getComponent(TurretTargetableComponent.class);

            boolean isInFOV = turretTargetableComponent.isInFov();

            if (!isInFOV && enemyIsInFOV(enemy)) {
                turretTargetableComponent.setInFov(true);
                entered.call(enemy);

            } else if (isInFOV && !enemyIsInFOV(enemy)) {
                turretTargetableComponent.setInFov(false);
                exited.call(enemy);
            }
            else if (isInFOV) {
                entered.call(enemy);
            }
        }
    }

    public boolean enemyIsInFOV(Entity enemy) {
        float distance = this.entity.getCenterPosition().dst(enemy.getCenterPosition());
        return distance <= this.radius;
    }

    public interface FOVFunc {
        void call(ArrayList<Entity> entities);
    }
}

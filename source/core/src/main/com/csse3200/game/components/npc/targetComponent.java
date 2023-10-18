package com.csse3200.game.components.npc;

import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.components.Component;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.EnemyConfig;
import com.csse3200.game.entities.factories.EnemyFactory;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.services.ServiceLocator;

import java.util.List;

public class targetComponent extends Component {

    private final EnemyConfig config;
    private final AITaskComponent aiComponent;

    public targetComponent(EnemyConfig config, AITaskComponent aiComponent) {
        this.config = config;
        this.aiComponent = aiComponent;
    }
    public void create() {
        ServiceLocator.getGameArea().getEvent().addListener("reTarget", this::updateTargets);
    }
    public void updateTargets() {
        aiComponent.dispose();
        List<Entity> targets = ServiceLocator.getEntityService().getEntitiesByComponent(HitboxComponent.class);
        for (Entity target : targets) {
            // Adds the specific behaviour to entity
            EnemyFactory.enemyBehaviourSelector(target, config, aiComponent);
        }
    }
}

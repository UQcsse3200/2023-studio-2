package com.csse3200.game.components.npc;

import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.components.Component;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.EnemyConfig;
import com.csse3200.game.entities.factories.EnemyFactory;
import com.csse3200.game.entities.factories.EnemyFactoryAdapter;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.services.ServiceLocator;

import java.util.List;

import static com.csse3200.game.entities.factories.EnemyFactory.enemyBehaviourSelector;

public class targetComponent extends Component {

    private final EnemyConfig config;
    private final AITaskComponent aiComponent;
    private EnemyFactoryAdapter enemyFactoryAdapter;

    public targetComponent(EnemyConfig config, AITaskComponent aiComponent) {
        this.config = config;
        this.aiComponent = aiComponent;
    }

    public targetComponent(EnemyConfig config, AITaskComponent aiComponent, EnemyFactoryAdapter enemyFactoryAdapter) {
        this.config = config;
        this.aiComponent = aiComponent;
        this.enemyFactoryAdapter = enemyFactoryAdapter;
    }


    public void create() {
        ServiceLocator.getGameArea().getEvent().addListener("reTarget", this::updateTargets);
    }
    public void updateTargets() {
        aiComponent.dispose();
        List<Entity> targets = ServiceLocator.getEntityService().getEntitiesByComponent(HitboxComponent.class);
        for (Entity target : targets) {
            // Adds the specific behaviour to entity
            enemyFactoryAdapter.enemyBehaviourSelector(target, config, aiComponent);
        }
    }
}

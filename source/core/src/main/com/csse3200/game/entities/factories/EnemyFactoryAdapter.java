package com.csse3200.game.entities.factories;

import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.EnemyConfig;

public class EnemyFactoryAdapter {
    public void enemyBehaviourSelector(Entity target, EnemyConfig config, AITaskComponent aiComponent) {
        EnemyFactory.enemyBehaviourSelector(target, config, aiComponent);
    }
}


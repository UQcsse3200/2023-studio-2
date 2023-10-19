package com.csse3200.game.components.npc;

import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.EnemyConfig;
import com.csse3200.game.entities.factories.EnemyFactoryAdapter;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.entities.EntityService;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import java.util.Arrays;
import java.util.List;

public class targetComponentTest {

    private targetComponent componentUnderTest;
    private AITaskComponent aiComponent;
    private EnemyConfig config;
    private EnemyFactoryAdapter enemyFactoryAdapter;

    @Before
    public void setUp() {
        // Mock dependencies
        aiComponent = Mockito.mock(AITaskComponent.class);
        config = Mockito.mock(EnemyConfig.class);
        enemyFactoryAdapter = Mockito.mock(EnemyFactoryAdapter.class);

        componentUnderTest = new targetComponent(config, aiComponent, enemyFactoryAdapter);
    }

    @Test
    public void testUpdateTargets() {
        // Mock ServiceLocator and its returned EntityService within the scope of this test
        try (MockedStatic<ServiceLocator> serviceLocatorMockedStatic = Mockito.mockStatic(ServiceLocator.class)) {
            EntityService entityServiceMock = Mockito.mock(EntityService.class);
            serviceLocatorMockedStatic.when(ServiceLocator::getEntityService).thenReturn(entityServiceMock);

            List<Entity> mockEntities = Arrays.asList(Mockito.mock(Entity.class), Mockito.mock(Entity.class));
            Mockito.when(entityServiceMock.getEntitiesByComponent(HitboxComponent.class)).thenReturn(mockEntities);

            componentUnderTest.updateTargets();

            // Verify aiComponent.dispose() is called
            Mockito.verify(aiComponent).dispose();

            // Verify enemyBehaviourSelector(...) is called for each entity using the adapter
            Mockito.verify(enemyFactoryAdapter, Mockito.times(2)).enemyBehaviourSelector(
                    Mockito.any(Entity.class), Mockito.eq(config), Mockito.eq(aiComponent)
            );
        }
    }
}



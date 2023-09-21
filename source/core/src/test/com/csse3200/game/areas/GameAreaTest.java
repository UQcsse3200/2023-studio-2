package com.csse3200.game.areas;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.badlogic.gdx.math.GridPoint2;
import com.csse3200.game.areas.terrain.TerrainComponent;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;

@ExtendWith(GameExtension.class)
class GameAreaTest {
    @Mock
    TerrainFactory factory = mock(TerrainFactory.class);
    TerrainComponent testTerrain = factory.createTerrain("map/base.tmx");

    @BeforeEach
    void loadGameArea() {
        GameArea gameArea =
                new GameArea() {
                    @Override
                    public void create() {
                        terrain = testTerrain;
                    }
                };
        ServiceLocator.registerGameArea(gameArea);
    }

  @Test
  void getTerrainTest() {
      assertEquals(testTerrain, ServiceLocator.getGameArea().getTerrain());
  }

  @Test
  void shouldSpawnEntities() {
    ServiceLocator.registerEntityService(new EntityService());
    Entity entity = mock(Entity.class);
    entity.setPosition(0,0);

    ServiceLocator.getGameArea().spawnEntity(entity);
    verify(entity).create();

    ServiceLocator.getGameArea().dispose();
    verify(entity).dispose();
  }
}

package com.csse3200.game.entities;

import com.csse3200.game.components.*;
import com.csse3200.game.components.gamearea.PerformanceDisplay;
import com.csse3200.game.components.player.InventoryComponent;
import com.csse3200.game.components.player.KeyboardPlayerInputComponent;
import com.csse3200.game.components.player.PlayerActions;
import com.csse3200.game.components.player.PlayerStatsDisplay;
import com.csse3200.game.extensions.GameExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.*;


@ExtendWith(GameExtension.class)
class EntityServiceTest {
  @Test
  void shouldCreateEntity() {
    EntityService entityService = new EntityService();
    Entity entity = spy(Entity.class);
    entityService.register(entity);
    verify(entity).create();
  }

  @Test
  void shouldUpdateEntities() {
    EntityService entityService = new EntityService();
    Entity entity = spy(Entity.class);
    entityService.register(entity);
    entityService.update();

    verify(entity).update();
    verify(entity).earlyUpdate();
  }

  @Test
  void shouldNotUpdateUnregisteredEntities() {
    EntityService entityService = new EntityService();
    Entity entity = spy(Entity.class);
    entityService.register(entity);
    entityService.unregister(entity);
    entityService.update();
    verify(entity, times(0)).update();
    verify(entity, times(0)).earlyUpdate();
  }

  @Test
  void shouldDisposeEntities() {
    EntityService entityService = new EntityService();
    Entity entity = mock(Entity.class);
    entityService.register(entity);
    entityService.dispose();
    verify(entity).dispose();
  }

  /*
  Creating a test for array list in EntityService Class.
   */
  @Test
  void testGetEntitiesByComponent() {
    EntityService entityService = new EntityService();
// Checking whether the below components are in list
    Assertions.assertEquals(entityService.getEntitiesByComponent(PowerupComponent.class).size(), 0);
    Assertions.assertEquals(entityService.getEntitiesByComponent(InventoryComponent.class).size(), 0);
    Assertions.assertEquals(entityService.getEntitiesByComponent(KeyboardPlayerInputComponent.class).size(), 0);
    Assertions.assertEquals(entityService.getEntitiesByComponent(PlayerActions.class).size(), 0);
    Assertions.assertEquals(entityService.getEntitiesByComponent(PlayerStatsDisplay.class).size(), 0);
    Assertions.assertEquals(entityService.getEntitiesByComponent(CameraComponent.class).size(), 0);
    Assertions.assertEquals(entityService.getEntitiesByComponent(CombatStatsComponent.class).size(), 0);
    Assertions.assertEquals(entityService.getEntitiesByComponent(TouchAttackComponent.class).size(), 0);
    Assertions.assertEquals(entityService.getEntitiesByComponent(PerformanceDisplay.class).size(), 0);

    List<Entity> entity =  entityService.getEntitiesByComponent(CombatStatsComponent.class);
    Assertions.assertEquals(0, entity.size());

  }
// Creates test as in general
  private Component componentA,componentB;
  private Entity entityA,entityB;
  private EntityService entityService;

  //Creating objects for Entity and Component

  @BeforeEach
  void setup() {
    entityService = new EntityService();
    entityA = new Entity();
    entityB = new Entity();
    componentA = new Component();
    componentB = new Component();
  }
  @Test
 public void isTrueGetEntitiesByComponents() {

    entityA.addComponent(componentA);
    entityB.addComponent(componentB);

    entityService.register(entityA);
    entityService.register(entityB);

    List<Entity> test =  entityService.getEntitiesByComponent(componentA.getClass());
    Assertions.assertEquals(2, test.size());

  }

}
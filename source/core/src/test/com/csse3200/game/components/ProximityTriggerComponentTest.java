package com.csse3200.game.components;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.services.ServiceLocator;
import net.dermetfan.gdx.physics.box2d.PositionController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ExtendWith(GameExtension.class)
class ProximityTriggerComponentTest {
    @Mock
    EntityService entityService;
    List<Entity> entityList;

    @BeforeEach
    void beforeEach() {
        ServiceLocator.registerEntityService(entityService);

        entityList = new ArrayList<Entity>();

        when(entityService.getEntitiesByComponent(FlagComponent.class)).thenReturn(entityList);
    }

    @Test
    void testUpdateWithOneFlagEntityInRange() {
        entityList.add(mockEntityAtPosition(new Vector2(5, 5)));

        var triggerFunc = mock(ProximityTriggerComponent.TriggerFunc.class);

        var component = new ProximityTriggerComponent(FlagComponent.class, 10, triggerFunc);

        var entity = mock(Entity.class);
        when(entity.getCenterPosition()).thenReturn(new Vector2(0, 0));

        component.setEntity(entity);

        component.update();

        verify(triggerFunc, times(1)).call();
    }

    @Test
    void testUpdateWithOneFlagEntityOutOfRange() {
        entityList.add(mockEntityAtPosition(new Vector2(10, 10)));

        var triggerFunc = mock(ProximityTriggerComponent.TriggerFunc.class);

        var component = new ProximityTriggerComponent(FlagComponent.class, 10, triggerFunc);

        var entity = mock(Entity.class);
        when(entity.getCenterPosition()).thenReturn(new Vector2(0, 0));

        component.setEntity(entity);

        component.update();

        verify(triggerFunc, never()).call();
    }

    @Test
    void testUpdateWithOneFlagEntityBoundaryCondition() {
        entityList.add(mockEntityAtPosition(new Vector2(10, 0)));

        var triggerFunc = mock(ProximityTriggerComponent.TriggerFunc.class);

        var component = new ProximityTriggerComponent(FlagComponent.class, 10, triggerFunc);

        var entity = mock(Entity.class);
        when(entity.getCenterPosition()).thenReturn(new Vector2(0, 0));

        component.setEntity(entity);

        component.update();

        verify(triggerFunc, times(1)).call();
    }

    @Test
    void testUpdateWithOneFlagEntityInRangeMultipleOut() {
        entityList.add(mockEntityAtPosition(new Vector2(10, 10)));
        entityList.add(mockEntityAtPosition(new Vector2(15, 10)));
        entityList.add(mockEntityAtPosition(new Vector2(10, 2)));
        entityList.add(mockEntityAtPosition(new Vector2(10, 16)));
        entityList.add(mockEntityAtPosition(new Vector2(12, 110)));
        entityList.add(mockEntityAtPosition(new Vector2(5, 5)));

        var triggerFunc = mock(ProximityTriggerComponent.TriggerFunc.class);

        var component = new ProximityTriggerComponent(FlagComponent.class, 10, triggerFunc);

        var entity = mock(Entity.class);
        when(entity.getCenterPosition()).thenReturn(new Vector2(0, 0));

        component.setEntity(entity);

        component.update();

        verify(triggerFunc, times(1)).call();
    }

    @Test
    void testUpdateWithMultipleFlagEntitiesInRange() {
        entityList.add(mockEntityAtPosition(new Vector2(10, 10)));
        entityList.add(mockEntityAtPosition(new Vector2(15, 10)));
        entityList.add(mockEntityAtPosition(new Vector2(10, 2)));
        entityList.add(mockEntityAtPosition(new Vector2(10, 16)));
        entityList.add(mockEntityAtPosition(new Vector2(12, 110)));
        entityList.add(mockEntityAtPosition(new Vector2(5, 5)));

        var triggerFunc = mock(ProximityTriggerComponent.TriggerFunc.class);

        var component = new ProximityTriggerComponent(FlagComponent.class, 1000, triggerFunc);

        var entity = mock(Entity.class);
        when(entity.getCenterPosition()).thenReturn(new Vector2(0, 0));

        component.setEntity(entity);

        component.update();

        // should only be triggered once still
        verify(triggerFunc, times(1)).call();
    }

    @Test
    void testUpdateWithMultipleFlagEntitiesOutOfRange() {
        entityList.add(mockEntityAtPosition(new Vector2(5, 5)));
        entityList.add(mockEntityAtPosition(new Vector2(2, 1)));
        entityList.add(mockEntityAtPosition(new Vector2(0, 10)));
        entityList.add(mockEntityAtPosition(new Vector2(1, 5)));

        var triggerFunc = mock(ProximityTriggerComponent.TriggerFunc.class);

        var component = new ProximityTriggerComponent(FlagComponent.class, 1, triggerFunc);

        var entity = mock(Entity.class);
        when(entity.getCenterPosition()).thenReturn(new Vector2(0, 0));

        component.setEntity(entity);

        component.update();

        // should only be triggered once still
        verify(triggerFunc, never()).call();
    }

    Entity mockEntityAtPosition(Vector2 position) {
        var entity = mock(Entity.class);
        lenient().when(entity.getCenterPosition()).thenReturn(position);
        return entity;
    }

    public class FlagComponent extends Component {

    }
}
package com.csse3200.game.components.joinable;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.csse3200.game.components.Component;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.StructurePlacementService;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.MockitoRule;
import org.mockito.junit.jupiter.MockitoSettings;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@MockitoSettings
class JoinableComponentTest {
    @Mock
    StructurePlacementService placementService;
    @Mock
    TextureAtlas textureAtlas;
    @Mock
    JoinableComponentShapes shapes;
    @Mock
    Entity entity;
    @Mock
    ColliderComponent colliderComponent;
    @Mock
    SpriteBatch batch;
    @Mock
    TextureAtlas.AtlasRegion atlasRegion;

    @Test
    void create() {
        ChainShape shape = mock(ChainShape.class);

        ServiceLocator.registerStructurePlacementService(placementService);
        ServiceLocator.registerRenderService(mock(RenderService.class));

        Entity joinableEntity = mock(Entity.class);
        JoinableComponent neighbourComponent = mock(JoinableComponent.class);
        when(joinableEntity.getComponent(JoinableComponent.class)).thenReturn(neighbourComponent);
        when(neighbourComponent.getJoinLayer()).thenReturn(JoinLayer.WALLS);

        when(placementService.getStructureAt(new GridPoint2(2,4))).thenReturn(joinableEntity);
        when(placementService.getStructureAt(new GridPoint2(4,2))).thenReturn(null);
        when(placementService.getStructureAt(new GridPoint2(0,2))).thenReturn(null);
        when(placementService.getStructureAt(new GridPoint2(2,0))).thenReturn(joinableEntity);
        when(placementService.getStructurePosition(entity)).thenReturn(new GridPoint2(2,2));

        when(entity.getComponent(ColliderComponent.class)).thenReturn(colliderComponent);
        when(shapes.getShape("up-down")).thenReturn(shape);
        when(textureAtlas.findRegion("up-down")).thenReturn(atlasRegion);
        when(textureAtlas.findRegion("no-connection")).thenReturn(mock(TextureAtlas.AtlasRegion.class));

        JoinableComponent component = new JoinableComponent(textureAtlas, JoinLayer.WALLS, shapes);

        component.setEntity(entity);
        component.create();

        verify(colliderComponent).setShape(shape);

        when(entity.getPosition()).thenReturn(new Vector2(2,2));
        when(entity.getScale()).thenReturn(new Vector2(1,1));

        component.render(batch);
        verify(batch).draw(atlasRegion, 2,2,1,1);
    }

    @Test
    void updateJoin() {
        ChainShape shape = mock(ChainShape.class);

        when(entity.getComponent(ColliderComponent.class)).thenReturn(colliderComponent);
        when(shapes.getShape("up")).thenReturn(shape);
        when(textureAtlas.findRegion("up")).thenReturn(atlasRegion);
        when(textureAtlas.findRegion("no-connection")).thenReturn(mock(TextureAtlas.AtlasRegion.class));

        JoinableComponent component = new JoinableComponent(textureAtlas, JoinLayer.WALLS, shapes);

        component.setEntity(entity);
        component.updateJoin(JoinDirection.UP, true);

        verify(colliderComponent).setShape(shape);

        when(entity.getPosition()).thenReturn(new Vector2(2,2));
        when(entity.getScale()).thenReturn(new Vector2(1,1));

        component.render(batch);
        verify(batch).draw(atlasRegion, 2,2,1,1);
    }

    @Test
    void notifyNeighbours() {
        ServiceLocator.registerStructurePlacementService(placementService);
        ServiceLocator.registerRenderService(mock(RenderService.class));

        Entity joinableEntity = mock(Entity.class);
        JoinableComponent neighbourComponent = mock(JoinableComponent.class);
        when(joinableEntity.getComponent(JoinableComponent.class)).thenReturn(neighbourComponent);
        when(neighbourComponent.getJoinLayer()).thenReturn(JoinLayer.WALLS);

        when(placementService.getStructureAt(new GridPoint2(2,4))).thenReturn(joinableEntity);
        when(placementService.getStructureAt(new GridPoint2(4,2))).thenReturn(null);
        when(placementService.getStructureAt(new GridPoint2(0,2))).thenReturn(null);
        when(placementService.getStructureAt(new GridPoint2(2,0))).thenReturn(joinableEntity);
        when(placementService.getStructurePosition(entity)).thenReturn(new GridPoint2(2,2));

        JoinableComponent component = new JoinableComponent(textureAtlas, JoinLayer.WALLS, shapes);
        component.setEntity(entity);

        component.notifyNeighbours(true);
        verify(neighbourComponent).updateJoin(JoinDirection.UP, true);
        verify(neighbourComponent).updateJoin(JoinDirection.DOWN, true);

        component.notifyNeighbours(false);
        verify(neighbourComponent).updateJoin(JoinDirection.UP, false);
        verify(neighbourComponent).updateJoin(JoinDirection.DOWN, false);
    }

    @Test
    void getJoinLayer() {
        JoinableComponent component = new JoinableComponent(textureAtlas, JoinLayer.WALLS, shapes);

        assertEquals(component.getJoinLayer(), JoinLayer.WALLS);
    }
}
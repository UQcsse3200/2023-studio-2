package com.csse3200.game.input;

import com.csse3200.game.areas.ExtractorMiniGameArea;
import com.csse3200.game.areas.terrain.TerrainComponent;
import com.csse3200.game.entities.Entity;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.Vector2;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SpannerInputComponentTest {

    private SpannerInputComponent inputComponent;

    @Test
    public void testTouchDownWithinBoundingBox() {
        TerrainComponent terrain = Mockito.mock(TerrainComponent.class);
        ExtractorMiniGameArea area = Mockito.mock(ExtractorMiniGameArea.class);
        inputComponent = new SpannerInputComponent(terrain, area);

        // Set up a bounding box for the entity
        Rectangle boundingBox = new Rectangle(1, 1, 2, 2);

        // Mock the behavior of the entity
        Mockito.when(terrain.unproject(Mockito.any(Vector3.class))).thenReturn(new Vector3(2, 2, 0));

        Entity entity = new Entity();
        entity.setPosition(new Vector2(1, 1));
        entity.setScale(new Vector2(2, 2));
        inputComponent.setEntity(entity);

        // Mock the creation of a cursor
        Pixmap cursorPixmap = Mockito.mock(Pixmap.class);
        Mockito.when(cursorPixmap.getWidth()).thenReturn(1);
        Mockito.when(cursorPixmap.getHeight()).thenReturn(1);

        assertFalse(inputComponent.touchDown(0, 0, 0, 0));
    }

    @Test
    public void testTouchDownOutsideBoundingBox() {
        TerrainComponent terrain = Mockito.mock(TerrainComponent.class);
        ExtractorMiniGameArea area = Mockito.mock(ExtractorMiniGameArea.class);
        inputComponent = new SpannerInputComponent(terrain, area);

        // Set up a bounding box for the entity
        Rectangle boundingBox = new Rectangle(1, 1, 2, 2);

        // Mock the behavior of the entity
        Mockito.when(terrain.unproject(Mockito.any(Vector3.class))).thenReturn(new Vector3(4, 4, 0));

        Entity entity = new Entity();
        entity.setPosition(new Vector2(1, 1));
        entity.setScale(new Vector2(2, 2));
        inputComponent.setEntity(entity);

        // Mock the creation of a cursor
        Pixmap cursorPixmap = Mockito.mock(Pixmap.class);
        Mockito.when(cursorPixmap.getWidth()).thenReturn(1);
        Mockito.when(cursorPixmap.getHeight()).thenReturn(1);

        assertFalse(inputComponent.touchDown(0, 0, 0, 0));
    }
}

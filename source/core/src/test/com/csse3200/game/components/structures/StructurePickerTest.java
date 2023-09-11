package com.csse3200.game.components.structures;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.csse3200.game.components.structures.tools.Tool;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)
class StructurePickerTest {
    @Mock
    RenderService renderService;
    @Mock
    ResourceService resourceService;

    @BeforeEach
    void beforeEach() {
        ServiceLocator.registerRenderService(renderService);
        ServiceLocator.registerResourceService(resourceService);
    }

    @AfterEach
    void afterEach() {
        ServiceLocator.clear();
    }

    @Test
    void setLevel() {
        var texture = mock(Texture.class);
        var stage = mock(Stage.class);

        when(resourceService.getAsset(any(), any())).thenReturn(texture);
        when(renderService.getStage()).thenReturn(stage);

        var structurePicker = new StructurePicker();
        structurePicker.create();

        // should be 0 by default
        assertEquals(structurePicker.getLevel(), 0);

        structurePicker.setLevel(1);
        assertEquals(structurePicker.getLevel(), 1);

        structurePicker.setLevel(100);
        assertEquals(structurePicker.getLevel(), 100);

        structurePicker.setLevel(0);
        assertEquals(structurePicker.getLevel(), 0);
    }

    @Test
    void dispose() {
        var structurePicker = new StructurePicker();
        structurePicker.dispose();

        verify(renderService).unregister(structurePicker);
    }

    @Test
    void testShowAndHide() {
        var structurePicker = new StructurePicker();

        structurePicker.show();

        assertTrue(structurePicker.isVisible());

        structurePicker.hide();

        assertFalse(structurePicker.isVisible());
    }

    @Test
    void testSetAndGetSelectedTool() {
        var structurePicker = new StructurePicker();

        var tool = mock(Tool.class);

        structurePicker.setSelectedTool(tool);

        assertEquals(tool, structurePicker.getSelectedTool());
    }

    @Test
    void interact() {
        var structurePicker = new StructurePicker();

        var tool = mock(Tool.class);
        var player = mock(Entity.class);
        var position = mock(GridPoint2.class);

        structurePicker.setSelectedTool(tool);

        structurePicker.interact(player, position);

        verify(tool).interact(player, position);

        structurePicker.setSelectedTool(null);

        reset(tool);

        structurePicker.interact(player, position);

        verify(tool, never()).interact(any(), any());
    }
}
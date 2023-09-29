package com.csse3200.game.components.structures;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
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
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)
class StructureToolPickerTest {
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
    void dispose() {
        var structurePicker = new StructureToolPicker();
        structurePicker.dispose();

        verify(renderService).unregister(structurePicker);
    }

    @Test
    void testShowAndHide() {
        var structurePicker = new StructureToolPicker();

        structurePicker.show();

        assertTrue(structurePicker.isVisible());

        structurePicker.hide();

        assertFalse(structurePicker.isVisible());
    }

    @Test
    void testSetAndGetSelectedTool() {
        var structurePicker = new StructureToolPicker();

        var tool = mock(Tool.class);

        structurePicker.setSelectedTool(tool);

        assertEquals(tool, structurePicker.getSelectedTool());
    }

    @Test
    void interact() {
        var structurePicker = new StructureToolPicker();

        var tool = mock(Tool.class);
        var player = mock(Entity.class);
        var position = mock(GridPoint2.class);

        structurePicker.setSelectedTool(tool);
        structurePicker.setEntity(player);

        structurePicker.interact(position);

        verify(tool).interact(player, position);

        structurePicker.setSelectedTool(null);

        reset(tool);

        structurePicker.interact(position);

        verify(tool, never()).interact(any(), any());
    }
}
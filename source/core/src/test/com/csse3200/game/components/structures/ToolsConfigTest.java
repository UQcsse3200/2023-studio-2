package com.csse3200.game.components.structures;

import com.badlogic.gdx.utils.ObjectMap;
import com.csse3200.game.components.structures.tools.Tool;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.files.FileLoader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@ExtendWith(GameExtension.class)
class ToolsConfigTest {
    @Test
    void testToolsConfig() {
        ToolsConfig toolsConfig =
                FileLoader.readClass(ToolsConfig.class, "configs/structure_tools.json");

        assertNotNull(toolsConfig);
        assertNotNull(toolsConfig.toolConfigs);

        // shouldn't be empty
        assertNotEquals(toolsConfig.toolConfigs.size, 0);

        // each structureOption should not be null
        for (var structureOption : toolsConfig.toolConfigs) {
            assertNotNull(structureOption);

            assertNotNull(structureOption.value);

            // assuming png for now. may need to allow other extensions at some point
            assertTrue(structureOption.value.texture.endsWith(".png"));

            // description cannot be null
            assertNotNull(structureOption.value.description);

            // cost can't be null
            assertNotNull(structureOption.value.cost);

            // checks that the class specified exists
            Class<?> cls;
            try {
                cls = Class.forName(structureOption.key);
            } catch (ClassNotFoundException e) {
                fail(String.format("The class with name %s does not exist!", structureOption.key));
                return;
            }

            // checks that it can be created with the necessary constructor
            Object object;
            try {
                object = cls.getDeclaredConstructor(ObjectMap.class).newInstance(mock(ObjectMap.class));
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                fail(String.format("The class with name %s cannot be instantiated using a " +
                        "constructor taking an ObjectMap<String, Integer>!", structureOption.key));
                return;
            }

            // checks that it is in fact a tool and not entered by a tool.
            assertTrue(object instanceof Tool);
        }
    }
}
package com.csse3200.game.components.structures.tools;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.utils.ObjectMap;
import com.csse3200.game.entities.Entity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ToolTest {

    @Test
    void testState() {
        ObjectMap<String, Integer> cost = new ObjectMap<>();
        cost.put("resource1", 10);
        cost.put("resource2", 20);

        var tool = new MockTool(cost, 0, "image.png");

        assertEquals(tool.getCost(), cost);
        assertEquals(tool.getTexture(), "image.png");
    }
}

class MockTool extends Tool {
    public MockTool(ObjectMap<String, Integer> cost, int ordering, String texture) {
        super(cost, ordering, texture);
    }

    @Override
    protected void performInteraction(Entity player, GridPoint2 position) {

    }

    @Override
    protected ToolResponse canInteract(Entity player, GridPoint2 position) {
        return null;
    }
}
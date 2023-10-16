package com.csse3200.game.components.structures.tools;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.utils.ObjectMap;
import com.csse3200.game.entities.Entity;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

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

    @Test
    void testOrdering() {
        ObjectMap<String, Integer> cost = new ObjectMap<>();
        List<Tool> tools = new ArrayList<>();
        tools.add(new MockTool(cost, 0, "image.png"));
        tools.add(new MockTool(cost, 5, "image.png"));
        tools.add(new MockTool(cost, 2, "image.png"));
        tools.add(new MockTool(cost, 12, "image.png"));
        tools.add(new MockTool(cost, 3, "image.png"));

        Collections.sort(tools);

        var prevOrder = 0;
        for (var tool : tools) {
            if (tool.getOrdering() < prevOrder) {
                fail();
            }

            prevOrder = tool.getOrdering();
        }
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
package com.csse3200.game.components.structures.tools;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ObjectMap;
import com.csse3200.game.entities.Entity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ToolTest {

    @Test
    void testState() {
        ObjectMap<String, Integer> cost = new ObjectMap<>();
        cost.put("resource1", 10);
        cost.put("resource2", 20);

        var tool = new MockTool(cost, 5f, "image.png", 0);

        assertEquals(tool.getCost(), cost);
        assertEquals(tool.getTexture(), "image.png");
    }

    @Test
    void testOrdering() {
        ObjectMap<String, Integer> cost = new ObjectMap<>();
        List<Tool> tools = new ArrayList<>();
        tools.add(new MockTool(cost, 5f, "image.png", 0));
        tools.add(new MockTool(cost, 5f, "image.png", 5));
        tools.add(new MockTool(cost, 5f, "image.png", 2));
        tools.add(new MockTool(cost, 5f, "image.png", 12));
        tools.add(new MockTool(cost, 5f, "image.png", 3));

        Collections.sort(tools);

        var prevOrder = 0;
        for (var tool : tools) {
            if (tool.getOrdering() < prevOrder) {
                fail();
            }

            prevOrder = tool.getOrdering();
        }
    }

    @Test
    void TestCanPlaceOutOfRange() {
        ObjectMap<String, Integer> cost = new ObjectMap<>();
        var tool = new MockTool(cost, 5f, "image.png", 0);

        var player = mock(Entity.class);
        when(player.getCenterPosition()).thenReturn(new Vector2(0,0));

        // out of range by one tile
        Assertions.assertFalse(tool.canInteract(player, new GridPoint2(6, 0)).isValid());
        // out of range on the diagonal
        Assertions.assertFalse(tool.canInteract(player, new GridPoint2(4, 4)).isValid());

        var player2 = mock(Entity.class);
        when(player2.getCenterPosition()).thenReturn(new Vector2(10,10));

        // test being converted to world tile position before distance is calculated.
        Assertions.assertFalse(tool.canInteract(player, new GridPoint2(10, 10)).isValid());
    }

    @Test
    void TestCanPlaceInRange() {
        ObjectMap<String, Integer> cost = new ObjectMap<>();
        var tool = new MockTool(cost, 5f, "image.png", 0);

        var player = mock(Entity.class);
        when(player.getCenterPosition()).thenReturn(new Vector2(0,0));

        // test right in the middle
        Assertions.assertTrue(tool.canInteract(player, new GridPoint2(0, 0)).isValid());
        // test right on the edge
        Assertions.assertTrue(tool.canInteract(player, new GridPoint2(0, 5)).isValid());
        // test right on the edge diagonally
        Assertions.assertTrue(tool.canInteract(player, new GridPoint2(3, 4)).isValid());

        var player2 = mock(Entity.class);
        when(player2.getCenterPosition()).thenReturn(new Vector2(10,10));

        // test being converted to world tile position before distance is calculated.
        Assertions.assertTrue(tool.canInteract(player2, new GridPoint2(20, 20)).isValid());
    }
}

class MockTool extends Tool {
    public MockTool(ObjectMap<String, Integer> cost, float range, String texture, int ordering) {
        super(cost, range, texture, ordering);
    }

    @Override
    protected void performInteraction(Entity player, GridPoint2 position) {

    }
}
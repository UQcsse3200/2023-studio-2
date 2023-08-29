package com.csse3200.game.components.joinable;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.utils.ObjectMap;

/**
 * This class is used to read-in and store the vertices for entities
 * with a JoinableComponent attached.
 */
public class JoinableComponentShapes {
    public ObjectMap<String, Vector2[]> vertices;

    /**
     * Gets the shape with the given id.
     *
     * @param id - the cardinalities' id.
     * @return the shape for the given cardinality id.
     */
    public ChainShape getShape(String id) {
        ChainShape shape = new ChainShape();
        shape.createChain(vertices.get(id));
        return shape;
    }
}

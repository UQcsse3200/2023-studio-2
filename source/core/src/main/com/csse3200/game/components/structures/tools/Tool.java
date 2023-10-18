package com.csse3200.game.components.structures.tools;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ObjectMap;
import com.csse3200.game.entities.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A tool which can be selected via the StructureToolPicker and can be interacted with by the player.
 * This class must be inherited and interact implemented to function.
 */
public abstract class Tool implements Comparable<Tool> {
    private static final Logger logger = LoggerFactory.getLogger(Tool.class);

    /**
     * The cost associated with the tool.
     */
    protected final ObjectMap<String, Integer> cost;
    private final int ordering;
    private final float range;
    private String texture;

    /**
     * Creates a new Tool with the given cost.
     *
     * @param cost     - the cost of using the tool.
     * @param range    - how far away the tool can be used.
     * @param texture  - the texture of this tool.
     * @param ordering - the ordering of this tool.
     */
    protected Tool(ObjectMap<String, Integer> cost, float range, String texture, int ordering) {
        this.cost = cost;
        this.range = range;
        this.ordering = ordering;
        this.texture = texture;
    }

    /**
     * Interacts with the given position in the structure grid.
     *
     * @param player - the player interacting with the tool.
     * @param position - the position being interacted with.
     *
     * it triggers the alert sound on invalid position
     * and also triggers the structure placing sound on valid position
     */
    public void interact(Entity player, GridPoint2 position) {
        var validity = canInteract(player, position);

        if (!validity.isValid()) {
            if (validity.isError()) {
                logger.error(validity.getMessage());
            }

            player.getEvents().trigger("displayWarningAtPosition", validity.getMessage(),
                    new Vector2((float) position.x / 2, (float) position.y / 2));
            player.getEvents().trigger("playSound", "alert");
            return;
        }
        player.getEvents().trigger("playSound", "structurePlace");
        performInteraction(player, position);
    }

    /**
     * Peforms the tools interaction at the given position.
     * @param player - the player using the tool.
     * @param position - the position to use the tool.
     */
    protected abstract void performInteraction(Entity player, GridPoint2 position);

    /**
     * Returns whether the player can interact at the given position.
     * @param player - the player attempting to interact.
     * @param position - the position to interact.
     * @return whether the player can interact at the position.
     */
    protected ToolResponse canInteract(Entity player, GridPoint2 position) {
        var distance = player.getCenterPosition().scl(2).dst(new Vector2(position.x, position.y));

        if (distance <= range) {
            return ToolResponse.valid();
        }

        return new ToolResponse(PlacementValidity.OUT_OF_RANGE, "Out of range.");
    }

    /**
     * Gets the cost associated with the tool.
     * @return the associated cost.
     */
    public ObjectMap<String, Integer> getCost() {
        return cost;
    }

    /**
     * Returns whether this tool is equal to the given object.
     * @param obj - the other object to test equality with.
     * @return whether this tool is equal to the given object.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (this.getClass() != obj.getClass()) {
            return false;
        }

        return this.getCost() == ((Tool) obj).getCost();
    }

    /**
     * Compares two tools. Used for sorting.
     * @param o the object to be compared.
     * @return negative if this tool is less than the other tool,
     *         0 if equal, and positive if this tool is greater
     *         than the other tool.
     */
    @Override
    public int compareTo(Tool o) {
        return this.ordering - o.ordering;
    }

    /**
     * Gets the tools texture.
     * @return the tools texture.
     */
    public String getTexture() {
        return texture;
    }

    /**
     * Gets the ordering of the tool.
     * @return the tools ordering.
     */
    public int getOrdering(){
        return ordering;
    }
}

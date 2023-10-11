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
public abstract class Tool {
    private static final Logger logger = LoggerFactory.getLogger(Tool.class);

    /**
     * The cost associated with the tool.
     */
    protected final ObjectMap<String, Integer> cost;

    /**
     * Creates a new Tool with the given cost.
     * @param cost - the cost of using the tool.
     */
    protected Tool(ObjectMap<String, Integer> cost) {
        this.cost = cost;
    }

    /**
     * Interacts with the given position in the structure grid.
     *
     * @param player - the player interacting with the tool.
     * @param position - the position being interacted with.
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
    protected abstract ToolResponse canInteract(Entity player, GridPoint2 position);

    /**
     * Gets the cost associated with the tool.
     * @return the associated cost.
     */
    public ObjectMap<String, Integer> getCost() {
        return cost;
    }


}

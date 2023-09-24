package com.csse3200.game.components.upgradetree;

import com.csse3200.game.components.Weapons.WeaponType;
import com.csse3200.game.components.structures.ToolConfig;
import com.csse3200.game.entities.configs.WeaponConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a node within the upgrade tree.
 * Each node holds information about a particular weapon upgrade, its image, and its children in the tree.
 */
public class UpgradeNode {

    /** Represents the name of the weapon/tool as a string **/
    private final String name;

    /** The current node's parent node **/
    private UpgradeNode parent;

    /** Path to the image that represents the weapon upgrade. */
    private final String imagePath;

    /** Type of weapon this node represents. */
    private final WeaponType weaponType;

    /** List of child nodes, representing subsequent potential upgrades. */
    private final List<UpgradeNode> children;

    /** X-coordinate of the node, typically used for UI positioning. */
    private float x;

    /** Y-coordinate of the node, typically used for UI positioning. */
    private float y;

    /** Depth of the tree */
    private int depth;

    /** Cost of the root node in Nebulite materials **/
    private static final int BASE_COST = 100;

    /** Sets the parent of the current node**/
    public void setParent(UpgradeNode parent) {
        this.parent = parent;
    }

    /** Returns the current node's parent node**/
    public UpgradeNode getParent() {
        return parent;
    }

    /**
     * Constructs a new UpgradeNode with the given weapon type and image path.
     * @param config - The weapons config file
     */
    UpgradeNode(WeaponConfig config) {
        this.name = config.type.toString();
        this.imagePath = config.imagePath;
        this.weaponType = config.type;
        this.children = new ArrayList<>();
        this.depth = 0;
    }

    UpgradeNode(ToolConfig tool) {
        this.name = tool.name;
        this.imagePath = tool.texture;
        this.weaponType = null;
        this.children = new ArrayList<>();
        this.depth = 0;
    }

    /** Gets the name of the tool/weapon **/
    public String getName() {
        return this.name;
    }

    /** Sets the current node's depth **/
    public void setDepth(int depth) {
        this.depth = depth;
    }

    /** Returns the current node's depth **/
    public int getDepth() {
        return depth;
    }

    /**
     * Adds a child node to this node.
     * @param child The child node to be added.
     */
    public void addChild(UpgradeNode child) {
        child.setParent(this);
        this.children.add(child);
    }

    /**
     * Sets the x-coordinate of the node.
     * @param x The x-coordinate.
     */
    public void setX(float x) {
        this.x = x;
    }

    /**
     * Sets the y-coordinate of the node.
     * @param y The y-coordinate.
     */
    public void setY(float y) {
        this.y = y;
    }

    /**
     * Returns the x-coordinate of the node.
     * @return The x-coordinate.
     */
    public float getX() {
        return x;
    }

    /**
     * Returns the y-coordinate of the node.
     * @return The y-coordinate.
     */
    public float getY() {
        return y;
    }

    /**
     * Returns the type of weapon this node represents.
     * @return The weapon type.
     */
    public WeaponType getWeaponType() {
        return weaponType;
    }

    /**
     * Returns a list of child nodes.
     * @return The child nodes.
     */
    public List<UpgradeNode> getChildren() {
        return children;
    }

    /**
     * Returns the image path associated with this node.
     * @return The image path.
     */
    public String getImagePath() {
        return imagePath;
    }

    /**
     * Gets the selected nodes upgrade cost
     *
     * @return int: the nodes cost
     */
    public int getNodeCost() {
        return BASE_COST * (this.getDepth() + 1);
    }
}

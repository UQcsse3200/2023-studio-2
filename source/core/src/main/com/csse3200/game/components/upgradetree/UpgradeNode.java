package com.csse3200.game.components.upgradetree;

import com.csse3200.game.components.Weapons.WeaponType;
import com.csse3200.game.entities.configs.WeaponConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a node within the upgrade tree.
 * Each node holds information about a particular weapon upgrade, its image, and its children in the tree.
 */
public class UpgradeNode {

    /** Path to the image that represents the weapon upgrade. */
    private String imagePath;

    /** Type of weapon this node represents. */
    private WeaponType weaponType;

    /** List of child nodes, representing subsequent potential upgrades. */
    private List<UpgradeNode> children;

    /** X-coordinate of the node, typically used for UI positioning. */
    private float x;

    /** Y-coordinate of the node, typically used for UI positioning. */
    private float y;

    /** Depth of the tree */
    private int depth;

    /**
     * Constructs a new UpgradeNode with the given weapon type and image path.
     * @param config - The weapons config file
     */
    UpgradeNode(WeaponConfig config, WeaponType weaponType) {
        this.imagePath = config.imagePath;
        this.weaponType = weaponType;
        this.children = new ArrayList<>();
        this.depth = 0;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public int getDepth() {
        return depth;
    }

    /**
     * Adds a child node to this node.
     * @param child The child node to be added.
     */
    public void addChild(UpgradeNode child) {
        children.add(child);
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
}

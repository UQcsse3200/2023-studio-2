package com.csse3200.game.components.upgradetree;

import com.csse3200.game.components.Weapons.WeaponType;

import java.util.ArrayList;
import java.util.List;

public class UpgradeNode {
    private String imagePath;
    private WeaponType weaponType;
    private List<UpgradeNode> children;
    private float x;
    private float y;

    UpgradeNode(WeaponType weaponType, String imagePath) {
        this.imagePath = imagePath;
        this.weaponType = weaponType;
        this.children = new ArrayList<>();
    }

    public void addChild(UpgradeNode child) {
        children.add(child);
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public WeaponType getWeaponType() {
        return weaponType;
    }

    public List<UpgradeNode> getChildren() {
        return children;
    }

    public String getImagePath() {
        return imagePath;
    }
}

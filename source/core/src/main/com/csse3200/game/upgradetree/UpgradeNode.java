package com.csse3200.game.upgradetree;

import com.csse3200.game.components.Weapons.WeaponType;

import java.util.ArrayList;
import java.util.List;

public class UpgradeNode {
    String imagePath;
    WeaponType weaponType;
    List<UpgradeNode> children;
    float x;
    float y;

    UpgradeNode(WeaponType weaponType, String imagePath) {
        this.imagePath = imagePath;
        this.weaponType = weaponType;
        this.children = new ArrayList<>();
    }

    public void addChild(UpgradeNode child) {
        children.add(child);
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}

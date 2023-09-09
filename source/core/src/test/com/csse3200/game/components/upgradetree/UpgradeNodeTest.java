package com.csse3200.game.components.upgradetree;

import com.csse3200.game.components.Weapons.WeaponType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UpgradeNodeTest {

    private UpgradeNode node;

    @BeforeEach
    public void setUp() {
        node = new UpgradeNode(WeaponType.STICK, "images/upgradetree/stick.png");
    }

    @Test
    public void testAddChild() {
        UpgradeNode child = new UpgradeNode(WeaponType.WOODHAMMER, "images/upgradetree/hammer1.png");
        node.addChild(child);
        assertTrue(node.getChildren().contains(child));
    }

    @Test
    public void testMultipleChildren() {
        UpgradeNode child1 = new UpgradeNode(WeaponType.STONEHAMMER, "images/upgradetree/hammer2.png");
        UpgradeNode child2 = new UpgradeNode(WeaponType.ELEC_WRENCH, "images/wrench.png");
        UpgradeNode child3 = new UpgradeNode(WeaponType.KATANA, "images/upgradetree/katana.png");

        node.addChild(child1);
        node.addChild(child2);
        node.addChild(child3);

        List<UpgradeNode> children = node.getChildren();

        assertEquals(3, children.size());
        assertTrue(children.contains(child1));
        assertTrue(children.contains(child2));
        assertTrue(children.contains(child3));
    }

    @Test
    public void testSetXAndGetX() {
        node.setX(5.0f);
        assertEquals(5.0f, node.getX());
    }

    @Test
    public void testSetYAndGetY() {
        node.setY(10.0f);
        assertEquals(10.0f, node.getY());
    }

    @Test
    public void testGetWeaponType() {
        assertEquals(WeaponType.STICK, node.getWeaponType());
    }

    @Test
    public void testGetImagePath() {
        assertEquals("images/upgradetree/stick.png", node.getImagePath());
    }
}

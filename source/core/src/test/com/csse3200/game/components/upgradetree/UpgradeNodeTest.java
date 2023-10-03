package com.csse3200.game.components.upgradetree;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.csse3200.game.components.Weapons.WeaponType;
import com.csse3200.game.entities.configs.WeaponConfig;
import com.csse3200.game.entities.configs.WeaponConfigs;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UpgradeNodeTest {

    private UpgradeNode node;
    private WeaponConfigs weaponConfigs;

    @BeforeEach
    void setUp() {
        Gdx.files = mock(Files.class);
        weaponConfigs = mock(WeaponConfigs.class);

        WeaponConfig stickConfig = mock(WeaponConfig.class);
        when(weaponConfigs.GetWeaponConfig(WeaponType.STICK)).thenReturn(stickConfig);
        stickConfig.type = WeaponType.STICK;
        WeaponConfig woodHammerConfig = mock(WeaponConfig.class);
        woodHammerConfig.type = WeaponType.WOODHAMMER;
        when(weaponConfigs.GetWeaponConfig(WeaponType.WOODHAMMER)).thenReturn(woodHammerConfig);
        WeaponConfig katanaConfig = mock(WeaponConfig.class);
        katanaConfig.type = WeaponType.MELEE_KATANA;
        when(weaponConfigs.GetWeaponConfig(WeaponType.MELEE_KATANA)).thenReturn(katanaConfig);
        WeaponConfig elecWrenchConfig = mock(WeaponConfig.class);
        elecWrenchConfig.type = WeaponType.MELEE_WRENCH;
        when(weaponConfigs.GetWeaponConfig(WeaponType.MELEE_WRENCH)).thenReturn(elecWrenchConfig);

        node = new UpgradeNode(stickConfig);
    }

    @Test
    void testAddChild() {
        UpgradeNode child = new UpgradeNode(weaponConfigs.GetWeaponConfig(WeaponType.WOODHAMMER));
        node.addChild(child);
        assertTrue(node.getChildren().contains(child));
    }

    @Test
    void testAddMultipleChildren() {
        WeaponConfig woodhammerConfig = weaponConfigs.GetWeaponConfig(WeaponType.WOODHAMMER);
        WeaponConfig elecWrenchConfig = weaponConfigs.GetWeaponConfig(WeaponType.MELEE_WRENCH);
        WeaponConfig katanaConfig = weaponConfigs.GetWeaponConfig(WeaponType.MELEE_KATANA);

        UpgradeNode child1 = new UpgradeNode(woodhammerConfig);
        UpgradeNode child2 = new UpgradeNode(elecWrenchConfig);
        UpgradeNode child3 = new UpgradeNode(katanaConfig);

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
    void testGetSingleChild() {
        WeaponConfig woodhammerConfig = weaponConfigs.GetWeaponConfig(WeaponType.WOODHAMMER);
        UpgradeNode child1 = new UpgradeNode(woodhammerConfig);
        ArrayList<UpgradeNode> dummy = new ArrayList<>();

        node.addChild(child1);
        dummy.add(child1);

        // Ensures node and dummy have the same child
        assertEquals(node.getChildren(), dummy);
    }

    @Test
    void testGetMultipleChildren() {
        WeaponConfig woodhammerConfig = weaponConfigs.GetWeaponConfig(WeaponType.WOODHAMMER);
        WeaponConfig elecWrenchConfig = weaponConfigs.GetWeaponConfig(WeaponType.MELEE_WRENCH);
        WeaponConfig katanaConfig = weaponConfigs.GetWeaponConfig(WeaponType.MELEE_KATANA);

        UpgradeNode child1 = new UpgradeNode(woodhammerConfig);
        UpgradeNode child2 = new UpgradeNode(elecWrenchConfig);
        UpgradeNode child3 = new UpgradeNode(katanaConfig);
        ArrayList<UpgradeNode> dummy = new ArrayList<>();

        node.addChild(child1);
        node.addChild(child2);
        node.addChild(child3);
        dummy.add(child1);
        dummy.add(child2);
        dummy.add(child3);

        // Ensures node and dummy have the same children
        assertEquals(node.getChildren(), dummy);
    }

    @Test
    void testRootCost() {
        int baseCost = 100;

        // Ensure root cost is: base cost * (depth=0 + 1)
        assertEquals(baseCost, node.getNodeCost());
    }

    @Test
    void testMultipleDepthsCost() {
        // int baseCost = 100;
        WeaponConfig woodhammerConfig = weaponConfigs.GetWeaponConfig(WeaponType.WOODHAMMER);
        WeaponConfig elecWrenchConfig = weaponConfigs.GetWeaponConfig(WeaponType.MELEE_WRENCH);

        // Ensures depth1 cost is: base cost * (depth=1 + 1)
        UpgradeNode child1 = new UpgradeNode(woodhammerConfig);
        node.addChild(child1);
        child1.setDepth(1);
        assertEquals(200, child1.getNodeCost());

        // Ensures depth1 cost is: base cost * (depth=2 + 1)
        UpgradeNode child2 = new UpgradeNode(elecWrenchConfig);
        child1.addChild(child2);
        child2.setDepth(2);
        assertEquals(300, child2.getNodeCost());
    }

    @Test
    void testSetXAndGetX() {
        node.setX(5.0f);
        assertEquals(5.0f, node.getX());
    }

    @Test
    void testSetYAndGetY() {
        node.setY(10.0f);
        assertEquals(10.0f, node.getY());
    }

    @Test
    void testGetWeaponType() {
        assertEquals(WeaponType.STICK, node.getWeaponType());
    }
}

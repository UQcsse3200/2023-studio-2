package com.csse3200.game.components.upgradetree;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.csse3200.game.components.Weapons.WeaponType;
import com.csse3200.game.entities.configs.WeaponConfig;
import com.csse3200.game.entities.configs.WeaponConfigs;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UpgradeNodeTest {

    private UpgradeNode node;
    private WeaponConfigs weaponConfigs;

    @BeforeEach
    public void setUp() {
        Gdx.files = mock(Files.class);
        weaponConfigs = mock(WeaponConfigs.class);

        WeaponConfig stickConfig = mock(WeaponConfig.class);
        when(weaponConfigs.GetWeaponConfig(WeaponType.STICK)).thenReturn(stickConfig);
        WeaponConfig woodHammerConfig = mock(WeaponConfig.class);
        when(weaponConfigs.GetWeaponConfig(WeaponType.WOODHAMMER)).thenReturn(woodHammerConfig);
        WeaponConfig katanaConfig = mock(WeaponConfig.class);
        when(weaponConfigs.GetWeaponConfig(WeaponType.KATANA)).thenReturn(katanaConfig);
        WeaponConfig elecWrenchConfig = mock(WeaponConfig.class);
        when(weaponConfigs.GetWeaponConfig(WeaponType.ELEC_WRENCH)).thenReturn(elecWrenchConfig);

        node = new UpgradeNode(stickConfig, WeaponType.STICK);
    }

    @Test
    public void testAddChild() {
        UpgradeNode child = new UpgradeNode(weaponConfigs.GetWeaponConfig(WeaponType.WOODHAMMER), WeaponType.WOODHAMMER);
        node.addChild(child);
        assertTrue(node.getChildren().contains(child));
    }

    @Test
    public void testMultipleChildren() {
        WeaponConfig woodhammerConfig = weaponConfigs.GetWeaponConfig(WeaponType.WOODHAMMER);
        WeaponConfig elecWrenchConfig = weaponConfigs.GetWeaponConfig(WeaponType.ELEC_WRENCH);
        WeaponConfig katanaConfig = weaponConfigs.GetWeaponConfig(WeaponType.KATANA);

        UpgradeNode child1 = new UpgradeNode(woodhammerConfig, WeaponType.STONEHAMMER);
        UpgradeNode child2 = new UpgradeNode(elecWrenchConfig, WeaponType.ELEC_WRENCH);
        UpgradeNode child3 = new UpgradeNode(katanaConfig, WeaponType.KATANA);

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
}

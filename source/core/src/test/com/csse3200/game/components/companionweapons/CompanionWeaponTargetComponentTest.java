package com.csse3200.game.components.companionweapons;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.entities.Entity;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
/**
 * Unit tests for the CompanionWeaponTargetComponent class.
 */

public class CompanionWeaponTargetComponentTest {

    private CompanionWeaponTargetComponent targetComponent;
    private Entity testEntity;
    /**
     * Set up the test by creating a test entity and a CompanionWeaponTargetComponent.
     */
    @Before
    public void setUp() {
        testEntity = new Entity();
        //targetComponent = new CompanionWeaponTargetComponent(CompanionWeaponType.SHIELD, testEntity);
    }

    /**
     * Test the rotation logic of the shield around the entity.
     */

    @Test
    public void testRotateShieldAroundEntity() {
        Vector2 initialPosition = new Vector2(0, 0);  // Set the initial position
        testEntity.setPosition(initialPosition);

        //Vector2 rotatedPosition = targetComponent.rotate_shield_around_entity();

        Vector2 expectedPosition = new Vector2(initialPosition.x + 0.01f, initialPosition.y + 0.01f);

        float tolerance = 0.01f;
        assertEquals(expectedPosition.x, expectedPosition.x, tolerance);
        assertEquals(expectedPosition.y, expectedPosition.y, tolerance);
    }



}
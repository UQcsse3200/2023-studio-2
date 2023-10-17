package com.csse3200.game.components.companion;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.services.EntityPlacementService;
import com.csse3200.game.services.ServiceLocator;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Test class for the CompanionWeaponComponent class.
 */
public class CompanionWeaponComponentTest {

    @Mock
    private EntityPlacementService entityPlacementService;

    private CompanionWeaponComponent weaponComponent;
    private Entity companionEntity;
    public Vector2 position;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        weaponComponent = new CompanionWeaponComponent();

        companionEntity = new Entity();
        companionEntity.addComponent(new HitboxComponent());

        // Set up mock services and dependencies
        ServiceLocator.getEntityService();

        // Create a mock companion entity
        Entity mockCompanion = mock(Entity.class);

        // Set the companion in the EntityService
        //when(ServiceLocator.getEntityService().getCompanion()).thenReturn(mockCompanion);

        // Set up the EntityPlacementService
        ServiceLocator.getEntityPlacementService();
    }
    /**
     * Test the positionInDirection method to calculate a position vector based on direction and distance.
     */

    @Test
    public void testPositionInDirection() {
        // Arrange
        Entity attackEntity = new Entity();
        attackEntity.setScale(new Vector2(1, 1));
        weaponComponent.entity= new Entity();
        weaponComponent.entity.setPosition(2f,2f);
        weaponComponent.entity.setScale(2f,2f);

        // Act
        Vector2 position =new Vector2(weaponComponent.positionInDirection(45, 1, attackEntity));

    }
    /**
     * Test the calcRotationAngleInDegrees method to calculate the rotation angle in degrees.
     */
    @Test
    public void testCalcRotationAngleInDegrees() {
        // Arrange
        Vector2 targetPt = new Vector2(1, 1);

        // Act
        float angle = weaponComponent.calcRotationAngleInDegrees(companionEntity.getPosition(), targetPt);

        // Assert
        // Verify that the calculated angle is correct
        assertEquals(45.0f, angle, 0.001);
    }
}

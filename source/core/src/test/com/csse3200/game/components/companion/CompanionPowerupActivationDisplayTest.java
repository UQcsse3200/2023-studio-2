package com.csse3200.game.components.companion;

import com.csse3200.game.components.PowerupType;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.services.ServiceLocator;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class CompanionPowerupActivationDisplayTest {

    private CompanionPowerupActivationDisplay powerupActivationDisplay;

    @Mock
    private CompanionPowerupInventoryComponent powerupInventory;

    @Before
    public void setUp() {
        // Initialize Mockito annotations for the test class
        MockitoAnnotations.openMocks(this);

    }

    @Test
    public void testInitialization() {
        // Mock the powerup inventory component behavior
        when(powerupInventory.getPowerupsInventory()).thenReturn(new HashMap<>());

        // Initialize the UI component
        //powerupActivationDisplay.create();


    }

    @Test
    public void testPowerupActivation() {
        // Simulate adding a powerup to the inventory
        PowerupType testPowerup = PowerupType.HEALTH_BOOST;
        HashMap<PowerupType, Integer> powerupInventoryMap = new HashMap<>();
        powerupInventoryMap.put(testPowerup, 1);

        // Mock the powerup inventory component behavior
        when(powerupInventory.getPowerupsInventory()).thenReturn(powerupInventoryMap);

    }

}
package com.csse3200.game.components.companion;

import com.csse3200.game.GdxGame;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.services.PlanetTravel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.verify;

public class CompanionDeathScreenActionsTest {

    @Mock
    private GdxGame game;

    @Mock
    private Entity entity;

    @Mock
    private CombatStatsComponent combatStatsComponent;

    @Mock
    private PlanetTravel planetTravel;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);

        // Configure any specific behavior for your mocks here
        Mockito.when(entity.getComponent(CombatStatsComponent.class)).thenReturn(combatStatsComponent);
    }

    @Test
    public void testOnExit() {
        CompanionDeathScreenActions companionDeathScreenActions = new CompanionDeathScreenActions(game);
        companionDeathScreenActions.onExit();

        // Verify that the game's setScreen method is called with MAIN_MENU as an argument
        verify(game).setScreen(GdxGame.ScreenType.MAIN_MENU);
    }}


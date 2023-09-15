package com.csse3200.game;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.csse3200.game.components.mainmenu.MainMenuActions;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.events.EventHandler;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.when;

public class MainMenuActionsTest {

    @Mock
    private GdxGame game;
    @Mock
    private Stage stage;
    @Mock
    private Skin skin;
    @Mock
    private Entity entity; // Mock the Entity object
    @Mock
    private EventHandler events; // Mock the EventHandler object

    private MainMenuActions mainMenuActions;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mainMenuActions = new MainMenuActions(game, stage, skin);

        // Mock the behavior of entity.getEvents()
        when(entity.getEvents()).thenReturn(events);

        // Assign the mocked entity to mainMenuActions
        mainMenuActions.entity = entity;

        mainMenuActions.create();
    }

    @Test
    public void testStartAction() {

    }


}
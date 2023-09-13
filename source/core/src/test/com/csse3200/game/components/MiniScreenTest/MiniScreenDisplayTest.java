package com.csse3200.game.components.MiniScreenTest;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.csse3200.game.GdxGame;
import com.csse3200.game.components.MiniDisplay.MiniScreenDisplay;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MiniScreenDisplayTest {

    private GdxGame mockGame;
    private Stage mockStage;
    private Texture mockTexture;
    private Skin mockSkin;
    private MiniScreenDisplay miniScreenDisplay;

    @Before
    public void setUp() {
        mockGame = new GdxGame();
        mockStage = new Stage();
        mockTexture = new Texture("images/start.png");
        mockSkin = new Skin();
        miniScreenDisplay = new MiniScreenDisplay(mockGame);

        miniScreenDisplay.setSkin(mockSkin);
        miniScreenDisplay.create();
    }

    @Test
    public void testStartGame() {
        miniScreenDisplay.startGame();
        assertEquals(GdxGame.ScreenType.SPACE_MAP, mockGame.getScreen());
    }

    @Test
    public void testAddUIElements() {
        miniScreenDisplay.addUIElements();

        // Verify that the stage contains actors
        assertTrue(mockStage.getActors().size > 0);
    }

    @Test
    public void testUpdate() {
        miniScreenDisplay.update();

        // Perform assertions as needed for the update method
    }

    @Test
    public void testDispose() {
        miniScreenDisplay.dispose();

        // Perform assertions as needed for the dispose method
    }
}

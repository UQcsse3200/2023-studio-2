package com.csse3200.game.components.backgrounds;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

class StarBackgroundTest {

    private StarBackground starBackground;

    @Before
    public void setup() {
        Gdx.app = Mockito.mock(Application.class);
        Gdx.files = Mockito.mock(Files.class);
        Mockito.when(Gdx.files.internal(Mockito.anyString())).thenReturn(null);
    }

    @Test
    public void instantiationTest() {
        starBackground = new StarBackground(150);
        assertEquals(150, starBackground.numStars);

        starBackground = new StarBackground(100);
        assertEquals(100, starBackground.numStars);
    }

    @Test
    public void actTest() {
        starBackground = new StarBackground(2);
        float prevTime0 = starBackground.stateTimes[0];
        float prevTime1 = starBackground.stateTimes[1];

        starBackground.act(2.0f);

        assertEquals(starBackground.stateTimes[0], prevTime0 + 2.0f, 0.01f);
        assertEquals(starBackground.stateTimes[1], prevTime1 + 2.0f, 0.01f);
    }

    @Test
    public void starShiftTest() {
        starBackground = new StarBackground(150);
        Vector2 newPosition = starBackground.starShift(2,3);
        assertEquals(2, newPosition.x, 0.01f);
        assertEquals(3, newPosition.y, 0.01f);
    }

    @Test
    public void starShiftShouldReturnSameCoordinatesForPositiveIntegers() {
        StarBackground starBackground = new StarBackground(150);

        Vector2 newPosition = starBackground.starShift(10, 20);

        assertEquals(10, newPosition.x, 0.01f);
        assertEquals(20, newPosition.y, 0.01f);
    }

    @Test
    public void starShiftShouldReturnSameCoordinatesForZero() {
        StarBackground starBackground = new StarBackground(150);

        Vector2 newPosition = starBackground.starShift(0, 0);

        assertEquals(0, newPosition.x, 0.01f);
        assertEquals(0, newPosition.y, 0.01f);
    }

    @Test
    public void starShiftShouldReturnSameCoordinatesForNegativeIntegers() {
        StarBackground starBackground = new StarBackground(150);

        Vector2 newPosition = starBackground.starShift(-10, -20);

        assertEquals(-10, newPosition.x, 0.01f);
        assertEquals(-20, newPosition.y, 0.01f);
    }

}
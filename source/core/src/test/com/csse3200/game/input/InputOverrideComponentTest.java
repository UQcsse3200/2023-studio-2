package com.csse3200.game.input;

import com.csse3200.game.input.InputOverrideComponent;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class InputOverrideComponentTest {

    @Test
    public void testKeyDown() {
        InputOverrideComponent inputOverride = new InputOverrideComponent();
        assertTrue(inputOverride.keyDown(123)); // Any keycode should return true
    }

    @Test
    public void testKeyUp() {
        InputOverrideComponent inputOverride = new InputOverrideComponent();
        assertTrue(inputOverride.keyUp(456)); // Any keycode should return true
    }

    @Test
    public void testTouchDown() {
        InputOverrideComponent inputOverride = new InputOverrideComponent();
        assertTrue(inputOverride.touchDown(0, 0, 1, 2)); // Any input values should return true
    }
}
package com.csse3200.game.input;
import com.csse3200.game.input.InputOverrideComponent;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * This class contains unit tests for the InputOverrideComponent class.
 */
public class InputOverrideComponentTest {

    /**
     * Test case for the keyDown method.
     */
    @Test
    public void testKeyDown() {
        InputOverrideComponent inputOverride = new InputOverrideComponent();

        // Verify that keyDown method returns true for any keycode.
        assertTrue(inputOverride.keyDown(123));
    }

    /**
     * Test case for the keyUp method.
     */
    @Test
    public void testKeyUp() {
        InputOverrideComponent inputOverride = new InputOverrideComponent();

        // Verify that keyUp method returns true for any keycode.
        assertTrue(inputOverride.keyUp(456));
    }

    /**
     * Test case for the touchDown method.
     */
    @Test
    public void testTouchDown() {
        InputOverrideComponent inputOverride = new InputOverrideComponent();

        // Verify that touchDown method returns true for any input values.
        assertTrue(inputOverride.touchDown(0, 0, 1, 2));
    }
}

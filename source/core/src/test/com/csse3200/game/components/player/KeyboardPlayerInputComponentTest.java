package com.csse3200.game.components.player;

import org.junit.jupiter.api.Test;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.utils.math.Vector2Utils;
import com.badlogic.gdx.Input.Keys;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class KeyboardPlayerInputComponentTest {

    private Vector2 direction(KeyboardPlayerInputComponent comp) {
        return comp.getDirection();
    }

    @Test
    void testUp() {
        
        KeyboardPlayerInputComponent test = new KeyboardPlayerInputComponent();
        test.setTesting(1);

        test.keyDown(Keys.W);
        assertTrue(direction(test).epsilonEquals(Vector2Utils.UP));
        test.keyUp(Keys.W);
        assertTrue(direction(test).epsilonEquals(Vector2.Zero));

    }

    @Test
    void testLeft() {

        KeyboardPlayerInputComponent test = new KeyboardPlayerInputComponent();
        test.setTesting(1);

        test.keyDown(Keys.A);
        assertTrue(direction(test).epsilonEquals(Vector2Utils.LEFT));
        test.keyUp(Keys.A);
        assertTrue(direction(test).epsilonEquals(Vector2.Zero));
    }

    @Test
    void testDown() {

        KeyboardPlayerInputComponent test = new KeyboardPlayerInputComponent();
        test.setTesting(1);

        test.keyDown(Keys.S);
        assertTrue(direction(test).epsilonEquals(Vector2Utils.DOWN));
        test.keyUp(Keys.S);
        assertTrue(direction(test).epsilonEquals(Vector2.Zero));

    }

    @Test
    void testRight() {

        KeyboardPlayerInputComponent test = new KeyboardPlayerInputComponent();
        test.setTesting(1);

        test.keyDown(Keys.D);
        assertTrue(direction(test).epsilonEquals(Vector2Utils.RIGHT));
        test.keyUp(Keys.D);
        assertTrue(direction(test).epsilonEquals(Vector2.Zero));

    }

    @Test
    void testTopLeft() {

        KeyboardPlayerInputComponent test = new KeyboardPlayerInputComponent();
        test.setTesting(1);

        test.keyDown(Keys.W);
        test.keyDown(Keys.A);
        assertTrue(
                direction(test).epsilonEquals(Vector2Utils.UP.add(Vector2Utils.LEFT).scl(new Vector2(0.707f, 0.707f))));
        test.keyUp(Keys.A);
        test.keyUp(Keys.W);
        assertTrue(direction(test).epsilonEquals(Vector2.Zero));

    }

    @Test
    void testTopRight() {

        KeyboardPlayerInputComponent test = new KeyboardPlayerInputComponent();
        test.setTesting(1);

        test.keyDown(Keys.W);
        test.keyDown(Keys.D);
        assertTrue(
                direction(test)
                        .epsilonEquals(Vector2Utils.UP.add(Vector2Utils.RIGHT).scl(new Vector2(0.707f, 0.707f))));
        test.keyUp(Keys.D);
        test.keyUp(Keys.W);
        assertTrue(direction(test).epsilonEquals(Vector2.Zero));

    }

    @Test
    void testBottomLeft() {
        KeyboardPlayerInputComponent test = new KeyboardPlayerInputComponent();
        test.setTesting(1);

        test.keyDown(Keys.S);
        test.keyDown(Keys.A);
        assertTrue(
                direction(test)
                        .epsilonEquals(Vector2Utils.DOWN.add(Vector2Utils.LEFT).scl(new Vector2(0.707f, 0.707f))));
        test.keyUp(Keys.A);
        test.keyUp(Keys.S);
        assertTrue(direction(test).epsilonEquals(Vector2.Zero));
    }

    @Test
    void testBottomRight() {
        KeyboardPlayerInputComponent test = new KeyboardPlayerInputComponent();
        test.setTesting(1);

        test.keyDown(Keys.S);
        test.keyDown(Keys.D);
        assertTrue(
                direction(test)
                        .epsilonEquals(Vector2Utils.DOWN.add(Vector2Utils.RIGHT).scl(new Vector2(0.707f, 0.707f))));
        test.keyUp(Keys.D);
        test.keyUp(Keys.S);
        assertTrue(direction(test).epsilonEquals(Vector2.Zero));
    }

}

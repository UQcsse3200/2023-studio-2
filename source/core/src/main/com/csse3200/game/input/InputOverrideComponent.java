package com.csse3200.game.input;

/**
 * Class to override any input components being handled. This creates a new input component
 * that returns true for all input calls and thus overrides all input handlers with priority less than 10.
 */
public class InputOverrideComponent extends InputComponent {
    public InputOverrideComponent() {
        super(10);
    }

    @Override
    public boolean keyDown(int keycode) {
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        return true;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return true;
    }
}

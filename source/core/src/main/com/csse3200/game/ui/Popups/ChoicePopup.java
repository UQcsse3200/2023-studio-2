package com.csse3200.game.ui.Popups;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A popup that allows the user to select from two choices.
 * Two Listener triggers based upon the button names
 */
public class ChoicePopup extends PopupBox {
    protected static final Logger logger = LoggerFactory.getLogger(ChoicePopup.class);
    protected String choice1;
    protected String choice2;

    /**
     * Creates a PopupBox that allows for two choices.
     * Options are "Continue" and "Cancel", and title is "Choice".
     * @param message   The message to display along with the choices
     * @param skin      The skin of the choice popup box.
     */
    public ChoicePopup(String message, Skin skin) {
        this(message, "Choice", skin);
    }
    /**
     * Creates a PopupBox that allows for two choices.
     * Options are "Continue" and "Cancel".
     * @param message   The message to display along with the choices
     * @param title     The title of the choice popup box.
     * @param skin      The skin of the choice popup box.
     */
    public ChoicePopup(String message, String title, Skin skin) {
        this(message, title, "Continue", "Cancel", skin);
    }

    /**
     * Creates a PopupBox that allows for two choices.
     * @param message   The message to display along with the choices
     * @param title     The title of the choice popup box.
     * @param choice1   The text to display for the choice 1 button.
     * @param choice2   The text to display for the choice 2 button.
     * @param skin      The skin of the choice popup box.
     */
    public ChoicePopup(String message, String title, String choice1, String choice2, Skin skin) {
        super(message, title, skin, new String[] {choice1, choice2});
        this.choice1 = choice1;
        this.choice2 = choice2;
    }

    /**
     * Get the name of the choice 1 button and its trigger event name.
     * @return  The button text.
     */
    public String getChoice1() {
        return choice1;
    }

    /**
     * Get the name of the choice 2 button and its trigger event name.
     * @return  The button text.
     */
    public String getChoice2() {
        return choice2;
    }
}

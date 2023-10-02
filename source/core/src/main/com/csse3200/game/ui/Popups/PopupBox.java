package com.csse3200.game.ui.Popups;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.csse3200.game.events.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A Popup dialog box that has a single OK action button, text message, and title.
 * Allows registering of EventListeners to detect button clicks. ("OK")
 */
public class PopupBox extends Dialog {
    protected static final Logger logger = LoggerFactory.getLogger(PopupBox.class);
    protected final Skin skin;
    protected final EventHandler eventHandler = new EventHandler();
    protected final String[] buttons;

    /**
     * Creates a PopupBox with the given message and skin.
     * Has default title of "PopupBox".
     *
     * @param message   Message to display in popup.
     * @param skin      Skin of the popup.
     */
    public PopupBox(String message, Skin skin) {
        this(message, "PopupBox", skin);
    }

    /**
     * Creates a PopupBox with the given message, title and skin.
     *
     * @param message   Message to display in popup.
     * @param title     Title of the popup window.
     * @param skin      Skin of the popup.
     */
    public PopupBox(String message, String title, Skin skin) {
        this(message, title, skin, new String[] {"OK"});
    }

    public PopupBox(String message, String title, Skin skin, String[] buttons) {
        super(title, skin);
        logger.info("Creating Popup box: " + title);
        this.skin = skin;
        this.buttons = buttons;
        createText(message);
        createButtons();
    }

    /**
     * Get the event handler attached to this popup. Meant to be used to listen
     * for button press triggers.
     *
     * @return popup boxes event handler
     */
    public EventHandler getEvents() {
        return eventHandler;
    }

    /**
     * Create and display all the text for the popup box.
     * Uses the large styleName type as default.
     *
     * @param text  The text to display.
     */
    protected void createText(String text) {
        createText(text, "large");
    }

    /**
     * Create and display all the text for the popup box.
     *
     * @param text      The text to display.
     * @param styleName Name of the skin style to use.
     */
    protected void createText(String text, String styleName) {
        text(new Label(text, skin, styleName));
    }

    /**
     * Create the buttons to display on the popup.
     * Single OK button.
     */
    protected void createButtons() {
        for (String name : this.buttons) {
            createButton(name);
        }
    }

    protected void createButton(String name) {
        TextButton button = new TextButton(name, skin);
        button.addListener(new ChangeListener() {
            public void changed(ChangeEvent changeEvent, Actor actor) {
                logger.debug("OK button clicked");
                eventHandler.trigger(name);
            }
        });
        button(button);
    }
}

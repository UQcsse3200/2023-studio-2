package com.csse3200.game.input;
import com.csse3200.game.components.player.KeyboardPlayerInputComponent;
import com.csse3200.game.components.Companion.KeyboardCompanionInputComponent;
import com.csse3200.game.components.ships.KeyboardShipInputComponent;
import com.csse3200.game.ui.terminal.KeyboardTerminalInputComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * KeyboardInputFactory creates input handlers that process keyboard and touch support.
 */
public class KeyboardInputFactory extends InputFactory {
    private static final Logger logger = LoggerFactory.getLogger(KeyboardInputFactory.class);

    /**
     * Creates an input handler for the player.
     * @return Player input handler
     */
    @Override
    public InputComponent createForPlayer() {
        logger.debug("Creating player input handler");
        return new KeyboardPlayerInputComponent();
    }
    /**
     * Creates an input handler for the Companion.
     * @return Companion input handler
     */
    @Override
    public InputComponent createForCompanion() {
        logger.debug("Creating Companion input handler");
        return new KeyboardCompanionInputComponent();
    }

    @Override
    public InputComponent createForShip() {
        logger.debug("Creating ship input handler");
        return new KeyboardShipInputComponent();
    }

    /**
     * Creates an input handler for the terminal.
     *
     * @return Terminal input handler
     */
    public InputComponent createForTerminal() {
        logger.debug("Creating terminal input handler");
        return new KeyboardTerminalInputComponent();
    }
}

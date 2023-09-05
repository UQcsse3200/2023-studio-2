package com.csse3200.game.components.player;

import com.csse3200.game.GdxGame;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.maingame.MainGameActions;
import com.csse3200.game.components.mainmenu.MainMenuActions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlayerDeath extends Component {
    private static final Logger logger = LoggerFactory.getLogger(MainMenuActions.class);

    public static GdxGame game;
    public PlayerDeath(GdxGame game) {
        this.game = game;
    }

    public static void death() {

        System.out.println("playerdeath triggered");
    }
}

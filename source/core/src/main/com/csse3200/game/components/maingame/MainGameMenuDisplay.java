package com.csse3200.game.components.maingame;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.csse3200.game.GdxGame;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.csse3200.game.components.mainmenu.MainMenuActions.game;


public class MainGameMenuDisplay extends UIComponent {

    private static final Logger logger = LoggerFactory.getLogger(MainGameMenuDisplay.class);
    private static final float Z_INDEX = 2f;
    private Table table;

    private Entity entity;

    public MainGameMenuDisplay (Entity entity){
        this.entity=entity;
    }

    @Override
    public void create() {
        super.create();
        addActors();
    }

    private void addActors() {
        table = new Table();
        table.top().right();
        table.setFillParent(true);

        TextButton mainMenuBtn = new TextButton("Exit", skin);

        // Triggers an event when the button is pressed.
        mainMenuBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Exit button clicked");
                        entity.getEvents().trigger("exit");
                    }
                });

        TextButton controlMenuBtn = new TextButton("Controls", skin);

        // Triggers an event when the button is pressed.
        controlMenuBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Control Screen button clicked");
                        game.setScreen(GdxGame.ScreenType.CONTROL_SCREEN);
                    }
                });

        table.add(mainMenuBtn).padTop(500f).padRight(500f);
        table.add(controlMenuBtn).padTop(520f).padRight(500f);

        stage.addActor(table);
    }

    @Override
    public void draw(SpriteBatch batch) {
        // draw is handled by the stage
    }

    @Override
    public float getZIndex() {
        return Z_INDEX;
    }

    @Override
    public void dispose() {
        table.clear();
        super.dispose();
    }
}

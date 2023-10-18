package com.csse3200.game.components.controls;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.csse3200.game.GdxGame;
import com.csse3200.game.components.mainmenu.InsertButtons;
import com.csse3200.game.services.PlanetTravel;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A UI component responsible for displaying the controls screen's user interface.
 */
public class ControlsScreenDisplay extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(ControlsScreenDisplay.class);
    private Table table,table1;
    private GdxGame game;

    private PlanetTravel planetTravel;
    private boolean Isgame= true;

    public ControlsScreenDisplay(GdxGame game, boolean isgame){
        this.game=game;
        planetTravel = new PlanetTravel(game);
        Isgame=isgame;
    }


    @Override
    public void create() {
        super.create();
        addActors();
    }

    private void addActors() {
        table = new Table();
        table.setFillParent(true);

        InsertButtons bothButtons = new InsertButtons();


        String exitTexture = "images/controls-images/on_exit.png";
        String exitTextureHover = "images/controls-images/on_exit_hover.PNG";
        ImageButton exitBtn;
        exitBtn = bothButtons.draw(exitTexture, exitTextureHover);
        exitBtn.setPosition(800f, 100f);
        exitBtn.setSize(250, 100);

        exitBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                logger.debug("Exit button clicked");
                entity.getEvents().trigger("exit");
            }
        });

        stage.addActor(exitBtn);

    }

    @Override
    public void draw(SpriteBatch batch) {
        // draw is handled by the stage
    }

    @Override
    public void dispose() {
        table.clear();
        stage.clear();
        super.dispose();
    }
}
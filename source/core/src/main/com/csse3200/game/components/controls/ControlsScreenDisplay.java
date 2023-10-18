package com.csse3200.game.components.controls;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.csse3200.game.GdxGame;
import com.csse3200.game.components.mainmenu.InsertButtons;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.services.PlanetTravel;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

import static com.csse3200.game.GdxGame.ScreenType.EXTRACTOR_GAME;

/**
 * A UI component responsible for displaying the controls screen's user interface.
 */
public class ControlsScreenDisplay extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(ControlsScreenDisplay.class);
    private Table table,table1;
    private GdxGame.ScreenType screenType;
    private GdxGame game;
    private PlanetTravel planetTravel;

    public ControlsScreenDisplay(GdxGame game, GdxGame.ScreenType screenType){
        this.game=game;
        planetTravel = new PlanetTravel(game);
        this.screenType=screenType;
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
        exitBtn.setPosition(1600f, 200f);
        exitBtn.setSize(250, 100);

        exitBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                logger.debug("Exit button clicked");
                if (screenType.equals(GdxGame.ScreenType.SETTINGS)) {
                    game.setScreen(GdxGame.ScreenType.SETTINGS);
                } else {
                    entity.getEvents().trigger("exitToGame");
                }
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
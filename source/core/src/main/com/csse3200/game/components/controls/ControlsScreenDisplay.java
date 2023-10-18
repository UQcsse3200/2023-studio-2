package com.csse3200.game.components.controls;

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
        /**
        ArrayList<String> storyImages = new ArrayList<>();
        storyImages.add("images/controls-images/Controls.png");
        int start = 0;
        table1 = new Table();
        table1.top().right();
        table1.setFillParent(true);
        TextButton Return = new TextButton("Return", skin);
        Return.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                logger.debug("Return button clicked");
                planetTravel.returnToCurrent();
            }
        });

        Texture storyLine = new Texture(Gdx.files.internal(storyImages.get(start)));
        TextureRegionDrawable storyBackground = new TextureRegionDrawable(storyLine);
        table.setBackground(storyBackground);
        stage.addActor(table);
        table1.add(Return).padTop(10f).padRight(10f);
       if(Isgame) {
           stage.addActor(table1);
       }
         **/
        InsertButtons bothButtons = new InsertButtons();
        /**
        TextButton WBtn = new TextButton("W", skin);
        TextButton ABtn = new TextButton("A", skin);
        TextButton SBtn = new TextButton("S", skin);
        TextButton DBtn = new TextButton("D", skin);
        TextButton FBtn = new TextButton("F", skin);
        WBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                logger.debug("W button clicked");
                entity.getEvents().trigger("w");

            }
        });
        ABtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                logger.debug("A button clicked");
                entity.getEvents().trigger("a");

            }
        });
        SBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                logger.debug("S button clicked");
                entity.getEvents().trigger("s");

            }
        });
        DBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                logger.debug("D button clicked");
                entity.getEvents().trigger("d");

            }
        });
        FBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                logger.debug("F button clicked");
                entity.getEvents().trigger("f");

            }
        });

        float tablePaddingTop = 350f;  // Adjust this value to move the table higher or lower

        table.padBottom(tablePaddingTop);  // Set padding for the top of the table
        table.row();
        table.add(WBtn).expandX().top().padBottom(40f).padRight(900f);
        table.row();
        table.add(ABtn).expandX().top().padBottom(40f).padRight(900f);
        table.row();
        table.add(SBtn).expandX().top().padBottom(40f).padRight(900f);
        table.row();
        table.add(DBtn).expandX().top().padBottom(40f).padRight(900f);
        table.row();
        table.add(FBtn).expandX().top().padBottom(40f).padRight(900f);

**/


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
/**
        Table table2 = new Table();
        TextButton SpaceBtn = new TextButton("Space", skin);
        TextButton OneBtn = new TextButton("1", skin);
        TextButton TwoBtn = new TextButton("2", skin);
        TextButton ThreeBtn = new TextButton("3", skin);
        TextButton FourBtn = new TextButton("4", skin);

        float newTablePaddingTop = 1710f;
        table2.padBottom(newTablePaddingTop);
        table2.row();
        table2.add(SpaceBtn).expandX().top().padTop(40f).padLeft(2500f);
        table2.row();
        table2.add(OneBtn).expandX().top().padTop(40f).padLeft(2500f);
        table2.row();
        table2.add(TwoBtn).expandX().top().padTop(40f).padLeft(2500f);
        table2.row();
        table2.add(ThreeBtn).expandX().top().padTop(40f).padLeft(2500f);
        table2.row();
        table2.add(FourBtn).expandX().top().padTop(40f).padLeft(2500f);
        SpaceBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                logger.debug("Space key clicked");
                entity.getEvents().trigger("Space");

            }
        });
        OneBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                logger.debug("1 key clicked");
                entity.getEvents().trigger("1");

            }
        });
        TwoBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                logger.debug("2 key clicked");
                entity.getEvents().trigger("2");

            }
        });
        ThreeBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                logger.debug("3 key clicked");
                entity.getEvents().trigger("3");

            }
        });
        FourBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                logger.debug("4 key clicked");
                entity.getEvents().trigger("4");

            }
        });




        stage.addActor(table2);
        **/

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
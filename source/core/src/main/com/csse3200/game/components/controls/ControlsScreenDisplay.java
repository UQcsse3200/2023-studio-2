package com.csse3200.game.components.controls;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.csse3200.game.components.mainmenu.InsertButtons;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

/**
 * A UI component responsible for displaying the controls screen's user interface.
 */
public class ControlsScreenDisplay extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(ControlsScreenDisplay.class);
    private Table table;
    private ArrayList<String> storyImages;

    @Override
    public void create() {
        super.create();
        addActors();
    }

    private void addActors() {
        table = new Table();
        table.setFillParent(true);

        storyImages = new ArrayList<>();
        storyImages.add("images/Controls.png");
        int start = 0;

        Texture storyLine = new Texture(Gdx.files.internal(storyImages.get(start)));
        TextureRegionDrawable storyBackground = new TextureRegionDrawable(storyLine);
        table.setBackground(storyBackground);
        stage.addActor(table);

        InsertButtons bothButtons = new InsertButtons();

//        TextButton WBtn = new TextButton("W", skin);
//        TextButton ABtn = new TextButton("A", skin);
//        TextButton SBtn = new TextButton("S", skin);
//        TextButton DBtn = new TextButton("D", skin);

//        WBtn.addListener(new ChangeListener() {
//            @Override
//            public void changed(ChangeEvent changeEvent, Actor actor) {
//                logger.debug("W button clicked");
//                entity.getEvents().trigger("w");
//            }
//        });
//
//        ABtn.addListener(new ChangeListener() {
//            @Override
//            public void changed(ChangeEvent changeEvent, Actor actor) {
//                logger.debug("A button clicked");
//                entity.getEvents().trigger("a");
//            }
//        });
//
//        SBtn.addListener(new ChangeListener() {
//            @Override
//            public void changed(ChangeEvent changeEvent, Actor actor) {
//                logger.debug("S button clicked");
//                entity.getEvents().trigger("s");
//            }
//        });
//
//        DBtn.addListener(new ChangeListener() {
//            @Override
//            public void changed(ChangeEvent changeEvent, Actor actor) {
//                logger.debug("D button clicked");
//                entity.getEvents().trigger("d");
//            }
//        });
//
//        table.row();
//        table.add(WBtn).padTop(15f).padRight(650f);
//        table.row();
//        table.add(ABtn).padTop(15f).padRight(650f);
//        table.row();
//        table.add(SBtn).padTop(15f).padRight(650f);
//        table.row();
//        table.add(DBtn).padTop(15f).padRight(650f);

        String exitTexture = "images/on_exit.png";
        String exitTextureHover = "images/on_exit_hover.PNG";
        ImageButton exitBtn;
        exitBtn = bothButtons.draw(exitTexture, exitTextureHover);
        exitBtn.setPosition(810f, 80f);
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

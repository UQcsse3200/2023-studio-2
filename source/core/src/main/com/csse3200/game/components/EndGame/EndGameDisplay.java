package com.csse3200.game.components.EndGame;



import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.csse3200.game.components.mainmenu.InsertButtons;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;


import java.util.ArrayList;

public class EndGameDisplay extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(EndGameDisplay.class);
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

//        String playAgainTexture = "images/playAgain.PNG";
//        String playAgainTextureHover = "images/playAgainHover.PNG";
//        ImageButton playAgainBtn;
//        playAgainBtn = bothButtons.draw(playAgainTextureHover, playAgainTexture);

        String exitTexture = "images/on_exit.png";
        String exitTextureHover = "images/on_exit_hover.PNG";
        ImageButton exitBtn;
        exitBtn = bothButtons.draw(exitTexture, exitTextureHover);


        exitBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {

                        logger.debug("Exit button clicked");
                        entity.getEvents().trigger("exit");
                    }
                });

        exitBtn.setPosition(810f, 80f);
        exitBtn.setSize(250, 100);
        stage.addActor(table);
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

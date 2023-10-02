package com.csse3200.game.story;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.csse3200.game.components.mainmenu.InsertButtons;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

/**
 * A ui component for displaying the Main menu.
 */
public class StoryDisplayTest extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(com.csse3200.game.components.story.StoryDisplay.class);
    private static final float Z_INDEX = 2f;
    private Table table;

    private ArrayList<String> storyImages;
    private int start;
    private int end;

    private static final String[] buttonImages = {"images/interface/next_cut.png", "images/prev-cut.png"};




    @Override
    public void create() {
        super.create();
        addActors();
        entity.getEvents().addListener("next", this::nextScene);
        entity.getEvents().addListener("previous", this::prevScene);
    }

    private void addActors() {
        table = new Table();
        table.setFillParent(true);

        table.bottom().right();

        storyImages = new ArrayList<>();
        storyImages.add("images/P1_B.png");
        storyImages.add("images/P2_B.png");
        storyImages.add("images/P3_B.png");
        storyImages.add("images/P4_B.png");
        storyImages.add("images/P5_B.png");
        storyImages.add("images/P6_B.png");
        start = 0;
        end = 6;

        Texture storyLine = new Texture(Gdx.files.internal(storyImages.get(start)));
        TextureRegionDrawable storyBackground = new TextureRegionDrawable(storyLine);
        table.setBackground(storyBackground);

        start += 1;
        stage.addActor(table);
        InsertButtons bothButtons = new InsertButtons();

        // next button

        String nextTexture = "images/interface/next_cut.png";
        String nextTextureHover = "images/interface/next_cut_hover.png";

        ImageButton nextBtn;
        nextBtn = bothButtons.draw(nextTexture, nextTextureHover);



        // prev buttons
        String prevTexture = "images/interface/prev_cut.png";
        String prevTextureHover = "images/interface/prev_cut_hover.png";
        ImageButton prevBtn;
        prevBtn = bothButtons.draw(prevTexture, prevTextureHover);

        // skip button
        String skipTexture = "images/interface/skip_btn.png";
        String skipTextureHover = "images/interface/skip_btn_hover.png";
        ImageButton skipBtn;
        skipBtn = bothButtons.draw(skipTexture, skipTextureHover);



        //TextButton nextBtn = new TextButton("Next", skin);
        //TextButton skipBtn = new TextButton("Skip", skin);
        //TextButton prevBtn = new TextButton("Previous", skin);

        // Triggers an event when the button is pressed

        nextBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Next button clicked");
                        entity.getEvents().trigger("next");
                    }
                });

        skipBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {

                        logger.debug("Skip button clicked");
                        entity.getEvents().trigger("skip");
                    }
                });

        prevBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {

                        logger.debug("Previous button clicked");
                        entity.getEvents().trigger("previous");
                    }
                });



        table.add(skipBtn).expand().top().right().width(200f);
        table.row();
        table.add(prevBtn).left().width(70f).padBottom(300f);
        table.add(nextBtn).right().width(70f).padBottom(300f);
        stage.addActor(table);

    }

    @Override
    public void draw(SpriteBatch batch) {
        // draw is handled by the stage
    }

    private void nextScene() {
        if (start < end) {
            Drawable next = new TextureRegionDrawable(new Texture(Gdx.files.internal(storyImages.get(start))));
            table.setBackground(next);
            start += 1;
        } else {
            entity.getEvents().trigger("skip");
        }}

    private void prevScene(){
        if (end - start > 0 && start > 0){
            Drawable prev = new TextureRegionDrawable(new Texture(Gdx.files.internal(storyImages.get(start-1))));
            table.setBackground(prev);
            start -=1;
        }
    }


    @Override
    public float getZIndex() {
        return Z_INDEX;
    }

    @Override
    public void dispose() {
        table.clear();
        stage.clear();
        super.dispose();
    }
}

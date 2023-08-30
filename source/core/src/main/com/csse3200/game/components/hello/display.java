package com.csse3200.game.components.hello;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Align;
import com.csse3200.game.GdxGame;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.csse3200.game.components.mainmenu.InsertButtons;
import com.csse3200.game.components.mainmenu.MainMenuDisplay;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import java.util.ArrayList;

/**
 * A ui component for displaying the Main menu.
 */
public class display extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(display.class);
    private static final float Z_INDEX = 2f;
    private Table table;

    private ArrayList<String> storyImages;
    private int start;
    private int end;






    @Override
    public void create() {
        super.create();
        addActors();

    }

    private void addActors() {
        table = new Table();
        table.setFillParent(true);

        table.bottom().right();

        InsertButtons bothButtons = new InsertButtons();


        Image planetImage = new Image(ServiceLocator.getResourceService().getAsset("/Users/manandeepsingh/Desktop/2023-studio-2/source/core/assets/images/box_boy.png", Texture.class));
        //TextButton nextBtn = new TextButton("Next", skin);
        //TextButton skipBtn = new TextButton("Skip", skin);
        //TextButton prevBtn = new TextButton("Previous", skin);
        Label label=new Label("Level 1 ",skin,"large");
        // Triggers an event when the button is pressed
        Button button=new Button(label,skin);
        button.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {

                        entity.getEvents().trigger("Navigate");
                    }
                });
        table.add(planetImage).align(Align.top).size(100f);
        table.row();
        table.add(button);
        table.setSize(20,20);
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
package com.csse3200.game.components.ControlsAnimation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

import com.csse3200.game.screens.ControlsAnimationScreen;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.csse3200.game.components.ControlsAnimation.*;

public class ControlsAnimationDisplay extends UIComponent {

    private static final Logger logger = LoggerFactory.getLogger(ControlsAnimationDisplay.class);
    private static final float Z_INDEX = 2f;
    private Table table;

    public static int frame;
    private Image transitionFrames;
    private long lastFrameTime;
    private int fps = 11;
    private final long frameDuration =  (long)(800 / fps);

    @Override
    public void create() {
        frame=1;
        super.create();
        transitionFrames = new Image();
        addActors();
    }

    private void addActors() {
        table = new Table();
        table.setFillParent(true);

        // Display game title image
        Image titleImage = new Image(ServiceLocator.getResourceService().getAsset("images/Controls.png", Texture.class));
        titleImage.setWidth(Gdx.graphics.getWidth());
        titleImage.setHeight(Gdx.graphics.getHeight());
        titleImage.setPosition(0, 0);

        // Create buttons
        TextButton settingsBtn = new TextButton(" Back to Controls", skin);

        settingsBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Settings button clicked");
                        entity.getEvents().trigger("settings");
                    }
                });



        // Arrange UI elements in a table layout
        table.add(titleImage);
        table.row();
        table.add(settingsBtn).padTop(15f).padLeft(1200f);
        table.row();
        stage.addActor(titleImage);

        AmendAnimation();
        stage.addActor(transitionFrames);
        stage.addActor(table);
    }

    private void AmendAnimation() {
        if (frame < ControlsAnimationScreen.MountedFrames) {
            transitionFrames.setDrawable(new TextureRegionDrawable(new TextureRegion(ServiceLocator.getResourceService()
                    .getAsset(ControlsAnimationScreen.transitionTextures[frame], Texture.class))));
            transitionFrames.setWidth(Gdx.graphics.getWidth());
            transitionFrames.setHeight(Gdx.graphics.getHeight());
            transitionFrames.setPosition(0, 0);
            frame++;
            lastFrameTime = System.currentTimeMillis();
        } else {
            frame = 1;
        }
    }

    public void update() {
        if (System.currentTimeMillis() - lastFrameTime > frameDuration) {
            AmendAnimation();
        }
    }

    @Override
    public void draw(SpriteBatch batch) {
        // Drawing is handled by the stage
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

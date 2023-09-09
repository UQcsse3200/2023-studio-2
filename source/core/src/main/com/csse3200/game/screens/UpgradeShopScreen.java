package com.csse3200.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.csse3200.game.GdxGame;
import com.csse3200.game.components.spacenavigation.NavigationBackground;
import com.csse3200.game.entities.Entity;

/**
 * Represents the screen for the Shop that user can select items to upgrade their spaceship.
 */
public class UpgradeShopScreen extends ScreenAdapter {

    /** Reference to the main game instance */
    private final GdxGame game;

    /** Stage where all actors will be drawn */
    private Stage stage;

    /** Skin for the UI elements */
    private Skin skin;

    /** Player that can go arround and pick the item */
    private Entity player;

    /**
     * Constructs a new SpaceNavigationScreen with a reference to the main game.
     * @param game The main game instance.
     */
    public UpgradeShopScreen(GdxGame game) {
        this.game = game;
    }

    /**
     * Invoked when this screen becomes the current screen.
     */
    @Override
    public void show() {

        // Initialise a stage for the scene
        stage = new Stage(new ScreenViewport());

        // Animated background
        NavigationBackground animatedBackground = new NavigationBackground();
        stage.addActor(animatedBackground);

        // Create Back button
        skin = new Skin(Gdx.files.internal("flat-earth/skin/flat-earth-ui.json"));

        TextButton button = new TextButton("Back", skin);
        button.setPosition(Gdx.graphics.getWidth() - (button.getWidth() + 20),
                Gdx.graphics.getHeight() - (button.getHeight() + 20) );

        button.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                game.setScreen(GdxGame.ScreenType.MAIN_MENU);
            }
        });

        stage.addActor(button);
        // register input processor
        Gdx.input.setInputProcessor(stage);
    }



    /**
     * Renders the screen.
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    /**
     * Resizes the viewport dimensions based on the given width and height.
     * @param width The new width.
     * @param height The new height.
     */
    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    /**
     * Method called when the game is paused. Currently empty to prevent pausing on the space map.
     */
    @Override
    public void pause() {
        // Blank to prevent pausing on the space map
    }

    /**
     * Method called when the game is resumed after pausing. Currently empty as there is no pausing on the space map.
     */
    @Override
    public void resume() {
        // Left blank as there is no pausing on the space map
    }

    /**
     * Method called when this screen is no longer the current screen for the game.
     * Currently left blank to avoid any unwanted behavior.
     */
    @Override
    public void hide() {
        // Left blank as unwanted behaviour
    }
}
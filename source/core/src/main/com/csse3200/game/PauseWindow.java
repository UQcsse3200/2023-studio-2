package com.csse3200.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.csse3200.game.components.PowerupType;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.input.InputOverrideComponent;
import com.csse3200.game.services.ServiceLocator;

import static com.csse3200.game.screens.MainMenuScreen.logger;

/**
 * This is a window that can be added to a stage to pop up for the extractor Laboratory.
 */
public class PauseWindow extends Window {
    private final InputOverrideComponent inputOverrideComponent;
    Table buttonTable;
    Entity entity;
    Table exit;

    public static PauseWindow MakeNewPauseWindow(Entity entity) {
        Texture background = new Texture("images/structures/panel.png");
        background.setWrap(Texture.TextureWrap.ClampToEdge, Texture.TextureWrap.ClampToEdge);
        return new PauseWindow(background, entity);
    }

    public PauseWindow(Texture background, Entity entity) {
        super("", new WindowStyle(new BitmapFont(), Color.BLACK, new TextureRegionDrawable(background)));
        this.entity = entity;
        // Here set up the window to be centered on the stage with 80% width and 65% height.
        Stage stage = ServiceLocator.getRenderService().getStage();
        setWidth((float) (stage.getWidth() * 0.50));
        setHeight((float) (stage.getHeight() * 0.50));
        setPosition(stage.getWidth() / 2 - getWidth() / 2 * getScaleX(), stage.getHeight() / 2 - getHeight() / 2 * getScaleY());
        Skin skin = new Skin(Gdx.files.internal("kenney-rpg-expansion/kenneyrpg.json"));
        // Create a Table to hold the buttons and center them within the window
        Table buttonTable = new Table();
        buttonTable.setFillParent(true);
        // Fill the entire LabWindow

        Table exit = new Table();

        TextButton returnToGameButton = new TextButton("Return to Game", skin);
        TextButton returnToMainMenuButton = new TextButton("Exit to Main Menu", skin);
        Label gamePausedLabel = new Label("Game Paused", skin, "large");
        gamePausedLabel.setFontScale(0.5f);


        float buttonWidth = 150f; // Adjust as needed
        float buttonHeight = 180f;

        returnToGameButton.setWidth(buttonWidth);
        returnToGameButton.setHeight(buttonHeight);

        returnToMainMenuButton.setWidth(buttonWidth);
        returnToMainMenuButton.setHeight(buttonHeight);

        buttonTable.top();
        buttonTable.add(gamePausedLabel).padTop(70);
        buttonTable.row();
        buttonTable.add(returnToGameButton).padTop(70).padLeft(20);
        buttonTable.row();
        buttonTable.add(returnToMainMenuButton).padTop(50).padLeft(20);
        addActor(buttonTable);
        addActor(exit);


        returnToGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                exitToGame();
            }
        });
        returnToMainMenuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                exitToMainMenu();
            }
        });
        // Override all normal user input
        inputOverrideComponent = new InputOverrideComponent();
        ServiceLocator.getInputService().register(inputOverrideComponent);
    }

    /**
     * Call this method to exit the Laboratory
     */
    private void exitToGame() {
        entity.getEvents().trigger("returnPressed");
        remove();
    }

    private void exitToMainMenu() {
        entity.getEvents().trigger("exitPressed");
        remove();
    }

    /**
     * Call this method to exit the Laboratory and repair the extractor's health.
     */

    @Override
    public boolean remove() {
        // Stop overriding input when exiting the Laboratory
        ServiceLocator.getInputService().unregister(inputOverrideComponent);
        return super.remove();
    }
}
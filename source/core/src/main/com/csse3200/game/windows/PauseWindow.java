package com.csse3200.game.windows;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.input.InputOverrideComponent;
import com.csse3200.game.services.ServiceLocator;

/**
 * This is a window which opens when the pause button is pressed. The user is given the choice
 * to either return to the game or exit to the main menu.
 */
public class PauseWindow extends Window {
    private final InputOverrideComponent inputOverrideComponent;
    private Entity entity;

    public static PauseWindow MakeNewPauseWindow(Entity entity) {
        Texture background = new Texture("images/structures/panel.png");
        background.setWrap(Texture.TextureWrap.ClampToEdge, Texture.TextureWrap.ClampToEdge);
        return new PauseWindow(background, entity);
    }

    public PauseWindow(Texture background, Entity entity) {
        super("", new WindowStyle(new BitmapFont(), Color.BLACK, new TextureRegionDrawable(background)));
        this.entity = entity;
        Stage stage = ServiceLocator.getRenderService().getStage();
        setWidth((float) (stage.getWidth() * 0.50));
        setHeight((float) (stage.getHeight() * 0.50));
        setPosition(stage.getWidth() / 2 - getWidth() / 2 * getScaleX(), stage.getHeight() / 2 - getHeight() / 2 * getScaleY());
        Skin skin = new Skin(Gdx.files.internal("kenney-rpg-expansion/kenneyrpg.json"));
        Table pauseTable = new Table();
        pauseTable.setFillParent(true);

        TextButton returnToGameButton = new TextButton("Return to Game", skin);
        TextButton returnToMainMenuButton = new TextButton("Exit to Main Menu", skin);
        Label gamePausedLabel = new Label("Game Paused", skin, "large");
        gamePausedLabel.setFontScale(0.5f);


        float buttonWidth = 150f;
        float buttonHeight = 180f;

        returnToGameButton.setWidth(buttonWidth);
        returnToGameButton.setHeight(buttonHeight);

        returnToMainMenuButton.setWidth(buttonWidth);
        returnToMainMenuButton.setHeight(buttonHeight);

        pauseTable.top();
        pauseTable.add(gamePausedLabel).padTop(70);
        pauseTable.row();
        pauseTable.add(returnToGameButton).padTop(70).padLeft(20);
        pauseTable.row();
        pauseTable.add(returnToMainMenuButton).padTop(50).padLeft(20);
        addActor(pauseTable);


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
     * Returns to current game, and removes pause window.
     */
    private void exitToGame() {
        entity.getEvents().trigger("returnPressed");
        remove();
    }

    /**
     * Exits to main menu screen, and removes pause window.
     */
    private void exitToMainMenu() {
        entity.getEvents().trigger("exitPressed");
        remove();
    }

    /**
     * This method removes the pause window popup and its buttons/label.
     */

    @Override
    public boolean remove() {
        // Stop overriding input when exiting the Laboratory
        ServiceLocator.getInputService().unregister(inputOverrideComponent);
        return super.remove();
    }
}
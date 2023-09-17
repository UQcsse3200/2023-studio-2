package com.csse3200.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.csse3200.game.components.PotionType;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.input.InputOverrideComponent;
import com.csse3200.game.services.ServiceLocator;

/**
 * This is a window that can be added to a stage to pop up for the extractor Laboratory.
 */
public class LabWindow extends Window {
    private final InputOverrideComponent inputOverrideComponent;
    Table buttonTable;
    Table exit;

    public static LabWindow MakeNewLaboratory(Entity deathpotion) {
        Texture background = new Texture("images/inventorynew.png");
        background.setWrap(Texture.TextureWrap.ClampToEdge, Texture.TextureWrap.ClampToEdge);
        return new LabWindow(background);
    }

    public LabWindow(Texture background) {
        super("", new WindowStyle(new BitmapFont(), Color.BLACK, new TextureRegionDrawable(background)));

        // Here set up the window to be centered on the stage with 80% width and 65% height.
        Stage stage = ServiceLocator.getRenderService().getStage();
        setWidth((float) (stage.getWidth() * 0.8));
        setHeight((float) (stage.getHeight() * 0.65));
        setPosition(stage.getWidth() / 2 - getWidth() / 2 * getScaleX(), stage.getHeight() / 2 - getHeight() / 2 * getScaleY());
        Skin skin = new Skin(Gdx.files.internal("kenney-rpg-expansion/kenneyrpg.json"));
        // Create a Table to hold the buttons and center them within the window
        Table buttonTable = new Table();
        buttonTable.setFillParent(true); // Fill the entire LabWindow


        Table exit = new Table();
        Texture potion1Image = new Texture("images/Potion3re.png");
        Texture potion2Image = new Texture("images/Potion4re.png");
        Texture potion3Image = new Texture("images/Potion2re.png");
        Texture potion4Image = new Texture("images/Potion1re.png");
        Image potion1ImageWidget = new Image(potion1Image);
        Image potion2ImageWidget = new Image(potion2Image);
        Image potion3ImageWidget = new Image(potion3Image);
        Image potion4ImageWidget = new Image(potion4Image);
        TextButton potion1 = new TextButton("Death", skin);
        TextButton potion2 = new TextButton("Speed", skin);
        TextButton potion3 = new TextButton("Health", skin);
        TextButton potion4 = new TextButton("Potion", skin);
        TextButton returnToGameButton = new TextButton("Return to Game", skin);
        float buttonWidth = 200f; // Adjust as needed
        float buttonHeight = 200f;
        potion1.setWidth(buttonWidth);
        potion1.setHeight(buttonHeight);

        potion2.setWidth(buttonWidth);
        potion2.setHeight(buttonHeight);

        potion3.setWidth(buttonWidth);
        potion3.setHeight(buttonHeight);

        potion4.setWidth(buttonWidth);
        potion4.setHeight(buttonHeight);

        returnToGameButton.setWidth(buttonWidth);
        returnToGameButton.setHeight(buttonHeight);

        potion1.add(potion1ImageWidget).width(60).height(64);
        potion2.add(potion2ImageWidget).width(60).height(64);
        potion3.add(potion3ImageWidget).width(60).height(64);
        potion4.add(potion4ImageWidget).width(60).height(64);
        buttonTable.top().left();
        buttonTable.add(potion1).padTop(100).padLeft(150);
        buttonTable.add(potion2).padTop(100).padLeft(190);
        buttonTable.add(potion3).padTop(100).padLeft(165);
        buttonTable.add(potion4).padTop(100).padLeft(180);
        buttonTable.row(); //Move to the next row
        exit.add(returnToGameButton).bottom().right().padBottom(70).padLeft(2800);
        addActor(buttonTable);
        addActor(exit);

        returnToGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                failLaboratory();
            }
        });
        potion1.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event,float x, float y){
                ServiceLocator.getGameArea().getCompanion().getEvents().trigger("SpawnPotion",PotionType.DEATH_POTION);
                failLaboratory();
            }
        });
        potion2.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ServiceLocator.getGameArea().getCompanion().getEvents().trigger("SpawnPotion",PotionType.HEALTH_POTION);
                failLaboratory();
            }
        });
        potion3.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ServiceLocator.getGameArea().getCompanion().getEvents().trigger("SpawnPotion",PotionType.SPEED_POTION);
                failLaboratory();
            }
        });

        potion4.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                failLaboratory();
            }
        });

        // Override all normal user input
        inputOverrideComponent = new InputOverrideComponent();
        ServiceLocator.getInputService().register(inputOverrideComponent);
    }

    /**
     * Call this method to exit the Laboratory
     */
    private void failLaboratory() {
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

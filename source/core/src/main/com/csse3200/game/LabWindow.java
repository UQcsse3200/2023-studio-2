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
import com.csse3200.game.areas.EarthGameArea;
import com.csse3200.game.areas.GameArea;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.PotionType;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.input.InputOverrideComponent;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.entities.factories.PotionFactory;

//**
/* LabWindow represents a pop-up window for the extractor Laboratory.
         * It provides options for the player to interact with the laboratory, such as using potions.
         */
public class LabWindow extends Window {

    private final InputOverrideComponent inputOverrideComponent;
    Table buttonTable;
    Table exit;

    /**
     * Creates a new Laboratory window with the specified background texture.
     *
     * @param background The background texture for the window.
     */
    public LabWindow(Texture background) {
        super("", new WindowStyle(new BitmapFont(), Color.BLACK, new TextureRegionDrawable(background)));

        // Set up the window's size and position to be centered on the stage
        Stage stage = ServiceLocator.getRenderService().getStage();
        setWidth((float) (stage.getWidth() * 0.8));
        setHeight((float) (stage.getHeight() * 0.65));
        setPosition(stage.getWidth() / 2 - getWidth() / 2 * getScaleX(), stage.getHeight() / 2 - getHeight() / 2 * getScaleY());

        Skin skin = new Skin(Gdx.files.internal("flat-earth/skin/flat-earth-ui.json"));

        // Create a Table to hold the buttons and center them within the window
        Table buttonTable = new Table();
        buttonTable.setFillParent(true);

        Table exit = new Table();

        Texture potion1Image = new Texture("images/deathpotion.png");
        Texture potion2Image = new Texture("images/potion2.png");
        Texture potion3Image = new Texture("images/potion3.png");
        Texture potion4Image = new Texture("images/potion4.png");
        Image potion1ImageWidget = new Image(potion1Image);
        Image potion2ImageWidget = new Image(potion2Image);
        Image potion3ImageWidget = new Image(potion3Image);
        Image potion4ImageWidget = new Image(potion4Image);

        TextButton potion1 = new TextButton("Death", skin);
        TextButton potion2 = new TextButton("Speed", skin);
        TextButton potion3 = new TextButton("Health", skin);
        TextButton potion4 = new TextButton("Potion", skin);
        TextButton button = new TextButton("Exit", skin);

        float buttonWidth = 200f; // Adjust as needed
        float buttonHeight = 200f;

        // Set button sizes
        potion1.setWidth(buttonWidth);
        potion1.setHeight(buttonHeight);

        potion2.setWidth(buttonWidth);
        potion2.setHeight(buttonHeight);

        potion3.setWidth(buttonWidth);
        potion3.setHeight(buttonHeight);

        potion4.setWidth(buttonWidth);
        potion4.setHeight(buttonHeight);

        // Add potion images to buttons
        potion1.add(potion1ImageWidget).width(60).height(64);
        potion2.add(potion2ImageWidget).width(60).height(64);
        potion3.add(potion3ImageWidget).width(60).height(64);
        potion4.add(potion4ImageWidget).width(60).height(64);

        // Configure button layout
        buttonTable.top().left();
        buttonTable.add(potion1).padTop(100).padLeft(150);
        buttonTable.add(potion2).padTop(100).padLeft(190);
        buttonTable.add(potion3).padTop(100).padLeft(165);
        buttonTable.add(potion4).padTop(100).padLeft(180);
        buttonTable.row(); // Move to the next row
        buttonTable.row();

        exit.add(button).bottom().right().padBottom(70).padLeft(2400);

        addActor(buttonTable);
        addActor(exit);

        // Add listeners for potion buttons
        potion1.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                failLaboratory();
            }
        });

        potion2.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                failLaboratory();
            }
        });

        potion3.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
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
     * Call this method to exit the Laboratory without repairing the extractor.
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

    /**
     * Creates a new Laboratory window with the specified background texture.
     *
     * @param deathpotion The entity representing the death potion.
     * @return A new Laboratory window.
     */
    public static LabWindow MakeNewLaboratory(Entity deathpotion) {
        Texture background = new Texture("images/inventorynew.png");
        background.setWrap(Texture.TextureWrap.ClampToEdge, Texture.TextureWrap.ClampToEdge);
        return new LabWindow(background);
    }
}

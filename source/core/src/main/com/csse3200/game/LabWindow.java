package com.csse3200.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.input.InputOverrideComponent;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.entities.factories.PotionFactory;

/**
 * This is a window that can be added to a stage to pop up for the extractor Laboratory.
 */
public class LabWindow extends Window {
    private final InputOverrideComponent inputOverrideComponent;
    private final Entity deathpotion;

    public static LabWindow MakeNewLaboratory(Entity deathpotion) {
        Texture background = new Texture("images/lab.png");
        background.setWrap(Texture.TextureWrap.ClampToEdge, Texture.TextureWrap.ClampToEdge);
        return new LabWindow(background, deathpotion);
    }

    public LabWindow(Texture background, Entity deathpotion) {
        super("", new WindowStyle(new BitmapFont(), Color.BLACK, new TextureRegionDrawable(background)));

        this.deathpotion = deathpotion;

        // Here set up the window to be centered on the stage with 80% width and 65% height.
        Stage stage = ServiceLocator.getRenderService().getStage();
        setWidth((float) (stage.getWidth() * 0.7));
        setHeight((float) (stage.getHeight() * 0.65));
        setPosition(stage.getWidth() / 2 - getWidth() / 2 * getScaleX(), stage.getHeight() / 2 - getHeight() / 2 * getScaleY());

        // Create a Table to hold the buttons and center them within the window
        Table buttonTable = new Table();
        buttonTable.center(); // Center-align the buttons

        Skin skin = new Skin(Gdx.files.internal("flat-earth/skin/flat-earth-ui.json"));
        TextButton button = new TextButton("Explore more", skin);
        TextButton buttonexit = new TextButton("Exit Laboratory", skin);

        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                succeedLaboratory();
            }
        });

        buttonexit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                failLaboratory();
            }
        });

        buttonTable.add(button).pad(10); // Add padding to the "Explore" button
        buttonTable.add(buttonexit).pad(10); // Add padding to the "Exit Laboratory" button

        // Add the buttonTable to the LabWindow and make it fill the window's width
        add(buttonTable).expand().fill();
        row(); // Move to the next row

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
    private void succeedLaboratory() {
        CombatStatsComponent getHealth = deathpotion.getComponent(CombatStatsComponent.class);
        getHealth.setHealth(getHealth.getMaxHealth());
        remove();
    }

    @Override
    public boolean remove() {
        // Stop overriding input when exiting the Laboratory
        ServiceLocator.getInputService().unregister(inputOverrideComponent);
        return super.remove();
    }
}

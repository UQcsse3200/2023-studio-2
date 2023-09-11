package com.csse3200.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.input.InputOverrideComponent;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.entities.factories.PotionFactory;

/**
 * This is a window that can be added to a stage to pop up for the Laboratory.
 */
public class LabWindow extends Window {
    private final InputOverrideComponent inputOverrideComponent;
    private final Entity deathpotion;
    Table buttonTable;
    Table exit;


    public static LabWindow MakeNewLaboratory(Entity deathpotion) {

        Texture background = new Texture("images/inventorynew.png");
        background.setWrap(Texture.TextureWrap.ClampToEdge, Texture.TextureWrap.ClampToEdge);
        return new LabWindow(background, deathpotion);
    }

    public LabWindow(Texture background, Entity deathpotion) {
        super("", new WindowStyle(new BitmapFont(), Color.BLACK, new TextureRegionDrawable(background)));

        this.deathpotion = deathpotion;

        // Here set up the window to be centered on the stage with 80% width and 65% height.
        Stage stage = ServiceLocator.getRenderService().getStage();
        setWidth((float) (stage.getWidth() * 0.8));
        setHeight((float) (stage.getHeight() * 0.65));
        setPosition(stage.getWidth() / 2 - getWidth() / 2 * getScaleX(), stage.getHeight() / 2 - getHeight() / 2 * getScaleY());
        Skin skin = new Skin(Gdx.files.internal("flat-earth/skin/flat-earth-ui.json"));

        Table buttonTable = new Table();
        buttonTable.setFillParent(true); // Fill the entire LabWindow


        Table exit = new Table();
        TextButton potion1 = new TextButton("Potion1", skin);
        TextButton potion2 = new TextButton("Potion2", skin);
        TextButton potion3 = new TextButton("Potion3", skin);
        TextButton potion4 = new TextButton("Potion4", skin);
        TextButton button = new TextButton("Exit", skin);

        buttonTable.top().left();
        buttonTable.add(potion1).padLeft(50).padTop(100);
        buttonTable.add(potion2).padLeft(20).padTop(100);
        buttonTable.add(potion3).padLeft(20).padTop(100);
        buttonTable.add(potion4).padLeft(20).padTop(100);
        buttonTable.row(); //Move to the next row
        buttonTable.row();
        exit.add(button).bottom().right().padBottom(70).padLeft(2400);
        addActor(buttonTable);
        addActor(exit);

        button.addListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                failLaboratory();
            }
        });
        potion1.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
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
    private void succeedLaboratory() {
        CombatStatsComponent extractorHealth = deathpotion.getComponent(CombatStatsComponent.class);
        extractorHealth.setHealth(extractorHealth.getMaxHealth());
        remove();
    }

    @Override
    public boolean remove() {
        // Stop overriding input when exiting the Laboratory
        ServiceLocator.getInputService().unregister(inputOverrideComponent);
        return super.remove();
    }
}
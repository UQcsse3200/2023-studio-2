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

/**
 * This is a window that can be added to a stage to pop up for the extractor Laboratory.
 */
public class LabWindow extends Window {
    private final InputOverrideComponent inputOverrideComponent;
    Table buttonTable;
    Table exit;

    public static LabWindow MakeNewLaboratory() {
        Texture background = new Texture("images/lab.png");
        background.setWrap(Texture.TextureWrap.ClampToEdge, Texture.TextureWrap.ClampToEdge);
        return new LabWindow(background);
    }

    public LabWindow(Texture background) {
        super("", new WindowStyle(new BitmapFont(), Color.BLACK, new TextureRegionDrawable(background)));

        // Here set up the window to be centered on the stage with 80% width and 65% height.
        Stage stage = ServiceLocator.getRenderService().getStage();
        setWidth((float) (stage.getWidth() * 0.70));
        setHeight((float) (stage.getHeight() * 0.50));
        setPosition(stage.getWidth() / 2 - getWidth() / 2 * getScaleX(), stage.getHeight() / 2 - getHeight() / 2 * getScaleY());
        Skin skin = new Skin(Gdx.files.internal("kenney-rpg-expansion/kenneyrpg.json"));
        // Create a Table to hold the buttons and center them within the window
        Table buttonTable = new Table();
        buttonTable.setFillParent(true);
        // Fill the entire LabWindow

        Table exit = new Table();
        Texture deathpotionImage = new Texture("images/powerups/death_potion.png");
        Texture speedpotionImage = new Texture("images/powerups/speed_boost.png");
        Texture healthpotionImage = new Texture("images/powerups/health_boost.png");
        Texture invincibilitypotionImage = new Texture("images/powerups/temp_immunity.png");
        Texture doubledamageImage = new Texture("images/powerups/double_damage.png");
        Texture extralifeImage = new Texture("images/powerups/extra_life.png");
        Texture snapImage = new Texture("images/powerups/snap.png");
        Texture doublecrossImage = new Texture("images/powerups/double_cross.png");


        Image potion1ImageWidget = new Image(deathpotionImage);
        Image potion2ImageWidget = new Image(speedpotionImage);
        Image potion3ImageWidget = new Image(healthpotionImage);
        Image potion4ImageWidget = new Image(invincibilitypotionImage);
        Image potion5ImageWidget = new Image(doubledamageImage);
        Image potion6ImageWidget = new Image(extralifeImage);
        Image potion7ImageWidget = new Image(snapImage);
        Image potion8ImageWidget = new Image(doublecrossImage);


        TextButton potion1 = new TextButton("Death", skin);
        TextButton potion2 = new TextButton("Speed", skin);
        TextButton potion3 = new TextButton("Health", skin);
        TextButton potion4 = new TextButton("Invincibility", skin);
        TextButton potion5 = new TextButton("2x Damage", skin);
        TextButton potion6 = new TextButton("Extra Life", skin);
        TextButton potion7 = new TextButton("Snap", skin);
        TextButton potion8 = new TextButton("Double Cross", skin);
        TextButton returnToGameButton = new TextButton("EXIT", skin);

        float buttonWidth = 150f; // Adjust as needed
        float buttonHeight = 180f;


        potion1.setWidth(buttonWidth);
        potion1.setHeight(buttonHeight);

        potion2.setWidth(buttonWidth);
        potion2.setHeight(buttonHeight);

        potion3.setWidth(buttonWidth);
        potion3.setHeight(buttonHeight);

        potion4.setWidth(buttonWidth);
        potion4.setHeight(buttonHeight);

        potion5.setWidth(buttonWidth);
        potion5.setHeight(buttonHeight);

        potion6.setWidth(buttonWidth);
        potion6.setHeight(buttonHeight);

        potion7.setWidth(buttonWidth);
        potion7.setHeight(buttonHeight);

        potion8.setWidth(buttonWidth);
        potion8.setHeight(buttonHeight);

        returnToGameButton.setWidth(buttonWidth);
        returnToGameButton.setHeight(buttonHeight);

        potion1.add(potion1ImageWidget).width(100).height(64);
        potion2.add(potion2ImageWidget).width(100).height(64);
        potion3.add(potion3ImageWidget).width(100).height(64);
        potion4.add(potion4ImageWidget).width(100).height(64);
        potion5.add(potion5ImageWidget).width(100).height(64);
        potion6.add(potion6ImageWidget).width(100).height(64);
        potion7.add(potion7ImageWidget).width(100).height(64);
        potion8.add(potion8ImageWidget).width(100).height(64);
        potion8.add(potion8ImageWidget).width(100).height(64);

        buttonTable.top();
        buttonTable.add(potion1).padTop(70).padLeft(40);
        buttonTable.add(potion2).padTop(70).padLeft(40);
        buttonTable.add(potion3).padTop(70).padLeft(40);
        buttonTable.add(potion4).padTop(70).padLeft(40);
        buttonTable.row();
        buttonTable.add(potion5).padTop(70).padLeft(40);
        buttonTable.add(potion6).padTop(70).padLeft(40);
        buttonTable.add(potion7).padTop(70).padLeft(40);
        buttonTable.add(potion8).padTop(70).padLeft(40);
        buttonTable.row(); //Move to the next row
//        buttonTable.add(returnToGameButton).padTop(100).padLeft(600);
        addActor(buttonTable);
        //addActor(exit);

//

        Table button2Table = new Table();
        button2Table.setFillParent(true);
        button2Table.add(returnToGameButton).padTop(400).padLeft(860);
        addActor(button2Table);


        returnToGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                failLaboratory();
            }
        });

        potion1.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ServiceLocator.getEntityService().getCompanion().getEvents().trigger("SpawnPowerup",PowerupType.DEATH_POTION);
                failLaboratory();
            }
        });
        potion2.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ServiceLocator.getEntityService().getCompanion().getEvents().trigger("SpawnPowerup",PowerupType.SPEED_BOOST);
                failLaboratory();
            }
        });
        potion3.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ServiceLocator.getEntityService().getCompanion().getEvents().trigger("SpawnPowerup",PowerupType.HEALTH_BOOST);
                failLaboratory();
            }
        });

        potion4.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ServiceLocator.getEntityService().getCompanion().getEvents().trigger("SpawnPowerup",PowerupType.TEMP_IMMUNITY);
                failLaboratory();
            }
        });

        potion5.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ServiceLocator.getEntityService().getCompanion().getEvents().trigger("SpawnPowerup",PowerupType.DOUBLE_DAMAGE);
                failLaboratory();
            }
        });

        potion6.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ServiceLocator.getEntityService().getCompanion().getEvents().trigger("SpawnPowerup",PowerupType.EXTRA_LIFE);
                failLaboratory();
            }
        });
        potion7.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ServiceLocator.getEntityService().getCompanion().getEvents().trigger("SpawnPowerup",PowerupType.SNAP);
                failLaboratory();
            }
        });
        potion8.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ServiceLocator.getEntityService().getCompanion().getEvents().trigger("SpawnPowerup",PowerupType.DOUBLE_CROSS);
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
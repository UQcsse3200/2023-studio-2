package com.csse3200.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.input.InputOverrideComponent;
import com.csse3200.game.services.ServiceLocator;

/**
 * This is a window that can be added to a stage to pop up for the extractor minigame.
 */
public class ExtractorMinigameWindow extends Window {
    private final InputOverrideComponent inputOverrideComponent;
    private final Entity extractor;

    /**
     * Returns a new Minigame window intialised with appropriate background.
     * @param extractor This extractor will be repaired to max health if the minigame is finished correctly.
     * @return New extractor minigame window
     */
    public static ExtractorMinigameWindow MakeNewMinigame(Entity extractor) {
        Texture background = ServiceLocator.getResourceService().getAsset("images/SpaceMiniGameBackground.png", Texture.class);
        background.setWrap(Texture.TextureWrap.ClampToEdge, Texture.TextureWrap.ClampToEdge);
        return new ExtractorMinigameWindow(background, extractor);
    }

    public ExtractorMinigameWindow(Texture background, Entity extractor) {
        super("", new Window.WindowStyle(new BitmapFont(), Color.BLACK, new TextureRegionDrawable(background)));

        this.extractor = extractor;

        //Here setup window to be centered on the stage with 80% width and 65% height.
        Stage stage = ServiceLocator.getRenderService().getStage();
        setWidth((float) (stage.getWidth()*0.8));
        setHeight((float) (stage.getHeight()*0.65));
        setPosition(stage.getWidth()/2 - getWidth()/2 * getScaleX(), stage.getHeight()/2 - getHeight()/2 * getScaleY());
        Skin skin = new Skin(Gdx.files.internal("flat-earth/skin/flat-earth-ui.json"));
        TextButton button = new TextButton("Complete Minigame", skin);
        TextButton button2 = new TextButton("Exit Minigame", skin);
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                succeedMinigame();
            }
        });
        button2.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                failMinigame();
            }
        });
        add(button);
        add(button2);

        //Overide all normal user input
        inputOverrideComponent = new InputOverrideComponent();

        ServiceLocator.getInputService().register(inputOverrideComponent);
    }

    /**
     * Call this method to exit the minigame without repairing the extractor.
     */
    private void failMinigame() {
        remove();
    }

    /**
     * Call this method to exit the minigame and repair the extractors health
     */
    private void succeedMinigame() {
        CombatStatsComponent extractorHealth = extractor.getComponent(CombatStatsComponent.class);
        extractorHealth.setHealth(extractorHealth.getMaxHealth());
        remove();
    }

    @Override
    public boolean remove() {
        //Stop overriding input when exiting minigame
        ServiceLocator.getInputService().unregister(inputOverrideComponent);
        return super.remove();
    }
}

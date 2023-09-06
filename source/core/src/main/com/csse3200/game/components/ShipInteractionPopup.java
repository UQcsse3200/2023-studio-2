package com.csse3200.game.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.input.InputOverrideComponent;
import com.csse3200.game.services.ServiceLocator;

public class ShipInteractionPopup extends Window {
    private final InputOverrideComponent inputOverrideComponent;

    public ShipInteractionPopup() {
        super("", new Window.WindowStyle(new BitmapFont(), Color.BLACK, getBrownBackgroundStatic()));

        Stage stage = ServiceLocator.getRenderService().getStage();
        setHeight((float) (stage.getHeight()*0.5));
        setWidth((float) (stage.getWidth()*0.3));
        setPosition(stage.getWidth()/2 - getWidth()/2 * getScaleX(), stage.getHeight()/2 - getHeight()/2 * getScaleY());
        Skin skin = new Skin(Gdx.files.internal("flat-earth/skin/flat-earth-ui.json"));

        inputOverrideComponent = new InputOverrideComponent();

        ServiceLocator.getInputService().register(inputOverrideComponent);
    }

    @Override
    public boolean remove() {
        ServiceLocator.getInputService().unregister(inputOverrideComponent);
        return super.remove();
    }
    private static TextureRegionDrawable getBrownBackgroundStatic() {
        Texture texture = createColoredTextureStatic(1, 1, Color.BROWN);
        return new TextureRegionDrawable(new TextureRegion(texture));
    }

    private static Texture createColoredTextureStatic(int width, int height, Color color) {
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fill();
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return texture;
    }

}


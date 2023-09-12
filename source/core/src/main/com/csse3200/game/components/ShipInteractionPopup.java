package com.csse3200.game.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.csse3200.game.input.InputOverrideComponent;
import com.csse3200.game.services.ServiceLocator;

public class ShipInteractionPopup extends Window {
    private final InputOverrideComponent inputOverrideComponent;

    private Label descriptionLabel;

    public ShipInteractionPopup() {
        super("", new Window.WindowStyle(new BitmapFont(), Color.BLACK, getBrownBackgroundStatic()));

        Stage stage = ServiceLocator.getRenderService().getStage();
        float popupWidth = (float) (stage.getWidth() * 0.3);  //setting the width of the popup
        float popupHeight = (float) (stage.getHeight() * 0.5);  //setting the height of the popup
        setPosition(stage.getWidth() / 2 - popupWidth / 2, stage.getHeight() / 2 - popupHeight / 2);
        setSize(popupWidth, popupHeight); //setting the popup size at once using width and height variables

        Skin skin = new Skin(Gdx.files.internal("flat-earth/skin/flat-earth-ui.json"));
        Label.LabelStyle labelStyle = skin.get(Label.LabelStyle.class);

        Label titleLabel = new Label("ship", labelStyle);
        titleLabel.setColor(Color.RED);
        add(titleLabel).padTop(20).center().top().expandX();
        row();

        descriptionLabel = new Label("The ship is not ready yet for takeoff", labelStyle);
        descriptionLabel.setWidth(popupWidth * 0.9f);  // Using 90% of the popup width
        descriptionLabel.setWrap(true); //helps the text to stay within the bounds of the popup
        add(descriptionLabel).width(popupWidth * 0.9f).padTop(20).padLeft(popupWidth * 0.05f).expand().fill();

        TextButton okButton = new TextButton("OK", skin);
        okButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ShipInteractionPopup.this.remove(); // Remove the popup from the stage
            }
        });
        okButton.setSize(100, 50);
        okButton.setPosition((getWidth() - okButton.getWidth()) / 2, 20);
        this.addActor(okButton);

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


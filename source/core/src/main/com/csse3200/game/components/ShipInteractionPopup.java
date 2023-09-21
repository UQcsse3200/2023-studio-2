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
import com.csse3200.game.GdxGame;
import com.csse3200.game.input.InputOverrideComponent;
import com.csse3200.game.screens.PlanetScreen;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.AlertBox;
import com.csse3200.game.ui.TitleBox;

import static com.csse3200.game.components.mainmenu.MainMenuActions.game;

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

        Skin skin = new Skin(Gdx.files.internal("kenney-rpg-expansion/kenneyrpg.json"));
        Label.LabelStyle labelStyle = skin.get(Label.LabelStyle.class);

        Label titleLabel = new Label("WELCOME!!", labelStyle);
        titleLabel.setColor(Color.RED);
        add(titleLabel).padTop(20).center().top().expandX();
        row();

        descriptionLabel = new Label("Welcome aboard the 'Phoenix,' travelers. I am the ship's AI, here to assist you on your cosmic journey. Destination coordinates are to be set, awaiting your command to initiate departure", labelStyle);
        descriptionLabel.setWidth(popupWidth * 0.9f);  // Using 90% of the popup width
        descriptionLabel.setWrap(true); //helps the text to stay within the bounds of the popup
        add(descriptionLabel).width(popupWidth * 0.9f).padTop(20).padLeft(popupWidth * 0.05f).expand().fill();

        TextButton okButton = new TextButton("Take Off", skin);
        okButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {


                game.setScreen(GdxGame.ScreenType.NAVIGATION_SCREEN);
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
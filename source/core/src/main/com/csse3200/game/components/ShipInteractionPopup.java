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
import com.badlogic.gdx.utils.Align;
import com.csse3200.game.GdxGame;
import com.csse3200.game.input.InputOverrideComponent;
import com.csse3200.game.screens.PlanetScreen;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.AlertBox;

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

        Label titleLabel = new Label("", labelStyle);
        titleLabel.setColor(Color.RED);
        add(titleLabel).padTop(20).center().top().expandX();
        row();

        descriptionLabel = new Label("Player: (Breathing heavily) That was tough, but we did it.\n\nEmily: (Nodding) Yeah, and now we need to get this spaceship up and running.\n\n(Sadly, the skilled astronaut NPC who had been helping them collapses)\n\nAstronaut NPC: (Weakly) I... I can't go on. The infection from that cut... it's too much." +
                "\nPlayer: (Gently) You've been a great help. Can you tell us about the planet we need to visit?\n\nAstronaut NPC: (Coughing) You must go to... the planet Verdant Heaven. It's... it's the only place where you can find... use this cosmic element dropped by the boss. Use the ship's AI to control it. I'll give you...", labelStyle);
        descriptionLabel.setAlignment(Align.center);
        descriptionLabel.setWidth(popupWidth * 0.9f);  // Using 90% of the popup width
        descriptionLabel.setWrap(true); //helps the text to stay within the bounds of the popup
        add(descriptionLabel).width(popupWidth * 0.9f).padTop(20f).padLeft(popupWidth * 0.05f).expand().fill();

        TextButton okButton = new TextButton("Start Journey", skin);
        okButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {


                game.setScreen(GdxGame.ScreenType.NAVIGATION_SCREEN);
            }
        });
        okButton.setSize(200, 50);
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
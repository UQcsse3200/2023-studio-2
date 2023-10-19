package com.csse3200.game.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Timer;
import com.csse3200.game.GdxGame;
import com.csse3200.game.components.maingame.MainGameActions;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.services.ServiceLocator;

import static com.csse3200.game.components.mainmenu.MainMenuActions.game;

public class ShipInteractionPopup extends Window {
    public ShipInteractionPopup() {
        super("", new Window.WindowStyle(new BitmapFont(), Color.BLACK, getBrownBackgroundStatic()));
        for (Entity mainGame : ServiceLocator.getEntityService().getEntitiesByComponent(MainGameActions.class)) {
            mainGame.getEvents().trigger("pauseGame");
        }
        Stage stage = ServiceLocator.getRenderService().getStage();
        float popupWidth = (float) (stage.getWidth() * 0.3);  //setting the width of the popup
        float popupHeight = (float) (stage.getHeight() * 0.5);  //setting the height of the popup
        setPosition(stage.getWidth() / 2 - popupWidth / 2, stage.getHeight() / 2 - popupHeight / 2);
        setSize(popupWidth, popupHeight); //setting the popup size at once using width and height variables

        Skin skin = new Skin(Gdx.files.internal("kenney-rpg-expansion/kenneyrpg.json"));
        Label.LabelStyle labelStyle = skin.get(Label.LabelStyle.class);

        row();

        TextButton okButton = new TextButton("Start Journey", skin);
        okButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (ServiceLocator.getGameStateObserverService().getStateData("nextPlanet").equals("END")) {
                    Texture endScreen = new Texture(Gdx.files.internal("images/Deathscreens/endscreen.jpg"));
                    TextureRegionDrawable endBackground = new TextureRegionDrawable(endScreen);
                    Image image = new Image(endBackground);
                    image.setHeight((float) Gdx.graphics.getHeight());
                    image.setWidth(Gdx.graphics.getWidth());
                    image.setPosition(0,stage.getHeight()-image.getHeight());
                    stage.addActor(image);
                    final Timer timer = new Timer();
                    Timer.Task switchToMainMenu = new Timer.Task() {
                        @Override
                        public void run() {
                            game.setScreen(GdxGame.ScreenType.MAIN_MENU);
                            timer.clear();
                        }
                    };
                    timer.schedule(switchToMainMenu, 5);
                } else {
                    game.setScreen(GdxGame.ScreenType.NAVIGATION_SCREEN);
                }
            }
        });
        okButton.setSize(150, 50);
        okButton.setPosition((getWidth() - okButton.getWidth()) / 2, 10);
        this.addActor(okButton);
    }

    @Override
    public boolean remove() {
        for (Entity mainGame : ServiceLocator.getEntityService().getEntitiesByComponent(MainGameActions.class)) {
            mainGame.getEvents().trigger("resumeGame");
        }
        return super.remove();
    }
    private static TextureRegionDrawable getBrownBackgroundStatic() {
        Texture texture = createColoredTextureStatic(1, 1);
        return new TextureRegionDrawable(new TextureRegion(texture));
    }

    private static Texture createColoredTextureStatic(int width, int heightr) {
        Pixmap pixmap = new Pixmap(width, heightr, Pixmap.Format.RGBA8888);
        pixmap.fill();
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return texture;
    }

}
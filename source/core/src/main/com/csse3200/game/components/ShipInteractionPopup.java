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
import com.csse3200.game.areas.map_config.ResourceCondition;
import com.csse3200.game.components.maingame.MainGameActions;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.rendering.ColorDrawable;
import com.csse3200.game.services.ServiceLocator;

import java.util.List;

public class ShipInteractionPopup extends Window {
    public ShipInteractionPopup(List<ResourceCondition> requirements) {
        super("", new Window.WindowStyle(new BitmapFont(), Color.BLACK, getBrownBackgroundStatic()));
        for (Entity mainGame : ServiceLocator.getEntityService().getEntitiesByComponent(MainGameActions.class)) {
            mainGame.getEvents().trigger("pauseGame");
        }

        setBackground(new ColorDrawable(0,0,0,1));

        Stage stage = ServiceLocator.getRenderService().getStage();
        float popupWidth = (float) (stage.getWidth() * 0.3);  //setting the width of the popup
        float popupHeight = (float) (stage.getHeight() * 0.5);  //setting the height of the popup
        setPosition(stage.getWidth() / 2 - popupWidth / 2, stage.getHeight() / 2 - popupHeight / 2);
        setSize(popupWidth, popupHeight); //setting the popup size aft once using width and height variables

        Skin skin = new Skin(Gdx.files.internal("kenney-rpg-expansion/kenneyrpg.json"));
        Label.LabelStyle labelStyle = skin.get(Label.LabelStyle.class);

        Label titleLabel = new Label("Warning", labelStyle);
        titleLabel.setColor(Color.RED);
        add(titleLabel).padTop(20).center().top().expandX();
        row();

        Label descriptionLabel = getLabel(requirements, labelStyle, popupWidth);
        descriptionLabel.setAlignment(Align.center);
        add(descriptionLabel).width(popupWidth * 0.9f).padTop(20).padLeft(popupWidth * 0.05f).expand().fill();

        TextButton okButton = new TextButton("Okay", skin);
        okButton.addListener(new CloseListener(this));
        okButton.setSize(150, 50);
        okButton.setPosition((getWidth() - okButton.getWidth()) / 2, 10);
        this.addActor(okButton);
    }

    private static Label getLabel(List<ResourceCondition> requirements, Label.LabelStyle labelStyle, float popupWidth) {
        StringBuilder requirementsString = new StringBuilder("You do not have enough resources to go to the next planet!");

        for (var requirement : requirements) {
            requirementsString.append(String.format("\n%d %s required.", requirement.threshold, requirement.resource));
        }

        Label descriptionLabel = new Label(requirementsString.toString(), labelStyle);

        descriptionLabel.setWidth(popupWidth * 0.9f);  // Using 90% of the popup width
        descriptionLabel.setWrap(true); //helps the text to stay within the bounds of the popup
        return descriptionLabel;
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

    class CloseListener extends ChangeListener {

        private final ShipInteractionPopup popup;

        public CloseListener(ShipInteractionPopup popup) {
            this.popup = popup;
        }

        @Override
        public void changed(ChangeEvent event, Actor actor) {
            popup.remove();
        }
    }
}

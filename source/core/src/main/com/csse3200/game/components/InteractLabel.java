package com.csse3200.game.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.csse3200.game.services.ServiceLocator;

public class InteractLabel extends Table {

    public InteractLabel() {
        super();

        TextureRegionDrawable fButtonDrawable = new TextureRegionDrawable(new TextureRegion(ServiceLocator.getResourceService().getAsset("images/f_button.png", Texture.class)));
        Image fButtonImage = new Image(fButtonDrawable);

        Skin skin = new Skin(Gdx.files.internal("kenney-rpg-expansion/kenneyrpg.json"));

        Label messageLabelBeforeF = new Label("Press ", skin);
        messageLabelBeforeF.setColor(Color.WHITE);

        Label messageLabelAfterF = new Label(" to interact", skin);
        messageLabelAfterF.setColor(Color.WHITE);

        this.add(messageLabelBeforeF);
        this.add(fButtonImage);
        this.add(messageLabelAfterF);

        this.pack();

        Stage stage = ServiceLocator.getRenderService().getStage();
        this.setPosition((stage.getWidth() - this.getWidth()) / 2, 10);
        this.setVisible(false);

        this.addAction(Actions.repeat(RepeatAction.FOREVER, Actions.sequence(Actions.fadeOut(0.5f), Actions.fadeIn(0.5f)))); //this makes the interaction prompt blink for better ui

    }
}



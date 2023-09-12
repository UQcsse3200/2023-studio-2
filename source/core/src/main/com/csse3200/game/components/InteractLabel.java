package com.csse3200.game.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.csse3200.game.services.ServiceLocator;

public class InteractLabel extends Label {

    public InteractLabel() {
        super("Press 'F' to interact", new Skin(Gdx.files.internal("flat-earth/skin/flat-earth-ui.json")));
        this.setColor(Color.WHITE);

        Stage stage = ServiceLocator.getRenderService().getStage();
        this.setPosition((stage.getWidth() - this.getWidth()) / 2, 10);
        this.setVisible(false);

        this.addAction(Actions.repeat(RepeatAction.FOREVER, Actions.sequence(Actions.fadeOut(0.5f), Actions.fadeIn(0.5f)))); //this makes the interaction prompt blink for better ui
    }
}


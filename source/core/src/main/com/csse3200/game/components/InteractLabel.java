package com.csse3200.game.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.csse3200.game.services.ServiceLocator;

public class InteractLabel extends Label {

    public InteractLabel(Skin skin) {
        super("Press 'F' to interact", skin);
        this.setColor(Color.WHITE);

        Stage stage = ServiceLocator.getRenderService().getStage();
        this.setPosition((stage.getWidth() - this.getWidth()) / 2, 10);
        this.setVisible(false);
    }
}


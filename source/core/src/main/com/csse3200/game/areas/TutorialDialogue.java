package com.csse3200.game.areas;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TutorialDialogue extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(TutorialDialogue.class);
   // private Window window = new Window("Tutorial",skin);
    private Table table = new Table(skin);
    private String message;

    @Override
    protected void draw(SpriteBatch batch) {

    }

    @Override
    public void create() {
        super.create();
        createDialogue();
    }
    public void createDialogue () {
        logger.info("Tutorial Dialogue 1");
        message = "Hello Player";
        table.center().left();
        Label label = new Label(message,skin);
        label.setColor(Color.WHITE);
        table.pad(50);
        table.add(label);
        message = "Welcome to the game";
        label = new Label(message,skin);
        table.pad(10).row();
        table.add(label);
        table.setFillParent(true);
        table.padBottom(45f).padRight(45f);
        stage.addActor(table);
    }
}

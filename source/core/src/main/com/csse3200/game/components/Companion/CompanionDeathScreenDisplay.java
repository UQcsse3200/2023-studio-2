package com.csse3200.game.components.Companion;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CompanionDeathScreenDisplay extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(CompanionDeathScreenDisplay.class);
    private static final float Z_INDEX = 2f;
    private Table tableImage;
    private Table tableButtons;
    @Override
    public void create() {
        super.create();
        addActors();
    }
    private void addActors() {
        tableImage = new Table();
        tableButtons = new Table();
        tableButtons.bottom();
        tableImage.setFillParent(true);
        tableButtons.setFillParent(true);
        Image titleImage = new Image(ServiceLocator.getResourceService().getAsset("images/companiondeathscreen.png", Texture.class));
        titleImage.setWidth(Gdx.graphics.getWidth());
        titleImage.setHeight(Gdx.graphics.getHeight());
        titleImage.setPosition(0, 0);
        TextButton exitBtn = new TextButton("Exit", skin);
        TextButton respawnBtn = new TextButton("Respawn", skin);

        exitBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Exit button clicked");
                        entity.getEvents().trigger("exit");
                    }
                });
        respawnBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Respawn button clicked");
                        entity.getEvents().trigger("respawn");
                    }
                });
        tableImage.add(titleImage);
        tableButtons.add(respawnBtn).padBottom(55f);
        tableButtons.row();
        tableButtons.add(exitBtn).padBottom(305f);
        stage.addActor(titleImage);
        stage.addActor(tableImage);
        stage.addActor(tableButtons);
    }
    @Override
    public void draw(SpriteBatch batch) {}

    @Override
    public float getZIndex() {
        return Z_INDEX;
    }

    @Override
    public void dispose() {
        tableImage.clear();
        tableButtons.clear();
        super.dispose();
    }
}

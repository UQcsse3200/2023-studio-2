package com.csse3200.game.components.ships;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.csse3200.game.components.maingame.MainGameExitDisplay;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Text;

public class ExitShopDisplay extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(ExitShopDisplay.class);
    private static final float Z_INDEX = 2f;
    private Table table;

    @Override
    public void create() {
        super.create();
        addActors();
    }

    private void addActors() {
        table = new Table();
        table.top().right();
        table.setFillParent(true);

        TextButton back = new TextButton("Back", skin);
        Table miniFrame = new Table();
        miniFrame.setVisible(false);
        TextButton mainMenu = new TextButton("Main menu", skin);
        TextButton resume = new TextButton("Resume", skin);
        miniFrame.add(mainMenu).pad(10f).row();
        miniFrame.add(resume);
        // Triggers an event when the button is pressed.
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        Color color = new Color(1f, 1f, 1f, 0.4f); // white with 30% opacity
        pixmap.setColor(color);
        pixmap.fill();
        Texture texture = new Texture(pixmap);
        TextureRegionDrawable drawable = new TextureRegionDrawable(new TextureRegion(texture));
        miniFrame.setBackground(drawable);
        pixmap.dispose();
        miniFrame.setPosition(Gdx.graphics.getWidth()/2f - 200, Gdx.graphics.getHeight()/2f - 50);
        miniFrame.setSize(500, 300);
        back.addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                miniFrame.setVisible(true); // Show the mini-frame when Back button is clicked
            }
        });
        mainMenu.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Exit button clicked");
                        entity.getEvents().trigger("exit");
                    }
                });
        resume.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                miniFrame.setVisible(false); // Hide the mini-frame when Current Game button is clicked
            }
        });

        table.add(back).padTop(10f).padRight(10f);
        stage.addActor(miniFrame);
        stage.addActor(table);
    }

    @Override
    public void draw(SpriteBatch batch) {
        // draw is handled by the stage
    }

    @Override
    public float getZIndex() {
        return Z_INDEX;
    }

    @Override
    public void dispose() {
        table.clear();
        super.dispose();
    }
}

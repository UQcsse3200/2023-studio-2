package com.csse3200.game.components.resources;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Scaling;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;

/**
 * A ui component for displaying player stats, e.g. health.
 */
public class ResourceDisplay extends UIComponent {
    Table table;

    /**
     * Creates reusable ui styles and adds actors to the stage.
     */
    @Override
    public void create() {
        super.create();
        addActors();
    }

    Stack produceBar(Resource resource) {
        Image barForegroundImage= new Image(ServiceLocator.getResourceService().getAsset("images/resourcebar_foreground.png", Texture.class));
        Image resourceBar = new Image(ServiceLocator.getResourceService().getAsset("images/resourcebar_" + resource.toString().toLowerCase() + ".png", Texture.class));
        Image barBackgroundImage= new Image(ServiceLocator.getResourceService().getAsset("images/resourcebar_background.png", Texture.class));
        Image lightsImage = new Image(ServiceLocator.getResourceService().getAsset("images/resourcebar_lights.png", Texture.class));

        Stack barStack = new Stack();
        barStack.add(barBackgroundImage);
        barStack.add(resourceBar);
        barStack.add(lightsImage);
        barStack.add(barForegroundImage);
        return barStack;
    }

    /**
     * Creates actors and positions them on the stage using a table.
     * @see Table for positioning options
     */
    private void addActors() {
        table = new Table();
        table.bottom().right();
        table.setFillParent(true);
        table.padBottom(45f).padRight(45f);
        int scale = 5;

        table.add(produceBar(Resource.Nebulite)).size(53*scale, 6*scale).pad(5);
        table.row();
        table.add(produceBar(Resource.Durasteel)).size(53*scale, 6*scale).pad(5);
        table.row();
        table.add(produceBar(Resource.Solstite)).size(53*scale, 6*scale).pad(5);
        stage.addActor(table);
    }

    @Override
    public void draw(SpriteBatch batch)  {
        // draw is handled by the stage
    }
}

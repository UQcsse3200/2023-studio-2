package com.csse3200.game.components.navigation;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;

public class PlanetComponent extends UIComponent {
    private final String PlanetImage;
    private final float x;
    private final float y;
    private Table table;
    public PlanetComponent(String PlanetImage,float x,float y)
    {

        this.PlanetImage=PlanetImage;
        this.x=x;
        this.y=y;


    }

    @Override
    public void create() {
        super.create();
        table=new Table();
        Image planetImage = new Image(ServiceLocator.getResourceService().getAsset(PlanetImage, Texture.class));
        Label label=new Label("Level 1 ",skin,"large");
        Button button=new Button(label,skin);
        button.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {

                        entity.getEvents().trigger("Navigate");
                    }
                });

        table.add(planetImage).align(Align.top).size(100f);
        table.row();
        table.add(button);
        table.setPosition(x,y);
        table.setSize(20,20);
        stage.addActor(table);
    }

    @Override
    protected void draw(SpriteBatch batch) {


    }

    @Override
    public void dispose() {
        table.clear();
        super.dispose();
        table.remove();
    }
}



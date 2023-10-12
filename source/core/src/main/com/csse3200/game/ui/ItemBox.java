package com.csse3200.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.rendering.Renderer;

public class ItemBox {
    ShapeRenderer shapeRenderer = new ShapeRenderer();
    Rectangle rectangle = new Rectangle(Gdx.graphics.getWidth() - 360,300,350,350);
    Rectangle rectangle2 = new Rectangle(rectangle.getX(),rectangle.getY() + rectangle.getHeight() - 100,100,100);

    Entity extractorIcon;
    private final Renderer renderer;

    boolean showing = false;

    public ItemBox(Entity extractorIcon, Renderer renderer){
        this.extractorIcon = extractorIcon;
        this.renderer = renderer;
    }

    public void triggerShow(){
        showing = !showing;
    }

    public void render(){
        if (!showing){
            extractorIcon.setPosition(999,999);
            return;
        }

        Vector2 extractorIconPos = renderer.getCamera().getWorldPositionFromScreen(new Vector2(Gdx.graphics.getWidth() - 360,Gdx.graphics.getHeight() - 550));
        extractorIcon.setPosition(extractorIconPos.x,extractorIconPos.y);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.rect(rectangle.getX(), rectangle.getY(), rectangle.getWidth(), rectangle.getHeight());
        shapeRenderer.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.rect(rectangle2.getX(), rectangle2.getY(), rectangle2.getWidth(), rectangle2.getHeight());
        shapeRenderer.end();
    }

    public boolean itemContainMouse(){
        if (!showing){
            return false;
        }
        return (Gdx.input.isButtonPressed(0) && rectangle2.contains(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY()));
    }

}

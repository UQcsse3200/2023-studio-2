package com.csse3200.game.rendering;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;

public class InteractionIndicator implements Renderable {
    private Vector2 position;
    private String message;
    private BitmapFont font;

    public InteractionIndicator(String message) {
        this.message = message;
        this.position = new Vector2();
        this.font = new BitmapFont();
    }

    public void setPosition(float x, float y) {
        this.position.set(x, y);
    }

    @Override
    public void render(SpriteBatch batch) {
        font.draw(batch, message, position.x, position.y);
    }

    @Override
    public int getLayer() {
        return 10;  // Render on top
    }

    public float getZIndex() {
        return 1000.0f;
    }

    public int compareTo(Renderable other) {
        return Float.compare(this.getZIndex(), other.getZIndex());
    }

}



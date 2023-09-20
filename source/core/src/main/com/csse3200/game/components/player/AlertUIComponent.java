package com.csse3200.game.components.player;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.csse3200.game.ui.UIComponent;

public class AlertUIComponent extends UIComponent implements IAlpha {
    private final String alert;
    private float alpha = 1.0f;
    private Table table;
    public AlertUIComponent(String alert) {
        this.alert = alert;
    }

    @Override
    public void create() {
        super.create();

        table = new Table();
        Label label = new Label(alert, skin);
        table.add(label);
    }

    @Override
    protected void draw(SpriteBatch batch) {
        Matrix4 originalMatrix = batch.getProjectionMatrix().cpy();

        var newScale = new Vector3();
        newScale = originalMatrix.getScale(newScale);
        newScale.scl(1/10f);

        var originalPosition = originalMatrix.getTranslation(new Vector3());
        var originalRotation = originalMatrix.getRotation(new Quaternion());

        var newMatrix = new Matrix4(originalPosition, originalRotation, newScale);
        batch.setProjectionMatrix(newMatrix);

        var currentPosition = entity.getPosition();
        entity.setPosition(currentPosition.scl(10));
        table.draw(batch, alpha);
        batch.setProjectionMatrix(originalMatrix); //revert projection
        entity.setPosition(currentPosition);
    }


    /**
     * Sets the alpha of the spritebatch
     *
     * @param alpha the alpha to set
     */
    @Override
    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }
}

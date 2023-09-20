package com.csse3200.game.components.player;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.csse3200.game.rendering.ColorDrawable;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;

public class AlertUIComponent extends UIComponent implements IAlpha {
    private static final float scaleFactor = 115;
    private final String alert;
    private float alpha = 1.0f;
    private Table table;
    private ColorDrawable backgroundColor1;
    private ColorDrawable backgroundColor2;
    public AlertUIComponent(String alert) {
        this.alert = alert;
    }

    @Override
    public void create() {
        super.create();

        table = new Table();

        Table innerTable = new Table();

        Label label = new Label(alert, skin);
        label.setColor(Color.BLACK);

        Texture texture = ServiceLocator.getResourceService()
                .getAsset("images/ActionFeedback/warning.png", Texture.class);
        Image image = new Image(texture);

        innerTable.add(image).padLeft(5).size(30,30);
        innerTable.add(label).padLeft(5).padRight(10);

        backgroundColor1 = new ColorDrawable(1,0.7f,0,1);
        table.setBackground(backgroundColor1);
        backgroundColor2 = new ColorDrawable(1,1,1,1);
        innerTable.setBackground(backgroundColor2);

        innerTable.pack();

        table.add(innerTable).pad(3);

        table.pack();
    }

    @Override
    protected void draw(SpriteBatch batch) {
        Matrix4 originalMatrix = batch.getProjectionMatrix().cpy();

        var newScale = new Vector3();
        newScale = originalMatrix.getScale(newScale);
        newScale.scl(1/scaleFactor);

        var originalPosition = originalMatrix.getTranslation(new Vector3());
        var originalRotation = originalMatrix.getRotation(new Quaternion());

        var newMatrix = new Matrix4(originalPosition, originalRotation, newScale);
        batch.setProjectionMatrix(newMatrix);


        var position = new Vector2(entity.getCenterPosition().x, entity.getPosition().y);
        position.scl(scaleFactor);
        table.setPosition(position.x, position.y);
        table.draw(batch, alpha);
        batch.setProjectionMatrix(originalMatrix); //revert projection
    }


    /**
     * Sets the alpha of the spritebatch
     *
     * @param alpha the alpha to set
     */
    @Override
    public void setAlpha(float alpha) {
        this.alpha = alpha;
        backgroundColor1.setAlpha(alpha);
        backgroundColor2.setAlpha(alpha);
    }
}

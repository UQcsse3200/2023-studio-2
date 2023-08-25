package com.csse3200.game.components;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.rendering.RenderComponent;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;

/**
 * A ui component for displaying a walls health on top of its location on the map.
 */
public class DisplayEntityHealthComponent extends UIComponent {
    private static final float SCALE_REDUCTION = 70;
    Table table;
    private Image heartImage;
    private Label healthLabel;

    /**
     * Creates reusable ui styles and adds actors to map
     */
    @Override
    public void create() {
        super.create();
        addActors();

        entity.getEvents().addListener("updateHealth", this::updateWallHealthUI);
        entity.getEvents().addListener("setPosition", this::updateUIPosition);
    }

    private void updateUIPosition() {
        table.setPosition(entity.getCenterPosition().x, entity.getCenterPosition().y);
    }

    /**
     * Creates actors and positions them on the map using a table.
     * @see Table for positioning options
     */
    private void addActors() {
        table = new Table();
        updateUIPosition();

        // Heart image
        float heartSideLength = 20f;
        heartImage = new Image(ServiceLocator.getResourceService().getAsset("images/heart.png", Texture.class));

        // Health text
        int health = entity.getComponent(CombatStatsComponent.class).getHealth();
        CharSequence healthText = String.format("Health: %d", health);
        healthLabel = new Label(healthText, skin);

        table.add(heartImage).size(heartSideLength).padRight(5f);
        table.add(healthLabel);
    }

    /**
     * Override the draw method in order to make text display smaller on the map.
     * Does this by making the projectionMatrix a smaller scale when drawing the
     * UI components, before reverting to the original projectionMatrix after.
     *
     * @param batch the SpriteBatch being used to render the table.
     * @see RenderComponent for default implementation
     */
    @Override
    public void draw(SpriteBatch batch)  {
        Matrix4 originalMatrix = batch.getProjectionMatrix().cpy(); // cpy() needed to properly set afterwards because calling set() seems to modify kept matrix, not replaces it

        var newScale = new Vector3();
        newScale = originalMatrix.getScale(newScale);
        newScale.scl(1/SCALE_REDUCTION);

        var originalPosition = originalMatrix.getTranslation(new Vector3());
        var originalRotation = originalMatrix.getRotation(new Quaternion());

        var newMatrix = new Matrix4(originalPosition, originalRotation, newScale);
        batch.setProjectionMatrix(newMatrix);
        var old_x = table.getX();
        var old_y = table.getY();

        var position = new Vector3(old_x, old_y, 0);
        position.scl(SCALE_REDUCTION);
        // scale the positions to match the new projection matrix
        table.setPosition(position.x, position.y);
        table.draw(batch, 1.0f);
        table.setPosition(old_x, old_y);
        batch.setProjectionMatrix(originalMatrix); //revert projection
    }

    /**
     * Returns the scale the projection matrix should now use for the height.
     *
     * @return the height scale to use when drawing the health info.
     */
    public float getHeightUnitsInPixel() {
        return 20f / SCALE_REDUCTION;
    }

    /**
     * Returns the scale the projection matrix should now use for the width.
     *
     * @return the width scale to use when drawing the health info.
     */
    public float getWidthUnitsInPixel() {
        return 20f / SCALE_REDUCTION;
    }

    /**
     * Updates the Wall's health on the ui.
     * @param health player health
     */
    public void updateWallHealthUI(int health) {
        CharSequence text = String.format("Health: %d", health);
        healthLabel.setText(text);
    }

    @Override
    public void dispose() {
        super.dispose();
        heartImage.remove();
        healthLabel.remove();
    }

}

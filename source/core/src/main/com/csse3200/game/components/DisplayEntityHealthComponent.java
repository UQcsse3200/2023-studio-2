package com.csse3200.game.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.csse3200.game.rendering.RenderComponent;
import com.csse3200.game.ui.UIComponent;

/**
 * A ui component for displaying a walls health on top of its location on the map.
 */
public class DisplayEntityHealthComponent extends UIComponent {
    private static final float SCALE_REDUCTION = 70;
    private final boolean center;
    private final float offsetX;
    private final float offsetY;
    private ProgressBar healthBar;


    public DisplayEntityHealthComponent(boolean center, float offsetX, float offsetY) {
        this.center = center;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    /**
     * Creates reusable ui styles and adds actors to map
     */
    @Override
    public void create() {
        super.create();

        if (getCombatStatsComponent() == null) {
            return;
        }

        healthBar = new ProgressBar(0, getCombatStatsComponent().getMaxHealth(), 1, false, skin);
        updateWallHealthUI(getCombatStatsComponent().getHealth());
        healthBar.setColor(new Color(0, 1, 0, 1));
        healthBar.setWidth(SCALE_REDUCTION - (10));
        updateUIPosition();

        entity.getEvents().addListener("updateHealth", this::updateWallHealthUI);
    }

    /**
     * Helper function to return the entities CombatStatsComponent
     * @return the entities CombatStatsComponent
     */
    private CombatStatsComponent getCombatStatsComponent() {
        return entity.getComponent(CombatStatsComponent.class);
    }

    /**
     * Updates the position of the UI in the game world (accounting for the difference in scale)
     */
    private void updateUIPosition() {
        float yPos = offsetY;
        float centerOffset = 0;
        if (center) {
            yPos += entity.getCenterPosition().y;
            centerOffset = healthBar.getHeight()/2;
        } else {
            yPos += entity.getPosition().y;
        }

        var position = new Vector3(entity.getCenterPosition().x + offsetX, yPos, 0);
        position.scl(SCALE_REDUCTION);
        updateWallHealthUI(100);
        healthBar.setPosition(position.x - healthBar.getWidth()/2, position.y - centerOffset);
    }

    /**
     * Override the draw method in order to make the UI elements display smaller on the map.
     * Does this by making the projectionMatrix a smaller scale when drawing the UI components,
     * before reverting to the original projectionMatrix after.
     *
     * @param batch the SpriteBatch being used to render the table.
     * @see RenderComponent for default implementation
     */
    @Override
    public void draw(SpriteBatch batch)  {
        updateUIPosition();
        Matrix4 originalMatrix = batch.getProjectionMatrix().cpy(); // cpy() needed to properly set afterwards because calling set() seems to modify kept matrix, not replaces it

        var newScale = new Vector3();
        newScale = originalMatrix.getScale(newScale);
        newScale.scl(1/SCALE_REDUCTION);

        var originalPosition = originalMatrix.getTranslation(new Vector3());
        var originalRotation = originalMatrix.getRotation(new Quaternion());

        var newMatrix = new Matrix4(originalPosition, originalRotation, newScale);
        batch.setProjectionMatrix(newMatrix);

        healthBar.draw(batch, 1.0f);
        batch.setProjectionMatrix(originalMatrix); //revert projection
    }

    /**
     * Updates the Wall's health on the ui.
     * @param health player health
     */
    public void updateWallHealthUI(int health) {
        healthBar.setValue(health);
    }

    @Override
    public void dispose() {
        super.dispose();
        healthBar.remove();
    }

}

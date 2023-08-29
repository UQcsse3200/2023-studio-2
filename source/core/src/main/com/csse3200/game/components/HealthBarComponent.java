package com.csse3200.game.components;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.csse3200.game.rendering.RenderComponent;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;

/**
 * A ui component for displaying an entities health at its location on the map.
 */
public class HealthBarComponent extends UIComponent {
    private static final float SCALE_REDUCTION = 70;
    private final boolean center;
    private final float offsetY;
    private final float width;
    private boolean setVisible = false;
    private ProgressBar healthBar;
    private Vector2 healthBarPosition;


    public HealthBarComponent(boolean center, float offsetY, float width) {
        this.center = center;
        this.width = width;
        this.offsetY = offsetY;
    }

    public HealthBarComponent(boolean center, Vector2 position) {
        this.center = center;
        this.width = 0.9f;
        this.offsetY = 0;
        this.healthBarPosition = position;
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

        healthBar = new ProgressBar(0, 800, 1, false, skin);
        updateWallHealthUI(entity.getComponent(CombatStatsComponent.class).getHealth());
        this.healthBar.setWidth(SCALE_REDUCTION * width);
        updateUIPosition(this.healthBarPosition);
        entity.getEvents().addListener("updateHealth", this::updateWallHealthUI);
        ServiceLocator.getRenderService().unregister(this);
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
    private void updateUIPosition(Vector2 position) {

        float yPos = offsetY;
        float centerOffset = 0;
        if (center) {
            yPos += entity.getCenterPosition().y;
            centerOffset = healthBar.getHeight();
        } else {
            yPos += entity.getCenterPosition().y;
        }

        var position2 = new Vector3(entity.getCenterPosition().x - width/2, yPos, 0);
        position2.scl(SCALE_REDUCTION);
        healthBar.setPosition(position2.x, position2.y - centerOffset);
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
        updateUIPosition(this.healthBarPosition);
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
        this.healthBar.setValue(health);
    }

    @Override
    public void dispose() {
        super.dispose();
        this.healthBar.remove();
    }

    public void show() {

        stage.addActor(healthBar);
        setVisible = false;

    }

    public Component hide() {
        this.healthBar.remove();
        setVisible = true;
        return null;
    }

}

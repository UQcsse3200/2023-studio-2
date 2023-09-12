/*package com.csse3200.game.components.structures;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.GridPoint2;
import com.csse3200.game.components.structures.tools.Tool;
import com.csse3200.game.entities.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.services.ServiceLocator;

import javax.swing.*;
import java.beans.PropertyChangeListener;

public abstract class HealTool extends Tool implements InputProcessor {
    private Entity selectedEntity;
    private boolean hKeyPressed;
    private Vector2 worldCoords;

    private Vector2 lastRightClickCoords;

    private boolean isSecondRightClick;
    private JScrollPane stage;

    public HealTool() {
        selectedEntity = null;
        hKeyPressed = false;
        worldCoords = new Vector2();
        lastRightClickCoords = null;
        isSecondRightClick = false;
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void interact(Entity player, GridPoint2 position) {
        if (rightClickEventOccurred()) {
            if (!isSecondRightClick) {
                lastRightClickCoords = new Vector2(Gdx.input.getX(), Gdx.input.getY());
                getStage().getViewport().addPropertyChangeListener((PropertyChangeListener) lastRightClickCoords);
                selectedEntity = determineSelectedEntity(new GridPoint2((int) lastRightClickCoords.x, (int) lastRightClickCoords.y));
            } else {
                selectedEntity = null;
                lastRightClickCoords = null;
                isSecondRightClick = false;
            }
            isSecondRightClick = !isSecondRightClick;
        }

    }

    // logic to determine if right-click occurred
    private boolean rightClickEventOccurred() {
        return Gdx.input.isButtonPressed(Input.Buttons.RIGHT);
    }

    // To implement logic for determining the selected entity
    private Entity determineSelectedEntity(GridPoint2 position) {
        // Iterate through all entities in the game
        for (Entity entity : ServiceLocator.getEntityService().getEntities()) {
            // Get the position and scale of the entity
            Vector2 entityPosition = entity.getPosition();
            Vector2 entityScale = entity.getScale();

            // Calculate the boundaries of the entity's bounding box
            float left = entityPosition.x - entityScale.x / 2;
            float right = entityPosition.x + entityScale.x / 2;
            float top = entityPosition.y + entityScale.y / 2;
            float bottom = entityPosition.y - entityScale.y / 2;

            // Check if the click position is within the boundaries of the entity
            if (position.x >= left && position.x <= right && position.y >= bottom && position.y <= top) {
                // The click position is within this entity's boundaries, so it's selected
                return entity;
            }
        }

        // No entity was found at the click position
        return null;
    }


    // logic to check if the H-key is pressed
    private boolean isHKeyPressed() {
        return Gdx.input.isKeyPressed(Keys.H);
    }

    // update world coordinates when right-click occurs
    public void updateWorldCoords(Vector2 coords) {
        worldCoords.set(coords);
    }

    public boolean keyDown(int keycode) {
        if (keycode == Keys.H) {
            handleHKeyPress();
        }
        return false;
    }

    private void handleHKeyPress() {
        if (isHKeyPressed() && selectedEntity != null) {
            selectedEntity.setHealAm(hKeyPressed = true);
            System.out.println("Health Boosted!");
            hKeyPressed = false;
        }
    }

    public JScrollPane getStage() {
        return stage;
    }

    public void setStage(JScrollPane stage) {
        this.stage = stage;
    }
}
*/
package com.csse3200.game.input;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.csse3200.game.GdxGame;
import com.csse3200.game.areas.ExtractorMiniGameArea;
import com.csse3200.game.areas.terrain.TerrainComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.services.ServiceLocator;

import static com.badlogic.gdx.math.MathUtils.floor;
import static com.csse3200.game.ui.UIComponent.skin;


public class FireInputComponent extends InputComponent {

    private final TerrainComponent terrain;
    private final ExtractorMiniGameArea area;

    public FireInputComponent(TerrainComponent terrain, ExtractorMiniGameArea area) {
        this.terrain = terrain;
        this.area = area;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector3 worldCoordinates = new Vector3(screenX, screenY, 0);
        terrain.unproject(worldCoordinates);

        // Create a bounding box for the entity
        Rectangle boundingBox =
                new Rectangle(entity.getPosition().x, entity.getPosition().y, entity.getScale().x, entity.getScale().y);
        // Check if the click/touch is within the bounds of the entity
        if (boundingBox.contains(worldCoordinates.x, worldCoordinates.y)) {
            if (area.mouseState == ExtractorMiniGameArea.MouseState.EXTINGUISHER) {
                // Dispose the entity when clicked
                entity.dispose();
                if (ServiceLocator.getEntityService().getEntitiesByComponent(FireInputComponent.class).isEmpty()) {
                    // Check and spawn fire entities if needed
                    checkAndSpawnFireEntitiesIfNeeded();
                }
                if (area.firePositions.isEmpty() && area.holePositions.isEmpty() && ServiceLocator.getEntityService().
                        getEntitiesByComponent(FireInputComponent.class).isEmpty() && ServiceLocator.getEntityService().
                        getEntitiesByComponent(HoleInputComponent.class).isEmpty()) {
                    createSuccessLabel();
                }
            } else if (area.mouseState == ExtractorMiniGameArea.MouseState.SPANNER) {
                area.spawnExtractorBang(floor(entity.getPosition().x), floor(entity.getPosition().y));
                this.dispose();
            }
            return true; // Return true to consume the event
        }
        return false; // Event not consumed
    }

    private void checkAndSpawnFireEntitiesIfNeeded() {
        // Check if there are no more fire entities in areaEntities
        if (!area.firePositions.isEmpty()) {
            area.spawnExtractorsFirePart();
        }
    }

    private void createSuccessLabel() {
        Label label = new Label("Success!", skin);
        label.setPosition(920, 1000);
        label.setSize(200, 50);
        label.setColor(Color.BLACK);
        ServiceLocator.getRenderService().getStage().addActor(label);
    }

}
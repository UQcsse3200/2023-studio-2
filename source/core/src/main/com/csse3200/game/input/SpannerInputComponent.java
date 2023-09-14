package com.csse3200.game.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.csse3200.game.areas.ExtractorMiniGameArea;
import com.csse3200.game.areas.terrain.TerrainComponent;
import com.csse3200.game.services.ServiceLocator;

public class SpannerInputComponent extends InputComponent {

    private final TerrainComponent terrain;
    private final ExtractorMiniGameArea area;

    public SpannerInputComponent(TerrainComponent terrain, ExtractorMiniGameArea area) {
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
            Pixmap spannerPixmap = new Pixmap(Gdx.files.internal("images/spannerCursor.png"));
            Gdx.graphics.setCursor(Gdx.graphics.newCursor(spannerPixmap, 0, 0));
            area.mouseState = ExtractorMiniGameArea.MouseState.SPANNER;
            return true; // Return true to consume the event
        }
        return false; // Event not consumed
    }

    @Override
    public void dispose() {
        super.dispose();
    }

}


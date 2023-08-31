package com.csse3200.game.components;

import com.csse3200.game.components.Component;

public class ExtractorRepairPartComponent extends Component {
    private int collectedCount = 0;

    @Override
    public void create() {
        // No action required on creation
    }

    @Override
    public void update() {
        // No continuous update logic required
    }

    @Override
    public void dispose() {
        // No resources to dispose
    }

    public void collect() {
        if (getEntity() != null) {
            collectedCount++;
            getEntity().dispose(); // Remove the entity from the world
        }
    }

    public int getCollectedCount() {
        return collectedCount;
    }
}

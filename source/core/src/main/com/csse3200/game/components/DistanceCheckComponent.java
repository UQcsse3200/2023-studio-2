package com.csse3200.game.components;

import com.csse3200.game.entities.Entity;

public class DistanceCheckComponent extends Component {
    private final float proximityDistance;
    private final InteractLabel interactLabel;

    public DistanceCheckComponent(float distance, InteractLabel label) {
        this.proximityDistance = distance;
        this.interactLabel = label;
    }

    public void checkDistance(Entity player) {
        interactLabel.setVisible(entity.getCenterPosition().dst(player.getCenterPosition()) <= proximityDistance);
    }
}


package com.csse3200.game.components;

import com.csse3200.game.entities.Entity;

public class DistanceCheckComponent extends Component {
    private float proximityDistance;
    private InteractLabel interactLabel;

    public DistanceCheckComponent(float distance, InteractLabel label) {
        this.proximityDistance = distance;
        this.interactLabel = label;
    }

    public void checkDistance(Entity player) {
        if (entity.getCenterPosition().dst(player.getCenterPosition()) <= proximityDistance) {
            interactLabel.setVisible(true);
        } else {
            interactLabel.setVisible(false);
        }
    }
}


package com.csse3200.game.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.entities.Entity;

public class FollowComponent extends Component {
    private final Entity playerEntity;
    private final float followspeed;
    private final float minimumDistance = 0.5f;

    public FollowComponent(Entity playerEntity, float followspeed) {
        this.playerEntity = playerEntity;
        this.followspeed = followspeed;
    }
    /*public void create(){
        entity.getEvents().addListener();
    }*/

    public void update() {
        if (playerEntity != null) {
            Vector2 playerPosition = playerEntity.getPosition();
            Vector2 currentPosition = entity.getPosition();

            Vector2 direction = playerPosition.cpy().sub(currentPosition);
            float distance = direction.len();

            if (distance > minimumDistance) {
                // Calculate movement only if the distance is greater than the minimum
                direction.nor().scl(followspeed * Gdx.graphics.getDeltaTime());

                currentPosition.add(direction);
                entity.setPosition(currentPosition);
            }
        }
    }
}


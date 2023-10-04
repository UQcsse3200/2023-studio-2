package com.csse3200.game.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.Companion.CompanionActions;
import com.csse3200.game.entities.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class FollowComponent extends Component{
    private final Entity followEntity;
    private float followSpeed;
    private float minimumDistance = 0.8f;

    private static Logger logger; // how to log print

    /**
     *
     * @param followEntity - the entity which we are following!
     * @param followSpeed - the speed of attraction towards that entity
     */
    public FollowComponent(Entity followEntity,float followSpeed){
        this.followEntity = followEntity;
        this.followSpeed = followSpeed;
    }


    /**
     * Sets the entities follow speed to the given input
     * @param followSpeed - the speed of which this entity approaches the followEntity. Positive = going towards,
     *                    negative means repelled away from the followEntity
     */
    public void setFollowSpeed(float followSpeed) {
        this.followSpeed = followSpeed;
    }
    public float getFollowSpeed() {
        return followSpeed;
    }

    /**
     * Update the entity following the followEntity.
     * Check if it exists, and if it does, apply our saved speed movement towards/away from that entity
     * <p>
     * Firstly, get the 'moving' property from the CompanionActions
     * If the moving property is true, don't suck in.
     * If the moving property is false, then suck in.
     *
     */
    public void update() {

            // move companion towards following entity
            moveEntityTowardFollowingEntity();


    }

    public void moveEntityTowardFollowingEntity() {
        //If the following entity is still existing, and not null, follow it
        if (followEntity != null) {
            //get position of the followEntity and the current entity
            Vector2 followEntityPosition = followEntity.getPosition();
            Vector2 currentPosition = entity.getPosition();

            Vector2 direction = followEntityPosition.cpy().sub(currentPosition);
            float distance = direction.len();

            //The entities are not overlapping on one another
            if (distance > minimumDistance) {
                // Calculate movement only if the distance is greater than the minimum
                direction.nor().scl(followSpeed * Gdx.graphics.getDeltaTime());
                currentPosition.add(direction);
                entity.setPosition(currentPosition);
            }
        }
    }
}
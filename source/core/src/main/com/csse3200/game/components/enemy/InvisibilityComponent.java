package com.csse3200.game.components.enemy;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.Component;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.rendering.AnimationRenderComponent;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class InvisibilityComponent extends Component {
    private boolean isInvis = true;
    private PhysicsMovementComponent movementComponent;
    private Entity entity;

    private long invisDuration;
    public InvisibilityComponent(Entity enemy){
        this.entity = enemy;
        this.movementComponent = entity.getComponent(PhysicsMovementComponent.class);
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(this::toggleInvis, 0, 5, TimeUnit.SECONDS);
    }

    /**
     * Switches the speed variable
     */
    public void toggleInvis() {
        // Toggle the speed field
        isInvis = !isInvis;
    }

    /**
     * Get the invis status of the entity
     * @return boolean value for that mode
     */
    public boolean getInvisMode(){
        return isInvis;
    }
    /**
     * Reduce movement speed and update movement task
     */
    @Override
    public void update() {
        if(isInvis){
            movementComponent.changeMaxSpeed(new Vector2(2f,2f));
            entity.getEvents().trigger("goInvisible");
        }else{
            movementComponent.changeMaxSpeed(new Vector2(0.5f,0.5f));
        }
        movementComponent.update();
    }
}

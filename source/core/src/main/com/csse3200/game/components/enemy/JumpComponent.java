package com.csse3200.game.components.enemy;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.Component;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.components.PhysicsMovementComponent;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class JumpComponent extends Component {
    private boolean Mode = true;
    private PhysicsMovementComponent movementComponent;
    private Entity entity;
    public  JumpComponent(Entity enemy){
        this.entity = enemy;
        this.movementComponent = entity.getComponent(PhysicsMovementComponent.class);
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(this::toggleMode, 0, 2, TimeUnit.SECONDS);
    }

    /**
     * Toggle the firing mode. If true fires and false otherwise
     */
    public void toggleMode() {
        // Toggle the speed field
        Mode = !Mode;
    }

    /**
     * Set movement speed to 0 when not jumping and activate jump by speeding up movement
     */
    @Override
    public void update() {
        if(Mode){
            movementComponent.changeMaxSpeed(new Vector2(4f,4f));
        }else{
            movementComponent.changeMaxSpeed(new Vector2(0f,0f));
        }
    }

}

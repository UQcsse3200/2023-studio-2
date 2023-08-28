package com.csse3200.game.components;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.PlayerFactory;
public class FollowComponent extends Component{
    private Entity playerEntity;
    private float followspeed;

    public FollowComponent(Entity playerEntity,float followspeed){
        this.playerEntity = playerEntity;
        this.followspeed = followspeed;
    }
    /*public void create(){
        entity.getEvents().addListener();
    }*/

    public void update(){
        if (playerEntity != null) {
            Vector2 playerPosition = playerEntity.getPosition();
            Vector2 currentPosition = entity.getPosition();

            Vector2 direction = playerPosition.cpy().sub(currentPosition).nor();
            Vector2 movement = direction.scl(followspeed * Gdx.graphics.getDeltaTime());

            currentPosition.add(movement);
            entity.setPosition(currentPosition);
        }
    }
}

package com.csse3200.game.components.Companion;

import com.csse3200.game.components.Component;
import com.csse3200.game.entities.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyCustomComponent extends Component {

    @Override
    public void create() {
        // This method is called when the entity is created and registered.
        // You can perform initialization logic here.
        // For example, you can set up initial properties or load resources.
    }

    @Override
    public void update() {
        // This method is called once per frame of the game and should be used for most component logic.
        // You can access the entity to which this component belongs using 'getEntity()'.
        Entity entity = getEntity();
        if (entity != null) {
            // Example: Update the position of the entity.
            entity.setPosition(entity.getPosition().add(1, 1));
        }
    }
}

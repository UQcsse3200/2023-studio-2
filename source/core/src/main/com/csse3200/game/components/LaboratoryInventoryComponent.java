package com.csse3200.game.components;

import com.csse3200.game.components.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LaboratoryInventoryComponent extends Component {
    private static final Logger logger = LoggerFactory.getLogger(InteractableComponent.class);
    private int potion;

    public LaboratoryInventoryComponent(int potion) {
        setpotion(potion);
    }

    public int getpotion() {
        return this.potion;
    }

    public Boolean haspotion(int potion) {
        return this.potion >= potion;
    }


    public void setpotion(int potion) {
        this.potion = Math.max(potion, 0);
        logger.debug("Setting potion to {}", this.potion);
    }


    public void addpotion(int potion) {
        setpotion(this.potion + potion);
    }
}

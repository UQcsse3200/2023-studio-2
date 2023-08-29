package com.csse3200.game.components.Companion;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.player.InventoryComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class CompanionInventoryComponent extends Component {
    private static final Logger logger = LoggerFactory.getLogger(InventoryComponent.class);
    private int gold;

    public CompanionInventoryComponent(int gold) {
        setGold(gold);
    }

    public int getGold() {
        return this.gold;
    }

    public Boolean hasGold(int gold) {
        return this.gold >= gold;
    }
    public void setGold(int gold) {
        this.gold = Math.max(gold, 0);
        logger.debug("Setting gold to {}", this.gold);
    }

    public void addGold(int gold) {
        setGold(this.gold + gold);
    }
}

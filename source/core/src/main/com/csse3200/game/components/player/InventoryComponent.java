package com.csse3200.game.components.player;

import com.csse3200.game.components.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A component intended to be used by the player to track their inventory.
 *
 * Currently only stores the gold amount but can be extended for more advanced
 * functionality such as storing items.
 * Can also be used as a more generic component for other entities.
 */
public class InventoryComponent extends Component {
  private static final Logger logger = LoggerFactory.getLogger(InventoryComponent.class);
  private int equipped = 1;

  public int getEquipped() {
    return equipped;
  }

  public void setEquipped(int i) {
    this.equipped = i;
  }

  public void cycleEquipped() {
    int equiped = getEquipped();
    if (equiped == 3) {
      this.equipped = 1;
    } else
      this.equipped++;
  }

}

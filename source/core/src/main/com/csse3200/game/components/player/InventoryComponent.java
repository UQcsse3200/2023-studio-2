package com.csse3200.game.components.player;

import com.csse3200.game.components.Component;
import com.csse3200.game.components.Weapons.WeaponType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

/**
 * A component intended to be used by the player to track their inventory.
 * Player can switch between weapons and weapons can be updated
 * Can also be used as a more generic component for other entities.
 */
public class InventoryComponent extends Component {
  private static final Logger logger = LoggerFactory.getLogger(InventoryComponent.class);
  private int equipped = 1;
  private final HashMap<Integer, WeaponType> equippedWMap = new HashMap<Integer, WeaponType>();

  @Override
  public void create() {
    equippedWMap.put(1, WeaponType.MELEE_KATANA);
    equippedWMap.put(2, WeaponType.RANGED_BOOMERANG);
    equippedWMap.put(3, WeaponType.WOODHAMMER);
  }

  public InventoryComponent() {
    create();
  }

  /**
   * @return int - the equipped weapon
   */
  public int getEquipped() {
    return equipped;
  }

  /**
   * Changes active inventory slot to a specific slot
   * 
   * @param i - the weapon to be equipped
   */
  public void setEquipped(int i) {
    this.equipped = i;
  }

  /**
   * Replaces the specified slot with a given weapon.
   *
   * @param slot       the slot to be updated
   * @param weaponType the weapon type to be placed in the slot
   */
  public void replaceSlotWithWeapon(int slot, WeaponType weaponType) {
    if (slot > 3 || slot < 1) {
      throw new IllegalArgumentException("Slot must be in range 1-3");
    }
    equippedWMap.remove(slot);
    equippedWMap.put(slot, weaponType);
  }

  /** Returns the current equipped weapons represented in a hash map **/
  public HashMap<Integer, WeaponType> getEquippedWeaponMap() {
    return equippedWMap;
  }

  public void placeInSlot(WeaponType weaponType) {
    int slot = switch (weaponType) {
      case MELEE_WRENCH, MELEE_KATANA, MELEE_BEE_STING -> 1; // melee
      case RANGED_SLINGSHOT, RANGED_BOOMERANG, RANGED_HOMING, RANGED_GRENADE -> 2; // ranged
      case WOODHAMMER, STONEHAMMER, STEELHAMMER -> 3; // building
      default -> throw new IllegalArgumentException("Slot not assigned: " + weaponType);
    };
    replaceSlotWithWeapon(slot, weaponType);
  }

  /**
   * Cycles between equiped weapons
   */
  public void cycleEquipped() {
    int equiped = getEquipped();
    if (equiped == 3) {
      this.equipped = 1;
    } else
      this.equipped++;
  }

  /**
   * Returns the equipped weapon type
   * 
   * @return WeaponType - Type of cureently equiped weapon
   */
  public WeaponType getEquippedType() {
    return this.equippedWMap.get(getEquipped());
  }

  /**
   * Updates weapon of the active inventory slot
   * 
   * @param weaponType - Type of the new weapon
   */
  public void changeEquipped(WeaponType weaponType) {
    System.out.println(weaponType);
    WeaponType equippedType = getEquippedType();
    this.equippedWMap.remove(equippedType);
    this.equippedWMap.put(getEquipped(), weaponType);
  }

}

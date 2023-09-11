package com.csse3200.game.components.player;

import com.csse3200.game.components.Component;
import com.csse3200.game.components.Weapons.WeaponType;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A component intended to be used by the player to track their inventory.
 * Player can switch between weapons and weapons can be updated
 * Can also be used as a more generic component for other entities.
 */
public class InventoryComponent extends Component {
  private static final Logger logger = LoggerFactory.getLogger(InventoryComponent.class);
  private int equipped = 1;
  private HashMap<Integer, WeaponType> equippedWMap = new HashMap<Integer, WeaponType>();

  public void create() {
    equippedWMap.put(1, WeaponType.ELEC_WRENCH);
    equippedWMap.put(2, WeaponType.SLING_SHOT);
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
   * @param i - the weapon to be equipped
   */
  public void setEquipped(int i) {
    this.equipped = i;
  }

  /**
   * Replaces the specified slot with a given weapon.
   *
   * @param slot the slot to be updated
   * @param weaponType the weapon type to be placed in the slot
   */
  public void replaceSlotWithWeapon(int slot, WeaponType weaponType) {
    if (slot > 3 || slot < 1) {
      throw new IllegalArgumentException("Slot must be in range 1-3");
    }
    equippedWMap.put(slot, weaponType);
  }

  public void placeInSlot(WeaponType weaponType) {
    int slot;

    switch (weaponType) {
      case STICK:
      case KATANA:
        slot = 1;
        break;

      case SLING_SHOT:
      case ELEC_WRENCH:
      case THROW_ELEC_WRENCH:
        slot = 2;  // ranged weapons
        break;

      case WOODHAMMER:
      case STONEHAMMER:
      case STEELHAMMER:
        slot = 3;  // building hammers
        break;

      default:
        throw new IllegalArgumentException("Slot not assigned: " + weaponType);
    }

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
   * @return WeaponType - Type of cureently equiped weapon
   */
  public WeaponType getEquippedType() {
    return this.equippedWMap.get(getEquipped());
  }

  /**
   * @param weaponType - Type of the new weapon
   */
  public void changeEquipped(WeaponType weaponType) {
    WeaponType equippedType = getEquippedType();
    this.equippedWMap.remove(equippedType);
    this.equippedWMap.put(getEquipped(), weaponType);
  }

}

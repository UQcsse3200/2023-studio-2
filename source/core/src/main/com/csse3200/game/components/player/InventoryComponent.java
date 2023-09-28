package com.csse3200.game.components.player;

import com.csse3200.game.components.Component;
import com.csse3200.game.components.Weapons.WeaponType;
import com.csse3200.game.entities.configs.SpawnerConfig;
import com.csse3200.game.entities.configs.WeaponConfigs;
import com.csse3200.game.services.GameTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

class InventoryItem {
  WeaponType weaponType;
  int ammoCount;
  int maxAmmo;
  private int attackCooldown;

  public InventoryItem(WeaponType weaponType, int ammo, int maxAmmo) {
    this.weaponType = weaponType;
    this.ammoCount = ammo;
    this.maxAmmo = maxAmmo;
    attackCooldown = 0;
  }
  public InventoryItem(WeaponType weaponType) {
    this.weaponType = weaponType;
    ammoCount = 100;
    maxAmmo = 100;
  }

  public WeaponType getItem() {
    return this.weaponType;
  }

  public int getAmmo() {
    return this.ammoCount;
  }

  public void changeItem(WeaponType weaponType) {
    this.weaponType = weaponType;
  }

  public void changeAmmo(int change) {
    ammoCount = Math.min(maxAmmo, Math.max(0, ammoCount + change));
  }

  public int getMaxAmmo() {
    return this.maxAmmo;
  }

  public void setAttackCooldown(int cooldown) {
    this.attackCooldown = cooldown;
  }

  public int getAttackCooldown() {
    return this.attackCooldown;
  }

  public void decCoolDown() {
    if (attackCooldown == 0)
      return;
    attackCooldown--;
  }
}

/**
 * A component intended to be used by the player to track their inventory.
 * Player can switch between weapons and weapons can be updated
 * Can also be used as a more generic component for other entities.
 */
public class InventoryComponent extends Component {
  private static final Logger logger = LoggerFactory.getLogger(InventoryComponent.class);
  private int equipped = 1;
  private final HashMap<Integer, InventoryItem> equippedWMap = new HashMap<Integer, InventoryItem>();
  private final WeaponConfigs config;

  @Override
  public void create() {
    equippedWMap.put(1, new InventoryItem(WeaponType.MELEE_KATANA));
    equippedWMap.put(2, new InventoryItem(WeaponType.RANGED_BOOMERANG, 30, 30));
    equippedWMap.put(3, new InventoryItem(WeaponType.WOODHAMMER, 1, 1));
  }

  @Override
  public void update() {
    this.equippedWMap.get(getEquipped()).decCoolDown();
  }

  public InventoryComponent(WeaponConfigs config) {
    create();
    this.config = config;
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
    equippedWMap.get(slot).changeItem(weaponType);
  }

  /** Returns the current equipped weapons represented in a hash map **/
  public ArrayList<WeaponType> getEquippedWeapons() {
    return equippedWMap.values().stream().map(InventoryItem::getItem).collect(Collectors.toCollection(ArrayList::new));
  }

  public void placeInSlot(WeaponType weaponType) {
    int slot = switch (config.GetWeaponConfig(weaponType).slotType) {
      case "melee" -> 1; // melee
      case "ranged" -> 2; // ranged
      case "building" -> 3; // building
      default -> throw new IllegalArgumentException("Slot not assigned: " + weaponType);
    };
    replaceSlotWithWeapon(slot, weaponType);
  }

  /**
   * Updates weapon of the active inventory slot
   *
   * @param weaponType - Type of the new weapon
   */
  public void changeEquipped(WeaponType weaponType) {
    replaceSlotWithWeapon(getEquipped(), weaponType);
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
    return this.equippedWMap.get(getEquipped()).getItem();
  }

  public int getCurrentAmmo() {
    return this.equippedWMap.get(getEquipped()).getAmmo();
  }

  public int getCurrentMaxAmmo() {
    return this.equippedWMap.get(getEquipped()).getMaxAmmo();
  }

  public void changeEquippedAmmo(int ammoChange) {
    this.equippedWMap.get(getEquipped()).changeAmmo(ammoChange);
  }

  public int getEquippedCooldown() {
    return this.equippedWMap.get(getEquipped()).getAttackCooldown();
  }

  public void setEquippedCooldown(int coolDown) {
    this.equippedWMap.get(getEquipped()).setAttackCooldown(coolDown);
  }
}

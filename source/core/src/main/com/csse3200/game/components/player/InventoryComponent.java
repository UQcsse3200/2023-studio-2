package com.csse3200.game.components.player;

import com.csse3200.game.components.Component;
import com.csse3200.game.components.Weapons.WeaponType;
import com.csse3200.game.entities.configs.SpawnerConfig;
import com.csse3200.game.entities.configs.WeaponConfig;
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
  private String equipped = "melee";
  private final HashMap<String, InventoryItem> equippedWMap = new HashMap<String, InventoryItem>();
  private final WeaponConfigs config;

  @Override
  public void create() {
    equippedWMap.put("melee", new InventoryItem(WeaponType.MELEE_KATANA));
    equippedWMap.put("ranged", new InventoryItem(WeaponType.RANGED_BOOMERANG, 30, 30));
    equippedWMap.put("building", new InventoryItem(WeaponType.WOODHAMMER));
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
  public String getEquipped() {
    return equipped;
  }

  /**
   * Changes active inventory slot to a specific slot
   * 
   * @param slot - the weapon to be equipped
   */
  public void setEquipped(String slot) {
    this.equipped = slot;
  }

  /**
   * Replaces the specified slot with a given weapon.
   *
   * @param slot       the slot to be updated
   * @param weaponType the weapon type to be placed in the slot
   */
  public void replaceSlotWithWeapon(String slot, WeaponType weaponType) {
    equippedWMap.get(slot).changeItem(weaponType);
  }

  /** Returns the current equipped weapons represented in a hash map **/
  public ArrayList<WeaponType> getEquippedWeapons() {
    return equippedWMap.values().stream().map(InventoryItem::getItem).collect(Collectors.toCollection(ArrayList::new));
  }

  public void placeInSlot(WeaponType weaponType) {
    replaceSlotWithWeapon(config.GetWeaponConfig(weaponType).slotType, weaponType);
  }

  /**
   * Updates weapon of the active inventory slot
   *
   */
  public void changeEquipped(WeaponType type) {
    this.equipped = config.GetWeaponConfig(type).slotType;
  }

  public String getSlotType(WeaponType weaponType) {
    return config.GetWeaponConfig(weaponType).slotType;
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

package com.csse3200.game.components.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.Weapons.WeaponType;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.WeaponConfig;
import com.csse3200.game.entities.factories.AttackFactory;
import com.csse3200.game.entities.factories.PlayerWeaponFactory;
import com.csse3200.game.services.ServiceLocator;
import net.dermetfan.utils.Pair;

import java.util.ArrayList;

/**
 * Class to implement weapon functionality in the player,
 * Acts as the central component for weapons being to
 * respond to an enemy attack use an attack factory to generate a weapon entity
 */
public class WeaponComponent extends Component {
    /* Entity reference to projectile that follows */
    private Entity holdingWeapon;

    /**
     * Function to Set up "weaponAttack" listener to respond to attacks with a weapon
     */
    @Override
    public void create() {
        entity.getEvents().addListener("weaponAttack", this::playerAttacking);
        entity.getEvents().addListener("changeWeapon", this::updateHolding);
        this.holdingWeapon = null;
        this.updateHolding(WeaponType.MELEE_KATANA);
    }

    /**
     * Core function to respond to weapon attacks takes a position and a rotation and spawn an entity
     * in that direction and begin the animation of the weapon
     *
     * @param weaponType    - click position
     * @param clickPosition - click location of mouse
     */
    private void playerAttacking(WeaponType weaponType, Vector2 clickPosition) {
        ArrayList<Pair<Entity, Integer>> attacks = AttackFactory.createAttacks(weaponType, clickPosition, entity);

        if (attacks == null) {
            return;
        }

        for (Pair<Entity, Integer> attack : attacks) {
            ServiceLocator.getEntityPlacementService().placeEntityAfter(attack.getKey(), attack.getValue());
        }

        InventoryComponent invComp = entity.getComponent(InventoryComponent.class);
        float attackDirection = calcRotationAngleInDegrees(entity.getCenterPosition(), clickPosition);
        makeNewHolding(weaponType, attackDirection);
        entity.getEvents().trigger("updateAmmo", invComp.getCurrentAmmo(),
                invComp.getCurrentMaxAmmo(), invComp.getCurrentAmmoUse());
    }

    private void updateHolding(WeaponType weaponType) {
        InventoryComponent invComp = entity.getComponent(InventoryComponent.class);
        WeaponConfig config = invComp.getConfigs().GetWeaponConfig(weaponType);

        if (config.slotType.equals("building")) {
            makeNewHolding(weaponType, 0);
        } else if (this.holdingWeapon != null && !this.holdingWeapon.getDisposed()) {
            this.holdingWeapon.dispose();
        }
    }

    /**
     * Creates a new static weapon for the player
     *
     * @param weapon - weapon to make the player hold
     */
    private void makeNewHolding(WeaponType weapon, float attackDirection) {
        if (this.holdingWeapon != null && !this.holdingWeapon.getDisposed()) {
            this.holdingWeapon.dispose();
        }
        this.holdingWeapon = PlayerWeaponFactory.createPlayerWeapon(weapon, attackDirection, entity);
        ServiceLocator.getEntityPlacementService().placeEntity(this.holdingWeapon);
    }

    /**
     * Calcuate angle between 2 points from the center point to the target point,
     * angle is
     * in degrees with 0degrees being in the direction of the positive x-axis going
     * counter clockwise
     * up to 359.9... until wrapping back around
     *
     * @param centerPt - point from where angle is calculated from
     * @param targetPt - Tart point to where angle is calculated to
     * @return return angle between points in degrees from the positive x-axis
     */
    private float calcRotationAngleInDegrees(Vector2 centerPt, Vector2 targetPt) {
        double angle = Math.toDegrees(Math.atan2(targetPt.y - centerPt.y, targetPt.x - centerPt.x));
        if (angle < 0) {
            angle += 360;
        }
        return (float) angle;
    }

}
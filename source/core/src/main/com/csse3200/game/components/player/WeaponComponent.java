package com.csse3200.game.components.player;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.Weapons.WeaponType;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.AttackFactory;
import com.csse3200.game.entities.factories.PlayerWeaponFactory;
import com.csse3200.game.services.ServiceLocator;

/**
 * Class to implement weapon functionality in the player,
 * Acts as the central component for weapons being to
 * respond to an enemy attack use an attack factory to generate a weapon entity
 */
public class WeaponComponent extends Component {
    /* Variable to store current duration before play can attack again */
    private int attackCooldown;
    /* Entity reference to projectile that follows */
    private Entity holdingWeapon;

    @Override
    public void update() {
        if (attackCooldown > 0) {attackCooldown--;}
    }

    /**
     * Function to Set up "weaponAttack" listener to respond to attacks with a weapon
     */
    @Override
    public void create() {
        entity.getEvents().addListener("weaponAttack", this::playerAttacking);
        entity.getEvents().addListener("changeWeapon", this::makeNewHolding);
        this.holdingWeapon = null;
        makeNewHolding(WeaponType.MELEE_KATANA);
        attackCooldown = 0;
    }

    /**
     * Core function to respond to weapon attacks takes a position and a rotation and spawn an entity
     * in that direction and begin the animation of the weapon
     * @param weaponType - click position
     * @param clickPosition - click location of mouse
     */
    private void playerAttacking(WeaponType weaponType, Vector2 clickPosition) {
        float initialRotation = calcRotationAngleInDegrees(entity.getPosition(), clickPosition);
        if (weaponType == WeaponType.MELEE_BEE_STING) {
            initialRotation = (initialRotation + (float) ((Math.random() - 0.5) * 270)) % 360;
        }

        int spawnAngleOffset = 0;
        switch (weaponType) {
            case MELEE_WRENCH, MELEE_KATANA:
                if (initialRotation < 120 || initialRotation > 300) {
                    spawnAngleOffset += 40;
                } else {
                    spawnAngleOffset -= 40;
                }
                break;
            case RANGED_SLINGSHOT, RANGED_BOOMERANG, RANGED_HOMING:
            default:
                spawnAngleOffset = 0;
        }

        float distance = 1.25f;

        Entity newAttack = AttackFactory.createAttack(weaponType, initialRotation, entity);
        var newPos = positionInDirection(initialRotation + spawnAngleOffset, distance, newAttack);
        ServiceLocator.getEntityPlacementService().PlaceEntityAt(newAttack, newPos);
    }

    /**
     * Creates a new static weapon for the player
     * @param weapon
     */
    private void makeNewHolding(WeaponType weapon) {
        if (this.holdingWeapon != null) {this.holdingWeapon.dispose();}

        this.holdingWeapon = PlayerWeaponFactory.createPlayerWeapon(weapon, entity);
        Vector2 placePos = positionInDirection(10, 0.3f, this.holdingWeapon);

        ServiceLocator.getEntityPlacementService().PlaceEntityAt(this.holdingWeapon, placePos);
    }

    /**
     * Returns the game position in a given direction at a given distance relative to the player
     * to center a given attack entity
     * @param direction direction the position should be in
     * @param distance distance infront of the player
     * @param attack the entity
     * @return position in the given direction at the distance
     */
    private Vector2 positionInDirection(double direction, float distance, Entity attack) {
        double radians = Math.toRadians(direction);
        float xOffset = (float) Math.cos(radians) * distance;
        float yOffset = (float) Math.sin(radians) * distance;
        Vector2 weaponScale = attack.getScale();
        Vector2 position = entity.getPosition();
        Vector2 playerScale = entity.getScale();

        return new Vector2(position.x + xOffset + playerScale.x/2 - weaponScale.x/2,
                position.y + yOffset + playerScale.y/2 - weaponScale.y/2 );
    }

    /**
     * sets atatck cooldown
     * @param cooldown to set attack cooldown to
     */
    public void setAttackCooldown(int cooldown) {
        this.attackCooldown = cooldown;
    }

    public int getAttackCooldown() {
        return this.attackCooldown;
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
        if (angle < 0) {angle += 360;        }
        return (float) angle;
    }

}
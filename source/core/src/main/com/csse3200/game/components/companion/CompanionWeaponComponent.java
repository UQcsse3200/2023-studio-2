package com.csse3200.game.components.companion;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.companionweapons.CompanionWeaponType;

import com.csse3200.game.entities.Entity;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.entities.factories.CompanionAttackFactory;
import com.csse3200.game.entities.factories.CompanionWeaponFactory;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.services.ServiceLocator;


/**
 * This handles the control of all companion weapons that interact with entities
 */
public class CompanionWeaponComponent extends Component {

    //only one weapon equipped
    private Entity CurrentWeapon;


    /**
     * Set up the listeners on the key binds
     */
    public void create() {
        ServiceLocator.getEntityService().getCompanion().getEvents().addListener("weaponAttack", this::playerAttacking);
        ServiceLocator.getEntityService().getCompanion().getEvents().addListener("changeWeapon", this::makeNewHolding);
//        ServiceLocator.getEntityService().getCompanion().getEvents().addListener("updateAmmo", this::tempPrintAmmo);
        this.CurrentWeapon = null;
        /*makeNewHolding(CompanionWeaponType.Death_Potion);*/
    }

//    private void tempPrintAmmo(int ammo, int maxAmmo) {
//    }


    public void playerAttacking(CompanionWeaponType weaponType, Vector2 clickPosition) {
        float initialRotation = calcRotationAngleInDegrees(ServiceLocator.getEntityService().getCompanion().getPosition(), clickPosition);



        int spawnAngleOffset = 0;
        switch (weaponType) {
            case Death_Potion:
                if (initialRotation < 120 || initialRotation > 300) {
                    spawnAngleOffset += 40;
                } else {
                    spawnAngleOffset -= 40;
                }
                break;
            default:
                spawnAngleOffset = 0;
        }

        float distance = 1.25f;
        Entity newAttack = CompanionAttackFactory.createAttack(weaponType, initialRotation,ServiceLocator.getEntityService().getCompanion());
        var newPos = positionInDirection(initialRotation + spawnAngleOffset, distance, newAttack);
        ServiceLocator.getEntityPlacementService().placeEntityAt(newAttack, newPos);
    }

    // change the weapon in use
    public void makeNewHolding(CompanionWeaponType weapon) {
        if (this.CurrentWeapon  != null) {this.CurrentWeapon .dispose();}
        this.CurrentWeapon  = CompanionWeaponFactory.createCompanionWeapon(weapon, ServiceLocator.getEntityService().getCompanion());

        Vector2 placePos = positionInDirection(10, 0.3f, this.CurrentWeapon );
        if (weapon == CompanionWeaponType.SHIELD) {
            placePos = positionInDirection(90, -0.7f, this.CurrentWeapon );
            this.CurrentWeapon.getEvents().trigger("startEffect", "shield");
        }


        ServiceLocator.getEntityPlacementService().placeEntityAt(this.CurrentWeapon , placePos);
        this.CurrentWeapon.addComponent(new HitboxComponent());
        if (weapon == CompanionWeaponType.Death_Potion  ){
        this.CurrentWeapon.getEvents().trigger("startEffect", "deathPotion");
        }
        if (weapon == CompanionWeaponType.SHIELD || weapon == CompanionWeaponType.SWORD ){
            this.CurrentWeapon.getEvents().trigger("startEffect", "shield");
        }
    }

    public Vector2 positionInDirection(double direction, float distance, Entity attack) {
        double radians = Math.toRadians(direction);
        float xOffset = (float) Math.cos(radians) * distance;
        float yOffset = (float) Math.sin(radians) * distance;
        Vector2 weaponScale = attack.getScale();
        Vector2 position = entity.getPosition();
        Vector2 playerScale = entity.getScale();

        return new Vector2(position.x + xOffset + playerScale.x/2 - weaponScale.x/2,
                position.y + yOffset + playerScale.y/2 - weaponScale.y/2 );
    }


    public float calcRotationAngleInDegrees(Vector2 centerPt, Vector2 targetPt) {
        double angle = Math.toDegrees(Math.atan2(targetPt.y - centerPt.y, targetPt.x - centerPt.x));
        if (angle < 0) {angle += 360;        }
        return (float) angle;
    }

}

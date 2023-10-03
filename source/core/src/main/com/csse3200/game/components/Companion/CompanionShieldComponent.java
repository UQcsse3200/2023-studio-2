package com.csse3200.game.components.Companion;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.Weapons.WeaponControllerComponent;
import com.csse3200.game.components.Weapons.WeaponType;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.AttackFactory;
import com.csse3200.game.entities.factories.CompanionShieldFactory;
import com.csse3200.game.entities.factories.PlayerWeaponFactory;
import com.csse3200.game.services.ServiceLocator;

import java.util.Objects;

/**
 * Class to implement the shield on the companion
 * Is the central component for weapons to respond to enemy attack
 * uses attack factory to generate the shield
 */
public class CompanionShieldComponent extends Component {
    private Entity holdingShield;

    /**
     * constructor to set up the companion mode switch
     */
    //public CompanionShieldComponent() {
        //entity.getEvents().addListener("companionModeChange", this::companionModeSwitched);
    //}

    /**
     * Function to set up attack listeners e.g. If you want the shield to do anything
     * Also set up the shield to follow
     */
    @Override
    public void create() {
        entity.getEvents().addListener("companionModeChange", this::companionModeSwitched);
        this.holdingShield = null;
        makeNewHolding(WeaponType.SHIELD);
    }


    /**
     * Function is called whenever a new mode has been set
     * Defence means you get a shield next to you, attack and normal mean the shield does get created
     * But it gets placed ages  away
     * This is due to the nature of the implementation of WeaponControllerConfig
     * @param newMode - string such as Normal, Attack, Defence
     */
    public void companionModeSwitched(String newMode) {
        //if it is defence, create a new shield
        if (Objects.equals(newMode, "Defence")) {

            makeNewHolding(WeaponType.SHIELD);
        } else {
            //dispose of the current shield
            //options "Normal" and "Attack"
            makeFakeHolding();
        }
    }

    /**
     * Creates a new static shield for the companion
     * @param weapon - the type of weapon (should be shield)
     */
    private void makeNewHolding(WeaponType weapon) {
        if (this.holdingShield != null) {this.holdingShield.dispose();}
        this.holdingShield = CompanionShieldFactory.createCompanionShield(weapon, entity);
        Vector2 placePos = positionInDirection(90, -0.8f, this.holdingShield);

        ServiceLocator.getEntityPlacementService().PlaceEntityAt(this.holdingShield, placePos);
    }

    /**
     * Call this if you want to create a shield ages away from the companion
     * Essentially, we must provide the WeaponController class with an entity (we think) or else it will break
     * So, we provide it with a shield ages away
     */
    private void makeFakeHolding() {
        if (this.holdingShield != null) {this.holdingShield.dispose();}
        this.holdingShield = CompanionShieldFactory.createCompanionShield(WeaponType.SHIELD, entity);
        Vector2 placePos = positionInDirection(90, 8000f, this.holdingShield);

        ServiceLocator.getEntityPlacementService().PlaceEntityAt(this.holdingShield, placePos);
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




}

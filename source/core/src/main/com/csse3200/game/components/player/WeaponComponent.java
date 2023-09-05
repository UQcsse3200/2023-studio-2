package com.csse3200.game.components.player;

import com.csse3200.game.entities.factories.AttackFactory;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.Weapons.WeaponType;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.ServiceLocator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.g2d.Animation;

/**
 * Class to implement weapon functionality in the player,
 * Acts as the central component for weapons being to
 * repond to an enemy attack use an attack factory to generate a weapon entity
 */
public class WeaponComponent extends Component {
    /* Variable to hold reference to animation render component */
    AnimationRenderComponent animator;

    /**
     * Function to Set up "weaponAttack" listener to respond to attacks with a weapon
     */
    @Override
    public void create() {
        //Trigger to repond to player attack
        entity.getEvents().addListener("weaponAttack", this::playerAttacking);
    }

    /**
     * Core function to reponsd to weapon attacks takes a position and a rotation and spawn an entity
     * in that direction and begin the animation of the weapon
     * @param position - position of the player at the time of attack
     * @param initRot - direction in which mouse is relative to the player in degrees
     *                  0<=initRot<=360, East:0, North:90: West:180, South:270
     */
    private void playerAttacking(Vector2 position, WeaponType weaponType, float initRot) {
        //Player will likely store a weapon type in the future (entity.getCurrentWeapon) or something
        Entity newAttack = AttackFactory.createAttack(weaponType, initRot);
        ServiceLocator.getEntityPlacementService().PlaceEntityAt(newAttack, position);
        // animator = newAttack.getComponent(AnimationRenderComponent.class);  todo: daniel plz
        // animator.startAnimation("attack");
    }
}
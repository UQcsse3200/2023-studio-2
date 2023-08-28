package com.csse3200.game.components.player;

import com.csse3200.game.entities.factories.AttackFactory;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.components.Component;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.ServiceLocator;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.g2d.Animation;
//import com.badlogic.gdx.Gdx;
/**
 *
 */
public class WeaponComponent extends Component {
    AnimationRenderComponent animator;
    /**
     *
     */
    public WeaponComponent() {
        return;
    }

    @Override
    public void create() {
        entity.getEvents().addListener("playerAttack", this::playerAttacking);
    }

    private void playerAttacking(Vector2 position, int initRot) {
        Entity newAttack = AttackFactory.createAttack(initRot);
        ServiceLocator.getStructurePlacementService().PlaceStructureAt(newAttack, position);
        animator = newAttack.getComponent(AnimationRenderComponent.class);
        animator.startAnimation("attack");
    }
}


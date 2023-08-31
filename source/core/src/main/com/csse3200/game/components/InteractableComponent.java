package com.csse3200.game.components;

import com.csse3200.game.entities.Entity;

import java.util.function.Consumer;

public class InteractableComponent extends Component {
    private final Consumer<Entity> interactFunction;
    private final float interactableDistance;
    public InteractableComponent(Consumer<Entity> runOnInteract, float distance) {
        interactFunction = runOnInteract;
        interactableDistance = distance;
    }

    public void interact(Entity target) {
        if (entity.getCenterPosition().dst(target.getCenterPosition()) <= interactableDistance) {
            interactFunction.accept(target);
        }
    }
}

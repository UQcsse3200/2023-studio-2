package com.csse3200.game.components;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.rendering.InteractionIndicator;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.ServiceLocator;

import java.util.function.Consumer;

public class InteractableComponent extends Component {
    private final Consumer<Entity> interactFunction;
    private final float interactableDistance;
    private InteractionIndicator interactionIndicator;
    private RenderService renderService;

    public InteractableComponent(Consumer<Entity> runOnInteract, float distance) {
        this.interactFunction = runOnInteract;
        this.interactableDistance = distance;
        this.interactionIndicator = new InteractionIndicator("Interact!");
        this.renderService = ServiceLocator.getRenderService();
    }

    public void interact(Entity target) {
        if (entity.getCenterPosition().dst(target.getCenterPosition()) <= interactableDistance) {
            interactFunction.accept(target);
            showInteractionIndicator(target);
        } else {
            hideInteractionIndicator();
        }
    }

    private void showInteractionIndicator(Entity target) {
        // Set the position of the indicator just above the target entity for visualization.
        Vector2 targetPos = target.getCenterPosition();
        interactionIndicator.setPosition(targetPos.x, targetPos.y + 1);  // 1 unit above the target entity
        renderService.register(interactionIndicator);
    }

    private void hideInteractionIndicator() {
        renderService.unregister(interactionIndicator);
    }
}


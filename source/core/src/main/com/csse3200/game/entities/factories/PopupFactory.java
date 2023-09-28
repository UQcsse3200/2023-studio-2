package com.csse3200.game.entities.factories;

import com.csse3200.game.components.resources.PopupComponent;
import com.csse3200.game.components.resources.Resource;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.rendering.TextureRenderComponent;

public class PopupFactory {
    public static Entity createPopup(Resource resource) {

        Entity popup = new Entity()
                .addComponent(new PopupComponent(500, 0.001));

        popup.setScale(0.4f, 0.4f);

        // Assigns texture based on the specific resource
        switch (resource) {
            case Durasteel -> popup.addComponent(new TextureRenderComponent("images/resources/durasteel.png"));
            case Nebulite -> popup.addComponent(new TextureRenderComponent("images/resources/nebulite.png"));
            case Solstite -> popup.addComponent(new TextureRenderComponent("images/resources/solstite.png"));
            default -> throw new IllegalArgumentException("You must assign a valid resource!");
        }

        ((TextureRenderComponent) popup.getComponent(TextureRenderComponent.class)).overrideZIndex(1000);
        return popup;
    }
}

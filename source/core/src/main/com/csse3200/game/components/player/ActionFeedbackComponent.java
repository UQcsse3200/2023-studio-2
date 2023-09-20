package com.csse3200.game.components.player;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.resources.PopupComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.rendering.RenderComponent;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class ActionFeedbackComponent extends Component {
    public ActionFeedbackComponent() {
    }

    @Override
    public void create() {
        super.create();

        entity.getEvents().addListener("displayWarning", this::displayWarning);
        entity.getEvents().addListener("displayError", this::displayError);
    }

    private void displayError() {
    }

    public void displayWarning(String alert) {
        Entity alertEntity = new Entity();
        AlertUIComponent alertUIComponent = new AlertUIComponent(alert);

        alertEntity.addComponent(alertUIComponent);

        alertEntity.addComponent(new PopupComponent(2000, 0.2));

        ServiceLocator.getEntityPlacementService().PlaceEntityAt(alertEntity, this.entity.getPosition());


    }
}

package com.csse3200.game.components.player;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.resources.PopupComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.services.ServiceLocator;

/**
 * This component is used to create action feedback alerts based on Entity events.
 */
public class ActionFeedbackComponent extends Component {
    /**
     * How long the alert will display for.
     */
    private static final int DURATION = 2000;
    /**
     * How fast the alert will float upwards.
     */
    private static final double SPEED = 0.001;

    /**
     * Adds two listeners to the entities events which, when invoked, can be used
     * to create and display custom warning alerts.
     */
    @Override
    public void create() {
        super.create();

        entity.getEvents().addListener("displayWarning", this::displayWarning);
        entity.getEvents().addListener("displayWarningAtPosition", this::displayWarningAtPosition);
    }

    /**
     * Displays an alert at the entities current position.
     * @param alert - the text to be displayed.
     */
    public void displayWarning(String alert) {
        displayWarningAtPosition(alert, this.entity.getPosition());
    }

    /**
     * Displays an alert at the given position.
     * @param alert - the text to be displayed.
     * @param position - the position to display the alert.
     */
    public void displayWarningAtPosition(String alert, Vector2 position) {
        Entity alertEntity = new Entity();
        AlertUIComponent alertUIComponent = new AlertUIComponent(alert);

        alertEntity.addComponent(alertUIComponent);

        // uses the popup component to automatically make it float away and fade out over
        // the predefined duration.
        alertEntity.addComponent(new PopupComponent(DURATION, SPEED));

        ServiceLocator.getEntityPlacementService().PlaceEntityAt(alertEntity, position);
    }
}

package com.csse3200.game.components.companion;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.TimerTask;

/**
 * A UI component which will display the useful in game alerts
 *
 */
public class CompanionInGameAlerts extends UIComponent {
    //The one alert Label whose location will change
    public Label alertLabel;
    //The player entity associated with this companion.
    public Entity player = ServiceLocator.getEntityService().getPlayer();
    public Entity companion = ServiceLocator.getEntityService().getCompanion();


    //store the previous player health and previous companion health
    public int previousPlayerHealth;
    public int previousCompanionHealth;

    /**
     * Default constructor for the companions in game alerts
     */
    public CompanionInGameAlerts() {
    }

    /**
     * Creates reusable UI styles and adds actors to the stage.
     * Also sets up listeners, such as for players health
     */
    @Override
    public void create() {
        super.create();
        addActors();
        //Listeners
        //List if the COMPANION has a health change
        entity.getEvents().addListener("updateHealth", this::companionHealthUpdate);

        //Player has a health change
        player.getEvents().addListener("updateHealth", this::playerHealthUpdate);

        previousPlayerHealth = player.getComponent(CombatStatsComponent.class).getHealth();
        previousCompanionHealth = entity.getComponent(CombatStatsComponent.class).getHealth();
    }

    /**
     * Add anything to a set table on screen
     */
    private void addActors() {
        return;
    }

    /**
     * Update.
     * Called once per frame
     * Check if there is an alert currently on the screen.
     * If there is, update its location to be tracking the companion (of course)
     */
    @Override
    public void update() {
    }

    /**
     * Function that returns the current companion position
     * @return - a vector2 with the current companion position in x and y coordinates
     */
    public Vector2 getCurrentPosition() {
        PhysicsComponent companionPhysics = entity.getComponent(PhysicsComponent.class);
        return companionPhysics.getBody().getPosition();
    }

    /**
     * This function will create a following alert
     * @param alertText - this is the TEXTSTRING which will go on the alert. Must be provided
     */
    private void addFollowingAlert(CharSequence alertText) {
        //FIND WHERE THE COMPANION IS IN THE GAME
        Vector2 companionPosition = getCurrentPosition();
        if (alertLabel != null) {
            alertLabel.remove();
        }
        //create the alert
        alertLabel = new Label(alertText,skin);
        alertLabel.setFontScale(0.2f);

        //set the position to be above the companions UI SECTION
        alertLabel.setPosition(100,240);


        stage.addActor(alertLabel);


        //SET UP A TIMER TO REMOVE THE LABEL
        final java.util.Timer timer = new java.util.Timer();
        TimerTask removeAlert = new TimerTask() {
            @Override
            public void run() {
                alertLabel.remove();
                timer.cancel();
                timer.purge();
            }
        };
        timer.schedule(removeAlert, 1000); // removes alert after 1 second (3000 is good)
    }

    /**
     * Create a necessary alert when the companion is at low health
     * @param health - the health of the companion
     */
    public void companionHealthUpdate(int health) {
        //check that the companion hasn't actually increased health
        int changeInHealth = previousCompanionHealth - health;
        //if the change in health is positive, that means that the health is decreasing.
        if (changeInHealth > 0) {
            previousCompanionHealth = health;
            //if health below 10, and youre alive, make alert
            if (health <= 10 && health > 0) {
                //add an alert
                CharSequence companionAlertUpdate = String.format("Companion Health Critically Low! %d", health);
                addFollowingAlert(companionAlertUpdate);
            } else if (health <= 25 && health > 0) {
                //add an alert
                CharSequence companionAlertUpdate = String.format("Companion Health Low! %d", health);
                addFollowingAlert(companionAlertUpdate);
            }
        }

    }

    /**
     * Create a necessary alert when the companion is at low health
     * @param health - the health of the companion
     */
    public void playerHealthUpdate(int health) {
        //check that the companion hasn't actually increased health
        int changeInHealth = previousPlayerHealth - health;
        //if the change in health is positive, that means that the health is decreasing.
        if (changeInHealth > 0) {
            previousPlayerHealth = health;
            //if the player health is 20% of its theoretical max health
            if (health <= 20 && health > 0) {
                // add an alert
                CharSequence companionAlertUpdate = String.format("Player Health Low! %d", health);
                addFollowingAlert(companionAlertUpdate);
            }
        }

    }


    /**
     * Drawing code from the parent UI component class
     * @param batch Batch to render to.
     */
    @Override
    public void draw(SpriteBatch batch) {
        // Code for drawing UI elements and updating the projection matrix.
    }

    /**
     * remove all labels form the screen when disposing
     */
    @Override
    public void dispose() {
        if (alertLabel != null) {
            alertLabel.remove();
        }
        super.dispose();
    }
}

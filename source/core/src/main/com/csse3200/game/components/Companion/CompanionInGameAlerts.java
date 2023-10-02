package com.csse3200.game.components.Companion;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.CompanionWeapons.CompanionWeaponType;
import com.csse3200.game.components.Weapons.WeaponType;
import com.csse3200.game.components.player.InventoryComponent;
import com.csse3200.game.components.player.KeyboardPlayerInputComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.CompanionWeaponConfig;
import com.csse3200.game.entities.configs.WeaponConfig;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
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
    private static final Logger logger = LoggerFactory.getLogger(CompanionInGameAlerts.class);

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

        //forced update
        companionHealthUpdate(100);

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
        //logger.info("ONCE A FRAME THIS SHOULD BE PRINTING");

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
        //FIND WHERE THE COMPANION IS
        Vector2 companionPosition = getCurrentPosition();
        logger.info(companionPosition.toString());


        //create the alert
        alertLabel = new Label(alertText,skin);
        alertLabel.setFontScale(0.2f);

        //set the position to be at the companion
        alertLabel.setPosition(companionPosition.x,companionPosition.y);

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
        timer.schedule(removeAlert, 3000); // removes alert after 1 second
    }

    /**
     * Create a necessary alert when the companion is at low health
     * @param health - the health of the companion
     */
    public void companionHealthUpdate(int health) {
        if (health <= 100) {
            //add an alert
            CharSequence companionAlertUpdate = String.format("Companion Health Low! %d", health);
            addFollowingAlert(companionAlertUpdate);
        }
    }

    /**
     * Create a necessary alert when the companion is at low health
     * @param health - the health of the companion
     */
    public void playerHealthUpdate(int health) {
        if (health <= 50) {
            // add an alert
            CharSequence companionAlertUpdate = String.format("Oh no! Player Health Low! %d", health);
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
        super.dispose();
    }
}

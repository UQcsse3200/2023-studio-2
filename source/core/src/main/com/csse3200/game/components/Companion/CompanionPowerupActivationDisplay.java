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
import com.csse3200.game.components.PowerupType;
import com.csse3200.game.components.Weapons.WeaponType;
import com.csse3200.game.components.player.InventoryComponent;
import com.csse3200.game.components.player.KeyboardPlayerInputComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.*;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import java.util.HashMap;
import java.util.Map;
/**
 * This UI component represents the activation button on the right hand side for powerups
 *
 * Essentially, the UI component will show individual powerups and a way to toggle between them
 *
 * it will only show the powerups which are actually stocked (>0) in the inventory
 *
 * When you click on the powerup, it will activate it, and refresh the UI component
 */
public class CompanionPowerupActivationDisplay extends UIComponent {
    // variable declaration
    public Entity companion = ServiceLocator.getEntityService().getCompanion();
    private final String labelStyle;

    // local copy of PowerupsInventoryAmount
    private HashMap<PowerupType, Integer> localPowerupsInventoryAmount = new HashMap<>();

    // arraylist of type PowerupType is the way we will implement this
    private ArrayList<PowerupType> localPowerupActivationList = new ArrayList<>();
    private Integer powerupActivationlistIndex = 0;
    private PowerupType previousPowerupType = null;
    private static final Logger logger = LoggerFactory.getLogger(CompanionDeathScreenActions.class);

    PowerupConfig deathPotion;
    PowerupConfig healthPotion;
    PowerupConfig speedPotion;
    PowerupConfig invincibilityPotion;
    PowerupConfig extraLife;
    PowerupConfig doubleCross;
    PowerupConfig doubleDamage;
    PowerupConfig snap;
    public PowerupConfigs powerupConfigs;

    /**
     * Constructor, sets label style
     */
    public CompanionPowerupActivationDisplay() {
        labelStyle = "small";
    }
    /**
     * This function is called to get the latest inventory amounts from the powerup inventory component
     */
    public void fetchLatestPowerupInventory() {
        localPowerupsInventoryAmount = ServiceLocator.getEntityService().getCompanion().getComponent(CompanionPowerupInventoryComponent.class).getPowerupsInventory();
    }

    /**
     * Draw
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
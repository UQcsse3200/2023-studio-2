package com.csse3200.game.components.player;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.csse3200.game.components.Weapons.WeaponType;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.WeaponConfig;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents the display component for the player's inventory in the game.
 * This component provides UI elements to display weapons equipped by the player,
 * allowing players to see and change their currently equipped weapon.
 */
public class InventoryDisplayComponent extends UIComponent {

    private Map<Button, WeaponType> buttonWeaponMap = new HashMap<>();
    Table table = new Table();
    InventoryComponent inventory;
    Entity player;

    /**
     * Initialises the inventory display.
     * Listens for weapon changes and updates currently equipped weapon display.
     **/
    public InventoryDisplayComponent() {
        player = ServiceLocator.getEntityService().getPlayer();
        inventory = player.getComponent(InventoryComponent.class);
        makeTable();
        player.getEvents().addListener("updateHotbar", this::equipEvent);
    }

    /**
     * Creates and populates a table with buttons representing each weapon.
     * Each button displays the weapon's image and name.
     */
    private void makeTable() {

        ArrayList<WeaponType> weapons = inventory.getEquippedWeapons();

        for (WeaponType weapon : weapons) {
            WeaponConfig config = inventory.getConfigs().GetWeaponConfig(weapon);
            Button button = new Button(skin);
            Table buttonTable = new Table();
            Label nameLabel = new Label(config.name, skin,"thick");
            nameLabel.setColor(Color.BLACK);
            nameLabel.setFontScale(0.2f, 0.2f);
            Image image = new Image( new Texture(config.imagePath));
            buttonTable.add(image).size(64, 64).row();
            buttonTable.add(nameLabel);
            updateButtonTableColor(button, weapon);

            buttonWeaponMap.put(button, weapon);
            button.add(buttonTable).width(75).height(75);

            final WeaponType currentWeapon = weapon;
            button.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    inventory.changeEquipped(currentWeapon);
                    player.getEvents().trigger("changeWeapon", currentWeapon);
                    equipEvent();
                }
            });

            table.add(button).pad(10).row();
        }
    }

    /**
     * Updates the color of the button associated with a weapon.
     * Grey color indicates the weapon is not equipped.
     * White color indicates the weapon is currently equipped.
     *
     * @param button The button to update.
     * @param weapon The weapon associated with the button.
     */
    private void updateButtonTableColor(Button button, WeaponType weapon) {
        if (!inventory.getEquippedType().equals(weapon)) {
            button.setColor(0.5f, 0.5f, 0.5f, 0.5f); // grey it out
        } else {
            button.setColor(Color.WHITE);
        }
    }

    /**
     * Handles the event when a weapon is equipped.
     * Recreates the weapon table and updates button colors.
     */
    private void equipEvent() {
        table.clear();
        buttonWeaponMap.clear();
        makeTable();
        for (Map.Entry<Button, WeaponType> entry : buttonWeaponMap.entrySet()) {
            updateButtonTableColor(entry.getKey(), entry.getValue());
        }
    }

    /**
     * Adds the main table to the stage.
     * Positions the table on the stage and sets its size.
     */
    private void addActors() {
        table.center().right();
        table.setFillParent(true);
        table.padBottom(45f).padRight(45f);
        stage.addActor(table);
    }

    /**
     * Creates UI styles and adds actors to the stage.
     */
    @Override
    public void create() {
        super.create();
        addActors();
    }

    /**
     * Draws the component.
     * This is handled by the stage.
     *
     * @param batch The sprite batch used for drawing.
     */
    @Override
    protected void draw(SpriteBatch batch) {
        // Handled by stage
    }
}

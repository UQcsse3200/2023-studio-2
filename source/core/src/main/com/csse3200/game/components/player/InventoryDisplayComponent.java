package com.csse3200.game.components.player;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.csse3200.game.components.Weapons.WeaponType;
import com.csse3200.game.components.structures.StructureToolPicker;
import com.csse3200.game.components.structures.ToolConfig;
import com.csse3200.game.components.upgradetree.UpgradeTree;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.WeaponConfig;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;

import java.util.*;

/**
 * Represents the display component for the player's inventory in the game.
 * This component provides UI elements to display weapons equipped by the player,
 * allowing players to see and change their currently equipped weapon.
 */
public class InventoryDisplayComponent extends UIComponent {

    private Map<Button, WeaponType> buttonWeaponMap = new HashMap<>();
    Table table = new Table();
    Table hotbar = new Table();
    InventoryComponent inventory;
    Entity player;
    private final LinkedHashMap<WeaponConfig, Button> buttons;
    StructureToolPicker structureToolPicker;

    /**
     * Initialises the inventory display.
     * Listens for weapon changes and updates currently equipped weapon display.
     **/
    public InventoryDisplayComponent() {
        buttons = new LinkedHashMap<>();
        player = ServiceLocator.getEntityService().getPlayer();
        inventory = player.getComponent(InventoryComponent.class);
        structureToolPicker = player.getComponent(StructureToolPicker.class);
        makeTable();
        makeHotbar();
        player.getEvents().addListener("updateHotbar", this::equipEvent);
        player.getEvents().addListener("selectWeaponIndex", this::selectIndex);
    }

    /**
     * Creates and populates a table with buttons representing each weapon.
     * Each button displays the weapon's image and name.
     */
    void makeTable() {
        for (WeaponType weapon : inventory.getEquippedWeapons()) {
            WeaponConfig config = inventory.getConfigs().GetWeaponConfig(weapon);
            Button button = new Button(skin);
            Table buttonTable = new Table();

            // Create label and images
            Label nameLabel = new Label(config.name, skin,"thick");
            nameLabel.setColor(Color.BLACK);
            nameLabel.setFontScale(0.2f, 0.2f);
            Image image = new Image(new Texture(config.imagePath));

            // Add button to table and update colour
            buttonTable.add(image).size(64, 64).row();
            buttonTable.add(nameLabel);
            updateButtonColor(button, weapon);
            button.add(buttonTable).width(75).height(75);
            buttonWeaponMap.put(button, weapon);

            // Handle button presses
            final WeaponType currentWeapon = weapon;
            button.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    buttons.clear();
                    inventory.changeEquipped(currentWeapon);
                    player.getEvents().trigger("changeWeapon", currentWeapon);

                    if (config.slotType.equals("melee") || config.slotType.equals("ranged")) {
                        structureToolPicker.hide();
                        show();
                    } else {
                        hide();
                        structureToolPicker.show();
                    }

                    equipEvent();
                }
            });
            table.add(button).pad(10).row();
        }
    }

    /**
     * Creates and populates a table with buttons representing each weapon.
     * Each button displays the weapon's image.
     */
    void makeHotbar() {
        hotbar.align(Align.center);
        hotbar.center().bottom().padBottom(10);
        hotbar.setFillParent(true);

        // Iterate through all unlocked weapons and add them to a table
        for (Object config : player.getComponent(UpgradeTree.class).getUnlockedWeaponsConfigs()) {
            WeaponConfig equippedConfig = inventory.getConfigs().GetWeaponConfig(inventory.getEquippedType());
            String equippedCategory = equippedConfig.slotType;

            // Skip tools
            if (config instanceof ToolConfig) {
                continue;
            }

            // Extract the weapon config and type
            WeaponConfig weaponConfig = (WeaponConfig) config;
            WeaponType weaponType = weaponConfig.type;

            if (weaponType.equals(WeaponType.WOODHAMMER) || !weaponConfig.slotType.equals(equippedCategory)) {
                continue;
            }

            // Create button with image
            var button = new Button(skin);
            var image = new Image(new Texture(weaponConfig.imagePath));
            button.add(image).size(30,30);
            updateButtonColor(button, weaponType);
            image.setScaling(Scaling.fill);

            // Change weapons on click
            final WeaponType weaponToEquip = weaponType;
            button.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    inventory.replaceSlotWithWeapon(weaponConfig.slotType, weaponToEquip);
                    player.getEvents().trigger("changeWeapon", weaponToEquip);
                    equipEvent();
                }
            });

            buttons.put(weaponConfig, button);
            hotbar.add(button).width(30 + button.getPadLeft() + button.getPadRight());
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
    void updateButtonColor(Button button, WeaponType weapon) {
        WeaponType equippedType = inventory.getEquippedType();

        if (equippedType == null) {
            button.setColor(0.5f, 0.5f, 0.5f, 0.5f); // for testing..
            return;
        }

        if (!equippedType.equals(weapon)) {
            button.setColor(0.5f, 0.5f, 0.5f, 0.5f); // grey it out
        } else {
            button.setColor(Color.WHITE); // un-grey it out
        }
    }


    /**
     * Handles the event when a weapon is equipped.
     * Recreates the weapon table and updates button colors.
     */
    void equipEvent() {
        // clear old tables
        table.clear();
        hotbar.clear();
        buttonWeaponMap.clear();

        // Make new ones
        makeTable();
        makeHotbar();

        // Update the colours of each button - un-greyed out for equipped, else greyed out
        for (Map.Entry<Button, WeaponType> entry : buttonWeaponMap.entrySet()) {
            updateButtonColor(entry.getKey(), entry.getValue());
        }
        for (Map.Entry<WeaponConfig, Button> entry : buttons.entrySet()) {
            updateButtonColor(entry.getValue(), entry.getKey().type);
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
        stage.addActor(hotbar);
    }

    public void selectIndex(int index) {
        // Ensure the weapons index is in range
        if (index < 0 || index > buttons.size() - 1) {
            return;
        }

        // Extract the WeaponConfig from the unlocked items inside the button map
        WeaponConfig weaponConfig = (WeaponConfig) buttons.keySet().toArray()[index];

        // Switch to the extracted weapon
        inventory.replaceSlotWithWeapon(weaponConfig.slotType, weaponConfig.type);
        player.getEvents().trigger("changeWeapon", weaponConfig.type);
        equipEvent();
    }

    /**
     * Shows the hotbar.
     */
    public void show() {
        hotbar.setVisible(true);
    }

    /**
     * Hides the hotbar.
     */
    public void hide() {
        hotbar.setVisible(false);
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

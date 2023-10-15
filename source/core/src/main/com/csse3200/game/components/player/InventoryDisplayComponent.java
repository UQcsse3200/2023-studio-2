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
import java.util.List;

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
    private final Map<WeaponConfig, Button> buttons;
    StructureToolPicker structureToolPicker;

    /**
     * Initialises the inventory display.
     * Listens for weapon changes and updates currently equipped weapon display.
     **/
    public InventoryDisplayComponent() {
        buttons = new HashMap<>();
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

    void makeHotbar() {
        hotbar.align(Align.center);
        hotbar.center().bottom().padBottom(10);
        hotbar.setFillParent(true);

        for (Object config : player.getComponent(UpgradeTree.class).getUnlockedWeaponsConfigs()) {
            WeaponConfig equippedConfig = inventory.getConfigs().GetWeaponConfig(inventory.getEquippedType());
            String equippedCategory = equippedConfig.slotType;

            if (config instanceof ToolConfig) {
                continue;
            }

            WeaponConfig weaponConfig = (WeaponConfig) config;
            WeaponType weaponType = weaponConfig.type;

            if (weaponType.equals(WeaponType.WOODHAMMER) || !weaponConfig.slotType.equals(equippedCategory)) {
                continue;
            }

            var button = new Button(skin);
            var image = new Image(new Texture(weaponConfig.imagePath));
            button.add(image).size(30,30);
            updateButtonColor(button, weaponType);

            image.setScaling(Scaling.fill);
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
    void equipEvent() {
        table.clear();
        hotbar.clear();
        buttonWeaponMap.clear();
        makeTable();
        makeHotbar();
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
        System.out.println("IN");
        if (index < 0 || index > 9) {
            logger.warn("Invalid index selected: " + index);
            return;
        }

        List<String> keys = new ArrayList<>(inventory.getEquippedWMap().keySet());
        keys.get(index);


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

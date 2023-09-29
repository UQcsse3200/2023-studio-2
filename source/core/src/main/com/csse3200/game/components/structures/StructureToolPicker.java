package com.csse3200.game.components.structures;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.Scaling;
import com.csse3200.game.components.structures.tools.Tool;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * This component can be placed onto the player and allows them to select and interact
 * with structure tools.
 */
public class StructureToolPicker extends UIComponent {

    private final Logger logger;
    private final Table table;
    private final ArrayList<Button> buttons;

    private final HashSet<String> unlockedTools;

    private final ToolsConfig structureTools =
            FileLoader.readClass(ToolsConfig.class, "configs/structure_tools.json");
    private Tool selectedTool;
    private int level = 0;

    /**
     * Creates a new structure tool picker
     */
    public StructureToolPicker() {
        super();
        buttons = new ArrayList<>();
        logger = LoggerFactory.getLogger(StructureToolPicker.class);
        table = new Table();
        unlockedTools = new HashSet<>();

        // Default buildables
        unlockedTools.add("Extractor");
        unlockedTools.add("Heal");
    }

    /**
     * Adds actors to the stage on creation
     */
    @Override
    public void create() {
        super.create();
        addActors();
    }

    /**
     * Creates an entry for each structure tool in the config file
     * and positions them on the stage using a table.
     * @see Table for positioning options
     */
    private void addActors() {
        table.clear();

        table.align(Align.center);
        table.center();
        table.setFillParent(true);

        for (var option : structureTools.toolConfigs) {
            var optionValue = option.value;
            var tool = getTool(option.key, optionValue.cost);

            // skip items which are not unlocked
            if (!isToolUnlocked(option.value.name)) {
                continue;
            }

            var button = new Button(skin);
            var buttonTable = new Table();
            buttonTable.center();
            var nameLabel = new Label(optionValue.name, skin);
            nameLabel.setColor(Color.BLACK);
            var image = new Image(ServiceLocator.getResourceService().getAsset(optionValue.texture, Texture.class));

            buttonTable.add(image).size(30,30).right();
            buttonTable.add(nameLabel).padLeft(10).left();

            for (var cost : optionValue.cost) {
                var costLabel = new Label(String.format("%s - %d", cost.key, cost.value), skin);
                costLabel.setColor(Color.BLACK);

                buttonTable.row().colspan(2);
                buttonTable.add(costLabel).padTop(10).center();
            }

            image.setScaling(Scaling.fill);
            button.add(buttonTable);
            button.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    selectedTool = tool;
                    hide();
                }
            });
            buttons.add(button);
            table.row().padTop(10);
            table.add(button).width(280);
        }

        stage.addActor(table);

        table.setVisible(false);
    }

    /**
     * Gets the tool class referred to in the config file.
     *
     * @param key - the name of the class to get
     * @param cost - the cost of the tool
     * @return an instance of the specified tool class if it exists, otherwise null
     */
    private Tool getTool(String key, ObjectMap<String, Integer> cost) {
        try {
            Class<?> cls = Class.forName(key);

            Object obj = cls.getDeclaredConstructor(ObjectMap.class).newInstance(cost);

            if (obj instanceof Tool tool) {
                return tool;
            } else {
                logger.error("{} is not an instance of Tool", key);
                return null;
            }
        } catch (ClassNotFoundException | InvocationTargetException | NoSuchMethodException
                 | InstantiationException | IllegalAccessException e) {
            logger.error("{} cannot be instantiated", key);
            return null;
        }
    }

    /**
     *  Unlocks a tool, adding it to the structure picker menu
     *
     * @param toolName - the simple name of the tool, e.g. 'Dirt Wall'
     */
    public void unlockTool(String toolName) {
        System.out.println("Tool UNLOCKED: " + toolName);
        unlockedTools.add(toolName);
        addActors();
    }

    /**
     * Returns the unlocked status as a boolean
     *
     * @param toolName - the simple name of the tool, e.g. 'Dirt Wall'
     * @return boolean - true if unlocked, false if locked.
     */
    public boolean isToolUnlocked(String toolName) {
        return unlockedTools.contains(toolName);
    }

    /**
     * Sets the given tool to be the selected tool.
     * @param tool - the tool to be selected.
     */
    public void setSelectedTool(Tool tool) {
        this.selectedTool = tool;
    }

    /**
     * Returns the currently selected tool.
     * @return the selected tool.
     */
    public Tool getSelectedTool() {
        return selectedTool;
    }

    /**
     * Sets the level of the ToolPicker and updates the options displayed to
     * the user to match the new level of the picker.
     * @param level - the maximum level of tools to display.
     */
    public void setLevel(int level) {
        this.level = level;
        addActors();
    }

    /**
     * Gets the current level of the ToolPicker.
     * @return the maximum level of tools to display.
     */
    public int getLevel() {
        return level;
    }

    @Override
    public void draw(SpriteBatch batch)  {
        // draw is handled by the stage
    }

    /**
     * Shows the tool picker.
     */
    public void show() {
        table.setVisible(true);
    }

    /**
     * Hides the tool picker.
     */
    public void hide() {
        table.setVisible(false);
    }

    /**
     * Returns whether the tool picker is visible.
     * @return whether the tool picker is visible.
     */
    public boolean isVisible() {
        return table.isVisible();
    }

    /**
     * Interacts with the currently selected tool.
     * @param location - the location being interacted with.
     */
    public void interact(GridPoint2 location) {
        if (selectedTool == null) {
            return;
        }

        selectedTool.interact(entity, location);
    }
}
package com.csse3200.game.components.structures;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.Scaling;
import com.csse3200.game.components.structures.tools.Tool;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

/**
 * This component can be placed onto the player and allows them to select and interact
 * with structure tools.
 */
public class StructureToolPicker extends UIComponent {
    private final Logger logger;
    private final Table table;
    private final ArrayList<Button> buttons;

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

            // skip items which are above the current level
            if (optionValue.level > level || tool == null) {
                continue;
            }
            var button = new Button(skin);
            var image = new Image(ServiceLocator.getResourceService().getAsset(optionValue.texture, Texture.class));

            image.setScaling(Scaling.fill);
            button.add(image);
            button.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    selectedTool = tool;
                    hide();
                }
            });
            button.setSize(10, 10);
            buttons.add(button);
            table.add(button).size(50, 50).pad(5);
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
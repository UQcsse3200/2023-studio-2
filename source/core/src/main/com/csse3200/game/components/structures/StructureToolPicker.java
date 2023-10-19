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
import java.util.*;

/**
 * This component can be placed onto the player and allows them to select and interact
 * with structure tools.
 */
public class StructureToolPicker extends UIComponent {

    private final Logger logger;
    private final Table table;
    private final Map<Tool, Button> buttons;

    private final HashSet<String> unlockedTools;

    private final ToolsConfig structureTools =
            FileLoader.readClass(ToolsConfig.class, "configs/structure_tools.json");
    private Tool selectedTool;
    private final Map<String, Tool> tools;
    private final List<Tool> selectableTools;

    /**
     * Creates a new structure tool picker
     */
    public StructureToolPicker() {
        super();
        buttons = new HashMap<>();
        logger = LoggerFactory.getLogger(StructureToolPicker.class);
        table = new Table();
        unlockedTools = new HashSet<>();
        tools = new HashMap<>();
        selectableTools = new ArrayList<>();

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

        for (var option : structureTools.toolConfigs) {
            var tool = getTool(option.key, option.value.cost, option.value.range,
                    option.value.texture, option.value.ordering);

            if (tool == null) {
                continue;
            }

            tools.put(option.key, tool);
        }

        addActors();

        stage.addActor(table);
        hide(); // Initialise hidden
    }

    /**
     * Creates an entry for each structure tool in the config file
     * and positions them on the stage using a table.
     * @see Table for positioning options
     */
    void addActors() {
        table.clear();
        buttons.clear();
        selectableTools.clear();

        table.align(Align.center);
        table.center().bottom().padBottom(10);
        table.setFillParent(true);

        for (var option : structureTools.toolConfigs) {
            // skip items which are not unlocked
            if (!isToolUnlocked(option.value.name)) {
                continue;
            }

            var tool = tools.get(option.key);

            selectableTools.add(tool);
        }

        Collections.sort(selectableTools);

        for (var tool : selectableTools) {
            var button = new Button(skin);
            var image = new Image(ServiceLocator.getResourceService().getAsset(tool.getTexture(), Texture.class));

            button.add(image).size(30,30);
            if (selectedTool != tool) {
                button.setColor(1f, 1f, 1f, 0.5f);
            } else {
                button.setColor(1f, 1f, 1f, 1f);
            }

            image.setScaling(Scaling.fill);
            button.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    setSelectedTool(tool);
                }
            });
            buttons.put(tool, button);
            table.add(button).width(30 + button.getPadLeft() + button.getPadRight());
        }
    }

    /**
     * Gets the tool class referred to in the config file.
     *
     * @param key - the name of the class to get
     * @param cost - the cost of the tool
     * @param ordering - the ordering of the tool.
     * @param texture - the texture of the tool.
     * @return an instance of the specified tool class if it exists, otherwise null
     */
    private Tool getTool(String key, ObjectMap<String, Integer> cost, float range, String texture, int ordering) {
        try {
            Class<?> cls = Class.forName(key);

            Object obj = cls.getDeclaredConstructor(ObjectMap.class, float.class, String.class, int.class)
                    .newInstance(cost, range, texture, ordering);

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
        unlockedTools.add(toolName);
        addActors();
    }

    /**
     * Selects the tool at the given index.
     *
     * @param index - the index of the tool to select.
     */
    public void selectIndex(int index) {
        if (index < 0 || index >= selectableTools.size()) {
            return;
        }

        setSelectedTool(selectableTools.get(index));
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
        if (selectedTool != null && buttons.containsKey(selectedTool)) {
            buttons.get(selectedTool).setColor(1f, 1f, 1f, 0.5f);
        }

        if (buttons.containsKey(tool)) {
            buttons.get(tool).setColor(1f, 1f, 1f, 1f);
        }

        this.selectedTool = tool;
    }

    /**
     * Returns the currently selected tool.
     * @return the selected tool.
     */
    public Tool getSelectedTool() {
        return selectedTool;
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

    /**
     * Returns the currentlySelectableTools.
     *
     * @return selectableTools.
     */
    public List<Tool> getSelectableTools() {
        return selectableTools;
    }
}
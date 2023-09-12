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

public class StructurePicker extends UIComponent {
    Logger logger;
    Table table;
    private final ArrayList<Button> buttons;

    private final StructureOptions structureOptions =
            FileLoader.readClass(StructureOptions.class, "configs/structure_options.json");
    private Tool selectedTool;
    private int level = 0;

    public StructurePicker() {
        super();
        buttons = new ArrayList<>();
        logger = LoggerFactory.getLogger(StructurePicker.class);
        table = new Table();
    }

    /**
     * Creates reusable ui styles and adds actors to the stage.
     */
    @Override
    public void create() {
        super.create();
        addActors();
    }

    /**
     * Creates actors and positions them on the stage using a table.
     *
     * @see Table for positioning options
     */
    private void addActors() {
        table.clear();

        table.align(Align.center);
        table.center();
        table.setFillParent(true);

        for (var option : structureOptions.structureOptions) {
            var optionValue = option.value;

            // skip items which are above the current level
            if (optionValue.level > level) {
                continue;
            }

            var tool = getTool(option.key, optionValue.cost);

            if (tool == null) {
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

    private Tool getTool(String key, ObjectMap<String, Integer> cost) {
        try {
            Class<?> cls = Class.forName(key);

            Object obj = cls.getDeclaredConstructor(ObjectMap.class).newInstance(cost);

            if (obj instanceof Tool) {
                return (Tool) obj;
            } else {
                logger.error(key + " is not an instance of Tool");
                return null;
            }
        } catch (ClassNotFoundException | InvocationTargetException | NoSuchMethodException
                 | InstantiationException | IllegalAccessException e) {
            logger.error(key + " cannot be instantiated");
            return null;
        }
    }

    public Tool getSelectedTool() {
        return selectedTool;
    }

    public void setSelectedTool(Tool tool) {
        this.selectedTool = tool;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
        addActors();
    }

    @Override
    public void draw(SpriteBatch batch) {
        // draw is handled by the stage
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    public void show() {
        table.setVisible(true);
    }

    public void hide() {
        table.setVisible(false);
    }

    public boolean isVisible() {
        return table.isVisible();
    }

    public void interact(Entity player, GridPoint2 location) {
        if (selectedTool == null) {
            return;
        }

        selectedTool.interact(player, location);
    }
}
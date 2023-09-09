package com.csse3200.game.components.structures;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
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
    private Image heartImage;
    //private Label healthLabel;
    private ArrayList<Button> buttons;

    private StructureOptions structureOptions =
            FileLoader.readClass(StructureOptions.class, "configs/structure_options.json");
    private Tool selectedTool;

    public StructurePicker() {
        super();
        buttons = new ArrayList<>();
        logger = LoggerFactory.getLogger(StructurePicker.class);
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
     * @see Table for positioning options
     */
    private void addActors() {
        table = new Table();
        table.center();
        table.setFillParent(false);

        for (var option : structureOptions.structureOptions) {
            var tool = getTool(option.key);

            if (tool == null) {
                continue;
            }


            var optionValue = option.value;
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



        table.setPosition(stage.getWidth()/2, stage.getHeight()/2, Align.center);

        stage.addActor(table);

        table.setVisible(false);
    }

    private Tool getTool(String key) {
        try {
            Class<?> cls = Class.forName(key);

            Object obj = cls.getDeclaredConstructor().newInstance();

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

    @Override
    public void draw(SpriteBatch batch)  {
        // draw is handled by the stage
    }

    @Override
    public void dispose() {
        super.dispose();
        heartImage.remove();
    }

    public void show() {
        table.setVisible(true);
    }

    public void hide() {
        table.setVisible(false);
    }

    public void interact(Entity player, GridPoint2 location) {
        if (selectedTool == null) {
            return;
        }

        selectedTool.interact(player, location);
    }
}
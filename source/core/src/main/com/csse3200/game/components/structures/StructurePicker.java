package com.csse3200.game.components.structures;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.Scaling;
import com.csse3200.game.components.structures.tools.Tool;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class StructurePicker extends UIComponent {
    Table table;
    private Image heartImage;
    //private Label healthLabel;
    private ArrayList<Button> buttons;

    private StructureOptions structureOptions =
            FileLoader.readClass(StructureOptions.class, "configs/structure_options.json");
    private String selectedTool;
    public ArrayList<Tool> tools;

    public StructurePicker() {
        super();
        buttons = new ArrayList<>();
        tools = new ArrayList<>();
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

        // Heart image
        float heartSideLength = 30f;

        // Health text
//        int health = entity.getComponent(CombatStatsComponent.class).getHealth();
//        CharSequence healthText = String.format("Health: %d", health);
//        healthLabel = new Label(healthText, skin, "large");

        for (var option : structureOptions.structureOptions) {
            var optionValue = option.value;
            var button = new Button(skin);
            var image = new Image(ServiceLocator.getResourceService().getAsset(optionValue.texture, Texture.class));

            image.setScaling(Scaling.fill);
            button.add(image);

            var optionKey = option.key;
            button.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    selectedTool = optionKey;
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

        Tool tool;

        try {
            Class<?> cls = Class.forName(selectedTool);

            Object obj = cls.getDeclaredConstructor().newInstance();

            if (obj instanceof Tool) {
                tool = (Tool) obj;
            } else {
                return;
            }
        } catch (ClassNotFoundException | InvocationTargetException | NoSuchMethodException
                | InstantiationException | IllegalAccessException e) {
            Logger logger = new Logger(this.getClass().getName());
            logger.error(e.getMessage());
            return;
        }

        tool.interact(player, location);
    }
}
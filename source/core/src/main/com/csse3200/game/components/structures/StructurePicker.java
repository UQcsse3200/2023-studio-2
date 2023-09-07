package com.csse3200.game.components.structures;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.csse3200.game.GdxGame;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.PlaceableEntity;
import com.csse3200.game.entities.buildables.TurretType;
import com.csse3200.game.entities.buildables.WallType;
import com.csse3200.game.entities.configs.GateConfig;
import com.csse3200.game.entities.factories.BuildablesFactory;
import com.csse3200.game.entities.factories.ObstacleFactory;
import com.csse3200.game.entities.factories.StructureFactory;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;

import java.util.ArrayList;

import static com.csse3200.game.screens.MainMenuScreen.logger;

public class StructurePicker extends UIComponent {
    Table table;
    private Image heartImage;
    //private Label healthLabel;
    private ArrayList<Button> buttons;

    private StructureOptions structureOptions =
            FileLoader.readClass(StructureOptions.class, "configs/structure_options.json");
    private StructureOption selectedStructure;

    public StructurePicker() {
        super();
        buttons = new ArrayList<>();
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
            var button = new Button(skin);
            var image = new Image(ServiceLocator.getResourceService().getAsset(option.texture, Texture.class));

            image.setScaling(Scaling.fill);
            button.add(image);

            button.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    selectedStructure = option;
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

    public PlaceableEntity createStructure(Entity player) {
        if (selectedStructure == null) {
            return null;
        }

        return switch (selectedStructure.name) {
            case "wall" -> BuildablesFactory.createWall(WallType.basic, player);
            case "turret" -> ObstacleFactory.createCustomTurret(TurretType.levelOne, player);
            default -> null;
        };

    }
}
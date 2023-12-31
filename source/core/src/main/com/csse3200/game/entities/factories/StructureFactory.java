package com.csse3200.game.entities.factories;

import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.csse3200.game.entities.Extractor;
import com.csse3200.game.entities.configs.*;
import com.csse3200.game.GdxGame;
import com.csse3200.game.areas.ExtractorMiniGameArea;
import com.csse3200.game.areas.terrain.TerrainComponent;
import com.csse3200.game.components.InteractableComponent;
import com.csse3200.game.areas.map_config.ResourceCondition;
import com.csse3200.game.components.*;
import com.csse3200.game.components.npc.SpawnerComponent;
import com.csse3200.game.components.resources.Resource;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.enemies.EnemyName;
import com.csse3200.game.input.ExtinguisherInputComponent;
import com.csse3200.game.input.FireInputComponent;
import com.csse3200.game.input.HoleInputComponent;
import com.csse3200.game.input.SpannerInputComponent;

import com.csse3200.game.entities.PlaceableEntity;

import com.csse3200.game.files.FileLoader;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.PhysicsUtils;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.rendering.TextureRenderComponent;
import com.csse3200.game.screens.PlanetScreen;
import com.csse3200.game.services.GameStateObserver;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.TitleBox;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.csse3200.game.components.mainmenu.MainMenuActions.game;
import static com.csse3200.game.ui.DialogComponent.stage;
import static com.csse3200.game.ui.UIComponent.skin;

/**
 * Factory to create structure entities - such as extractors or ships.

 *
 * <p>Each obstacle entity type should have a creation method that returns a corresponding entity.
 */

public class StructureFactory {
    private StructureFactory() {
        throw new IllegalStateException("Factory class");
    }

    //Default configs
    public static final ShipConfig defaultShip =
            FileLoader.readClass(ShipConfig.class, "configs/ship.json");


    /**
     * Creates an extractor entity
     *
     * <p>Each obstacle entity type should have a creation method that returns a corresponding entity.

     * @param config Configuration file to match extractor to
     * @return a new extractor Entity
     */

    public static PlaceableEntity createExtractor(ExtractorConfig config) {
        return new Extractor(config);
    }

    public static Entity createExtractorRepair() {
        Entity extractorRepair = new Entity()
                //.addComponent(new TextureRenderComponent("images/elixir_collector.png")); //This image removed
                .addComponent(new TextureRenderComponent("images/minigame/extractor.png"));
        extractorRepair.setScale(2.2f, 2.6f);
        return extractorRepair;
    }

    /**
     * Creates an entity representing an extinguisher in the mini-game area.
     *
     * @param terrain The terrain component.
     * @param area    The mini-game area associated with the extractor.
     * @return A new extinguisher Entity.
     */

    public static Entity createExtinguisher(TerrainComponent terrain, ExtractorMiniGameArea area) {
        Entity extinguisher = new Entity()
                .addComponent(new TextureRenderComponent("images/minigame/extinguisher.png"));
        ExtinguisherInputComponent extinguisherComponent = new ExtinguisherInputComponent(terrain, area);
        ServiceLocator.getInputService().register(extinguisherComponent);
        extinguisher.addComponent(extinguisherComponent);
        extinguisher.setScale(2f, 2f);
        return extinguisher;
    }

    /**
     * Creates an entity representing a spanner in the mini-game area.
     *
     * @param terrain The terrain component.
     * @param area    The mini-game area associated with the extractor.
     * @return A new spanner Entity.
     */

    public static Entity createSpanner(TerrainComponent terrain, ExtractorMiniGameArea area) {
        Entity spanner = new Entity()
                .addComponent(new TextureRenderComponent("images/minigame/spanner.png"));
        SpannerInputComponent spannerComponent = new SpannerInputComponent(terrain, area);
        ServiceLocator.getInputService().register(spannerComponent);
        spanner.addComponent(spannerComponent);
        spanner.setScale(2f, 2f);
        return spanner;
    }

    /**
     * Creates an entity representing a fire part in the mini-game area associated with the extractor.
     *
     * @param terrain The terrain component.
     * @param area    The mini-game area associated with the extractor.
     * @return A new fire part Entity.
     */

    public static Entity createExtractorFirePart(TerrainComponent terrain, ExtractorMiniGameArea area) {
        Entity extractorFirePart = new Entity()
                .addComponent(new TextureRenderComponent("images/minigame/fire.png"));
        FireInputComponent fireComponent = new FireInputComponent(terrain, area);
        ServiceLocator.getInputService().register(fireComponent);

        extractorFirePart.addComponent(fireComponent);

        extractorFirePart.setScale(1.2f, 1.4f);

        return extractorFirePart;
    }

    /**
     * Creates an entity representing a hole part in the mini-game area associated with the extractor.
     *
     * @param terrain The terrain component.
     * @param area    The mini-game area associated with the extractor.
     * @return A new hole part Entity.
     */

    public static Entity createExtractorHolePart(TerrainComponent terrain, ExtractorMiniGameArea area) {
        Entity extractorHolePart = new Entity()
                .addComponent(new TextureRenderComponent("images/minigame/Hole.png"));
        HoleInputComponent holeComponent = new HoleInputComponent(terrain, area);
        ServiceLocator.getInputService().register(holeComponent);

        extractorHolePart.addComponent(holeComponent);

        extractorHolePart.setScale(1.4f, 1.6f);
        return extractorHolePart;
    }

    /**
     * Creates an entity representing a bang effect.
     *
     * @return A new bang Entity.
     */

    public static Entity createExtractorBang() {
        Entity extractorBang = new Entity().addComponent(new TextureRenderComponent("images/minigame/bang.png"));
        extractorBang.setScale(2.2f, 2.4f);
        return extractorBang;
    }

    /**
     * Creates an extractor entity
     *
     * <p>Each obstacle entity type should have a creation method that returns a corresponding entity.

     * @param health the max and initial health of the extractor
     * @param producedResource the resource type produced by the extractor
     * @param tickRate the frequency at which the extractor ticks (produces resources)
     * @param tickSize the amount of the resource produced at each tick
     * @return a new extractor Entity
     */

    public static PlaceableEntity createExtractor(int health, Resource producedResource, long tickRate, int tickSize) {
        ExtractorConfig extractorConfig = new ExtractorConfig();
        extractorConfig.health = health;
        extractorConfig.maxHealth = health;
        extractorConfig.resource = producedResource;
        extractorConfig.tickRate = tickRate;
        extractorConfig.tickSize = tickSize;
        extractorConfig.effects = new ParticleEffectsConfig();

        String effectPath = getEffectPath(producedResource);

        extractorConfig.effects.effectsMap.put("rubble", effectPath);

        return createExtractor(extractorConfig);
    }

    private static String getEffectPath(Resource producedResource) {
        return switch(producedResource) {
            case Nebulite -> "particle-effects/extractor/extractor_rubble_nebulite.effect";
            case Solstite -> "particle-effects/extractor/extractor_rubble_solstite.effect";
            case Durasteel -> "particle-effects/extractor/extractor_rubble_durasteel.effect";
        };
    }


    public static Entity createExtractorRepairPart() {
        Entity extractorRepairPart = new Entity()
                .addComponent(new TextureRenderComponent("images/minigame/fire.png"))
                .addComponent(new ExtractorRepairPartComponent());
        extractorRepairPart.setScale(1.8f, 2f);
        return extractorRepairPart;
    }



    private static boolean checkWinCondition(List<ResourceCondition> requirements) {
        if (requirements == null) {
            return true;
        }

        GameStateObserver stateObserver = ServiceLocator.getGameStateObserverService();
        for (ResourceCondition condition: requirements) {
            String resourceKey = "resource/" + condition.getResource();
            Object value = stateObserver.getStateData(resourceKey);
            int resourceCount = value == null ? 0 : (int) value;
            if (resourceCount < condition.getThreshold()) {
                return false;
            }
        }
        return true;
    }


    /**
     * Creates a ship entity that uses the default package
     */
    public static Entity createShip(GdxGame game, List<ResourceCondition> requirements) {
        return createShip(game, requirements, defaultShip);
    }

    /**
     * Creates a ship entity that matches the config file
     */

    public static Entity createShip(GdxGame game, List<ResourceCondition> requirements, ShipConfig config) {
        Entity ship =
                new Entity()
                        .addComponent(new TextureRenderComponent(config.spritePath))
                        .addComponent(new PhysicsComponent().setBodyType(BodyType.StaticBody))
                        .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE))
                        .addComponent(new HitboxComponent().setLayer(PhysicsLayer.OBSTACLE))
                        .addComponent(new InteractableComponent(entity -> {
                            if (checkWinCondition(requirements)) {
                                for (var requirement : requirements) {
                                    ServiceLocator.getGameStateObserverService().trigger("resourceAdd",
                                            requirement.getResource(), -requirement.getThreshold());
                                }
                                String[] storytext= {"{SLOW}Astro: This is my ship having an in built AI.\n The AI will guide you throughout. "
                                        ,"{SLOW}Player: It is of great help to us \n Why dont you join us ?"
                                        ,"{SLOW}Astro: I was attacked and I know I cant make it. \n The AI will guide you.",
                                        "Emily: Dont Worry ! I'll take the ship  from here.\nWe have to make this ship find a new home for us."};
                                String[] titletext= {"","","",""};
                                String[] window = {"dialogue_1", "dialogue_3","dialogue_1","dialogue_2"};

                                TitleBox titleBox = new TitleBox(game, titletext, storytext, skin, window);
                                titleBox.showDialog(stage);

                                game.setScreen(GdxGame.ScreenType.NAVIGATION_SCREEN);
                            } else {
                                ShipInteractionPopup shipPopup = new ShipInteractionPopup(requirements);
                                ServiceLocator.getRenderService().getStage().addActor(shipPopup);
                            }
                        }, 5));

        ship.addComponent(new SaveableComponent<ShipConfig>(s -> {
            ShipConfig shipConfig = config;
            shipConfig.position = s.getGridPosition();
            return shipConfig;
        }, ShipConfig.class));

        ship.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);
        ship.getComponent(TextureRenderComponent.class).scaleEntity();
        ship.setScale(5f, 4.5f);
        PhysicsUtils.setScaledCollider(ship, 0.9f, 0.9f);

        InteractLabel interactLabel = new InteractLabel(); //code for interaction prompt
        ship.addComponent(new DistanceCheckComponent(5f, interactLabel));
        ServiceLocator.getRenderService().getStage().addActor(interactLabel);

        return ship;

    }

    /**

     * Create an enemy spawner that spawns the desired enemies at a given tick rate and at a given location on the map
     *
     * @param config the config file to read and select the waves for the spawner to activate
     * @return
     */

    public static Entity createSpawner(SpawnerConfig config) {
        Entity spawner =
                new Entity()
                        .addComponent(new SpawnerComponent(config));

        spawner.addComponent(new SaveableComponent<>(p -> {
            SpawnerConfig spawnerConfig = config;
            spawnerConfig.wave1 = p.getComponent(SpawnerComponent.class).getWave1();
            spawnerConfig.wave2 = p.getComponent(SpawnerComponent.class).getWave2();
            spawnerConfig.wave3 = p.getComponent(SpawnerComponent.class).getWave3();
            spawnerConfig.position = p.getGridPosition();
            return spawnerConfig;
        }, SpawnerConfig.class));

        spawner.scaleHeight(1.5f);
        return spawner;
    }
}
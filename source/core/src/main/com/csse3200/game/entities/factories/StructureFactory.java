package com.csse3200.game.entities.factories;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.csse3200.game.ExtractorMinigameWindow;
import com.csse3200.game.GdxGame;
import com.csse3200.game.areas.ExtractorMiniGameArea;
import com.csse3200.game.areas.terrain.TerrainComponent;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.InteractableComponent;
import com.csse3200.game.areas.mapConfig.ResourceCondition;
import com.csse3200.game.components.*;
import com.csse3200.game.components.npc.SpawnerComponent;
import com.csse3200.game.components.resources.ProductionComponent;
import com.csse3200.game.components.resources.Resource;
import com.csse3200.game.components.structures.ExtractorAnimationController;
import com.csse3200.game.components.upgradetree.UpgradeDisplay;
import com.csse3200.game.components.upgradetree.UpgradeTree;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.SpawnerConfig;
import com.csse3200.game.input.ExtinguisherInputComponent;
import com.csse3200.game.input.FireInputComponent;
import com.csse3200.game.input.HoleInputComponent;
import com.csse3200.game.input.SpannerInputComponent;

import com.csse3200.game.entities.configs.ExtractorConfig;
import com.csse3200.game.entities.configs.ShipConfig;
import com.csse3200.game.entities.PlaceableEntity;

import com.csse3200.game.files.FileLoader;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.PhysicsUtils;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.rendering.TextureRenderComponent;
import com.csse3200.game.services.GameStateObserver;
import com.csse3200.game.services.ServiceLocator;

import java.util.List;

/**
 * Factory to create structure entities - such as extractors or ships.

 *
 * <p>Each obstacle entity type should have a creation method that returns a corresponding entity.
 */
public class StructureFactory {
    private StructureFactory() {
        throw new IllegalStateException("Factory class");
    }
    // * @param health the max and initial health of the extractor
    // * @param producedResource the resource type produced by the extractor
    // * @param tickRate the frequency at which the extractor ticks (produces resources)
    // * @param tickSize the amount of the resource produced at each tick

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
        AnimationRenderComponent animator =
                new AnimationRenderComponent(
                        ServiceLocator.getResourceService().getAsset(config.spritePath, TextureAtlas.class));
        animator.addAnimation("animateBroken", 0.2f,Animation.PlayMode.LOOP);
        animator.addAnimation("animateExtracting", 0.2f, Animation.PlayMode.LOOP);

        PlaceableEntity extractor = (PlaceableEntity) new PlaceableEntity()
                .addComponent(new PhysicsComponent().setBodyType(BodyType.StaticBody))
                .addComponent(new ColliderComponent().setLayer(PhysicsLayer.STRUCTURE))
                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.STRUCTURE))
                .addComponent(animator)
                .addComponent(new CombatStatsComponent(config.health, 0, 0, false))
                .addComponent(new ProductionComponent(config.resource, config.tickRate, config.tickSize))
                .addComponent(new ExtractorAnimationController());

        InteractLabel interactLabel = new InteractLabel();  //code for interaction prompt
        extractor.addComponent(new DistanceCheckComponent(5f, interactLabel));
        ServiceLocator.getRenderService().getStage().addActor(interactLabel);

        extractor.addComponent(new InteractableComponent(entity -> {
            CombatStatsComponent healthStats = extractor.getComponent(CombatStatsComponent.class);

            if (healthStats.isDead()) {
                ExtractorMinigameWindow minigame = ExtractorMinigameWindow.MakeNewMinigame(extractor);
                ServiceLocator.getRenderService().getStage().addActor(minigame);
            }
        }, 5f));
        extractor.setScale(1.8f, 2f);
        PhysicsUtils.setScaledCollider(extractor, 1f, 0.6f);

        return extractor;
    }

    //HEAD
    public static Entity createExtractorRepair() {
        Entity extractorRepair = new Entity()
                //.addComponent(new TextureRenderComponent("images/elixir_collector.png")); //This image removed
                .addComponent(new TextureRenderComponent("images/extractor.png"));
        extractorRepair.setScale(2.2f, 2.6f);
        return extractorRepair;
    }

    public static Entity createExtinguisher(TerrainComponent terrain, ExtractorMiniGameArea area) {
        Entity extinguisher = new Entity()
                .addComponent(new TextureRenderComponent("images/extinguisher.png"));
        ExtinguisherInputComponent extinguisherComponent = new ExtinguisherInputComponent(terrain, area);
        ServiceLocator.getInputService().register(extinguisherComponent);
        extinguisher.addComponent(extinguisherComponent);
        extinguisher.setScale(2f, 2f);
        return extinguisher;
    }

    public static Entity createSpanner(TerrainComponent terrain, ExtractorMiniGameArea area) {
        Entity spanner = new Entity()
                .addComponent(new TextureRenderComponent("images/spanner.png"));
        SpannerInputComponent spannerComponent = new SpannerInputComponent(terrain, area);
        ServiceLocator.getInputService().register(spannerComponent);
        spanner.addComponent(spannerComponent);
        spanner.setScale(2f, 2f);
        return spanner;
    }

    public static Entity createExtractorFirePart(TerrainComponent terrain, ExtractorMiniGameArea area) {
        Entity extractorFirePart = new Entity()
                .addComponent(new TextureRenderComponent("images/fire.png"));
        FireInputComponent fireComponent = new FireInputComponent(terrain, area);
        ServiceLocator.getInputService().register(fireComponent);

        extractorFirePart.addComponent(fireComponent);

        extractorFirePart.setScale(1.2f, 1.4f);

        return extractorFirePart;
    }

    public static Entity createExtractorHolePart(TerrainComponent terrain, ExtractorMiniGameArea area) {
        Entity extractorHolePart = new Entity()
                .addComponent(new TextureRenderComponent("images/Hole.png"));
        HoleInputComponent holeComponent = new HoleInputComponent(terrain, area);
        ServiceLocator.getInputService().register(holeComponent);

        extractorHolePart.addComponent(holeComponent);

        extractorHolePart.setScale(1.4f, 1.6f);
        return extractorHolePart;
    }

    public static Entity createExtractorBang() {
        Entity extractorBang = new Entity().addComponent(new TextureRenderComponent("images/bang.png"));
        extractorBang.setScale(2.2f, 2.4f);
        return extractorBang;
    }

    //TODO: REMOVE - LEGACY
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
        extractorConfig.resource = producedResource;
        extractorConfig.tickRate = tickRate;
        extractorConfig.tickSize = tickSize;
        return createExtractor(extractorConfig);
    }


    public static Entity createExtractorRepairPart() {
        Entity extractorRepairPart = new Entity()
                .addComponent(new TextureRenderComponent("images/fire.png"))
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


    //TODO: REMOVE - LEGACY
    /**
     * Creates a ship entity that uses the default package
     */
    public static Entity createShip(GdxGame game, List<ResourceCondition> requirements) {
        return createShip(game, requirements, defaultShip);
    }

    //CONFLICT HERE

    /**
     * Creates a ship entity that matches the config file
     */
    public static Entity createShip(GdxGame game, List<ResourceCondition> requirements, ShipConfig config) {
        Entity ship =
                new Entity()
                        .addComponent(new TextureRenderComponent(config.spritePath))
                        .addComponent(new PhysicsComponent().setBodyType(BodyType.StaticBody))
                        .addComponent(new ColliderComponent().setLayer(PhysicsLayer.STRUCTURE))
                        .addComponent(new HitboxComponent().setLayer(PhysicsLayer.STRUCTURE))
                        .addComponent(new InteractableComponent(entity -> {
                            if (checkWinCondition(requirements)) {
                                game.setScreen(GdxGame.ScreenType.NAVIGATION_SCREEN);
                            } else {
                                ShipInteractionPopup shipPopup = new ShipInteractionPopup();
                                ServiceLocator.getRenderService().getStage().addActor(shipPopup);
                            }
                        }, 5));

        ship.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);
        ship.getComponent(TextureRenderComponent.class).scaleEntity();
        ship.setScale(5f, 4.5f);
        PhysicsUtils.setScaledCollider(ship, 0.9f, 0.7f);

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

        spawner.scaleHeight(1.5f);
        return spawner;
    }
}


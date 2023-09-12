package com.csse3200.game.entities.factories;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.csse3200.game.ExtractorMinigameWindow;
import com.csse3200.game.GdxGame;
import com.csse3200.game.areas.mapConfig.ResourceCondition;
import com.csse3200.game.components.*;
import com.csse3200.game.components.npc.SpawnerComponent;
import com.csse3200.game.components.resources.ProductionComponent;
import com.csse3200.game.components.resources.Resource;
import com.csse3200.game.components.structures.ExtractorAnimationController;
import com.csse3200.game.components.upgradetree.UpgradeDisplay;
import com.csse3200.game.components.upgradetree.UpgradeTree;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.ExtractorConfig;
import com.csse3200.game.entities.configs.ShipConfig;
import com.csse3200.game.entities.configs.UpgradeBenchConfig;
import com.csse3200.game.entities.PlaceableEntity;
import com.csse3200.game.entities.enemies.EnemyBehaviour;
import com.csse3200.game.entities.enemies.EnemyType;
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

import java.util.ArrayList;
import java.util.List;

/**
 * Factory to create structure entities - such as extractors or ships.

 *
 * <p>Each obstacle entity type should have a creation method that returns a corresponding entity.
 */
public class StructureFactory {
    //Default configs
    public static final UpgradeBenchConfig defaultUpgradeBench =
            FileLoader.readClass(UpgradeBenchConfig.class, "configs/upgradeBench.json");
    public static final ShipConfig defaultShip =
            FileLoader.readClass(ShipConfig.class, "configs/ship.json");
    public static final ExtractorConfig defaultExtractor =
            FileLoader.readClass(ExtractorConfig.class, "configs/extractor.json");

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

    //TODO: REMOVE - LEGACY
    /**
     * Creates an upgrade bench entity using the default config
     */
    public static Entity createUpgradeBench() {
        return createUpgradeBench(defaultUpgradeBench);
    }

    /**
     * Creates an upgrade bench entity that matches the config file
     */
    public static Entity createUpgradeBench(UpgradeBenchConfig config) {
        Entity upgradeBench = new Entity()
                .addComponent(new PhysicsComponent().setBodyType(BodyType.StaticBody))
                .addComponent(new ColliderComponent().setLayer(PhysicsLayer.STRUCTURE))
                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.STRUCTURE))
                .addComponent(new TextureRenderComponent(config.spritePath))
                .addComponent(new UpgradeTree());

        InteractLabel interactLabel = new InteractLabel();  //code for interaction prompt
        upgradeBench.addComponent(new DistanceCheckComponent(0.5f, interactLabel));
        ServiceLocator.getRenderService().getStage().addActor(interactLabel);

        upgradeBench.addComponent(new InteractableComponent(entity -> {
            UpgradeDisplay minigame = UpgradeDisplay.createUpgradeDisplay(upgradeBench);
            ServiceLocator.getRenderService().getStage().addActor(minigame);
        }, 0.5f));

        upgradeBench.setScale(0.6f, 0.6f);

        return upgradeBench;
    }
    /**

     * Create an enemy spawner that spawns the desired enemies at a given tick rate and at a given location on the map
     *
     * @param targets the targets the entities that spawn will target
     * @param spawnRate the frequency of the enemy spawning
     * @param type the type of enemy to spawn
     * @param behaviour the behaviour type of the enemy to spawn
     * @param count the maximum amount of enemies the spawner will spawn
     * @return
     */
    public static Entity createSpawner(ArrayList<Entity> targets, long spawnRate, EnemyType type, EnemyBehaviour behaviour, int count) {
        Entity spawner =
                new Entity()
                        .addComponent(new TextureRenderComponent("images/Spawner.png"))
                        .addComponent(new PhysicsComponent().setBodyType(BodyType.StaticBody))
                        .addComponent(new ColliderComponent())
                        .addComponent(new SpawnerComponent(targets, spawnRate, type, behaviour, count));

        spawner.getComponent(TextureRenderComponent.class).scaleEntity();
        spawner.scaleHeight(1.5f);
        PhysicsUtils.setScaledCollider(spawner, 0.001f, 0.001f);
        return spawner;
    }
}


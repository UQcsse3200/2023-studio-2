package com.csse3200.game.entities.factories;

import com.csse3200.game.ExtractorMinigameWindow;
import com.csse3200.game.GdxGame;
import com.csse3200.game.areas.mapConfig.ResourceCondition;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.ExtractorRepairPartComponent;
import com.csse3200.game.components.InteractableComponent;
import com.csse3200.game.components.npc.SpawnerComponent;
import com.csse3200.game.components.resources.ProductionComponent;
import com.csse3200.game.components.resources.Resource;
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
import com.csse3200.game.rendering.DamageTextureComponent;
import com.csse3200.game.rendering.TextureRenderComponent;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import java.util.ArrayList;
import java.util.List;

import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.components.ShipInteractionPopup;
import com.csse3200.game.services.GameStateObserver;
import com.csse3200.game.components.upgradetree.UpgradeDisplay;
import com.csse3200.game.components.upgradetree.UpgradeTree;

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

    /**
     * Creates an extractor entity
     *
     * <p>Each obstacle entity type should have a creation method that returns a corresponding entity.

     * @param config Configuration file to match extractor to
     * @return a new extractor Entity
     */
    public static Entity createExtractor(ExtractorConfig config) {
        Entity extractor = new Entity()
                .addComponent(new DamageTextureComponent(config.spritePath)
                        .addTexture(0, "images/broken_extractor.png")) //TODO: Fix this when new animations are implemented
                .addComponent(new PhysicsComponent().setBodyType(BodyType.StaticBody))
                .addComponent(new ColliderComponent().setLayer(PhysicsLayer.STRUCTURE))
                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.STRUCTURE))
                .addComponent(new CombatStatsComponent(config.health, 0, 0, false))
                .addComponent(new ProductionComponent(config.resource, config.tickRate, config.tickSize));

        //For testing start at 0 so you can repair
        extractor.getComponent(CombatStatsComponent.class).setHealth(0);
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
        PlaceableEntity extractor = (PlaceableEntity) new PlaceableEntity()
                .addComponent(new DamageTextureComponent("images/refinedExtractor2.png")
                        .addTexture(0, "images/refinedBrokenExtractor.png"))
                .addComponent(new PhysicsComponent().setBodyType(BodyType.StaticBody))
                .addComponent(new ColliderComponent().setLayer(PhysicsLayer.STRUCTURE))
                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.STRUCTURE))
                .addComponent(new CombatStatsComponent(health, 0, 0, false))
                .addComponent(new ProductionComponent(producedResource, tickRate, tickSize));

        //For testing start at 0 so you can repair
        extractor.getComponent(CombatStatsComponent.class).setHealth(0);
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

    public static Entity createExtractorRepairPart() {
        Entity extractorRepairPart = new Entity()
                .addComponent(new TextureRenderComponent("images/fire.png"))
                .addComponent(new ExtractorRepairPartComponent());
        extractorRepairPart.setScale(1.8f, 2f);
        return extractorRepairPart;
    }

    private static boolean checkWinCondition(List<ResourceCondition>  requirements) {
        if (requirements == null) {
            return true;
        }

        GameStateObserver stateObserver = ServiceLocator.getGameStateObserverService();
        for (ResourceCondition condition: requirements) {
            String resourceKey = "resource/" + condition.getResource();
            int resourceCount = (int) stateObserver.getStateData(resourceKey);
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
                            }
                        }, 5));

        ship.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);
        ship.getComponent(TextureRenderComponent.class).scaleEntity();
        ship.setScale(4f, 4.5f);
        PhysicsUtils.setScaledCollider(ship, 0.9f, 0.7f);
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


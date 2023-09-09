package com.csse3200.game.entities.factories;

import com.csse3200.game.ExtractorMinigameWindow;
import com.csse3200.game.GdxGame;
import com.csse3200.game.areas.ExtractorMiniGameArea;
import com.csse3200.game.areas.terrain.TerrainComponent;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.InteractableComponent;
import com.csse3200.game.components.npc.SpawnerComponent;
import com.csse3200.game.components.resources.ProductionComponent;
import com.csse3200.game.components.resources.Resource;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.input.ExtinguisherInputComponent;
import com.csse3200.game.input.FireInputComponent;
import com.csse3200.game.input.HoleInputComponent;
import com.csse3200.game.input.SpannerInputComponent;
import com.csse3200.game.entities.enemies.EnemyBehaviour;
import com.csse3200.game.entities.enemies.EnemyType;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.PhysicsUtils;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.DamageTextureComponent;
import com.csse3200.game.rendering.TextureRenderComponent;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import java.util.ArrayList;

import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.GameStateObserver;
import com.csse3200.game.components.upgradetree.UpgradeDisplay;
import com.csse3200.game.components.upgradetree.UpgradeTree;

/**
 * Factory to create structure entities - such as extractors or ships.

 *
 * <p>Each obstacle entity type should have a creation method that returns a corresponding entity.
 */
public class StructureFactory {

    // * @param health the max and initial health of the extractor
    // * @param producedResource the resource type produced by the extractor
    // * @param tickRate the frequency at which the extractor ticks (produces resources)
    // * @param tickSize the amount of the resource produced at each tick

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
    public static Entity createExtractor(int health, Resource producedResource, long tickRate, int tickSize) {
        Entity extractor = new Entity()
                .addComponent(new DamageTextureComponent("images/extractor.png")
                        .addTexture(0, "images/broken_extractor.png"))
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

    public static Entity createExtractorRepair() {
        Entity extractorRepair = new Entity()
                .addComponent(new TextureRenderComponent("images/elixir_collector.png"));
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


    /**
     * Creates a ship entity
     */
    public static Entity createShip(GdxGame game) {
        Entity ship =
                new Entity()
                        .addComponent(new TextureRenderComponent("images/Ship.png"))
                        .addComponent(new PhysicsComponent().setBodyType(BodyType.StaticBody))
                        .addComponent(new ColliderComponent().setLayer(PhysicsLayer.STRUCTURE))
                        .addComponent(new HitboxComponent().setLayer(PhysicsLayer.STRUCTURE))
                        .addComponent(new InteractableComponent(entity -> {
                            //Exit to main menu if resource > 1000
                            GameStateObserver gameStateOb = ServiceLocator.getGameStateObserverService();
                            String resourceKey = "resource/" + Resource.Solstite;
                            int currentResourceCount = (int) gameStateOb.getStateData(resourceKey);
                            if (currentResourceCount > 1000) {
                                game.setScreen(GdxGame.ScreenType.MAIN_MENU);
                            }
                        }, 5));

        ship.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);
        ship.getComponent(TextureRenderComponent.class).scaleEntity();
        ship.setScale(4f, 4.5f);
        PhysicsUtils.setScaledCollider(ship, 0.9f, 0.7f);
        return ship;
    }

    /**
     * Creates an upgrade bench entity
     */
    public static Entity createUpgradeBench() {
        Entity upgradeBench = new Entity()
                .addComponent(new PhysicsComponent().setBodyType(BodyType.StaticBody))
                .addComponent(new ColliderComponent().setLayer(PhysicsLayer.STRUCTURE))
                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.STRUCTURE))
                .addComponent(new TextureRenderComponent("images/upgradetree/upgradebench.png"))
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


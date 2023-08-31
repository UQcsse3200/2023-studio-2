package com.csse3200.game.entities.factories;

import com.csse3200.game.ExtractorMinigameWindow;
import com.csse3200.game.GdxGame;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.ExtractorRepairPartComponent;
import com.csse3200.game.components.InteractableComponent;
import com.csse3200.game.components.npc.SpawnerComponent;
import com.csse3200.game.components.resources.ProductionComponent;
import com.csse3200.game.components.resources.Resource;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.enemies.EnemyBehaviour;
import com.csse3200.game.entities.enemies.EnemyType;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.PhysicsUtils;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.rendering.DamageTextureComponent;
import com.csse3200.game.rendering.TextureRenderComponent;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import java.util.ArrayList;

import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.GameStateObserver;

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

    public static Entity createExtractorRepairPart() {
        Entity extractorRepairPart = new Entity()
                .addComponent(new TextureRenderComponent("images/fire.png"))
                .addComponent(new ExtractorRepairPartComponent());
        extractorRepairPart.setScale(1.8f, 2f);
        return extractorRepairPart;
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


package com.csse3200.game.entities.factories;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.HealthBarComponent;
import com.csse3200.game.components.ProximityActivationComponent;
import com.csse3200.game.components.ProximityTriggerComponent;
import com.csse3200.game.components.explosives.ExplosiveComponent;
import com.csse3200.game.components.explosives.ExplosiveConfig;
import com.csse3200.game.components.flags.EnemyFlag;
import com.csse3200.game.components.structures.JoinLayer;
import com.csse3200.game.components.structures.JoinableComponent;
import com.csse3200.game.components.structures.StructureDestroyComponent;
import com.csse3200.game.entities.PlaceableEntity;
import com.csse3200.game.entities.configs.ExplosiveBarrelConfig;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.PhysicsUtils;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.TextureRenderComponent;
import com.csse3200.game.services.ServiceLocator;

public class ExplosivesFactory {
    private static final ExplosiveBarrelConfig explosiveBarrelConfig = FileLoader.readClass(ExplosiveBarrelConfig.class,
            "configs/explosiveBarrel.json");
    private static final LandmineConfig landmineConfig = FileLoader.readClass(LandmineConfig.class,
            "configs/landmine.json");

    public static PlaceableEntity createExplosiveBarrel() {
        var explosiveBarrel = new PlaceableEntity();

        Texture texture = ServiceLocator.getResourceService().getAsset(explosiveBarrelConfig.texture, Texture.class);

        explosiveBarrel.addComponent(new PhysicsComponent().setBodyType(BodyDef.BodyType.StaticBody))
                .addComponent(new ColliderComponent().setLayer(PhysicsLayer.WALL))
                .addComponent(new CombatStatsComponent(explosiveBarrelConfig.health,
                        0,0,false))
                .addComponent(new HealthBarComponent(true))
                .addComponent(new StructureDestroyComponent())
                .addComponent(new ExplosiveComponent(explosiveBarrelConfig.explosive))
                .addComponent(new TextureRenderComponent(texture));

        explosiveBarrel.setScale(0.5f,(float) texture.getHeight() / texture.getWidth() * 0.5f);

        PhysicsUtils.setScaledCollider(explosiveBarrel, 0.5f, 0.3f);

        return explosiveBarrel;
    }

    public static PlaceableEntity createLandmine() {
        var explosiveBarrel = new PlaceableEntity();

        Texture texture = ServiceLocator.getResourceService().getAsset(landmineConfig.texture, Texture.class);

        explosiveBarrel.addComponent(new PhysicsComponent().setBodyType(BodyDef.BodyType.StaticBody))
                .addComponent(new ExplosiveComponent(landmineConfig.explosive))
                .addComponent(new TextureRenderComponent(texture))
                .addComponent(new ProximityTriggerComponent(EnemyFlag.class, 1f,
                        () -> explosiveBarrel.getEvents().trigger("explode")));

        explosiveBarrel.setScale(0.5f,(float) texture.getHeight() / texture.getWidth() * 0.5f);

        return explosiveBarrel;
    }
}
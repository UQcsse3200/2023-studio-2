package com.csse3200.game.entities.buildables;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.HealthBarComponent;
import com.csse3200.game.components.ProximityActivationComponent;
import com.csse3200.game.components.joinable.JoinLayer;
import com.csse3200.game.components.joinable.JoinableComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.WallConfig;
import com.csse3200.game.entities.configs.WallConfigs;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.AtlasRenderComponent;
import com.csse3200.game.services.ServiceLocator;

public class Gate extends Entity {
    WallType type;
    private static final WallConfigs configs =
            FileLoader.readClass(WallConfigs.class, "configs/walls.json");
    private boolean isLeftRight;

    public Gate(WallType type, boolean isLeftRight, Entity player) {

        this.type = type;
        this.isLeftRight = isLeftRight;

        WallConfig config = configs.GetWallConfig(type);
        var textures = ServiceLocator.getResourceService().getAsset(config.textureAtlas, TextureAtlas.class);

        addComponent(new ProximityActivationComponent(1.5f, player, this::openGate, this::closeGate));
        addComponent(new PhysicsComponent().setBodyType(BodyDef.BodyType.StaticBody));
        addComponent(new ColliderComponent().setLayer(PhysicsLayer.WALL));
        addComponent(new CombatStatsComponent(config.health, 0,0,false));
        addComponent(new HealthBarComponent(true, true, getCenterPosition())).setEnabled(false);

        String region = isLeftRight ? "closed-left-right" : "closed-up-down";

        addComponent(new AtlasRenderComponent(textures, region));
        getComponent(AtlasRenderComponent.class).scaleEntity();

    }

    public void openGate(Entity player) {
        getComponent(PhysicsComponent.class).setEnabled(false);

        String region = isLeftRight ? "open-left-right" : "open-up-down";

        getComponent(AtlasRenderComponent.class).setRegion(region, false);
    }

    public void closeGate(Entity player) {
        getComponent(PhysicsComponent.class).setEnabled(true);

        String region = isLeftRight ? "closed-left-right" : "closed-up-down";

        getComponent(AtlasRenderComponent.class).setRegion(region, false);
    }

    public WallType getWallType() {
        return type;
    }
}

package com.csse3200.game.entities.buildables;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.HealthBarComponent;
import com.csse3200.game.components.ProximityActivationComponent;
import com.csse3200.game.components.joinable.JoinLayer;
import com.csse3200.game.components.joinable.JoinableComponent;
import com.csse3200.game.components.joinable.JoinableComponentShapes;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.GateConfig;
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
    private static final GateConfig config =
            FileLoader.readClass(GateConfig.class, "configs/gates.json");
    private static final JoinableComponentShapes shapes =
            FileLoader.readClass(JoinableComponentShapes.class, "vertices/walls.json");
    private TextureAtlas openAtlas;

    private TextureAtlas closedAtlas;

    public Gate(WallType type, boolean isLeftRight, Entity player) {
        openAtlas = ServiceLocator.getResourceService().getAsset(config.openTextureAtlas, TextureAtlas.class);
        closedAtlas = ServiceLocator.getResourceService().getAsset(config.closedTextureAtlas, TextureAtlas.class);
        this.type = type;


        addComponent(new ProximityActivationComponent(1.5f, player, this::openGate, this::closeGate));
        addComponent(new PhysicsComponent().setBodyType(BodyDef.BodyType.StaticBody));
        addComponent(new ColliderComponent().setLayer(PhysicsLayer.WALL));
        addComponent(new CombatStatsComponent(config.health, 0,0,false));
        addComponent(new HealthBarComponent(true));
        addComponent(new JoinableComponent(openAtlas,JoinLayer.WALLS, shapes));
        getComponent(JoinableComponent.class).scaleEntity();

    }

    public void openGate(Entity player) {
        getComponent(PhysicsComponent.class).setEnabled(false);




    }

    public void closeGate(Entity player) {
        getComponent(PhysicsComponent.class).setEnabled(true);




    }

    public WallType getWallType() {
        return type;
    }
}

package com.csse3200.game.entities.buildables;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.FOVComponent;
import com.csse3200.game.components.HealthBarComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.PlaceableEntity;
import com.csse3200.game.entities.configs.TurretConfig;
import com.csse3200.game.entities.configs.TurretConfigs;
import com.csse3200.game.entities.factories.EnemyFactory;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.TextureRenderComponent;
import com.csse3200.game.services.ServiceLocator;

public class Turret extends PlaceableEntity{

    private long start = System.currentTimeMillis();
    private Vector2 position;
    private Vector2 target;

    private static final TurretConfigs turretConfigs =
            FileLoader.readClass(TurretConfigs.class, "configs/turrets.json");

    TurretType type;

    int maxAmmo;
    int damage;

    public Turret(TurretType type, Entity player) {
        super();
        this.type = type;

        TurretConfig turretConfig = turretConfigs.GetTurretConfig(type);
        maxAmmo = turretConfig.maxAmmo;
        damage = turretConfig.damage;
        var texture = ServiceLocator.getResourceService().getAsset(turretConfig.texture, Texture.class);

        addComponent(new PhysicsComponent().setBodyType(BodyDef.BodyType.StaticBody));
        addComponent(new ColliderComponent().setLayer(PhysicsLayer.TURRET));
        addComponent(new HitboxComponent().setLayer(PhysicsLayer.STRUCTURE));
        addComponent(new CombatStatsComponent(turretConfig.health, 0, 0, false));
        addComponent(new HealthBarComponent(true));
        addComponent(new TextureRenderComponent(texture));
        addComponent(new FOVComponent(4f, EnemyFactory.enemies, this::startDamage, this::stopDamage));
    }

    @Override
    public void update() {
        super.update();
    }

    public void startDamage(Entity focus) {

        if(this.getComponent(CombatStatsComponent.class).getHealth() < this.getComponent(CombatStatsComponent.class).getMaxHealth()) {
            this.getComponent(HealthBarComponent.class).setEnabled(true);
        }
        else if(this.getComponent(CombatStatsComponent.class).getHealth() == this.getComponent(CombatStatsComponent.class).getMaxHealth()) {
            this.getComponent(HealthBarComponent.class).setEnabled(false);
        }
        else {
            this.getComponent(HealthBarComponent.class).setEnabled(false);
        }
        if (focus.getComponent(CombatStatsComponent.class) != null && focus.getComponent(CombatStatsComponent.class).getHealth() > 0) {
            // give damage until health is 0
            focus.getComponent(HealthBarComponent.class).setEnabled(true);
            if (System.currentTimeMillis() - this.start > 1000) {
                giveDamage(focus);
                this.start = System.currentTimeMillis();
                stopDamage(focus);
            }
        }
    }

    public void stopDamage(Entity focus) {

        if (focus.getComponent(CombatStatsComponent.class) != null && focus.getComponent(CombatStatsComponent.class).getHealth() > 0) {
            focus.getComponent(HealthBarComponent.class).setEnabled(false);
        }
    }

    public void giveDamage(Entity focus) {
        if (focus.getComponent(CombatStatsComponent.class) != null) {
            focus.getComponent(CombatStatsComponent.class).setHealth(focus.getComponent(CombatStatsComponent.class).getHealth() - damage);
            rotateTurret(focus);
        }
    }

    public void rotateTurret(Entity focus) {
        target = new Vector2(focus.getCenterPosition().x, focus.getCenterPosition().y);
        position = new Vector2(this.getPosition().x, this.getPosition().y);
        float angle = MathUtils.atan2(target.y - position.y, target.x - position.x);
        float degrees = MathUtils.radiansToDegrees * angle;

        TextureRenderComponent textureComponent = getComponent(TextureRenderComponent.class);
        textureComponent.setRotation(degrees - 90);
    }
}

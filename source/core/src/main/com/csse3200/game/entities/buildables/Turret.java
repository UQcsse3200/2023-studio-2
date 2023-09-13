package com.csse3200.game.entities.buildables;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.csse3200.game.components.*;
import com.csse3200.game.components.structures.StructureDestroyComponent;
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
import java.util.Objects;

public class Turret extends PlaceableEntity{

    private long start = System.currentTimeMillis();

    private static final TurretConfigs turretConfigs =
            FileLoader.readClass(TurretConfigs.class, "configs/turrets.json");

    TurretType type;

    int maxAmmo;
    int damage;

    //TODO: REMOVE - LEGACY
    public Turret(TurretType type, Entity player) {
        this(turretConfigs.GetTurretConfig(type));
    }

    /**
     * Create a new turret placeable entity to match the provided config file
     * @param turretConfig Configuration file to match turret to
     */
    public Turret(TurretConfig turretConfig ) {
        super();

        maxAmmo = turretConfig.maxAmmo;
        damage = turretConfig.damage;
        var texture = ServiceLocator.getResourceService().getAsset(turretConfig.spritePath, Texture.class);

        addComponent(new PhysicsComponent().setBodyType(BodyDef.BodyType.StaticBody));
        addComponent(new ColliderComponent().setLayer(PhysicsLayer.TURRET));
        addComponent(new HitboxComponent().setLayer(PhysicsLayer.STRUCTURE));
        addComponent(new CombatStatsComponent(turretConfig.health, turretConfig.damage, turretConfig.attackMultiplier, turretConfig.isImmune));
        addComponent(new HealthBarComponent(true));
        addComponent(new TextureRenderComponent(texture));
        addComponent(new FOVComponent(4f, this::startDamage, this::stopDamage));
        addComponent(new StructureDestroyComponent());
    }

    @Override
    public void update() {
        super.update();
    }

    public void startDamage(Entity focus) {
        var combatStatsComponent = getComponent(CombatStatsComponent.class);

        var focusCombatStatsComponent = focus.getComponent(CombatStatsComponent.class);

        var healthBarComponent = getComponent(HealthBarComponent.class);

        if (combatStatsComponent == null || healthBarComponent == null || focusCombatStatsComponent == null) {
            return;
        }

        if(combatStatsComponent.getHealth() < combatStatsComponent.getMaxHealth()) {
            healthBarComponent.setEnabled(true);
        }
        else if(combatStatsComponent.getHealth() == combatStatsComponent.getMaxHealth()) {
            healthBarComponent.setEnabled(false);
        }
        else {
            healthBarComponent.setEnabled(false);
        }

        if (focusCombatStatsComponent.getHealth() <= 0) {
            return;
        }

        // give damage until health is 0
        focus.getComponent(HealthBarComponent.class).setEnabled(true);
        if (System.currentTimeMillis() - this.start > 1000) {
            giveDamage(focus);
            this.start = System.currentTimeMillis();
            stopDamage(focus);
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
        var target = new Vector2(focus.getCenterPosition().x, focus.getCenterPosition().y);
        var position = new Vector2(this.getPosition().x, this.getPosition().y);
        float angle = MathUtils.atan2(target.y - position.y, target.x - position.x);
        float degrees = MathUtils.radiansToDegrees * angle;

        TextureRenderComponent textureComponent = getComponent(TextureRenderComponent.class);
        textureComponent.setRotation(degrees - 90);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Turret turret = (Turret) o;
        return start == turret.start && maxAmmo == turret.maxAmmo && damage == turret.damage
                && type == turret.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), start, type, maxAmmo, damage);
    }
}

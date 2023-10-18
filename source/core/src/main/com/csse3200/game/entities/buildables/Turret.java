package com.csse3200.game.entities.buildables;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.FOVComponent;
import com.csse3200.game.components.HealthBarComponent;
import com.csse3200.game.components.SaveableComponent;
import com.csse3200.game.components.structures.StructureDestroyComponent;
import com.csse3200.game.components.structures.TurretAnimationController;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.PlaceableEntity;
import com.csse3200.game.entities.configs.TurretConfig;
import com.csse3200.game.entities.configs.TurretConfigs;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.ServiceLocator;

import java.util.Objects;

/** Turret
 *  This class is used to create a turret entity.
 */
public class Turret extends PlaceableEntity{
    private long start = System.currentTimeMillis(); // start time

    TurretType type; // turret type
    private int currentAmmo; // current ammo

    int maxAmmo; // max ammo
    int damage; // damage

    /**
     * Create a new turret placeable entity to match the provided config file
     * @param turretConfig Configuration file to match turret to
     */
    public Turret(TurretConfig turretConfig) {
        super(2, 2);
        setScale(1.8f,1.8f);

        AnimationRenderComponent animator = new AnimationRenderComponent(
                ServiceLocator.getResourceService().getAsset(turretConfig.spritePath, TextureAtlas.class)
        ); // create animation render component
        animator.addAnimation("normal", 0.05f, Animation.PlayMode.NORMAL); // add animation
        animator.addAnimation("shoot", 0.05f, Animation.PlayMode.NORMAL); // add animation
        maxAmmo = turretConfig.maxAmmo; // set max ammo
        currentAmmo = maxAmmo;
        damage = turretConfig.damage; // set damage

        addComponent(new PhysicsComponent().setBodyType(BodyDef.BodyType.StaticBody)); // add physics component
        addComponent(new ColliderComponent().setLayer(PhysicsLayer.TURRET)); // add collider component
        addComponent(new HitboxComponent().setLayer(PhysicsLayer.STRUCTURE)); // add hitbox component
        addComponent(new CombatStatsComponent(turretConfig.health, turretConfig.maxHealth, turretConfig.damage, // add combat stats component
                turretConfig.attackMultiplier, turretConfig.isImmune));
        addComponent(new HealthBarComponent(true)); // add health bar component
        addComponent(new FOVComponent(4f, this::startDamage, this::stopDamage)); // add fov component
        addComponent(new StructureDestroyComponent());// add structure destroy component
        addComponent(animator); // add animation render component
        addComponent(new TurretAnimationController());
        addComponent(new SaveableComponent<>(gate -> save(gate, turretConfig), TurretConfig.class));
    }

    /**
     * This method is used manage shooting and requires a target.
     * @param focus The entity to check before targeting.
     */
    public void startDamage(Entity focus) {
        var combatStatsComponent = getComponent(CombatStatsComponent.class);

        var focusCombatStatsComponent = focus.getComponent(CombatStatsComponent.class);

        if (combatStatsComponent == null|| focusCombatStatsComponent == null) {
            return;
        }

        if (focusCombatStatsComponent.getHealth() <= 0 || !canFire()) {
            return;
        }
        // give damage until health is 0
        if (System.currentTimeMillis() - this.start > 1000) {
            giveDamage(focus);
            Sound shootingSound = ServiceLocator.getResourceService().getAsset("sounds/turret_shoot.mp3", Sound.class);
            shootingSound.play(1);
            rotateTurret(focus);
            this.start = System.currentTimeMillis();
        }
    }

    /**
     * This method is used to stop giving damage to an entity.
     * @param focus The entity to stop giving damage to.
     */
    public void stopDamage(Entity focus) {

        this.getComponent(AnimationRenderComponent.class).startAnimation("normal"); // set animation
    }

    /**
     * This method is used to give damage to an entity.
     * @param focus The entity to give damage to.
     */
    public void giveDamage(Entity focus) {
        if (focus.getComponent(CombatStatsComponent.class) != null) {
            focus.getComponent(CombatStatsComponent.class).setHealth(focus.getComponent(CombatStatsComponent.class).getHealth() - damage);
            /*rotateTurret(focus);*/
            currentAmmo --;
        }
    }

    /**
     * This method is used to rotate the turret.
     * @param focus The entity to rotate the turret towards.
     */
    public void rotateTurret(Entity focus) {
        var target = new Vector2(focus.getCenterPosition().x, focus.getCenterPosition().y);
        var position = new Vector2(this.getCenterPosition().x, this.getCenterPosition().y);
        float angle = MathUtils.atan2(target.y - position.y, target.x - position.x);
        float degrees = MathUtils.radiansToDegrees * angle;

        AnimationRenderComponent textureComponent = getComponent(AnimationRenderComponent.class);
        if (textureComponent != null) {
            textureComponent.setRotation(degrees - 90);
        }
        this.getComponent(AnimationRenderComponent.class).startAnimation("shoot"); // set animation
    }

    /**
     * Interact with the turret. This method can be called by a player entity to perform an interaction.
     *
     * @param player The player entity interacting with the turret.
     */
    public void interact(Entity player) {
        int requiredAmmo = maxAmmo - currentAmmo; // calculate required ammo
        var gameStateObserver = ServiceLocator.getGameStateObserverService(); // get game state observer
        int availableResources = (int) gameStateObserver.getStateData("resource/nebulite"); // get available resources

        if (availableResources >= requiredAmmo) {
            gameStateObserver.trigger("resourceAdd", "nebulite", -requiredAmmo); // remove resources
            refillAmmo(requiredAmmo);
            System.out.println("Turret ammo refilled!"); // print message
        } else {
            System.out.println("Insufficient resources to refill turret ammo."); // print message

        }
    }

    /**
     * Refill the turret's ammunition to its maximum capacity.
     */
    public void refillAmmo() {
        currentAmmo = maxAmmo; // set current ammo to max ammo
    }

    /**
     * Check if the turret can fire (has ammunition).
     *
     * @return True if the turret can fire, else false.
     */

    public boolean canFire() {
        return  (currentAmmo > 0) ; // return true if current ammo is greater than 0
    }

    /**
     * Refill the turret's ammunition by the given amount.
     * @param ammo The amount of ammunition to refill.
     */
    public void refillAmmo(int ammo) {
        if (currentAmmo + ammo <= maxAmmo) {
            currentAmmo += ammo;
        }
    }

    /**
     * This method is used to check for similar type of turret.
     * @return True if similar, else false.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Turret turret = (Turret) o;
        return start == turret.start && maxAmmo == turret.maxAmmo && damage == turret.damage
                && type == turret.type;
    }

    /**
     * This method is used to generate a hash code using its properties.
     * @return The hash code.
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), start, type, maxAmmo, damage);
    }

    public int getCurrentAmmo() {
        return this.currentAmmo;
    }

    public void setCurrentAmmo(int currentAmmo) {
        this.currentAmmo = currentAmmo;
    }

    /**
     * A function to save the Turret's properties into its config.
     * @param entity - the turret to save.
     * @param config - the existing config for the turret.
     * @return the updated config for the turret.
     */
    private static TurretConfig save(Entity entity, TurretConfig config) {
        if (!(entity instanceof Turret)) {
            return new TurretConfig();
        }

        config.position = entity.getGridPosition();
        config.health = entity.getComponent(CombatStatsComponent.class).getHealth();

        return config;
    }
}

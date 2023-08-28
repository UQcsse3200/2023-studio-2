package com.csse3200.game.components.Weapons;

import com.csse3200.game.components.Component;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.TouchAttackComponent;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.PhysicsUtils;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.areas.GameArea;
import com.csse3200.game.entities.factories.AttackFactory;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.areas.terrain.TerrainFactory.TerrainType;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.NPCFactory;
import com.csse3200.game.entities.factories.ObstacleFactory;
import com.csse3200.game.entities.factories.PlayerFactory;
import com.csse3200.game.entities.factories.PowerupFactory;
import com.csse3200.game.utils.math.GridPoint2Utils;
import com.csse3200.game.utils.math.RandomUtils;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.components.gamearea.GameAreaDisplay;
import com.badlogic.gdx.Gdx;
import java.util.*;

/**
 */
public class MeleeComponent extends Component {
    int life;
    int swingSpeed;
    int currentRot;
    /**
     *
     */
    public MeleeComponent(int life, int swingSpeed, int initRot) {
        this.currentRot = initRot;
        this.life = life;
        this.swingSpeed = swingSpeed;
    }

    @Override
    public void update() {
        entity.setRotation(this.currentRot);
        this.currentRot -= this.swingSpeed;

        Vector2 position = entity.getPosition();
        double degrees = entity.getRotation() + 45 + 90;
        double radians = Math.toRadians(degrees);
        float sinValue = (float) Math.sin(radians) * 0.015f * this.swingSpeed;
        float cosValue = (float) Math.cos(radians) * 0.015f * this.swingSpeed;

        entity.setPosition(new Vector2(position.x - cosValue, position.y - sinValue));

        this.life--;
        if (this.life == 0) {
            entity.setPosition(new Vector2(-100, -100));
        }
    }
}


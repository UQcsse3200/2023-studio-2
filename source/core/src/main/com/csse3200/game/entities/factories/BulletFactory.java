package com.csse3200.game.entities.factories;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.csse3200.game.entities.buildables.Bullet;

public class BulletFactory {
    private Array<Bullet> bullets;

    public BulletFactory() {
        bullets = new Array<>();
    }

    public void createBullet(Vector2 startPosition, float rotation) {
        Bullet bullet = new Bullet(startPosition, rotation, "bulletTexture.png");
        bullets.add(bullet);
    }

    public void update(float deltaTime) {
        for (Bullet bullet : bullets) {
        }
    }

    }
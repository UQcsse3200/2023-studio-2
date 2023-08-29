package com.csse3200.game.rendering;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ExtendWith(GameExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class DamageTextureComponentTest {
    @Mock Texture texture;
    @Mock Texture texture2;
    @Mock Texture texture3;
    @Mock SpriteBatch spriteBatch;
    @Mock ResourceService resourceService;
    Entity entity;

    private final float x = 2f;
    private final float y = 2f;
    private final float width = 1f;
    private final float height = 1f;
    private final int maxHealth = 100;

    @BeforeEach
    void setup() {
        entity = new Entity().addComponent(new CombatStatsComponent(maxHealth, 0, 1, false));
        entity.setPosition(x, y);
        entity.setScale(width, height);
        ServiceLocator.registerResourceService(resourceService);
        when(resourceService.getAsset("texture.png", Texture.class))
                .thenReturn(texture);
        when(resourceService.getAsset("texture2.png", Texture.class))
                .thenReturn(texture2);
        when(resourceService.getAsset("texture3.png", Texture.class))
                .thenReturn(texture3);
    }

    private void verifyRender(DamageTextureComponent dtc, Texture texture) {
        dtc.setEntity(entity);
        dtc.render(spriteBatch);

        verify(spriteBatch).draw(texture, x, y, width, height);
    }

    @Test
    void shouldAddTexturePathConstructor() {
        DamageTextureComponent dtc = new DamageTextureComponent(texture);
        verifyRender(dtc, texture);
    }

    @Test
    void shouldAddTextureConstructor() {
        DamageTextureComponent dtc = new DamageTextureComponent("texture.png");
        verifyRender(dtc, texture);
    }

    @Test
    void shouldAddTexturePath() {
        DamageTextureComponent dtc = new DamageTextureComponent("texture.png");
        dtc.addTexture(maxHealth/2, "texture2.png");

        entity.getComponent(CombatStatsComponent.class).setHealth(maxHealth/3);
        verifyRender(dtc, texture2);
    }

    @Test
    void shouldAddTexture() {
        DamageTextureComponent dtc = new DamageTextureComponent(texture);
        dtc.addTexture(maxHealth/2, texture2);

        verifyRender(dtc, texture);
        entity.getComponent(CombatStatsComponent.class).setHealth(maxHealth/3);
        verifyRender(dtc, texture2);
    }

    @Test
    void shouldAddMultipleTextures() {
        DamageTextureComponent dtc = new DamageTextureComponent(texture)
                .addTexture(maxHealth/2, texture2)
                .addTexture(maxHealth/4, texture3);

        verifyRender(dtc, texture);
        entity.getComponent(CombatStatsComponent.class).setHealth(maxHealth/3);
        verifyRender(dtc, texture2);
        entity.getComponent(CombatStatsComponent.class).setHealth(maxHealth/5);
        verifyRender(dtc, texture3);
    }

    @Test
    void shouldAddMultipleTexturePaths() {
        DamageTextureComponent dtc = new DamageTextureComponent("texture.png")
                .addTexture(maxHealth/2, "texture2.png")
                .addTexture(maxHealth/4, "texture3.png");

        verifyRender(dtc, texture);
        entity.getComponent(CombatStatsComponent.class).setHealth(maxHealth/3);
        verifyRender(dtc, texture2);
        entity.getComponent(CombatStatsComponent.class).setHealth(maxHealth/5);
        verifyRender(dtc, texture3);
    }

    @Test
    void shouldDisplayTextureOnThreshold() {
        DamageTextureComponent dtc = new DamageTextureComponent("texture.png")
                .addTexture(maxHealth/2, "texture2.png")
                .addTexture(maxHealth/4, "texture3.png");

        verifyRender(dtc, texture);
        entity.getComponent(CombatStatsComponent.class).setHealth(maxHealth/2);
        verifyRender(dtc, texture2);
        entity.getComponent(CombatStatsComponent.class).setHealth(maxHealth/5);
        verifyRender(dtc, texture3);
    }
}

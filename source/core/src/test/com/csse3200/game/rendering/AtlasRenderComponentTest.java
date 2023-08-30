package com.csse3200.game.rendering;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.extensions.GameExtension;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.verification.VerificationMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)
class AtlasRenderComponentTest {
    @Mock
    TextureAtlas textureAtlas;
    @Mock
    SpriteBatch spriteBatch;
    @Mock
    Entity entity;

    @Test
    void shouldDrawRegion() {
        Vector2 position = new Vector2(10,10);
        Vector2 scale = new Vector2(1,1);

        String regionId = "region-1";
        TextureAtlas.AtlasRegion atlasRegion = mock(TextureAtlas.AtlasRegion.class);

        when(entity.getPosition()).thenReturn(position.cpy());
        when(entity.getScale()).thenReturn(scale.cpy());
        when(textureAtlas.findRegion(regionId)).thenReturn(atlasRegion);

        AtlasRenderComponent component = new AtlasRenderComponent(textureAtlas, regionId);
        component.setEntity(entity);
        component.render(spriteBatch);

        verify(spriteBatch).draw(atlasRegion, position.x, position.y, scale.x, scale.y);
    }


    @Test
    void shouldDrawChangedRegion() {
        Vector2 position = new Vector2(10,10);
        Vector2 scale = new Vector2(1,1);

        String regionId1 = "region-1";
        String regionId2 = "region-2";
        TextureAtlas.AtlasRegion atlasRegion1 = mock(TextureAtlas.AtlasRegion.class);
        TextureAtlas.AtlasRegion atlasRegion2 = mock(TextureAtlas.AtlasRegion.class);

        when(entity.getPosition()).thenReturn(position.cpy());
        when(entity.getScale()).thenReturn(scale.cpy());
        when(textureAtlas.findRegion(regionId1)).thenReturn(atlasRegion1);
        when(textureAtlas.findRegion(regionId2)).thenReturn(atlasRegion2);

        AtlasRenderComponent component = new AtlasRenderComponent(textureAtlas, regionId1);
        component.setEntity(entity);
        component.setRegion(regionId2, false);

        component.render(spriteBatch);

        verify(spriteBatch).draw(atlasRegion2, position.x, position.y, scale.x, scale.y);
    }

    @Test
    void shouldScaleEntity() {
        String regionId = "region-1";
        TextureAtlas.AtlasRegion atlasRegion = mock(TextureAtlas.AtlasRegion.class);

        when(atlasRegion.getRegionHeight()).thenReturn(16);
        when(atlasRegion.getRegionWidth()).thenReturn(32);
        when(textureAtlas.findRegion(regionId)).thenReturn(atlasRegion);

        AtlasRenderComponent component = new AtlasRenderComponent(textureAtlas, regionId);
        component.setEntity(entity);
        component.scaleEntity();

        verify(entity).setScale(1f, 0.5f);
    }

    @Test
    void shouldChangeRegionScaleEntity() {
        String regionId1 = "region-1";
        String regionId2 = "region-2";
        TextureAtlas.AtlasRegion atlasRegion1 = mock(TextureAtlas.AtlasRegion.class);
        TextureAtlas.AtlasRegion atlasRegion2 = mock(TextureAtlas.AtlasRegion.class);

        when(textureAtlas.findRegion(regionId1)).thenReturn(atlasRegion1);
        when(textureAtlas.findRegion(regionId2)).thenReturn(atlasRegion2);

        when(atlasRegion2.getRegionHeight()).thenReturn(64);
        when(atlasRegion2.getRegionWidth()).thenReturn(16);

        AtlasRenderComponent component = new AtlasRenderComponent(textureAtlas, regionId1);
        component.setEntity(entity);

        component.setRegion(regionId2, true);
        verify(entity).setScale(1f, 4f);
    }
}
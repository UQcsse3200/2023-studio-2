package com.csse3200.game.areas.mapConfig;

import com.csse3200.game.entities.configs.BaseEntityConfig;
import com.csse3200.game.extensions.GameExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)
class GameAreaConfigTest {
    GameAreaConfig gameAreaConfig = new GameAreaConfig();
    AreaEntityConfig areaEntityConfig = mock(AreaEntityConfig.class);
    static BaseEntityConfig entity1 = mock(BaseEntityConfig.class);
    static BaseEntityConfig entity2 = mock(BaseEntityConfig.class);
    static BaseEntityConfig entity3 = mock(BaseEntityConfig.class);
    static BaseEntityConfig entity4 = mock(BaseEntityConfig.class);

    private static final List<BaseEntityConfig> entities1 = List.of(entity1, entity2, entity3, entity4);
    private static final List<BaseEntityConfig> entities2 = List.of(entity1, entity2);
    private static final List<BaseEntityConfig> entities3 = List.of(entity3, entity4);

    private static final List<String> textures1 = List.of("Texture1.png", "Texture4.png");
    private static final List<String> textures2 = List.of("Texture2.png", "Texture2.png");
    private static final List<String> textures3 = List.of("Texture3.png", "Texture2.png");
    private static final List<String> textures4 = List.of("Texture4.png");

    @BeforeEach
    void setup() {
        gameAreaConfig.areaEntityConfig = areaEntityConfig;

        when(entity1.getTextures()).thenReturn(textures1);
        when(entity2.getTextures()).thenReturn(textures2);
        when(entity3.getTextures()).thenReturn(textures3);
        when(entity4.getTextures()).thenReturn(textures4);
    }

    @Test
    void getEntityTexturesReturnsSingleConfig() {
        when(areaEntityConfig.getAllConfigs()).thenReturn(List.of(entity1));

        unorderedMatch(List.of(gameAreaConfig.getEntityTextures()),
                textures1);
    }

    @Test
    void getEntityTexturesReturnsAllDistinct() {
        when(areaEntityConfig.getAllConfigs()).thenReturn(List.of(entity1, entity2, entity3, entity4));

        unorderedMatch(List.of(gameAreaConfig.getEntityTextures()),
                List.of("Texture1.png", "Texture2.png", "Texture3.png", "Texture4.png"));
    }

    @Test
    void getEntityTexturesReturnsAllDistinct2() {
        when(areaEntityConfig.getAllConfigs()).thenReturn(List.of(entity1, entity3));

        unorderedMatch(List.of(gameAreaConfig.getEntityTextures()),
                List.of("Texture1.png", "Texture2.png", "Texture3.png", "Texture4.png"));
    }

    @Test
    void getEntityTexturesReturnsAllUnique() {
        when(areaEntityConfig.getAllConfigs()).thenReturn(List.of(entity3, entity4));

        unorderedMatch(List.of(gameAreaConfig.getEntityTextures()),
                List.of("Texture2.png", "Texture3.png", "Texture4.png"));
    }

    private void unorderedMatch(List<String> expectedTextures, List<String> output) {
        assertTrue(expectedTextures.size() == output.size() && expectedTextures.containsAll(output) && output.containsAll(expectedTextures));
    }
}

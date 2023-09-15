package com.csse3200.game.areas.mapConfig;

import com.csse3200.game.extensions.GameExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class LevelConfigTest {
    LevelConfig levelConfig = new LevelConfig();
    @Mock
    static GameAreaConfig area1 = mock(GameAreaConfig.class);
    @Mock
    static GameAreaConfig area2 = mock(GameAreaConfig.class);
    @Mock
    static GameAreaConfig area3 = mock(GameAreaConfig.class);

    private static final String[] textures1 = new String[] {"Texture1.png", "Texture2.png"};
    private static final String[] textures2 = new String[] {"Texture2.png", "Texture3.png"};
    private static final String[] textures3 = new String[] {"Texture4.png", "Texture1.png"};

    @BeforeEach
    void setupEach() {
        levelConfig.gameAreas = new ArrayList<>();
        when(area1.getEntityTextures()).thenReturn(textures1);
        when(area2.getEntityTextures()).thenReturn(textures2);
        when(area3.getEntityTextures()).thenReturn(textures3);
    }

    @Test
    void getTexturesReturnsSingleArea() {
        levelConfig.gameAreas.add(area1);

        unorderedMatch(List.of(levelConfig.getTextures()),
                List.of(textures1));
    }

    @Test
    void getTexturesReturnsAllAndDistinct() {
        levelConfig.gameAreas.add(area1);
        levelConfig.gameAreas.add(area2);
        levelConfig.gameAreas.add(area3);

        unorderedMatch(List.of(levelConfig.getTextures()),
                Arrays.asList("Texture1.png", "Texture2.png", "Texture3.png", "Texture4.png"));
    }

    @Test
    void getTexturesReturnsAllAndDistinct2() {
        levelConfig.gameAreas.add(area1);
        levelConfig.gameAreas.add(area2);

        unorderedMatch(List.of(levelConfig.getTextures()),
                Arrays.asList("Texture1.png", "Texture2.png", "Texture3.png"));
    }

    @Test
    void getTexturesReturnsAllUnique() {
        levelConfig.gameAreas.add(area2);
        levelConfig.gameAreas.add(area3);

        unorderedMatch(List.of(levelConfig.getTextures()),
                Arrays.asList("Texture1.png", "Texture2.png", "Texture3.png", "Texture4.png"));
    }

    private void unorderedMatch(List<String> expectedTextures, List<String> output) {
        assertTrue(expectedTextures.size() == output.size() && expectedTextures.containsAll(output) && output.containsAll(expectedTextures));
    }
}

package com.csse3200.game.areas.mapConfig;

import com.badlogic.gdx.math.GridPoint2;
import com.csse3200.game.entities.configs.PlayerConfig;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.files.FileLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mockStatic;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)
class MapConfigLoaderTest {
    private static GameAreaConfig fullGameAreaConfig;
    private static GameAreaConfig gameAreaConfig;
    private static PlayerConfig playerConfig;
    private static AreaEntityConfig areaEntityConfig;
    private static GameAreaConfig expectedGameArea;

    private GameAreaConfig setupGameArea() {
        GameAreaConfig gameArea = new GameAreaConfig();
        gameArea.assets = new AssetsConfig();
        gameArea.assets.texturePaths = new String[] {"Texture1.png"};
        gameArea.assets.textureAtlasPaths = new String[] {"Texture1.atlas"};
        gameArea.assets.soundPaths = new String[] {"sound.wav"};
        gameArea.assets.particleEffectPaths = new String[] {"particle.effect"};
        gameArea.assets.backgroundMusicPath = "background.wav";
        gameArea.winConditions = List.of(new ResourceCondition());
        return gameArea;
    }

    private PlayerConfig setupPlayer() {
        PlayerConfig playerConfig = new PlayerConfig();
        playerConfig.spritePath = "playerSprite.png";
        playerConfig.position = new GridPoint2(0, 0);
        return playerConfig;
    }

    private AreaEntityConfig setupEntities() {
        return new AreaEntityConfig();
    }

    @BeforeEach
    void setup() {
        gameAreaConfig = setupGameArea();
        playerConfig = setupPlayer();
        areaEntityConfig = setupEntities();

        fullGameAreaConfig = setupGameArea();
        fullGameAreaConfig.areaEntityConfig = setupEntities();

        expectedGameArea = setupGameArea();
        expectedGameArea.areaEntityConfig = setupEntities();
    }

    @Test
    void loadValidMapDirectory() throws InvalidConfigException {
        try (MockedStatic<FileLoader> mockFileLoader = mockStatic(FileLoader.class)) {
            mockFileLoader.when(() -> FileLoader.readClass(eq(GameAreaConfig.class), any(), any()))
                    .thenReturn(gameAreaConfig);
            mockFileLoader.when(() -> FileLoader.readClass(eq(AreaEntityConfig.class), any(), any()))
                    .thenReturn(areaEntityConfig);

            assertEquals(GameAreaConfigLoader.loadMapDirectory("level", "area", false), expectedGameArea);
        }
    }

    @Test
    void loadInvalidMapDirectoryGameArea() {
        try (MockedStatic<FileLoader> mockFileLoader = mockStatic(FileLoader.class)) {
            mockFileLoader.when(() -> FileLoader.readClass(eq(GameAreaConfig.class), any(), any()))
                    .thenReturn(null);
            mockFileLoader.when(() -> FileLoader.readClass(eq(AreaEntityConfig.class), any(), any()))
                    .thenReturn(areaEntityConfig);

            assertThrows(InvalidConfigException.class, () -> GameAreaConfigLoader
                    .loadMapDirectory("level", "area", false));
        }
    }

    //TODO: Write tests to cover entity folder structure
    //TODO: Write tests to cover saving
}

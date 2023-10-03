package com.csse3200.game.areas.mapConfig;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.GridPoint2;
import com.csse3200.game.entities.configs.PlayerConfig;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.utils.LoadUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.csse3200.game.utils.LoadUtils.ROOT_PATH;
import static com.csse3200.game.utils.LoadUtils.SAVE_PATH;
import static org.mockito.ArgumentMatchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)
class ConfigLoaderTest { //TODO: Implement more testing
    private static GameAreaConfig fullGameAreaConfig;
    private static GameAreaConfig gameAreaConfig;
    private static PlayerConfig playerConfig;
    private static AreaEntityConfig areaEntityConfig;
    private static GameAreaConfig expectedGameArea;
    private static MockedStatic<LoadUtils> utilsMock;
    private static MockedStatic<FileLoader> fileLoaderMock;

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
        utilsMock = mockStatic(LoadUtils.class, Mockito.CALLS_REAL_METHODS);
        fileLoaderMock = mockStatic(FileLoader.class, Mockito.CALLS_REAL_METHODS);

        gameAreaConfig = setupGameArea();
        playerConfig = setupPlayer();
        areaEntityConfig = setupEntities();

        fullGameAreaConfig = setupGameArea();
        fullGameAreaConfig.areaEntityConfig = setupEntities();

        expectedGameArea = setupGameArea();
        expectedGameArea.areaEntityConfig = setupEntities();
    }

    @AfterEach
    void teardown() {
        utilsMock.close();
        fileLoaderMock.close();
    }

//    @Test
    void loadNewValidGame() {

    }

//    @Test
    void loadSavedGameFiles() {

    }

//    @Test
    void loadInvalidGameGameFile() {

    }

//    @Test
    void loadInvalidGameGamestateFile() {

    }

//    @Test
    void loadInvalidGameAssetsFile() {

    }


    @Test
    void loadValidMapDirectory() throws InvalidConfigException {
        try (MockedStatic<FileLoader> mockFileLoader = mockStatic(FileLoader.class)) {
            mockFileLoader.when(() -> FileLoader.readClass(eq(GameAreaConfig.class), any(), any()))
                    .thenReturn(gameAreaConfig);
            mockFileLoader.when(() -> FileLoader.readClass(eq(AreaEntityConfig.class), any(), any()))
                    .thenReturn(areaEntityConfig);

            assertEquals(ConfigLoader.loadMapDirectory("earth", LoadUtils.DEFAULT_AREA), expectedGameArea);
        }
    }

    @Test
    void loadInvalidMapDirectoryGameArea() {
        try (MockedStatic<FileLoader> mockFileLoader = mockStatic(FileLoader.class)) {
            mockFileLoader.when(() -> FileLoader.readClass(eq(GameAreaConfig.class), any(), any()))
                    .thenReturn(null);
            mockFileLoader.when(() -> FileLoader.readClass(eq(AreaEntityConfig.class), any(), any()))
                    .thenReturn(areaEntityConfig);

            assertThrows(InvalidConfigException.class, () -> ConfigLoader
                    .loadMapDirectory("earth", LoadUtils.DEFAULT_AREA));
        }
    }

    //TODO: Write tests to cover entity folder structure
    //TODO: Write tests to cover saving
}

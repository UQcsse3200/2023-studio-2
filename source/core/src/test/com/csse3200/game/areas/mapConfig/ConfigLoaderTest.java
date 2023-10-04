package com.csse3200.game.areas.mapConfig;

import com.badlogic.gdx.math.GridPoint2;
import com.csse3200.game.entities.configs.PlayerConfig;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.utils.LoadUtils;
import org.junit.Ignore;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.List;

import static com.csse3200.game.utils.LoadUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)
@Ignore //TODO: Finish Implementing Tests
class ConfigLoaderTest {
    private static GameAreaConfig fullGameAreaConfig;
    private static GameAreaConfig gameAreaConfig;
    private static GameConfig gameConfig;
    private static GameConfig expectedGameConfig;
    private static AssetsConfig assetsConfig;
    private static HashMap<String, Object> gameState;
    private static PlayerConfig playerConfig;
    private static AreaEntityConfig areaEntityConfig;
    private static GameAreaConfig expectedGameArea;
    private static MockedStatic<LoadUtils> utilsMock;
    private static MockedStatic<FileLoader> fileLoaderMock;

    private static final List<String> NAMES = List.of("earth", "mars", "pluto");

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

        gameState = new HashMap<String, Object>();
        assetsConfig = new AssetsConfig();

        gameConfig = new GameConfig();
        gameConfig.levelNames = NAMES;

        expectedGameConfig = new GameConfig();
        expectedGameConfig.levelNames = NAMES;
        expectedGameConfig.gameState = gameState;
        expectedGameConfig.assets = assetsConfig;
    }

    @AfterEach
    void teardown() {
        utilsMock.close();
        fileLoaderMock.close();
    }

    @Test
    void loadNewValidGame() throws InvalidConfigException {

    }

    @Test
    void loadSavedGameFiles() {

    }

    @Test
    void loadInvalidGameFile() {

    }

    @Test
    void loadInvalidGamestateFile() {

    }

    @Test
    void loadInvalidGameAssetsFile() {

    }

    @Test
    void loadNewLevel() {

    }

    @Test
    void loadSavedLevel() {

    }

    @Test
    void loadInvalidLevel() {

    }

    @Test
    void loadInvalidSaveLevel() {

    }

    @Test
    void loadLevelInvalidSecondSave() {

    }

    @Test
    void loadValidNewMapDirectory() {

    }

    @Test
    void loadInvalidNewMapDirectory() {

    }

    @Test
    void LoadValidSaveMapDirectory() {

    }

    @Test
    void LoadInvalidSaveMapDirectory() {

    }

    @Test
    void loadEntityFromFolder() {

    }

    @Test
    void loadEntitiesFromFolder() {

    }

    @Test
    void loadInvalidEntityFromFolder() {

    }

    @Test
    void loadInvalidEntityPath() {

    }

    @Test
    void loadValidConfigFile() {

    }

    @Test
    void loadInvalidConfigFile() {

    }
}

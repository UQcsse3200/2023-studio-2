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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)
@Ignore //TODO: Finish Implementing Tests
class ConfigLoaderTest {
    private static GameAreaConfig fullGameAreaConfig;
    private static GameAreaConfig gameAreaConfig;
    private static GameConfig gameConfig;
    private static GameConfig saveGameConfig;
    private static GameConfig expectedGameConfig;
    private static GameConfig expectedSaveGameConfig;
    private static AssetsConfig assetsConfig;
    private static AssetsConfig saveAssetsConfig;
    private static HashMap<String, Object> gameState;
    private static HashMap<String, Object> saveGameState;
    private static PlayerConfig playerConfig;
    private static AreaEntityConfig areaEntityConfig;
    private static GameAreaConfig expectedGameArea;
    private static MockedStatic<LoadUtils> utilsMock;
    private static MockedStatic<FileLoader> fileLoaderMock;

    private static final List<String> NAMES = List.of("earth", "mars", "pluto");
    private static final List<String> SAVE_NAMES = List.of("earth", "mars");

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

        gameConfig = new GameConfig();
        gameConfig.levelNames = NAMES;
        gameState = new HashMap<>();
        gameState.put("Test", new Object());
        assetsConfig = new AssetsConfig();

        saveGameConfig = new GameConfig();
        saveGameConfig.levelNames = SAVE_NAMES;
        saveGameState = new HashMap<>();
        saveGameState.put("SavedTest", new Object());
        saveAssetsConfig = new AssetsConfig();

        expectedGameConfig = new GameConfig();
        expectedGameConfig.levelNames = NAMES;
        expectedGameConfig.gameState = gameState;
        expectedGameConfig.assets = assetsConfig;

        expectedSaveGameConfig = new GameConfig();
        expectedSaveGameConfig.levelNames = SAVE_NAMES;
        expectedSaveGameConfig.gameState = saveGameState;
        expectedSaveGameConfig.assets = saveAssetsConfig;
    }

    @AfterEach
    void teardown() {
        utilsMock.close();
        fileLoaderMock.close();
    }

    @Test
    void loadNewValidGame() throws InvalidConfigException {
        utilsMock.when(() -> LoadUtils.pathExists(contains(SAVE_PATH))).thenReturn(false);
        fileLoaderMock.when(() -> FileLoader.readClass(any(), contains(SAVE_PATH), eq(FileLoader.Location.LOCAL))).thenReturn(null);
        fileLoaderMock.when(() -> FileLoader.readClass(eq(GameConfig.class), contains(ROOT_PATH), eq(FileLoader.Location.LOCAL))).thenReturn(gameConfig);
        fileLoaderMock.when(() -> FileLoader.readClass(eq(HashMap.class), contains(ROOT_PATH), eq(FileLoader.Location.LOCAL))).thenReturn(gameState);
        fileLoaderMock.when(() -> FileLoader.readClass(eq(AssetsConfig.class), contains(ROOT_PATH), eq(FileLoader.Location.LOCAL))).thenReturn(assetsConfig);
        GameConfig loadedConfig = ConfigLoader.loadGame();
        assertEquals(loadedConfig, expectedGameConfig);
    }

    @Test
    void loadSavedGameFiles() throws InvalidConfigException {
        utilsMock.when(() -> LoadUtils.pathExists(contains(SAVE_PATH))).thenReturn(true);
        fileLoaderMock.when(() -> FileLoader.readClass(any(), contains(ROOT_PATH), eq(FileLoader.Location.LOCAL))).thenReturn(null);
        fileLoaderMock.when(() -> FileLoader.readClass(eq(GameConfig.class), contains(SAVE_PATH), eq(FileLoader.Location.LOCAL))).thenReturn(saveGameConfig);
        fileLoaderMock.when(() -> FileLoader.readClass(eq(HashMap.class), contains(SAVE_PATH), eq(FileLoader.Location.LOCAL))).thenReturn(saveGameState);
        fileLoaderMock.when(() -> FileLoader.readClass(eq(AssetsConfig.class), contains(SAVE_PATH), eq(FileLoader.Location.LOCAL))).thenReturn(saveAssetsConfig);
        GameConfig loadedConfig = ConfigLoader.loadGame();
        assertEquals(loadedConfig, expectedSaveGameConfig);
    }

    @Test
    void loadInvalidGameFile() {
        utilsMock.when(() -> LoadUtils.pathExists(contains(SAVE_PATH))).thenReturn(false);
        fileLoaderMock.when(() -> FileLoader.readClass(any(), contains(ROOT_PATH), eq(FileLoader.Location.LOCAL))).thenReturn(null);
        fileLoaderMock.when(() -> FileLoader.readClass(eq(GameConfig.class), contains(SAVE_PATH), eq(FileLoader.Location.LOCAL))).thenReturn(null);
        fileLoaderMock.when(() -> FileLoader.readClass(eq(HashMap.class), contains(SAVE_PATH), eq(FileLoader.Location.LOCAL))).thenReturn(saveGameState);
        fileLoaderMock.when(() -> FileLoader.readClass(eq(AssetsConfig.class), contains(SAVE_PATH), eq(FileLoader.Location.LOCAL))).thenReturn(saveAssetsConfig);
        assertThrows(InvalidConfigException.class, ConfigLoader::loadGame);
    }

    @Test
    void loadInvalidGamestateFile() {
        utilsMock.when(() -> LoadUtils.pathExists(contains(SAVE_PATH))).thenReturn(false);
        fileLoaderMock.when(() -> FileLoader.readClass(any(), contains(ROOT_PATH), eq(FileLoader.Location.LOCAL))).thenReturn(null);
        fileLoaderMock.when(() -> FileLoader.readClass(eq(GameConfig.class), contains(SAVE_PATH), eq(FileLoader.Location.LOCAL))).thenReturn(saveGameConfig);
        fileLoaderMock.when(() -> FileLoader.readClass(eq(HashMap.class), contains(SAVE_PATH), eq(FileLoader.Location.LOCAL))).thenReturn(null);
        fileLoaderMock.when(() -> FileLoader.readClass(eq(AssetsConfig.class), contains(SAVE_PATH), eq(FileLoader.Location.LOCAL))).thenReturn(saveAssetsConfig);
        assertThrows(InvalidConfigException.class, ConfigLoader::loadGame);
    }

    @Test
    void loadInvalidGameAssetsFile() {
        utilsMock.when(() -> LoadUtils.pathExists(contains(SAVE_PATH))).thenReturn(true);
        fileLoaderMock.when(() -> FileLoader.readClass(any(), contains(ROOT_PATH), eq(FileLoader.Location.LOCAL))).thenReturn(null);
        fileLoaderMock.when(() -> FileLoader.readClass(eq(GameConfig.class), contains(SAVE_PATH), eq(FileLoader.Location.LOCAL))).thenReturn(saveGameConfig);
        fileLoaderMock.when(() -> FileLoader.readClass(eq(HashMap.class), contains(SAVE_PATH), eq(FileLoader.Location.LOCAL))).thenReturn(saveGameState);
        fileLoaderMock.when(() -> FileLoader.readClass(eq(AssetsConfig.class), contains(SAVE_PATH), eq(FileLoader.Location.LOCAL))).thenReturn(null);
        assertThrows(InvalidConfigException.class, ConfigLoader::loadGame);
    }

    @Test
    void loadSavedGameFileWhenExists() throws InvalidConfigException {
        String save_game_file_path = joinPath(List.of(SAVE_PATH, GAME_FILE));
        utilsMock.when(() -> LoadUtils.pathExists(contains(SAVE_PATH))).thenReturn(false);
        utilsMock.when(() -> LoadUtils.pathExists(eq(save_game_file_path))).thenReturn(true);
        fileLoaderMock.when(() -> FileLoader.readClass(eq(GameConfig.class), any(), eq(FileLoader.Location.LOCAL))).thenReturn(null);
        fileLoaderMock.when(() -> FileLoader.readClass(eq(GameConfig.class), eq(save_game_file_path), eq(FileLoader.Location.LOCAL))).thenReturn(saveGameConfig);
        fileLoaderMock.when(() -> FileLoader.readClass(eq(HashMap.class), any(), eq(FileLoader.Location.LOCAL))).thenReturn(null);
        fileLoaderMock.when(() -> FileLoader.readClass(eq(HashMap.class), contains(ROOT_PATH), eq(FileLoader.Location.LOCAL))).thenReturn(gameState);
        fileLoaderMock.when(() -> FileLoader.readClass(eq(AssetsConfig.class), any(), eq(FileLoader.Location.LOCAL))).thenReturn(null);
        fileLoaderMock.when(() -> FileLoader.readClass(eq(AssetsConfig.class), contains(ROOT_PATH), eq(FileLoader.Location.LOCAL))).thenReturn(assetsConfig);

        GameConfig expectedGame = new GameConfig();
        expectedGame.levelNames = SAVE_NAMES;
        expectedGame.gameState = gameState;
        expectedGame.assets = assetsConfig;

        assertEquals(expectedGame, ConfigLoader.loadGame());
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

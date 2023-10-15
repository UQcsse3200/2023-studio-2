package com.csse3200.game.areas.mapConfig;

import com.badlogic.gdx.math.GridPoint2;
import com.csse3200.game.entities.configs.CompanionConfig;
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

import static com.csse3200.game.utils.LoadUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)
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
    private static LevelConfig expectedLevelConfig;
    private static LevelConfig expectedSaveLevelConfig;
    private static PlayerConfig playerConfig;
    private static EntitiesConfigFile playerFile;
    private static CompanionConfig companionConfig;
    private static EntitiesConfigFile companionFile;
    private static AreaEntityConfig expectedAreaEntitiesConfig;
    private static AreaEntityConfig expectedSmallAreaEntitiesConfig;
    private static GameAreaConfig expectedGameArea;
    private static MockedStatic<LoadUtils> utilsMock;
    private static MockedStatic<FileLoader> fileLoaderMock;
    private static final List<String> NAMES = List.of("earth", "mars", "pluto");
    private static final List<String> SAVE_NAMES = List.of("earth", "mars");
    private static final List<String> AREA_NAMES = List.of("main", "cave", "secret");
    private static final List<String> SAVE_AREA_NAMES = List.of("main", "ocean");

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

    private CompanionConfig setupCompanion() {
        CompanionConfig companionConfig = new CompanionConfig();
        companionConfig.spritePath = "companionSprite.png";
        companionConfig.position = new GridPoint2(0, 0);
        return companionConfig;
    }

    private AreaEntityConfig setupFullEntities() throws InvalidConfigException {
        AreaEntityConfig areaEntityConfig = new AreaEntityConfig();
        areaEntityConfig.addEntry(Map.entry(PlayerConfig.class.getSimpleName(), List.of(playerConfig)));
        areaEntityConfig.addEntry(Map.entry(CompanionConfig.class.getSimpleName(), List.of(companionConfig)));
        return areaEntityConfig;
    }

    private AreaEntityConfig setupEntities() throws InvalidConfigException {
        AreaEntityConfig areaEntityConfig = new AreaEntityConfig();
        areaEntityConfig.addEntry(Map.entry(PlayerConfig.class.getSimpleName(), List.of(playerConfig)));
        return areaEntityConfig;
    }

    @BeforeEach
    void setup() throws InvalidConfigException {
        utilsMock = mockStatic(LoadUtils.class, Mockito.CALLS_REAL_METHODS);
        fileLoaderMock = mockStatic(FileLoader.class, Mockito.CALLS_REAL_METHODS);

        gameAreaConfig = setupGameArea();
        playerConfig = setupPlayer();
        companionConfig = setupCompanion();
        expectedAreaEntitiesConfig = setupFullEntities();
        expectedSmallAreaEntitiesConfig = setupEntities();

        fullGameAreaConfig = setupGameArea();
        fullGameAreaConfig.areaEntityConfig = setupFullEntities();

        expectedGameArea = setupGameArea();
        expectedGameArea.areaEntityConfig = setupFullEntities();

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

        expectedLevelConfig = new LevelConfig();
        expectedLevelConfig.areaNames = AREA_NAMES;
        expectedLevelConfig.nextPlanet = "next_level";

        expectedSaveLevelConfig = new LevelConfig();
        expectedSaveLevelConfig.areaNames = SAVE_AREA_NAMES;
        expectedSaveLevelConfig.nextPlanet = "level2";

        playerFile = new EntitiesConfigFile();
        playerFile.entityType = PlayerConfig.class.getSimpleName();
        playerFile.entities = List.of(playerConfig);

        companionFile = new EntitiesConfigFile();
        companionFile.entityType = CompanionConfig.class.getSimpleName();
        companionFile.entities = List.of(companionConfig);
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
        fileLoaderMock.when(() -> FileLoader.readClass(any(), contains(SAVE_PATH), eq(FileLoader.Location.LOCAL))).thenReturn(null);
        fileLoaderMock.when(() -> FileLoader.readClass(eq(GameConfig.class), contains(ROOT_PATH), eq(FileLoader.Location.LOCAL))).thenReturn(null);
        fileLoaderMock.when(() -> FileLoader.readClass(eq(HashMap.class), contains(ROOT_PATH), eq(FileLoader.Location.LOCAL))).thenReturn(gameState);
        fileLoaderMock.when(() -> FileLoader.readClass(eq(AssetsConfig.class), contains(ROOT_PATH), eq(FileLoader.Location.LOCAL))).thenReturn(assetsConfig);
        assertThrows(InvalidConfigException.class, ConfigLoader::loadGame);
    }

    @Test
    void loadInvalidGamestateFile() {
        utilsMock.when(() -> LoadUtils.pathExists(contains(SAVE_PATH))).thenReturn(false);
        fileLoaderMock.when(() -> FileLoader.readClass(any(), contains(SAVE_PATH), eq(FileLoader.Location.LOCAL))).thenReturn(null);
        fileLoaderMock.when(() -> FileLoader.readClass(eq(GameConfig.class), contains(ROOT_PATH), eq(FileLoader.Location.LOCAL))).thenReturn(gameConfig);
        fileLoaderMock.when(() -> FileLoader.readClass(eq(HashMap.class), contains(ROOT_PATH), eq(FileLoader.Location.LOCAL))).thenReturn(null);
        fileLoaderMock.when(() -> FileLoader.readClass(eq(AssetsConfig.class), contains(ROOT_PATH), eq(FileLoader.Location.LOCAL))).thenReturn(assetsConfig);
        assertThrows(InvalidConfigException.class, ConfigLoader::loadGame);
    }

    @Test
    void loadInvalidGameAssetsFile() {
        utilsMock.when(() -> LoadUtils.pathExists(contains(SAVE_PATH))).thenReturn(false);
        fileLoaderMock.when(() -> FileLoader.readClass(any(), contains(SAVE_PATH), eq(FileLoader.Location.LOCAL))).thenReturn(null);
        fileLoaderMock.when(() -> FileLoader.readClass(eq(GameConfig.class), contains(ROOT_PATH), eq(FileLoader.Location.LOCAL))).thenReturn(gameConfig);
        fileLoaderMock.when(() -> FileLoader.readClass(eq(HashMap.class), contains(ROOT_PATH), eq(FileLoader.Location.LOCAL))).thenReturn(gameState);
        fileLoaderMock.when(() -> FileLoader.readClass(eq(AssetsConfig.class), contains(ROOT_PATH), eq(FileLoader.Location.LOCAL))).thenReturn(null);
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
    void loadSavedGameStateWhenExists() throws InvalidConfigException {
        String save_game_state_path = joinPath(List.of(SAVE_PATH, GAMESTATE_FILE));
        utilsMock.when(() -> LoadUtils.pathExists(contains(SAVE_PATH))).thenReturn(false);
        utilsMock.when(() -> LoadUtils.pathExists(eq(save_game_state_path))).thenReturn(true);
        fileLoaderMock.when(() -> FileLoader.readClass(eq(GameConfig.class), any(), eq(FileLoader.Location.LOCAL))).thenReturn(null);
        fileLoaderMock.when(() -> FileLoader.readClass(eq(GameConfig.class), contains(ROOT_PATH), eq(FileLoader.Location.LOCAL))).thenReturn(gameConfig);
        fileLoaderMock.when(() -> FileLoader.readClass(eq(HashMap.class), any(), eq(FileLoader.Location.LOCAL))).thenReturn(null);
        fileLoaderMock.when(() -> FileLoader.readClass(eq(HashMap.class), eq(save_game_state_path), eq(FileLoader.Location.LOCAL))).thenReturn(saveGameState);
        fileLoaderMock.when(() -> FileLoader.readClass(eq(AssetsConfig.class), any(), eq(FileLoader.Location.LOCAL))).thenReturn(null);
        fileLoaderMock.when(() -> FileLoader.readClass(eq(AssetsConfig.class), contains(ROOT_PATH), eq(FileLoader.Location.LOCAL))).thenReturn(assetsConfig);

        GameConfig expectedGame = new GameConfig();
        expectedGame.levelNames = NAMES;
        expectedGame.gameState = saveGameState;
        expectedGame.assets = assetsConfig;

        assertEquals(expectedGame, ConfigLoader.loadGame());
    }

    @Test
    void loadSavedAssetsWhenExists() throws InvalidConfigException {
        String save_assets_path = joinPath(List.of(SAVE_PATH, ASSETS_FILE));
        utilsMock.when(() -> LoadUtils.pathExists(contains(SAVE_PATH))).thenReturn(false);
        utilsMock.when(() -> LoadUtils.pathExists(eq(save_assets_path))).thenReturn(true);
        fileLoaderMock.when(() -> FileLoader.readClass(eq(GameConfig.class), any(), eq(FileLoader.Location.LOCAL))).thenReturn(null);
        fileLoaderMock.when(() -> FileLoader.readClass(eq(GameConfig.class), contains(ROOT_PATH), eq(FileLoader.Location.LOCAL))).thenReturn(gameConfig);
        fileLoaderMock.when(() -> FileLoader.readClass(eq(HashMap.class), any(), eq(FileLoader.Location.LOCAL))).thenReturn(null);
        fileLoaderMock.when(() -> FileLoader.readClass(eq(HashMap.class), contains(ROOT_PATH), eq(FileLoader.Location.LOCAL))).thenReturn(gameState);
        fileLoaderMock.when(() -> FileLoader.readClass(eq(AssetsConfig.class), any(), eq(FileLoader.Location.LOCAL))).thenReturn(null);
        fileLoaderMock.when(() -> FileLoader.readClass(eq(AssetsConfig.class), eq(save_assets_path), eq(FileLoader.Location.LOCAL))).thenReturn(saveAssetsConfig);

        GameConfig expectedGame = new GameConfig();
        expectedGame.levelNames = NAMES;
        expectedGame.gameState = gameState;
        expectedGame.assets = saveAssetsConfig;

        assertEquals(expectedGame, ConfigLoader.loadGame());
    }

    @Test
    void loadNewLevel() throws InvalidConfigException {
        String path = joinPath(List.of(ROOT_PATH, "level", LEVEL_FILE));
        utilsMock.when(() -> LoadUtils.pathExists(contains(SAVE_PATH))).thenReturn(false);
        utilsMock.when(() -> LoadUtils.pathExists(eq(path))).thenReturn(true);
        fileLoaderMock.when(() -> FileLoader.readClass(any(), contains(SAVE_PATH), eq(FileLoader.Location.LOCAL))).thenReturn(null);
        fileLoaderMock.when(() -> FileLoader.readClass(eq(LevelConfig.class), eq(path), eq(FileLoader.Location.LOCAL))).thenReturn(expectedLevelConfig);
        LevelConfig loadedConfig = ConfigLoader.loadLevel("level");
        assertEquals(loadedConfig, expectedLevelConfig);
    }

    @Test
    void loadSavedLevel() throws InvalidConfigException {
        String path = joinPath(List.of(SAVE_PATH, "saved_level", LEVEL_FILE));
        utilsMock.when(() -> LoadUtils.pathExists(contains(ROOT_PATH))).thenReturn(false);
        utilsMock.when(() -> LoadUtils.pathExists(eq(path))).thenReturn(true);
        fileLoaderMock.when(() -> FileLoader.readClass(any(), contains(ROOT_PATH), eq(FileLoader.Location.LOCAL))).thenReturn(null);
        fileLoaderMock.when(() -> FileLoader.readClass(eq(LevelConfig.class), eq(path), eq(FileLoader.Location.LOCAL))).thenReturn(expectedSaveLevelConfig);
        LevelConfig loadedConfig = ConfigLoader.loadLevel("saved_level");
        assertEquals(loadedConfig, expectedSaveLevelConfig);
    }

    @Test
    void loadInvalidLevel() {
        String path = joinPath(List.of(ROOT_PATH, "level", LEVEL_FILE));
        utilsMock.when(() -> LoadUtils.pathExists(eq(path))).thenReturn(true);
        fileLoaderMock.when(() -> FileLoader.readClass(eq(LevelConfig.class), eq(path), eq(FileLoader.Location.LOCAL))).thenReturn(null);
        assertThrows(InvalidConfigException.class, () -> ConfigLoader.loadLevel("level"));
    }

    @Test
    void loadInvalidSaveLevel() {
        String path = joinPath(List.of(SAVE_PATH, "saved_level", LEVEL_FILE));
        utilsMock.when(() -> LoadUtils.pathExists(eq(path))).thenReturn(true);
        fileLoaderMock.when(() -> FileLoader.readClass(eq(LevelConfig.class), eq(path), eq(FileLoader.Location.LOCAL))).thenReturn(null);
        assertThrows(InvalidConfigException.class, () -> ConfigLoader.loadLevel("saved_level"));
    }

    //Unable to correctly test mocking of Gdx.files - relies on ConfigLoader.loadEntities
//    @Test
//    void loadValidNewMapDirectory() throws InvalidConfigException {
//        String areaPath = joinPath(List.of(ROOT_PATH, "level", "main_area"));
//        String filePath = joinPath(List.of(areaPath, MAIN_FILE));
//        String entitiesPath = joinPath(List.of(areaPath, ENTITIES_PATH));
//        utilsMock.when(() -> LoadUtils.pathExists(contains(SAVE_PATH))).thenReturn(false);
//        utilsMock.when(() -> LoadUtils.pathExists(eq(filePath))).thenReturn(true);
//        utilsMock.when(() -> LoadUtils.pathExists(eq(entitiesPath))).thenReturn(true);
//        fileLoaderMock.when(() -> FileLoader.readClass(any(), contains(SAVE_PATH), eq(FileLoader.Location.LOCAL))).thenReturn(null);
//        fileLoaderMock.when(() -> FileLoader.readClass(eq(GameAreaConfig.class), eq(filePath), eq(FileLoader.Location.LOCAL))).thenReturn(expectedGameArea);
//        GameAreaConfig loadedConfig = ConfigLoader.loadMapDirectory("level", "main_area");
//        assertEquals(loadedConfig, expectedLevelConfig);
//    }

    //Unable to correctly test mocking of Gdx.files - relies on ConfigLoader.loadEntities
//    @Test
//    void loadInvalidNewMapDirectory() {
//
//    }

    @Test
    void LoadValidSaveMapDirectory() throws InvalidConfigException {
        String areaPath = joinPath(List.of(SAVE_PATH, "level", "main_area"));
        String filePath = joinPath(List.of(areaPath, MAIN_FILE));
        String entitiesPath = joinPath(List.of(areaPath, ENTITIES_PATH + JSON_EXT));
        utilsMock.when(() -> LoadUtils.pathExists(contains(ROOT_PATH))).thenReturn(false);
        utilsMock.when(() -> LoadUtils.pathExists(eq(filePath))).thenReturn(true);
        utilsMock.when(() -> LoadUtils.pathExists(eq(entitiesPath))).thenReturn(true);
        fileLoaderMock.when(() -> FileLoader.readClass(any(), contains(ROOT_PATH), eq(FileLoader.Location.LOCAL))).thenReturn(null);
        fileLoaderMock.when(() -> FileLoader.readClass(eq(GameAreaConfig.class), eq(filePath), eq(FileLoader.Location.LOCAL))).thenReturn(gameAreaConfig);
        fileLoaderMock.when(() -> FileLoader.readClass(eq(AreaEntityConfig.class), eq(entitiesPath), eq(FileLoader.Location.LOCAL))).thenReturn(expectedAreaEntitiesConfig);
        GameAreaConfig loadedConfig = ConfigLoader.loadMapDirectory("level", "main_area");
        assertEquals(loadedConfig, expectedGameArea);
    }

    @Test
    void LoadSaveMapDirectoryInvalidFile() {
        String areaPath = joinPath(List.of(SAVE_PATH, "level", "main_area"));
        String filePath = joinPath(List.of(areaPath, MAIN_FILE));
        String entitiesPath = joinPath(List.of(areaPath, ENTITIES_PATH + JSON_EXT));
        utilsMock.when(() -> LoadUtils.pathExists(contains(ROOT_PATH))).thenReturn(false);
        utilsMock.when(() -> LoadUtils.pathExists(eq(filePath))).thenReturn(true);
        utilsMock.when(() -> LoadUtils.pathExists(eq(entitiesPath))).thenReturn(true);
        fileLoaderMock.when(() -> FileLoader.readClass(any(), contains(ROOT_PATH), eq(FileLoader.Location.LOCAL))).thenReturn(null);
        fileLoaderMock.when(() -> FileLoader.readClass(eq(GameAreaConfig.class), eq(filePath), eq(FileLoader.Location.LOCAL))).thenReturn(null);
        fileLoaderMock.when(() -> FileLoader.readClass(eq(AreaEntityConfig.class), eq(entitiesPath), eq(FileLoader.Location.LOCAL))).thenReturn(expectedAreaEntitiesConfig);
        assertThrows(InvalidConfigException.class, () -> ConfigLoader.loadMapDirectory("level", "main_area"));
    }

    @Test
    void LoadSaveMapDirectoryInvalidEntities() {
        String areaPath = joinPath(List.of(SAVE_PATH, "level", "main_area"));
        String filePath = joinPath(List.of(areaPath, MAIN_FILE));
        String entitiesPath = joinPath(List.of(areaPath, ENTITIES_PATH + JSON_EXT));
        utilsMock.when(() -> LoadUtils.pathExists(contains(ROOT_PATH))).thenReturn(false);
        utilsMock.when(() -> LoadUtils.pathExists(eq(filePath))).thenReturn(true);
        utilsMock.when(() -> LoadUtils.pathExists(eq(entitiesPath))).thenReturn(true);
        fileLoaderMock.when(() -> FileLoader.readClass(any(), contains(ROOT_PATH), eq(FileLoader.Location.LOCAL))).thenReturn(null);
        fileLoaderMock.when(() -> FileLoader.readClass(eq(GameAreaConfig.class), eq(filePath), eq(FileLoader.Location.LOCAL))).thenReturn(gameAreaConfig);
        fileLoaderMock.when(() -> FileLoader.readClass(eq(AreaEntityConfig.class), eq(entitiesPath), eq(FileLoader.Location.LOCAL))).thenReturn(null);
        assertThrows(InvalidConfigException.class, () -> ConfigLoader.loadMapDirectory("level", "main_area"));
    }

    //Unable to correctly test mocking of Gdx.files
//    @Test
//    void loadEntityFromFolder() throws InvalidConfigException {
//        String loadPath = "entities/";
//        Gdx.files = mock(HeadlessFiles.class);
//        FileHandle fileHandle = mock(FileHandle.class);
//        when(Gdx.files.local(eq(loadPath))).thenReturn(fileHandle);
//        when(fileHandle.list()).thenReturn(new FileHandle[] { new FileHandle("player.json") });
//        fileLoaderMock.when(() -> FileLoader.readClass(eq(EntitiesConfigFile.class), contains("player.json"), eq(FileLoader.Location.LOCAL))).thenReturn(playerFile);
//        assertEquals(ConfigLoader.loadEntities(loadPath), expectedSmallAreaEntitiesConfig);
//    }

    //Unable to correctly test mocking of Gdx.files
//    @Test
//    void loadEntitiesFromFolder() throws InvalidConfigException {
//        String loadPath = "entities/";
//        Gdx.files = mock(HeadlessFiles.class);
//        FileHandle fileHandle = mock(FileHandle.class);
//        when(Gdx.files.local(eq(loadPath))).thenReturn(fileHandle);
//        when(fileHandle.list()).thenReturn(new FileHandle[] { new FileHandle("player.json"), new FileHandle("companion.json") });
//        fileLoaderMock.when(() -> FileLoader.readClass(eq(EntitiesConfigFile.class), contains("player.json"), eq(FileLoader.Location.LOCAL))).thenReturn(playerFile);
//        fileLoaderMock.when(() -> FileLoader.readClass(eq(EntitiesConfigFile.class), contains("companion.json"), eq(FileLoader.Location.LOCAL))).thenReturn(companionFile);
//        assertEquals(ConfigLoader.loadEntities(loadPath), expectedAreaEntitiesConfig);
//    }

    //Unable to correctly test mocking of Gdx.files
//    @Test
//    void loadInvalidEntityInFolder() throws InvalidConfigException {
//        String loadPath = "entities/";
//        Gdx.files = mock(HeadlessFiles.class);
//        FileHandle fileHandle = mock(FileHandle.class);
//        when(Gdx.files.local(eq(loadPath))).thenReturn(fileHandle);
//        when(fileHandle.list()).thenReturn(new FileHandle[] { new FileHandle("player.json"), new FileHandle("companion.json") });
//        fileLoaderMock.when(() -> FileLoader.readClass(eq(EntitiesConfigFile.class), contains("player.json"), eq(FileLoader.Location.LOCAL))).thenReturn(playerFile);
//        fileLoaderMock.when(() -> FileLoader.readClass(eq(EntitiesConfigFile.class), contains("companion.json"), eq(FileLoader.Location.LOCAL))).thenReturn(null);
//        assertEquals(ConfigLoader.loadEntities(loadPath), expectedSmallAreaEntitiesConfig);
//    }

    @Test
    void loadValidConfigFile() throws InvalidConfigException {
        fileLoaderMock.when(() -> FileLoader.readClass(eq(PlayerConfig.class), any(), eq(FileLoader.Location.LOCAL))).thenReturn(playerConfig);
        assertEquals(ConfigLoader.loadConfigFile("playerPath", PlayerConfig.class), playerConfig);
    }

    @Test
    void loadInvalidConfigFile() {
        fileLoaderMock.when(() -> FileLoader.readClass(eq(PlayerConfig.class), any(), eq(FileLoader.Location.LOCAL))).thenReturn(null);
        assertThrows(InvalidConfigException.class, () -> ConfigLoader.loadConfigFile("playerPath", PlayerConfig.class));
    }

    @Test
    void loadProductionGame() throws InvalidConfigException {
        //Ensure no errors are thrown when trying to load the game
        //Note this may still be unable to load some entities if their config files are invalid
        GameConfig gameConfig = ConfigLoader.loadGame();
        for (String level : gameConfig.levelNames) {
            LevelConfig levelConfig = ConfigLoader.loadLevel(level);
            for (String area : levelConfig.areaNames) {
                ConfigLoader.loadMapDirectory(level, area);
            }
        }
    }
}

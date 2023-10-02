package com.csse3200.game.areas.mapConfig;

import com.badlogic.gdx.Gdx;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.utils.LoadUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.csse3200.game.utils.LoadUtils.*;

/**
 * Class to handle loading of game areas from config files
 */
public class ConfigLoader {
    private static final Logger logger = LoggerFactory.getLogger(ConfigLoader.class);

    /**
     * Loads the Game from the root directory.
     * @return GameConfig loaded from the directory
     * @throws InvalidConfigException If the files are unable to be loaded as a GameConfig.
     */
    public static GameConfig loadGame() throws InvalidConfigException {
        String path = getOptionalSavePath(PATH_OPTIONS, GAME_FILE);
        GameConfig gameConfig = FileLoader.readClass(GameConfig.class, path, FileLoader.Location.INTERNAL);
        if (gameConfig == null) throw new InvalidConfigException(FAIL_MESSAGE + GameConfig.class.getSimpleName());
        String statePath = getOptionalSavePath(PATH_OPTIONS, GAMESTATE_FILE);
        gameConfig.gameState = FileLoader.readClass(Map.class, statePath, FileLoader.Location.INTERNAL);
        String assetPath = getOptionalSavePath(PATH_OPTIONS, ASSETS_FILE);
        gameConfig.assets = FileLoader.readClass(AssetsConfig.class, assetPath, FileLoader.Location.INTERNAL);
        return gameConfig;
    }

    /**
     * Loads a Level from a directory.
     * @param levelName Level to be loaded
     * @return LevelConfig loaded from the directory
     * @throws InvalidConfigException If the files are unable to be loaded as a LevelConfig.
     */
    public static LevelConfig loadLevel(String levelName) throws InvalidConfigException {
        levelName = LoadUtils.formatName(levelName);
        String path = getOptionalSavePath(PATH_OPTIONS, levelName, LEVEL_FILE);
        LevelConfig levelConfig = FileLoader.readClass(LevelConfig.class, path, FileLoader.Location.INTERNAL);
        if (levelConfig == null) throw new InvalidConfigException(FAIL_MESSAGE + LevelConfig.class.getSimpleName() + " - " + levelName);
        return levelConfig;
    }

    /**
     * Loads a GameArea from a single .json file containing all game area data.
     * Note this may not have the best error messages
     * @param levelName Name of level to be loade
     * @param gameAreaName Name of game area in level to generate
     * @param fromSave Boolean - true if you want to load files som
     * @return The GameAreaConfig class loaded from the .json file
     * @throws InvalidConfigException If the file is unable to be loaded to a GameAreaConfig
     */
    public static GameAreaConfig loadMapFile(String levelName, String gameAreaName, boolean fromSave) throws InvalidConfigException {
        levelName = LoadUtils.formatName(levelName);
        String filePath = getOptionalSavePath(PATH_OPTIONS, levelName, gameAreaName + JSON_EXT);
        GameAreaConfig gameArea = FileLoader.readClass(GameAreaConfig.class, filePath, FileLoader.Location.INTERNAL);
        if (gameArea == null) throw new InvalidConfigException("Failed to load map " + filePath);
        return gameArea;
    }

    /**
     * Loads a folder containing various .json files that represent a given game area.
     * @param levelName Name of level to be loade
     * @param gameAreaName Name of game area in level to generate
     * @param fromSave Boolean - true if you want to load files som
     * @throws InvalidConfigException If any file is unable to be loaded to a GameAreaConfig
     */
    public static GameAreaConfig loadMapDirectory(String levelName, String gameAreaName, boolean fromSave)
            throws InvalidConfigException {
        levelName = LoadUtils.formatName(levelName);
        String mainPath = getOptionalSavePath(PATH_OPTIONS, levelName, gameAreaName, MAIN_FILE);
        GameAreaConfig gameAreaConfig = loadConfigFile(mainPath, GameAreaConfig.class);

        String entitySaveFilePath = joinPath(SAVE_PATH, levelName, gameAreaName, ENTITIES_PATH + JSON_EXT);
        if (Gdx.files.internal(entitySaveFilePath).exists()) {
            gameAreaConfig.areaEntityConfig = loadConfigFile(entitySaveFilePath, AreaEntityConfig.class);
        } else {
            String entitiesPath = joinPath(ROOT_PATH, levelName, gameAreaName, ENTITIES_PATH);
            gameAreaConfig.areaEntityConfig = loadEntities(entitiesPath);
        }

        return gameAreaConfig;
    }

    /**
     * Loads a directory containing entity .json files into an AreaEntityConfig object
     * @param loadPath Path of directory to be loaded from
     * @return Loaded AreaEntityConfig object
     * @throws InvalidConfigException If any file in the directory is unable to be loaded
     */
    public static AreaEntityConfig loadEntities(String loadPath) throws InvalidConfigException {
        AreaEntityConfig areaEntityConfig = new AreaEntityConfig();
        var files = Arrays.stream(Gdx.files.internal(loadPath).list()).map(x -> x.path()).toList();
        for (String file : files) {
            EntitiesConfigFile entitiesConfigFile =
                    FileLoader.readClass(EntitiesConfigFile.class, file, FileLoader.Location.INTERNAL);
            if (entitiesConfigFile == null) continue;
            areaEntityConfig.addEntry(entitiesConfigFile.getMapEntry());
        }
        return areaEntityConfig;
    }

    /**
     * Loads a generic config file from a given .json path.
     * @param configPath Path of .json file to be loaded from
     * @param target Class for data to be loaded to
     * @return Object of type T with loaded .json data
     * @param <T> Type of config file to be loaded to
     * @throws InvalidConfigException If the file is unable to be loaded to Class of type T
     */
    public static <T> T loadConfigFile(String configPath, Class<T> target) throws InvalidConfigException {
        T outClass = FileLoader.readClass(target, configPath, FileLoader.Location.INTERNAL);
        if (outClass == null) throw new InvalidConfigException(FAIL_MESSAGE + target);
        return outClass;
    }
}

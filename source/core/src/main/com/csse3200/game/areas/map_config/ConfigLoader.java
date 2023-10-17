package com.csse3200.game.areas.map_config;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.utils.LoadUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static com.csse3200.game.utils.LoadUtils.*;

/**
 * Class to handle loading of game areas from config files
 */
public class ConfigLoader {
    private static final Logger logger = LoggerFactory.getLogger(ConfigLoader.class);

    protected static void determine_prefix() {
        LoadUtils.setPrefix("");
        for (String origin :
                new String[]{"source/core/assets/", "core/assets/", "./"}) {
            if (Gdx.files.local(origin + getRootPath()).exists()) {
                LoadUtils.setPrefix(origin);
                break;
            }
        }
    }

    /**
     * Loads the Game from the root directory.
     * @return GameConfig loaded from the directory
     * @throws InvalidConfigException If the files are unable to be loaded as a GameConfig.
     */
    public static GameConfig loadGame() throws InvalidConfigException {
        determine_prefix();
        String path = getOptionalSavePath(getPathOptions(), GAME_FILE);
        GameConfig gameConfig = FileLoader.readClass(GameConfig.class, path, FileLoader.Location.LOCAL);
        if (gameConfig == null) throw new InvalidConfigException(FAIL_READ_CLASS + GAME_FILE);
        String statePath = getOptionalSavePath(getPathOptions(), GAMESTATE_FILE);
        gameConfig.gameState = FileLoader.readClass(HashMap.class, statePath, FileLoader.Location.LOCAL);
        if (gameConfig.gameState == null) throw new InvalidConfigException(FAIL_READ_CLASS + GAMESTATE_FILE);
        String assetPath = getOptionalSavePath(getPathOptions(), ASSETS_FILE);
        gameConfig.assets = FileLoader.readClass(AssetsConfig.class, assetPath, FileLoader.Location.LOCAL);
        if (gameConfig.assets == null) throw new InvalidConfigException(FAIL_READ_CLASS + ASSETS_FILE);
        return gameConfig;
    }

    /**
     * Loads a Level from a directory.
     * @param levelName Level to be loaded
     * @return LevelConfig loaded from the directory
     * @throws InvalidConfigException If the files are unable to be loaded as a LevelConfig.
     */
    public static LevelConfig loadLevel(String levelName) throws InvalidConfigException {
        determine_prefix();
        levelName = LoadUtils.formatName(levelName);
        String path = getOptionalSavePath(getPathOptions(), List.of(levelName, LEVEL_FILE));
        LevelConfig levelConfig = FileLoader.readClass(LevelConfig.class, path, FileLoader.Location.LOCAL);
        if (levelConfig == null) throw new InvalidConfigException(FAIL_MESSAGE + LevelConfig.class.getSimpleName() + " - " + levelName);
        return levelConfig;
    }

    /**
     * Loads a folder containing various .json files that represent a given game area.
     * @param levelName Name of level to be loade
     * @param gameAreaName Name of game area in level to generate
     * @throws InvalidConfigException If any file is unable to be loaded to a GameAreaConfig
     */
    public static GameAreaConfig loadMapDirectory(String levelName, String gameAreaName)
            throws InvalidConfigException {
        determine_prefix();
        levelName = LoadUtils.formatName(levelName);
        String mainPath = getOptionalSavePath(getPathOptions(), List.of(levelName, gameAreaName, MAIN_FILE));
        GameAreaConfig gameAreaConfig = loadConfigFile(mainPath, GameAreaConfig.class);

        String entitySaveFilePath = joinPath(List.of(getSavePath(), levelName, gameAreaName, ENTITIES_PATH + JSON_EXT));
        if (pathExists(entitySaveFilePath)) {
            gameAreaConfig.areaEntityConfig = loadConfigFile(entitySaveFilePath, AreaEntityConfig.class);
        } else {
            String entitiesPath = joinPath(List.of(getRootPath(), levelName, gameAreaName, ENTITIES_PATH));
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
        determine_prefix();
        AreaEntityConfig areaEntityConfig = new AreaEntityConfig();
        var files = Arrays.stream(Gdx.files.local(loadPath).list()).map(FileHandle::path).toList();
        for (String file : files) {
            EntitiesConfigFile entitiesConfigFile =
                    FileLoader.readClass(EntitiesConfigFile.class, file, FileLoader.Location.LOCAL);
            if (entitiesConfigFile == null) continue;
            logger.info("Successfully loaded entity file " + file);
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
        determine_prefix();
        T outClass = FileLoader.readClass(target, configPath, FileLoader.Location.LOCAL);
        if (outClass == null) throw new InvalidConfigException(FAIL_MESSAGE + target);
        return outClass;
    }
}

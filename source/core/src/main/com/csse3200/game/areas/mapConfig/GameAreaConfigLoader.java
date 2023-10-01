package com.csse3200.game.areas.mapConfig;

import com.badlogic.gdx.Gdx;
import com.csse3200.game.files.FileLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

import static com.csse3200.game.areas.mapConfig.LoadUtils.*;

/**
 * Class to handle loading of game areas from config files
 */
public class GameAreaConfigLoader {
    private static final Logger logger = LoggerFactory.getLogger(GameAreaConfigLoader.class);

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
        String filePath = joinPath(ROOT_PATH, levelName, gameAreaName, JSON_EXT);
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
        String path = joinPath(ROOT_PATH, levelName, gameAreaName);
        String mainPath = joinPath(path, MAIN_FILE);
        String entitiesPath = joinPath(path, ENTITIES_PATH);
        GameAreaConfig gameAreaConfig = loadConfigFile(mainPath, GameAreaConfig.class);
        gameAreaConfig.areaEntityConfig = loadEntities(entitiesPath);
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

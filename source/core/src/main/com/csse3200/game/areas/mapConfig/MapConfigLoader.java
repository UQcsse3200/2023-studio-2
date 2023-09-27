package com.csse3200.game.areas.mapConfig;

import com.csse3200.game.areas.MapGameArea;
import com.csse3200.game.entities.configs.PlayerConfig;
import com.csse3200.game.files.FileLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Class to handle loading of game areas from config files
 */
public class MapConfigLoader {
    private static final String MAIN_PATH = "main.json";
    private static final String ENTITIES_PATH = "entities";

    private static final String FAIL_MESSAGE = "Failed to load config of type ";
    private static final String FAIL_ENTITY = "Failed to load entities config";

    private static final Logger logger = LoggerFactory.getLogger(MapConfigLoader.class);

    /**
     * Loads a GameArea from a single .json file containing all game area data.
     * Note this may not have the best error messages
     * @param filePath Path of file to be loaded
     * @return The GameAreaConfig class loaded from the .json file
     * @throws InvalidConfigException If the file is unable to be loaded to a GameAreaConfig
     */
    public static GameAreaConfig loadMapFile(String filePath) throws InvalidConfigException {
        GameAreaConfig gameArea = FileLoader.readClass(GameAreaConfig.class, filePath, FileLoader.Location.INTERNAL);
        if (gameArea == null) throw new InvalidConfigException("Failed to load map");
        return gameArea;
    }

    /**
     * Loads a folder containing various .json files that represent a given game area.
     * @param mapDirPath Directory where files will be loaded from
     * @return The GameAreaConfig class loaded from the directory
     * @throws InvalidConfigException If any file is unable to be loaded to a GameAreaConfig
     */
    public static GameAreaConfig loadMapDirectory(String mapDirPath) throws InvalidConfigException {
        String mainPath = joinPath(mapDirPath, MAIN_PATH);
        String entitiesPath = joinPath(mapDirPath, ENTITIES_PATH);
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
        logger.info(getFiles(loadPath).toString());
        for (String file : getFiles(loadPath)) {
            String fullPath = joinPath(loadPath, file);
            EntitiesConfigFile entitiesConfigFile =
                    FileLoader.readClass(EntitiesConfigFile.class, fullPath, FileLoader.Location.INTERNAL);
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

    /**
     * Joins an arbitrary list of paths using the system's path separator
     * @param paths List of paths to join together
     * @return String of all the path strings joined by a path seperator
     */
    private static String joinPath(String... paths) {
        StringJoiner stringJoiner = new StringJoiner(File.separator);
        for (String part: paths) {
            stringJoiner.add(part);
        }
        return stringJoiner.toString();
    }

    private static List<String> getFiles(String path) {
        File[] files = new File(path).listFiles();
        if (files == null || files.length == 0) return new ArrayList<>();
        return Arrays.stream(files)
                .filter(file -> !file.isDirectory())
                .map(File::getName)
                .collect(Collectors.toList());
    }
}
